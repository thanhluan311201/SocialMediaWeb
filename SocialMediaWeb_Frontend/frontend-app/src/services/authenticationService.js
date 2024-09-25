import { API } from "../configurations/confiruration"
import httpClient from "../configurations/httpClient"
import { getToken, removeToken, setToken } from "./localStorageService";


export const logIn = async (username, password) => {
    const response = await httpClient.post(API.LOGIN, {
        username: username,
        password: password
    });

    setToken(response.data?.result?.token);

    return response;
};

export const register = async (username, password, firstname, lastname, dob) => {
    const response = await httpClient.post(API.REGISTER, {
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
    return getToken();
};