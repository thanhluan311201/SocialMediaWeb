import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import Toolbar from "@mui/material/Toolbar";
import { useTheme } from "@mui/material";
import Header from "../Common/Header";

function Scene({ children, hideSearch }) {
  const theme = useTheme();

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column'}}>
      <AppBar>
        <Toolbar>
          <Header hideSearch={hideSearch} />
        </Toolbar>
      </AppBar>
      <Box>
        <Box>
          {children}
        </Box>
      </Box>
    </Box>
  );
}

export default Scene;