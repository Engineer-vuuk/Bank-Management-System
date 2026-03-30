import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./ForgotPassword.css";

const ForgotPassword = () => {
    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    const handleReset = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post("http://localhost:8080/api/forgot-password", {
                email,
            });

            if (response.data === "Reset link sent!") {
                setMessage("Check your email for the reset link.");
            } else {
                setMessage("Email not found!");
            }
        } catch (error) {
            setMessage("Error sending reset link");
        }
    };

    return (
        <div className="forgot-container">
            <div className="forgot-box">
                <h2>Forgot Password?</h2>
                <p>Enter your email to receive a reset link.</p>
                <form onSubmit={handleReset}>
                    <label>Email</label>
                    <input
                        type="email"
                        placeholder="Enter your email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <button type="submit">Send Reset Link</button>
                </form>
                <p className="message">{message}</p>
                <button className="back-button" onClick={() => navigate("/Login")}>
                    Back to Login
                </button>
            </div>
        </div>
    );
};

export default ForgotPassword;
