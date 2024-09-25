import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import Login from "../components/User/Login";
import Home from "../components/Home/Home";


const AppRoutes = () => {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element = {<Navigate to = "/login"/>}/>
                <Route path="/login" element = {<Login />} />
                <Route path="/home" element = {<Home />} />
            </Routes>
        </Router>
    );
};

export default AppRoutes;