import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Login.css";

const Login = () => {
    const [username, setUsername] = useState("");
    const [pin, setPin] = useState("");
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigate = useNavigate();

    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

    useEffect(() => {
        const token = localStorage.getItem("token");
        const role = localStorage.getItem("role");

        if (token) {
            console.log("🔹 Redirecting based on role...");
            if (role === "ADMIN") {
                navigate("/admin/dashboard");
            } else {
                navigate("/dashboard");
            }
        }
    }, [isLoggedIn, navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setLoading(true);

        if (!username.trim() || !pin.trim()) {
            setMessage("⚠️ Please enter both username and PIN.");
            setLoading(false);
            return;
        }

        if (!/^\d{4}$/.test(pin)) {
            setMessage("⚠️ PIN must be exactly 4 digits.");
            setLoading(false);
            return;
        }

        console.log("Attempting to log in with username:", username);

        try {
            const response = await axios.post(`${API_BASE_URL}/auth/login`, {
                username: username.trim(),
                pin: pin.trim()
            }, {
                headers: { "Content-Type": "application/json" },
                withCredentials: true,
            });

            if (response.status === 200 && response.data?.token) {
                setMessage("✅ Login successful! Redirecting...");

                localStorage.setItem("token", response.data.token);
                localStorage.setItem("username", response.data.username || "");
                localStorage.setItem("role", response.data.role || "");
                localStorage.setItem("accountNumber", response.data.accountNumber || "");

                console.log("🔹 Stored in localStorage: ", {
                    token: response.data.token,
                    username: response.data.username,
                    role: response.data.role,
                    accountNumber: response.data.accountNumber
                });

                setTimeout(() => {
                    setIsLoggedIn(true);
                    if (response.data.role === "ADMIN") {
                        navigate("/admin/dashboard");
                    } else {
                        navigate("/dashboard");
                    }
                }, 500);
            } else {
                setMessage(response.data?.message || "⚠️ Invalid username or PIN.");
            }
        } catch (error) {
            console.error("❌ Login error:", error);
            setMessage(error.response?.data?.message || "⚠️ Unable to login. Check credentials and try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-container">
            <div className="login-box">
                <h2>Login</h2>
                <form onSubmit={handleSubmit}>
                    <label>Username</label>
                    <input
                        type="text"
                        placeholder="Enter your username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        disabled={loading}
                    />

                    <label>PIN</label>
                    <input
                        type="password"
                        placeholder="Enter your 4-digit PIN"
                        value={pin}
                        onChange={(e) => setPin(e.target.value)}
                        required
                        disabled={loading}
                    />

                    <div className="forgot-password">
                        <span onClick={() => navigate("/forgot-password")}>Forgot PIN?</span>
                    </div>

                    <button type="submit" disabled={loading}>
                        {loading ? "Signing in..." : "Sign In"}
                    </button>
                </form>

                {message && <p className="message">{message}</p>}

                <p className="register-link">
                    Don't have an account? <span onClick={() => navigate("/register")}>Sign Up</span>
                </p>
            </div>
        </div>
    );
};

export default Login;
