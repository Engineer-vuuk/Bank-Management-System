import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Register.css";

const Register = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [fullName, setFullName] = useState("");
    const [dob, setDob] = useState("");
    const [kinName, setKinName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [kinPhoneNumber, setKinPhoneNumber] = useState("");
    const [idNumber, setIdNumber] = useState("");
    const [pin, setPin] = useState("");
    const [confirmPin, setConfirmPin] = useState("");
    const [role, setRole] = useState("User");
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setLoading(true);

        if (!/^\d{4}$/.test(pin)) {
            setMessage("❌ Transaction PIN must be exactly 4 digits.");
            setLoading(false);
            return;
        }

        if (pin !== confirmPin) {
            setMessage("❌ PINs do not match!");
            setLoading(false);
            return;
        }

        try {
            const response = await axios.post(`${API_BASE_URL}/auth/register`, {
                username: username.trim(),
                email: email.trim(),
                fullName: fullName.trim(),
                dob,
                kinName: kinName.trim(),
                phoneNumber,
                kinPhoneNumber,
                idNumber,
                pin,
                role,
            });

            if (response.status === 201) {
                setMessage("✅ Registration successful! Please check your email to confirm your account.");

                console.log("✅ Navigating to login...");

                // ✅ IMMEDIATE REDIRECT AFTER REGISTRATION (NO DELAY)
                setTimeout(() => {
                    navigate("/login");
                }, 1500); // ✅ Redirect to login page faster
            } else {
                setMessage(response.data?.message || "⚠️ Registration failed. Please try again.");
            }
        } catch (error) {
            console.error("❌ Registration error:", error);
            setMessage(error.response?.data?.message || "⚠️ An unexpected error occurred. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="register-container">
            <div className="register-right">
                <h2>Create an Account</h2>
                <form onSubmit={handleSubmit}>
                    <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} required disabled={loading} />
                    <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required disabled={loading} />
                    <input type="text" placeholder="Full Name" value={fullName} onChange={(e) => setFullName(e.target.value)} required disabled={loading} />
                    <input type="date" placeholder="Date of Birth" value={dob} onChange={(e) => setDob(e.target.value)} required disabled={loading} />
                    <input type="text" placeholder="Next of Kin Name" value={kinName} onChange={(e) => setKinName(e.target.value)} required disabled={loading} />
                    <input type="text" placeholder="Phone Number" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} required disabled={loading} />
                    <input type="text" placeholder="Kin Phone Number" value={kinPhoneNumber} onChange={(e) => setKinPhoneNumber(e.target.value)} required disabled={loading} />
                    <input type="text" placeholder="ID Number" value={idNumber} onChange={(e) => setIdNumber(e.target.value)} required disabled={loading} />
                    <input type="password" placeholder="Transaction PIN (4 digits)" value={pin} onChange={(e) => setPin(e.target.value)} required disabled={loading} />
                    <input type="password" placeholder="Confirm PIN" value={confirmPin} onChange={(e) => setConfirmPin(e.target.value)} required disabled={loading} />
                    <select value={role} onChange={(e) => setRole(e.target.value)} disabled={loading}>
                        <option value="User">User</option>
                        <option value="Admin">Admin</option>
                    </select>
                    <label className="terms">
                        <input type="checkbox" required disabled={loading} />
                        I agree to SmartBank's <a href="#">Terms of Service</a> and <a href="#">Privacy Policy</a>.
                    </label>
                    <button type="submit" disabled={loading}>{loading ? "Signing Up..." : "Sign Up"}</button>
                </form>

                {/* ✅ FIXED: Ensure login button navigates to /login */}
                <p className="login-link">
                    Already have an account?
                    <span onClick={() => navigate("/login")} style={{ cursor: "pointer", color: "blue" }}>
                        Log in
                    </span>
                </p>

                {message && <p className="message">{message}</p>}
            </div>
        </div>
    );
};

export default Register;
