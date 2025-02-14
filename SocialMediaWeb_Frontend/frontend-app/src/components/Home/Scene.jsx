import * as React from "react";
import { Box, IconButton, Stack } from "@mui/material";
import LogoutIcon from '@mui/icons-material/Logout';
import { createTheme, ThemeProvider } from "@mui/material/styles";
import HomeOutlinedIcon from '@mui/icons-material/HomeOutlined';
import SearchIcon from "@mui/icons-material/Search";
import ChatBubbleOutlineOutlinedIcon from '@mui/icons-material/ChatBubbleOutlineOutlined';
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import { AppProvider } from "@toolpad/core/AppProvider";
import { DashboardLayout } from "@toolpad/core/DashboardLayout";
import { useDemoRouter } from "@toolpad/core/internal"; // Hoặc thay bằng router thực tế
import NotificationDropdown from '../Common/NotificationDropdown';
import ChatInterface from '../Common/ChatInterface';
import Home from './Home';
import DarkLogo from '../assests/DarkLogo.png';
import { isAuthenticated, logOut } from "../../services/authenticationService";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { SignOutButton, AccountPopoverFooter } from '@toolpad/core/Account';
import Divider from '@mui/material/Divider';
import './Scene.css';

const handleLogout = (event) => {
  logOut();
  window.location.href = "/login";
};

const NAVIGATION = [
  { kind: 'header', title: 'Main Navigation' },
  { segment: 'home', title: 'Home', icon: <HomeOutlinedIcon /> },
  { segment: 'search', title: 'Search', icon: <SearchIcon /> },
  { segment: 'chat', title: 'Messages', icon: <ChatBubbleOutlineOutlinedIcon /> },
  { segment: 'profile', title: 'Profile', icon: <AccountCircleOutlinedIcon /> },
];

const demoTheme = createTheme({
  cssVariables: { colorSchemeSelector: 'data-toolpad-color-scheme' },
  colorSchemes: { light: true, dark: true },
});




function DemoPageContent({ pathname }) {
  const navigate = useNavigate();
  console.log(pathname);
  console.log(isAuthenticated);

  useEffect(() => {
    if (pathname) {
      navigate(pathname);
    }
  }, [pathname, navigate]); // Chạy khi pathname thay đổi

  return null;
}

function SidebarFooter({ mini }) {
  return (
    <Stack>
      <IconButton
        size="small"
        aria-label="log out"
        color="inherit"
        sx={{
          margin: "10px 10px",
          borderRadius: "10px",
          justifyContent: "center",
          gap: "10px",
          padding: "12px 0"
        }} onClick={handleLogout}>
        <div className="logout-button" style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <LogoutIcon />
          {!mini && <p>Log out</p>}
        </div>
      </IconButton>
    </Stack>
  );
}


export default function Scene({ children, router}) {
  console.log(router); // Định nghĩa đường dẫn ban đầu
  console.log(demoTheme);

  return (
    <ThemeProvider theme={demoTheme}>
      <AppProvider
        navigation={NAVIGATION}
        branding={{
          logo: <img src={DarkLogo} alt="SMW logo" style={{ borderRadius: '50px' }} />,
          title: 'SocialMediaWeb',
          homeUrl: 'http://localhost:3000/home'
        }}
        theme={demoTheme}
        router={router}>
        <DashboardLayout slots={{ sidebarFooter: SidebarFooter }}>
          <DemoPageContent pathname={router.pathname} />
            {children}
        </DashboardLayout>
      </AppProvider>
    </ThemeProvider>
  );
}
