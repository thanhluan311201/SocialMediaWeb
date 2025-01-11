import axios from 'axios';
import { getToken, setToken } from './localStorageService';
import { CONFIG, API } from '../configurations/confiruration';
import { refreshToken } from './authenticationService';

// Tạo instance axios
const axiosInstance = axios.create({
    baseURL: CONFIG.API_GATEWAY, 
    timeout: 30000,
    headers: {
        'Content-Type': 'Application/json'
    }
});

// Thêm interceptor để xử lý token
axiosInstance.interceptors.request.use(
    async (config) => {
        const token = getToken();

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => Promise.reject(error)
);

// Xử lý response để kiểm tra token
axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response.status === 401 && !originalRequest._retry) {
            try {
                originalRequest._retry = true;
                const expiredToken = getToken();

                // Tạo một promise cho việc gọi API refresh token
                const refreshPromise = refreshToken(expiredToken).then(response => {
                    const { token } = response;
                    setToken(token);

                    // Cập nhật token vào headers
                    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

                    // Trả lại yêu cầu gốc với token mới
                    return axiosInstance(originalRequest);
                });

                // Tạo một promise cho timeout (ví dụ 5 giây)
                const timeoutPromise = new Promise((_, reject) => {
                    setTimeout(() => {
                        reject(new Error('Request timeout'));
                    }, 30000); // 5000 milliseconds = 5 seconds
                });

                // Chờ cho một trong hai promise hoàn thành
                return await Promise.race([refreshPromise, timeoutPromise]);
            } catch (error) {
                window.location.href = '/login';
            }
        }

        return Promise.reject(error);
    }
);


export default axiosInstance;
