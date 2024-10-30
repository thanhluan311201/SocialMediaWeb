import { API } from "../configurations/confiruration"
import axiosInstance from "./axiosInterceptor";

export const getNotifications = async () => {
    return await axiosInstance.get(API.GET_NOTIFICATIONS, {
    });
};