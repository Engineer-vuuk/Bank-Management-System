import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const WithdrawForm = () => {
    const [amount, setAmount] = useState("");
    const [pin, setPin] = useState("");
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

    const handleWithdraw = async (e) => {
        e.preventDefault();
        setMessage("");
        setLoading(true);

        if (!amount || amount <= 0) {
            setMessage("❌ Enter a valid withdrawal amount.");
            setLoading(false);
            return;
        }

        if (!/^\d{4}$/.test(pin)) {
            setMessage("❌ Transaction PIN must be exactly 4 digits.");
            setLoading(false);
            return;
        }

        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(
                `${API_BASE_URL}/accounts/withdraw`,
                { amount, pin },
                { headers: { Authorization: `Bearer ${token}` } }
            );

            if (response.status === 200) {
                setMessage("✅ Withdrawal successful!");
                setTimeout(() => navigate("/dashboard"), 2000);
            } else {
                setMessage(response.data?.message || "⚠️ Withdrawal failed. Try again.");
            }
        } catch (error) {
            setMessage(error.response?.data?.message || "❌ An error occurred. Try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="transaction-container">
            <h2>Withdraw Funds</h2>
            <form onSubmit={handleWithdraw}>
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
                <button type="submit" disabled={loading}>
                    {loading ? "Processing..." : "Withdraw"}
                </button>
            </form>
            {message && <p className="message">{message}</p>}
        </div>
    );
};

export default WithdrawForm;
