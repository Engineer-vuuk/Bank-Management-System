import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Route, Routes, Navigate, useLocation } from "react-router-dom";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import AdminDashboard from "./pages/AdminDashboard";
import ForgotPassword from "./pages/ForgotPassword";
import DepositForm from "./pages/DepositForm";
import WithdrawForm from "./pages/WithdrawForm";
import TransferForm from "./pages/TransferForm";
import RequestForm from "./pages/RequestForm";
import Transactions from "./pages/Transactions";
import ConfirmationPage from "./pages/ConfirmationPage"; // Email confirmation page
import LandingPage from "./pages/LandingPage"; // Import the LandingPage
import "./App.css";

// 🔹 ProtectedRoute Component
const ProtectedRoute = ({ element }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(null);
    const location = useLocation();

    useEffect(() => {
        const checkAuth = () => {
            const token = localStorage.getItem("token");
            setIsAuthenticated(!!token);
            console.log("Auth Check:", token ? "Authenticated" : "Not Authenticated");
        };

        checkAuth();
        window.addEventListener("storage", checkAuth); // ✅ Listen for changes to localStorage

        return () => window.removeEventListener("storage", checkAuth);
    }, [location.pathname]); // ✅ Runs on path change

    if (isAuthenticated === null) return <div>Loading...</div>; // ✅ Prevents redirect flickering

    return isAuthenticated ? element : <Navigate to="/login" replace />;
};

const App = () => {
    return (
        <Router>
            <Routes>
                {/* Landing Page Route */}
                <Route path="/" element={<LandingPage />} />

                {/* Other Routes */}
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/forgot-password" element={<ForgotPassword />} />
                <Route path="/confirm" element={<ConfirmationPage />} />

                {/* Protected routes */}
                <Route path="/dashboard" element={<ProtectedRoute element={<Dashboard />} />} />
                <Route path="/admin/dashboard" element={<ProtectedRoute element={<AdminDashboard />} />} />
                <Route path="/deposit" element={<ProtectedRoute element={<DepositForm />} />} />
                <Route path="/withdraw" element={<ProtectedRoute element={<WithdrawForm />} />} />
                <Route path="/transfer" element={<ProtectedRoute element={<TransferForm />} />} />
                <Route path="/request" element={<ProtectedRoute element={<RequestForm />} />} />
                <Route path="/transactions" element={<ProtectedRoute element={<Transactions />} />} />
            </Routes>
        </Router>
    );
};

export default App;
