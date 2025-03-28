import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import OpenAccount from "./pages/OpenAccount";
import ForgotPassword from "./pages/ForgotPassword";
import "./App.css";

const App = () => {
    return (
        <Router>
            <div>
                <Routes>
                    <Route path="/" element={<Register />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/open-account" element={<OpenAccount />} />
                    <Route path="/forgot-password" element={<ForgotPassword />} />
                </Routes>
            </div>
        </Router>
    );
};

export default App;
