import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./dashboard.css";

const Dashboard = () => {
    const navigate = useNavigate();
    const [accountNumber, setAccountNumber] = useState(null);
    const [menuOpen, setMenuOpen] = useState(false);
    const [profileOpen, setProfileOpen] = useState(false);

    useEffect(() => {
        // Get stored account number from localStorage
        const storedAccountNumber = localStorage.getItem("accountNumber");
        if (storedAccountNumber) {
            setAccountNumber(storedAccountNumber);
        }
    }, []);

    return (
        <div className="dashboard-container">
            <div className="sidebar">
                <button className="menu-toggle" onClick={() => setMenuOpen(!menuOpen)}>
                    ☰ Menu
                </button>
                {menuOpen && (
                    <div className="dropdown-menu">
                        <button onClick={() => navigate("/open-account")}>Open Account</button>
                        <button onClick={() => navigate("/deposit")}>Deposit</button>
                        <button onClick={() => navigate("/withdraw")}>Withdraw</button>
                        <button onClick={() => navigate("/transfer")}>Transfer</button>
                        <button className="logout-button" onClick={() => navigate("/login")}>
                            Logout
                        </button>
                    </div>
                )}
            </div>

            <div className="main-content">
                <div className="header">
                    <h2>Welcome to Your Dashboard</h2>
                    <button className="profile-button" onClick={() => setProfileOpen(!profileOpen)}>
                        👤 Profile
                    </button>
                </div>
                {profileOpen && (
                    <div className="profile-info">
                        <p><strong>Name:</strong> John Doe</p>
                        <p><strong>Email:</strong> johndoe@example.com</p>
                        <p><strong>Phone:</strong> +1234567890</p>
                    </div>
                )}
                {accountNumber ? (
                    <p>Your Account Number: <strong>{accountNumber}</strong></p>
                ) : (
                    <p>No account found. Please open an account.</p>
                )}
                <div className="home-images">
                    <img src="/images/banking1.jpg" alt="Banking Services" />
                    <img src="/images/banking2.jpg" alt="Financial Features" />
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
