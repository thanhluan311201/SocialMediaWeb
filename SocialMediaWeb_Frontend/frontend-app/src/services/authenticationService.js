import { API } from "../configurations/confiruration"
import axiosInstance from "./axiosInterceptor";
import { getToken, removeToken, setToken, isTokenExpired } from "./localStorageService";


export const logIn = async (username, password) => {
    const response = await axiosInstance.post(API.LOGIN, {
        username: username,
        password: password
    });

    setToken(response.data?.result?.token);

    return response;
};

export const register = async (username, password, firstname, lastname, dob) => {
    const response = await axiosInstance.post(API.REGISTER, {
        username: username,
        password: password,
        firstname: firstname,
        lastname: lastname,
        dob: dob
    });

    return response;
};

export const logOut = () => {
    removeToken();
};

export const isAuthenticated = () => {
    const token = getToken();
    return token && !isTokenExpired(token);
};

export const refreshToken = async (expiredToken) => {
    const response = await axiosInstance.post(API.REFRESH_TOKEN, {
        token: expiredToken
    });

    return response
};
