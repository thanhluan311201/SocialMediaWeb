import React, { useEffect } from "react";
import { isTokenExpired, getToken, removeToken } from "./services/localStorageService";
import './App.css';
import AppRoutes from "./routes/AppRoutes";

function App() {
  useEffect(() => {
    const token = getToken();
    if (isTokenExpired(token)) {
        removeToken(); // Xóa token nếu hết hạn
    }
    }, []);

  return (
    <>
      <AppRoutes />
    </>
    
  );
}

export default App;
