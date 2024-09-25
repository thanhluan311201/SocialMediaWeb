import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Box, Card, CircularProgress, Typography } from "@mui/material";
import { getMyInfo } from "../../services/userService";
import { isAuthenticated } from "../../services/authenticationService";
import Scene from "./Scene";
import './Home.css'; // Import CSS file
import Header from "../Common/Header";

export default function Home() {
    const navigate = useNavigate();
    const [userDetails, setUserDetails] = useState({});
  
    const getUserDetails = async () => {
      try {
        const response = await getMyInfo();
        const data = response.data;
  
        console.log(data);
  
        setUserDetails(data.result);
      } catch (error) {}
    };
  
    useEffect(() => {
      if (!isAuthenticated()) {
        navigate("/login");
      } else {
        getUserDetails();
      }
    }, [navigate]);
  
    return (
      <Scene>
        {userDetails ? (
          <Card className="card">
            <Box className="container-box">
              <Typography className="welcome-text">
                Welcome back to Devteria, {userDetails.username}!
              </Typography>
              <Box className="info-row">
                <Typography className="info-label">User Id</Typography>
                <Typography className="info-value">{userDetails.id}</Typography>
              </Box>
              <Box className="info-row">
                <Typography className="info-label">First Name</Typography>
                <Typography className="info-value">{userDetails.firstname}</Typography>
              </Box>
              <Box className="info-row">
                <Typography className="info-label">Last Name</Typography>
                <Typography className="info-value">{userDetails.lastname}</Typography>
              </Box>
              <Box className="info-row">
                <Typography className="info-label">Date of birth</Typography>
                <Typography className="info-value">{userDetails.dob}</Typography>
              </Box>
            </Box>
          </Card>
        ) : (
          <Box className="loading-box">
            <CircularProgress />
            <Typography>Loading ...</Typography>
          </Box>
        )}
      </Scene>
    );
}