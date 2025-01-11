import { API } from "../configurations/confiruration"
import axiosInstance from "./axiosInterceptor";



export const getConversations = async () => {
    return await axiosInstance.get(API.GET_CONVERSATION, {
    });
};

export const getMessages = async (conversationId) => {
    const url = `${API.GET_CONVERSATION}/${conversationId}`
    return await axiosInstance.get(`${url}`, {
    });
};

export const sendMessage = async (receiverId, content) => {
    return await axiosInstance.post(API.SEND_MESSAGES, {
        receiverId: receiverId,
        content: content
    });
};