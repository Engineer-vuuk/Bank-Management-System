import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./login.css";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    // 🔹 Redirect logged-in users to dashboard automatically
    useEffect(() => {
        if (localStorage.getItem("accountNumber")) {
            navigate("/dashboard");
        }
    }, [navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post("http://localhost:8080/api/login", {
                email,
                password,
            });

            if (response.status === 200 && response.data.accountNumber) {
                setMessage("Login successful! Redirecting to dashboard...");

                // ✅ Store account number to prevent "Open Account" link from showing
                localStorage.setItem("accountNumber", response.data.accountNumber);

                setTimeout(() => {
                    navigate("/dashboard");
                }, 2000);
            } else {
                setMessage("Invalid email or password!");
            }
        } catch (error) {
            setMessage("Error during login");
        }
    };

    return (
        <div className="login-container">
            <div className="login-box">
                <h2>Login</h2>
                <form onSubmit={handleSubmit}>
                    <label>Email</label>
                    <input
                        type="email"
                        placeholder="Enter your email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />

                    <label>Password</label>
                    <input
                        type="password"
                        placeholder="Enter your password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />

                    <div className="forgot-password">
                        <span onClick={() => navigate("/forgot-password")}>Forgot Password?</span>
                    </div>

                    <button type="submit">Sign In</button>
                </form>
                <p className="message">{message}</p>

                {/* ✅ Restored "Go to Dashboard" Button */}
                <button className="dashboard-button" onClick={() => navigate("/dashboard")}>
                    Go to Dashboard
                </button>
            </div>
        </div>
    );
};

export default Login;
