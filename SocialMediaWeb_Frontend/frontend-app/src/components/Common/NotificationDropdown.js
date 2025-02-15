import React, { useState, useEffect } from "react";
import { IconButton, Badge, Menu, MenuItem, ListItemText } from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import NotificationsNoneOutlinedIcon from '@mui/icons-material/NotificationsNoneOutlined';
import { getNotifications } from "../../services/notificationService";
import { Client } from '@stomp/stompjs';
import { getToken } from "../../services/localStorageService";
import { getMyInfo } from "../../services/userService";
import { isAuthenticated } from "../../services/authenticationService";
import { useNavigate } from "react-router-dom";

const NotificationDropdown = () => {
  const [anchorEl, setAnchorEl] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [newNotifications, setNewNotifications] = useState(0);
  const socketUrl = 'http://localhost:8080/ws';
  const isMenuOpen = Boolean(anchorEl);
  const navigate = useNavigate();

  useEffect(() => {
    fetchNotifications(); // Lấy danh sách thông báo ban đầu

    if(isAuthenticated()){
      const stompClient = new Client({
        webSocketFactory: () => {
          return new WebSocket(socketUrl);
        },
        debug: (str) => { console.log(str); },
        onConnect: async () => {
          try {
            console.log("Connected to WebSocket");
            const response = await getMyInfo();
            const userId = response.data.result.id;
            
            stompClient.subscribe(`/user/${userId}/notification`, (message) => {
              const notification = JSON.parse(message.body);
              setNotifications((prev) => [notification, ...prev]);
              setNewNotifications((prev) => prev + 1);
          });
          } catch (error) {
            console.error("Lỗi khi kết nối WebSocket:", error);
          }
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
  }, []);

  const fetchNotifications = async () => {
    try {
      const response = await getNotifications();
      setNotifications(response.data.result);
    } catch (error) {
      console.error("Lỗi khi lấy thông báo:", error);
    }
  };

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
    setNewNotifications(0);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleNotificationSelect = async (url) => {
    try {
        navigate("/chat");
        const response = await url;
    } catch (error) {
        console.error('Error fetching messages:', error);
    }
  };

  return (
    <div>
      <IconButton type="button" size="small" color="inherit" onClick={handleMenuOpen} sx={{padding: "8px"}}>
        <Badge badgeContent={newNotifications} color="error">
          <NotificationsNoneOutlinedIcon sx={{width: "24px", height: "24px"}} />
        </Badge>
      </IconButton>
      <Menu
        anchorEl={anchorEl}
        open={isMenuOpen}
        onClose={handleMenuClose}
        PaperProps={{
          style: { maxHeight: 300, width: '35ch' },
        }}
      >
        
        {notifications.length > 0 ? (
          notifications.map((notification, index) => {
            return(
              <MenuItem key={index} 
              onClick={() => navigate('/chat', { state: { redirectUrl: notification.redirectUrl } })}
              >
              <ListItemText primary={notification.content} />
            </MenuItem>
            )
          })
        ) : (
          <MenuItem>
            <ListItemText primary="Không có thông báo nào" />
          </MenuItem>
        )}
      </Menu>
    </div>
  );
};

export default NotificationDropdown;
