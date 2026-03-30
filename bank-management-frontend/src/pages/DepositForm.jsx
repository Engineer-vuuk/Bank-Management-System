import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const DepositForm = () => {
    const [amount, setAmount] = useState("");
    const [pin, setPin] = useState("");
    const [storedPin, setStoredPin] = useState(null);
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

    const AGENT_NUMBER = "AGENT12345"; // ✅ Fixed agent number

    useEffect(() => {
        const fetchUserDetails = async () => {
            try {
                const token = localStorage.getItem("token");
                if (!token) {
                    console.warn("No token found, redirecting to login...");
                    navigate("/login");
                    return;
                }

                const response = await axios.get(`${API_BASE_URL}/users/details`, {
                    headers: { Authorization: `Bearer ${token}` },
                });

                setStoredPin(response.data.pin); // Store the user's PIN from account details
            } catch (error) {
                console.error("Error fetching user details:", error);
                navigate("/login");
            }
        };

        fetchUserDetails();
    }, [navigate]);

    const handleDeposit = async (e) => {
        e.preventDefault();
        setMessage("");
        setLoading(true);

        if (!amount || amount <= 0) {
            setMessage("❌ Enter a valid deposit amount.");
            setLoading(false);
            return;
        }

        if (!/^\d{4}$/.test(pin)) {
            setMessage("❌ Transaction PIN must be exactly 4 digits.");
            setLoading(false);
            return;
        }

        if (pin !== storedPin) {
            setMessage("❌ Incorrect PIN. Please enter the correct transaction PIN.");
            setLoading(false);
            return;
        }

        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(
                `${API_BASE_URL}/users/deposit`,
                { amount, agentNumber: AGENT_NUMBER }, // ✅ PIN is validated before this step
                { headers: { Authorization: `Bearer ${token}` } }
            );

            if (response.status === 200) {
                setMessage("✅ Deposit successful!");
                setTimeout(() => navigate("/dashboard"), 2000);
            } else {
                setMessage(response.data?.message || "⚠️ Deposit failed. Try again.");
            }
        } catch (error) {
            setMessage(error.response?.data?.message || "❌ An error occurred. Try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="transaction-container">
            <h2>Deposit Funds</h2>
            <form onSubmit={handleDeposit}>
                <input
                    type="number"
                    placeholder="Enter amount"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    required
                    disabled={loading}
                />
                <input
                    type="password"
                    placeholder="Transaction PIN (4 digits)"
                    value={pin}
                    onChange={(e) => setPin(e.target.value)}
                    required
                    disabled={loading}
                />
                <input
                    type="text"
                    value={AGENT_NUMBER} // ✅ Fixed agent number
                    readOnly // ✅ Prevents user from editing
                    disabled
                />
                <button type="submit" disabled={loading}>
                    {loading ? "Processing..." : "Deposit"}
                </button>
            </form>
            {message && <p className="message">{message}</p>}
        </div>
    );
};

export default DepositForm;
