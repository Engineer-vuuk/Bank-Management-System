import React, { useEffect, useState } from "react";
import axios from "axios";

const Transactions = () => {
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

    useEffect(() => {
        const fetchTransactions = async () => {
            try {
                const token = localStorage.getItem("token");
                if (!token) {
                    throw new Error("Unauthorized. Please log in.");
                }

                const response = await axios.get(`${API_BASE_URL}/accounts/transactions`, {
                    headers: { Authorization: `Bearer ${token}` },
                });

                console.log("Fetched transactions:", response.data);
                setTransactions(response.data || []);
            } catch (err) {
                setError(err.response?.data?.message || err.message || "Failed to fetch transactions.");
            } finally {
                setLoading(false);
            }
        };

        fetchTransactions();
    }, []);

    return (
        <div className="transaction-container">
            <h2>Transaction History</h2>
            {loading ? (
                <p className="loading">Loading transactions...</p>
            ) : error ? (
                <p className="error">{error}</p>
            ) : transactions.length === 0 ? (
                <p>No transactions found.</p>
            ) : (
                <table className="transaction-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Account Number</th>
                            <th>Agent Number</th>
                            <th>Amount (KES)</th>
                            <th>Transaction Type</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                    {transactions.map(({ id, accountNumber, agentNumber, amount, transactionType, timestamp }) => (
                        <tr key={id}>
                            <td>{id}</td>
                            <td>{accountNumber || "N/A"}</td>
                            <td>{agentNumber || "N/A"}</td>
                            <td>{(amount ?? 0).toFixed(2)}</td>
                            <td className={(transactionType || "").toLowerCase()}>{transactionType || "N/A"}</td>
                            <td>{timestamp ? new Date(timestamp).toLocaleString() : "N/A"}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default Transactions;
