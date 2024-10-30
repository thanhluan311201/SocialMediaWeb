import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import Login from "../components/User/Login";
import Home from "../components/Home/Home";
import ChatInterface from "../components/Common/ChatInterface";


const AppRoutes = () => {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element = {<Navigate to = "/login"/>}/>
                <Route path="/login" element = {<Login />} />
                <Route path="/home" element = {<Home />} />
                <Route path="/chat" element = {<ChatInterface />} />
            </Routes>
        </Router>
    );
};

export default AppRoutes;