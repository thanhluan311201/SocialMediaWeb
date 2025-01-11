import * as React from "react";
import { styled } from "@mui/material/styles";
import Box from "@mui/material/Box";
import IconButton from "@mui/material/IconButton";
import InputBase from "@mui/material/InputBase";
import Badge from "@mui/material/Badge";
import MenuItem from "@mui/material/MenuItem";
import Menu from "@mui/material/Menu";
import SearchIcon from "@mui/icons-material/Search";
import AccountCircle from "@mui/icons-material/AccountCircle";
import MailIcon from "@mui/icons-material/Mail";
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import ChatBubbleOutlineOutlinedIcon from '@mui/icons-material/ChatBubbleOutlineOutlined';
import { logOut } from "../../services/authenticationService";
import './Header.css'; // Import CSS file
import NotificationDropdown from './NotificationDropdown'; // Import NotificationDropdown component
import HomeOutlinedIcon from '@mui/icons-material/HomeOutlined';

const SearchIconWrapper = styled("div")({
  padding: "0 16px",
  height: "100%",
  position: "absolute",
  pointerEvents: "none",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
});

const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: "inherit",
  "& .MuiInputBase-input": {
    padding: theme.spacing(1, 1, 1, 0),
    paddingLeft: `calc(1em + ${theme.spacing(4)})`,
    transition: theme.transitions.create("width"),
    width: "100%",
    [theme.breakpoints.up("md")]: {
      width: "20ch",
    },
  },
}));

export default function Header({ hideSearch }) {
  const [anchorElUser, setAnchorElUser] = React.useState(null);

  const isMenuOpen = Boolean(anchorElUser);

  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  const handleLogout = (event) => {
    handleCloseUserMenu();
    logOut();
    window.location.href = "/login";
  };

  const menuId = "primary-search-account-menu";
  const renderMenu = (
    <Menu
      sx={{ mt: '45px' }}
      id="menu-appbar"
      anchorEl={anchorElUser}
      anchorOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      keepMounted
      transformOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      open={Boolean(anchorElUser)}
      onClose={handleCloseUserMenu}
    >
      <MenuItem onClick={handleCloseUserMenu}>Profile</MenuItem>
      <MenuItem onClick={handleCloseUserMenu}>Settings</MenuItem>
      <MenuItem onClick={handleLogout}>Log Out</MenuItem>
    </Menu>
  );

  return (
    <>
      <div className="logo">
        <a href="/home">SocialMediaWeb</a>
      </div>

      <Box className="desktop-menu">
      <IconButton
          size="large"
          aria-label="show 4 new mails"
          color="inherit"
          sx={{
            margin: "10px 0",
            borderRadius: "10px",
            justifyContent: "flex-start",
            gap: "20px"
          }}  href="/home">
            <HomeOutlinedIcon sx={{ width: "1.5em", height: "1.5em" }}/>
          <p>Home</p>
        </IconButton>
        <IconButton
          size="large"
          color="inherit"
          sx={{
            margin: "10px 0",
            borderRadius: "10px",
            justifyContent: "flex-start",
            gap: "20px"
          }}>
          <SearchIcon sx={{ width: "1.5em", height: "1.5em" }} />
          <p>Search</p>
        </IconButton>
        <IconButton
          size="large"
          aria-label="show 4 new mails"
          color="inherit"
          sx={{
            margin: "10px 0",
            borderRadius: "10px",
            justifyContent: "flex-start",
            gap: "20px"
          }}  href="/chat">
            <ChatBubbleOutlineOutlinedIcon sx={{ width: "1.5em", height: "1.5em" }}/>
          <p>Messages</p>
        </IconButton>

        {/* Đặt NotificationDropdown trong desktop menu */}
        <NotificationDropdown />

        <IconButton
          size="large"
          aria-label="account of current user"
          aria-controls={menuId}
          aria-haspopup="true"
          onClick={handleOpenUserMenu}
          color="inherit"
          sx={{
            margin: "10px 0",
            borderRadius: "10px",
            justifyContent: "flex-start",
            gap: "20px"
          }}
        >
          <AccountCircleOutlinedIcon sx={{ width: "1.5em", height: "1.5em" }} />
          <p>Profile</p>
        </IconButton>

      </Box>
      {renderMenu}
    </>
  );
}
