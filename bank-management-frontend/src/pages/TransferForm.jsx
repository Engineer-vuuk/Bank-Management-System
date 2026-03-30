import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const TransferForm = () => {
    const [senderAccount, setSenderAccount] = useState("");
    const [receiverAccount, setReceiverAccount] = useState("");
    const [amount, setAmount] = useState("");
    const [transactionPin, setTransactionPin] = useState("");
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

    const handleTransfer = async (e) => {
        e.preventDefault();
        setMessage("");
        setLoading(true);

        if (!/^\d{4}$/.test(transactionPin)) {
            setMessage("❌ Transaction PIN must be exactly 4 digits.");
            setLoading(false);
            return;
        }

        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(
                `${API_BASE_URL}/transactions/transfer`,
                { senderAccount, receiverAccount, amount, transactionPin },
                { headers: { Authorization: `Bearer ${token}` } }
            );

            if (response.status === 200) {
                setMessage("✅ Transfer successful!");
                setTimeout(() => navigate("/dashboard"), 2000);
            } else {
                setMessage(response.data?.message || "⚠️ Transfer failed. Try again.");
            }
        } catch (error) {
            console.error("❌ Transfer error:", error);
            setMessage(error.response?.data?.message || "⚠️ An error occurred. Try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="transfer-container">
            <h2>Transfer Funds</h2>
            <form onSubmit={handleTransfer}>
                <input
                    type="text"
                    placeholder="Sender Account"
                    value={senderAccount}
                    onChange={(e) => setSenderAccount(e.target.value)}
                    required
                    disabled={loading}
                />
                <input
                    type="text"
                    placeholder="Receiver Account"
                    value={receiverAccount}
                    onChange={(e) => setReceiverAccount(e.target.value)}
                    required
                    disabled={loading}
                />
                <input
                    type="number"
                    placeholder="Amount"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    required
                    disabled={loading}
                />
                <input
                    type="password"
                    placeholder="Transaction PIN"
                    value={transactionPin}
                    onChange={(e) => setTransactionPin(e.target.value)}
                    required
                    disabled={loading}
                />
                <button type="submit" disabled={loading}>
                    {loading ? "Processing..." : "Transfer"}
                </button>
            </form>
            {message && <p className="message">{message}</p>}
        </div>
    );
};

export default TransferForm;
