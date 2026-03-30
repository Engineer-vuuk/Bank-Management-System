import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const RequestForm = () => {
    const [accountNumber, setAccountNumber] = useState("");
    const [transactionPin, setTransactionPin] = useState("");
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

    const handleRequest = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage("");

        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(
                `${API_BASE_URL}/transactions/request`,
                { accountNumber, transactionPin },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setMessage("Request submitted successfully.");
            setTimeout(() => navigate("/dashboard"), 2000);
        } catch (error) {
            setMessage(error.response?.data?.message || "Failed to submit request.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="form-container">
            <h2>Request Funds</h2>
            <form onSubmit={handleRequest}>
                <label>Account Number:</label>
                <input
                    type="text"
                    value={accountNumber}
                    onChange={(e) => setAccountNumber(e.target.value)}
                    required
                />

                <label>Transaction PIN:</label>
                <input
                    type="password"
                    value={transactionPin}
                    onChange={(e) => setTransactionPin(e.target.value)}
                    required
                />

                <button type="submit" disabled={loading}>{loading ? "Requesting..." : "Request Funds"}</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default RequestForm;
