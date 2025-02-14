import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Box, Card, CircularProgress, Typography } from "@mui/material";
import { getMyInfo } from "../../services/userService";
import { getPosts } from "../../services/postService";
import { isAuthenticated } from "../../services/authenticationService";
import Scene from "./Scene";
import './Home.css'; // Import CSS file
import { useDemoRouter } from "@toolpad/core/internal";
import { format, formatDistanceToNow, isToday, isYesterday, differenceInHours } from "date-fns";
import { vi } from "date-fns/locale";
import Button from '@mui/material/Button';
import ButtonGroup from '@mui/material/ButtonGroup';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import ModeCommentIcon from '@mui/icons-material/ModeComment';
import ReplyIcon from '@mui/icons-material/Reply';

export default function Home() {
    const navigate = useNavigate();
    const router = useDemoRouter('/home');
    const [newfeeds, setNewfeeds] = useState([]);
    console.log(router);

    const formatPostTime = (createdAt) => {
      const createdDate = new Date(createdAt);
    
      // Nếu bài viết được đăng trong vòng 24 giờ trước
      if (differenceInHours(new Date(), createdDate) < 24) {
        return formatDistanceToNow(createdDate, { addSuffix: true, locale: vi });
      }
    
      // Nếu bài viết đăng hôm qua
      if (isYesterday(createdDate)) {
        return `Hôm qua, ${format(createdDate, "HH:mm")}`;
      }
    
      // Nếu bài viết đăng trước đó, hiển thị ngày tháng năm
      return format(createdDate, "dd/MM/yyyy HH:mm");
    };


    const getNewfeeds = async () => {
      try {
        const response = await getPosts();
        const data = response.data;
    
        console.log("Dữ liệu nhận được:", data);
    
        if (Array.isArray(data.result)) {
          setNewfeeds(data.result);
        } else {
          setNewfeeds([]); 
        }
      } catch (error) {
        console.log("Lỗi khi lấy bài viết:", error);
        setNewfeeds([]); 
      }
    };
    
  
    useEffect(() => {
      if (!isAuthenticated()) {
        navigate("/login");
      } else {
        getNewfeeds();

        console.log(isAuthenticated);
      }
    }, [navigate]);

    useEffect(() => {
      document.body.className = 'home';  // Đặt class cho body là 'home'
      console.log(isAuthenticated);
      
      return () => {
          document.body.className = '';  // Xóa class khi component bị unmount
      };
    }, []);

    if (!isAuthenticated()) {
      return null; // Bạn có thể xử lý việc này theo cách khác nếu cần
    }

    return (
      <Scene router={router}>
          
            {Array.isArray(newfeeds) && newfeeds.length > 0 ? (
              [...newfeeds].reverse().map((newfeed, index) => (
                <div className="container">
                  <Card className="card">
                  <Box key={newfeed.id || index} className="container-box">
                    <Typography
                     sx={{
                      fontSize: "30px",
                      fontWeight: "bold"
                     }}
                    >
                      {newfeed.author.firstname + " " + newfeed.author.lastname}
                    </Typography>
                    <Typography
                      sx={{
                        fontSize: "15px"
                      }}
                    >
                      {formatPostTime(newfeed.createdAt)}
                    </Typography>
                    <Typography>{newfeed.content}</Typography>
                  </Box>
                  <ButtonGroup 
                    sx={{
                      width: "100%",

                    }}
                    variant="text" aria-label="Basic button group">
                    <Button sx={{width: "33%"}}><ThumbUpIcon/></Button>
                    <Button sx={{width: "33%"}}><ModeCommentIcon/></Button>
                    <Button sx={{width: "33%"}}><ReplyIcon/></Button>
                  </ButtonGroup>
                </Card>
                </div>
              ))
            ) : (
              <Typography className="empty-message">Không có bài viết nào.</Typography>
            )}
          
      </Scene>
    );
}