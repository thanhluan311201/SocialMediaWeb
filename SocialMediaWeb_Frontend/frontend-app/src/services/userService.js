import { API } from "../configurations/confiruration"
import httpClient from "../configurations/httpClient"
import { getToken } from "./localStorageService";


export const getMyInfo = async () => {
    return await httpClient.get(API.MY_INFO, {
        headers: {
            Authorization: `Bearer ${getToken()}`,
        },
    });
};