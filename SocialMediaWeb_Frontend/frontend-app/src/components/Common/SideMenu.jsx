import * as React from "react";
import Divider from "@mui/material/Divider";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Toolbar from "@mui/material/Toolbar";
import HomeIcon from "@mui/icons-material/Home";
import PeopleIcon from "@mui/icons-material/People";
import GroupsIcon from "@mui/icons-material/Groups";
import './SideMenu.css'; // Import CSS file

function SideMenu() {
  return (
    <>
      <Toolbar />
      <List>
        <ListItem key={"home"} disablePadding>
          <ListItemButton>
            <ListItemIcon>
              <HomeIcon />
            </ListItemIcon>
            <ListItemText
              primary={"Home"}
              primaryTypographyProps={{ className: "side-menu-list-item-text" }}
            />
          </ListItemButton>
        </ListItem>
        <ListItem key={"friends"} disablePadding>
          <ListItemButton>
            <ListItemIcon>
              <PeopleIcon />
            </ListItemIcon>
            <ListItemText
              primary={"Friends"}
              primaryTypographyProps={{ className: "side-menu-list-item-text" }}
            />
          </ListItemButton>
        </ListItem>
        <ListItem key={"groups"} disablePadding>
          <ListItemButton>
            <ListItemIcon>
              <GroupsIcon />
            </ListItemIcon>
            <ListItemText
              primary={"Groups"}
              primaryTypographyProps={{ className: "side-menu-list-item-text" }}
            />
          </ListItemButton>
        </ListItem>
      </List>
      <Divider />
    </>
  );
}

export default SideMenu;
