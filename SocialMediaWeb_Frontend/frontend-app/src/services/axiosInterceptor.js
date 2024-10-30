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
            try{
                originalRequest._retry = true;
                const expiredToken = getToken();
                // Gọi API refresh token
                const { token } = await refreshToken(expiredToken);
                setToken(token);

                // Thử lại request với token mới
                axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
                return axiosInstance(originalRequest);
            } catch (error){
                window.location.href = '/login';
            }
            
        }

        return Promise.reject(error);
    }
);

export default axiosInstance;
