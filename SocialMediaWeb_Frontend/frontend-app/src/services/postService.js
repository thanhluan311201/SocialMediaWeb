import { API } from "../configurations/confiruration"
import axiosInstance from "./axiosInterceptor";

export const getPosts = async () => {
    return await axiosInstance.get(API.GET_POSTS, {
    });
};