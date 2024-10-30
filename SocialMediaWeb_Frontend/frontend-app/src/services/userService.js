import { API } from "../configurations/confiruration"
import axiosInstance from "./axiosInterceptor";



export const getMyInfo = async () => {
    return await axiosInstance.get(API.MY_INFO, {
    });
};