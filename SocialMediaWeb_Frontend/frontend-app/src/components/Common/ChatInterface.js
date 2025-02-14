import React, { useEffect, useState } from 'react';
import { Box, Typography, InputBase, IconButton, Badge } from '@mui/material';
import { useNavigate } from "react-router-dom";
import { isAuthenticated } from "../../services/authenticationService";
import MailIcon from '@mui/icons-material/Mail';
import SearchIcon from '@mui/icons-material/Search';
import { Client } from '@stomp/stompjs';
import { getConversations, getMessages, sendMessage } from '../../services/chatService';
import { getMyInfo } from "../../services/userService";
import './ChatInterface.css'; // Import CSS file
import SendIcon from '@mui/icons-material/Send';
import Scene from '../Home/Scene';
import { getToken } from "../../services/localStorageService";
import { API } from '../../configurations/confiruration';
import { useLocation } from 'react-router-dom';
import { useDemoRouter } from "@toolpad/core/internal";
import { format, formatDistanceToNow, isToday, isYesterday, differenceInHours } from "date-fns";
import { vi } from "date-fns/locale";


export default function ChatInterface() {
    const [conversations, setConversations] = useState([]);
    const [messages, setMessages] = useState([]);
    const [currentChat, setCurrentChat] = useState(null);
    const [content, setContent] = useState('');
    const navigate = useNavigate();
    const [currentUserId, setCurrentUserId] = useState(null);
    const socketUrl = 'http://localhost:8080/ws';
    const location = useLocation();
    const redirectUrl = location.state?.redirectUrl;
    const router = useDemoRouter('/chat');
  
    // Declare stompClient at the component level
    let stompClient = null;

    const getUserDetails = async () => {
        try {
            const response = await getMyInfo();
            const data = response.data;
            console.log(data);
            setCurrentUserId(data.result.id); // Lưu ID của người dùng hiện tại
        } catch (error) {
            console.error('Error fetching user details:', error);
        }
    };

    const formatTime = (sentAt) => {
        const sentDate = new Date(sentAt);
      
        if (differenceInHours(new Date(), sentDate) < 24) {
          return formatDistanceToNow(sentDate, { addSuffix: true, locale: vi });
        }
      
        if (isYesterday(sentDate)) {
          return `Hôm qua, ${format(sentDate, "HH:mm")}`;
        }
      
        return format(sentDate, "dd/MM/yyyy HH:mm");
      };

    useEffect(() => {
        if (!isAuthenticated()) {
            navigate("/login");
            return;
        } 
        getUserDetails();
    }, [navigate]);

    useEffect(() => {
        if (redirectUrl) {
            fetch(redirectUrl)
                .then(response => {
                    // Kiểm tra nếu phản hồi không phải JSON
                    if (!response.ok || !response.headers.get("content-type")?.includes("application/json")) {
                        throw new Error("Phản hồi không phải là JSON hoặc có lỗi truy cập.");
                    }
                    return response.json();
                })
                .then(data => {
                    // Xử lý dữ liệu cuộc trò chuyện
                    console.log("Dữ liệu cuộc trò chuyện:", data);
                })
                .catch(error => console.error('Error fetching data:', error.message));
        }
    }, [redirectUrl]);
    

    useEffect(() => {
        const fetchConversations = async () => {
            try {
                const response = await getConversations();
                const data = response.data;
        
                // Kiểm tra xem data.result có phải là mảng không
                if (Array.isArray(data.result)) {
                    const conversationsWithMessages = data.result.map(conversation => {
                        // Đảm bảo rằng conversation có tồn tại và có tin nhắn
                        const messages = Array.isArray(conversation.messages) ? conversation.messages : [];
        
                        if (messages.length > 0) {
                            const lastMessage = messages[messages.length - 1];
                            return {
                                ...conversation,
                                lastMessage: lastMessage.content || 'No content', // Sử dụng giá trị mặc định nếu không có nội dung
                                lastMessageTime: lastMessage.sentAt || '', // Sử dụng giá trị mặc định nếu không có thời gian
                            };
                        } else {
                            // Nếu không có tin nhắn, gán giá trị mặc định
                            return {
                                ...conversation,
                                lastMessage: 'No messages',
                                lastMessageTime: '',
                            };
                        }
                    });
                    setConversations(conversationsWithMessages);
                } else {
                    console.error("Dữ liệu không phải là mảng:", data);
                    setConversations([]); // Gán giá trị mặc định nếu không phải là mảng
                }
            } catch (error) {
                console.error('Error fetching conversations:', error);
            }
        };
        

        fetchConversations();

        if (currentUserId) { // Chỉ kích hoạt khi currentUserId đã có giá trị
            stompClient = new Client({
                webSocketFactory: () => new WebSocket(socketUrl),
                debug: (str) => { console.log(str); },
                onConnect: () => {
                    console.log("Connected to WebSocket");
    
                    stompClient.subscribe(`/user/${currentUserId}/message`, (message) => {
                        const newMessage = JSON.parse(message.body);
                        const exists = messages.some(msg => msg.id === newMessage.id);
                        if (!exists) {
                            console.log(`New Message Sender: ${newMessage.sender}`);
                            setMessages(prevMessages => [...prevMessages, newMessage]);
    
                            setConversations(prevConversations => {
                                return prevConversations.map(conversation => {
                                    if (conversation.id === newMessage.conversationId) {
                                        return {
                                            ...conversation,
                                            lastMessage: newMessage.content,
                                            lastMessageTime: newMessage.sentAt 
                                        };
                                    }
                                    return conversation;
                                });
                            });
                        }
                        const chatWindow = document.querySelector(".messages");
                        if (chatWindow) {
                            chatWindow.scrollTop = chatWindow.scrollHeight;
                        }
                    });
                },
                onStompError: (frame) => {
                    console.error('Broker reported error:', frame.headers['message'], frame.body);
                },
                onWebSocketError: (event) => {
                    console.error("WebSocket error:", event);
                },
                onWebSocketClose: (event) => {
                    console.warn("WebSocket connection closed:", event);
                },
            });
    
            stompClient.activate();
    
            return () => {
                stompClient.deactivate();
            };
        }
    }, [currentUserId]);

    const handleChatSelect = async (conversationId) => {
        if (currentUserId) {
            try {
                const response = await getMessages(conversationId);
                const data = response.data;
                console.log(data.result);
                setMessages(data.result);
                setCurrentChat(conversationId);
            } catch (error) {
                console.error('Error fetching messages:', error);
            }
        }
    };

    const handleSendMessage = async (event) => {
        event.preventDefault();

        if (content && currentChat) {
            const currentConversation = conversations.find(conversation => conversation.id === currentChat);

            if (!currentConversation) {
                console.error("Không tìm thấy cuộc trò chuyện hiện tại.");
                return;
            }

            const receiverId = Array.from(currentConversation.participants)?.find(user => user.id !== currentUserId)?.id;
    
            if (!receiverId) {
                console.error("Không tìm thấy người nhận.");
                return;
            }
    
            console.log("Gửi tin nhắn tới:", receiverId, "Nội dung:", content);
    
            try {
                const response = await sendMessage(receiverId, content);
                console.log("Phản hồi khi gửi tin nhắn:", response.data.result);
                if (response.status === 200) {
                    console.log("Tin nhắn đã được gửi thành công:",  response.data.result);
                    const newMessage = response.data.result; // Giả sử backend trả về tin nhắn đã được tạo
                    console.log(`New Message Sender: ${newMessage.sender.id}`);
                    setMessages(prevMessages => [...prevMessages, newMessage]);
                    setContent(''); // Reset nội dung input
                } else {
                    const errorResponse = response.data;
                    console.log(errorResponse.message || 'Có lỗi xảy ra khi gửi tin nhắn.');
                }
            } catch (error) {
                console.error('Error sending message:', error.message);
                alert('Có lỗi xảy ra, vui lòng thử lại.');
            }
        } else {
            // console.log(currentChat);
            // console.log(content);
            // console.log(stompClient);
            console.log("Vui lòng kiểm tra lại thông tin cuộc trò chuyện và nội dung tin nhắn.");
        }
    };
    
    
    

    return (
        <Scene router={router}>
            <Box className="chat-container">
                <Box className="sidebar">
                    <Box className="search">
                        <IconButton>
                            <SearchIcon />
                        </IconButton>
                        <InputBase placeholder="Search…" />
                    </Box>
                    <Typography variant="h6">All Chats</Typography>
                    {conversations.map((conversation) => (
                        <Box key={conversation.id} className="chat-item" onClick={() => handleChatSelect(conversation.id)}>
                            <Typography className="chat-name">{conversation.name}</Typography>
                            <Typography className="chat-time">{formatTime(conversation.lastMessageTime)}</Typography>
                            <Typography className="chat-preview">{conversation.lastMessage}</Typography>
                        </Box>
                    ))}
                </Box>
                <Box className="chat-window">
                    {currentChat ? (
                        <>
                            <Typography variant="h5">{currentChat}</Typography>   
                            <Box className="messages">
                                {[...messages].reverse().map((message, index) => {
                                    return (
                                        <Box 
                                            key={index} 
                                            className={`message ${message.sender.id === currentUserId ? 'sent' : 'received'}`}
                                        >
                                            <Typography className="message-text">{message.content}</Typography>
                                            <Typography className="message-time">{formatTime(message.sentAt)}</Typography>
                                        </Box>
                                    );
                                })}
                            </Box>
                            <Box className="input-area">
                                <InputBase
                                    placeholder="Write a message..."
                                    className="input-message"
                                    value={content}
                                    onChange={(e) => setContent(e.target.value)}
                                />
                                <IconButton className="send-button" onClick={handleSendMessage}>
                                    <Badge>
                                        <SendIcon color="primary" />
                                    </Badge>
                                </IconButton>
                            </Box>
                        </>
                    ) : (
                        <Typography variant="h6" color="textSecondary" align="center">
                            Chọn một cuộc trò chuyện
                        </Typography>
                    )}
                </Box>
            </Box>
        </Scene>
    );
} 
