import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./register.css";

const Register = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Check if passwords match
        if (password !== confirmPassword) {
            setMessage("Passwords do not match!");
            return;
        }

        try {
            await axios.post("http://localhost:8080/api/register", {
                username,
                email,
                password,
            });

            setMessage("User registered successfully! Redirecting to login...");
            setTimeout(() => {
                navigate("/login"); // Redirect to login after successful registration
            }, 2000);
        } catch (error) {
            setMessage("Error during registration");
        }
    };

    return (
        <div className="register-container">
            {/* Left Panel */}
            <div className="register-left">
                <h2>Welcome to</h2>
                <h1>SmartBank Community</h1>
                <p>Secure. Smart. Seamless Banking</p>
            </div>

            {/* Right Panel (Form Section) */}
            <div className="register-right">
                <h2>Create an Account</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Confirm Password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />

                    <label className="terms">
                        <input type="checkbox" required />
                        I agree to SmartBank's <a href="#">Terms of Service</a> and <a href="#">Privacy Policy</a>.
                    </label>

                    <button type="submit">Sign Up</button>
                </form>

                {/* Login Link */}
                <p className="login-link">
                    Already have an account?{" "}
                    <span onClick={() => navigate("/login")}>Log in</span>
                </p>

                <p className="message">{message}</p>
            </div>
        </div>
    );
};

export default Register;
