import React, { useEffect, useState } from 'react';
import { Box, Typography, InputBase, IconButton, Badge } from '@mui/material';
import { useNavigate } from "react-router-dom";
import { isAuthenticated } from "../../services/authenticationService";
import MailIcon from '@mui/icons-material/Mail';
import SearchIcon from '@mui/icons-material/Search';
import { Client } from '@stomp/stompjs';
import { getConversations, getMessages } from '../../services/chatService';
import { getMyInfo } from "../../services/userService";
import './ChatInterface.css'; // Import CSS file
import SendIcon from '@mui/icons-material/Send';
import Scene from '../Home/Scene';
import { getToken } from "../../services/localStorageService";
import { API } from '../../configurations/confiruration';

export default function ChatInterface() {
    const [conversations, setConversations] = useState([]);
    const [messages, setMessages] = useState([]);
    const [currentChat, setCurrentChat] = useState(null);
    const [content, setContent] = useState('');
    const navigate = useNavigate();
    const [currentUserId, setCurrentUserId] = useState(null);
    const socketUrl = 'http://localhost:8080/ws';
  
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

    useEffect(() => {
        if (!isAuthenticated()) {
            navigate("/login");
            return;
        } 
        getUserDetails();
    }, [navigate]);

    useEffect(() => {
        const fetchConversations = async () => {
            try {
                const response = await getConversations();
                const data = response.data;

                if (Array.isArray(data.result)) {
                    const conversationsWithMessages = data.result.map(conversation => ({
                        ...conversation,
                        lastMessage: conversation.messages.length > 0 ? conversation.messages[conversation.messages.length - 1].content : 'No messages',
                        lastMessageTime: conversation.messages.length > 0 ? conversation.messages[conversation.messages.length - 1].sentAt : '',
                    }));
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

        stompClient = new Client({
            webSocketFactory: () => {
              return new WebSocket(socketUrl);
            },
            debug: (str) => { console.log(str); },
            onConnect: () => {
                console.log("Connected to WebSocket");

                stompClient.subscribe('/topic/messages', (message) => {
                    const newMessage = JSON.parse(message.body);
                    const exists = messages.some(msg => msg.id === newMessage.id);
                    if (!exists) {
                        console.log(`New Message Sender: ${newMessage.sender}`);
                        setMessages(prevMessages => [...prevMessages, newMessage]);

                        // Cập nhật conversations với lastMessage và lastMessageTime mới
                        setConversations(prevConversations => {
                            return prevConversations.map(conversation => {
                                if (conversation.id === newMessage.conversationId) {
                                    return {
                                        ...conversation,
                                        lastMessage: newMessage.content,
                                        lastMessageTime: newMessage.sentAt // Thay đổi thời gian gửi
                                    };
                                }
                                return conversation;
                            });
                        });
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
    }, []);

    const handleChatSelect = async (conversationId) => {
        if (currentUserId) {
            try {
                const response = await getMessages(conversationId);
                const data = response.data;
                console.log(data);
                setMessages(data.result.reverse());
                setCurrentChat(conversationId);
            } catch (error) {
                console.error('Error fetching messages:', error);
            }
        }
    };

    const handleSendMessage = () => {
        if (content && currentChat && stompClient) {
            const messageData = {
                conversationId: currentChat,
                text: content,
                sender: currentUserId, // Sử dụng ID của người dùng hiện tại
            };

            stompClient.publish({
                destination: "/app/message", // Địa chỉ nơi bạn sẽ gửi tin nhắn
                body: JSON.stringify(messageData),
            });

            setContent(''); // Reset nội dung input
        }
    };

    return (
        <Scene hideSearch={true}>
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
                            <Typography className="chat-time">{new Date(conversation.lastMessageTime).toLocaleTimeString()}</Typography>
                            <Typography className="chat-preview">{conversation.lastMessage}</Typography>
                        </Box>
                    ))}
                </Box>
                <Box className="chat-window">
                    {currentChat ? (
                        <>
                            <Typography variant="h5">{currentChat}</Typography>   
                            <Box className="messages">
                                {messages.map((message, index) => {
                                    console.log(`Message Sender: ${currentChat}`); // Log sender

                                    return (
                                        <Box 
                                            key={index} 
                                            className={`message ${message.sender.id === currentUserId ? 'sent' : 'received'}`}
                                        >
                                            <Typography className="message-text">{message.content}</Typography>
                                            <Typography className="message-time">{new Date(message.sentAt).toLocaleTimeString()}</Typography>
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
