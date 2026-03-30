import React, { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { logout } from "../services/authService";
import Transactions from "../pages/Transactions";
import "./Dashboard.css";
import backgroundImg from "../assets/dashboard-bg.jpg";

const Dashboard = () => {
    const navigate = useNavigate();
    const [accountNumber, setAccountNumber] = useState(null);
    const [fullName, setFullName] = useState("User");
    const [balance, setBalance] = useState(null);
    const [transactionPin, setTransactionPin] = useState("");
    const [transactions, setTransactions] = useState([]);
    const [menuOpen, setMenuOpen] = useState(false);
    const [showDepositForm, setShowDepositForm] = useState(false);
    const [showWithdrawForm, setShowWithdrawForm] = useState(false);
    const [showTransferForm, setShowTransferForm] = useState(false);
    const [depositAmount, setDepositAmount] = useState("");
    const [withdrawAmount, setWithdrawAmount] = useState("");
    const [pinInput, setPinInput] = useState("");
    const [message, setMessage] = useState("");
    const [transferRecipient, setTransferRecipient] = useState("");
    const [transferAmount, setTransferAmount] = useState("");
    const [transferPin, setTransferPin] = useState("");
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";
    const fetchCalled = useRef(false);

    useEffect(() => {
        if (fetchCalled.current) return;
        fetchCalled.current = true;

        const fetchUserData = async () => {
            try {
                const token = localStorage.getItem("token");
                if (!token) {
                    navigate("/login");
                    return;
                }
                const userResponse = await axios.get(`${API_BASE_URL}/accounts/details`, {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                });

                const userData = userResponse.data;
                setFullName(userData.fullName || "User");
                setAccountNumber(userData.accountNumber || null);
                setBalance(userData.balance || 0);
                setTransactionPin(userData.transactionPin);
            } catch (error) {
                console.error("Error fetching user data:", error);
                navigate("/login");
            }
        };

        const fetchTransactions = async () => {
            try {
                const token = localStorage.getItem("token");
                if (!token) {
                    navigate("/login");
                    return;
                }
                const response = await axios.get(`${API_BASE_URL}/accounts/transactions`, {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                });
                setTransactions(response.data || []);
            } catch (error) {
                console.error("Error fetching transactions:", error);
            }
        };

        fetchUserData();
        fetchTransactions();
    }, [navigate]);

    const handleDeposit = async () => {
        if (!depositAmount || !pinInput) {
            setMessage("Please enter all fields.");
            return;
        }

        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(
                `${API_BASE_URL}/transactions/deposit`,
                {
                    accountNumber,
                    amount: parseFloat(depositAmount),
                    agentNumber: "AGENT12345",
                    pin: pinInput,
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                }
            );

            const { message, newBalance } = response.data;
            if (newBalance !== undefined) {
                setBalance(parseFloat(newBalance));
            }

            setMessage(message);
            setShowDepositForm(false);
            setDepositAmount("");
            setPinInput("");
        } catch (error) {
            setMessage("Deposit failed: " + (error.response?.data?.error || error.message));
        }
    };

    const handleWithdraw = async () => {
        if (!withdrawAmount || !pinInput) {
            setMessage("Please enter all fields.");
            return;
        }

        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(
                `${API_BASE_URL}/transactions/withdraw`,
                {
                    accountNumber,
                    amount: parseFloat(withdrawAmount),
                    agentNumber: "AGENT12345",
                    pin: pinInput,
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                }
            );

            const { message, newBalance } = response.data;
            if (newBalance !== undefined) {
                setBalance(parseFloat(newBalance));
            }

            setMessage(message);
            setShowWithdrawForm(false);
            setWithdrawAmount("");
            setPinInput("");
        } catch (error) {
            setMessage("Withdrawal failed: " + (error.response?.data?.error || error.message));
        }
    };

    const handleTransfer = async () => {
        if (!transferRecipient || !transferAmount || !transferPin) {
            setMessage("Please enter all fields.");
            return;
        }

        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(
                `${API_BASE_URL}/transactions/transfer`,
                {
                    fromAccount: accountNumber,
                    toAccount: transferRecipient,
                    amount: parseFloat(transferAmount),
                    pin: transferPin,
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                }
            );

            const { message, newBalance } = response.data;
            if (newBalance !== undefined) {
                setBalance(parseFloat(newBalance));
            }

            setMessage(message);
            setTransferRecipient("");
            setTransferAmount("");
            setTransferPin("");
            setShowTransferForm(false);
        } catch (error) {
            setMessage("Transfer failed: " + (error.response?.data?.error || error.message));
        }
    };

    return (
        <div className="dashboard-container"
            style={{
                backgroundImage: `url(${backgroundImg})`,
                backgroundSize: "cover",
                backgroundPosition: "center",
                backgroundRepeat: "no-repeat",
                minHeight: "100vh",
            }}
        >
            <div className="header">
              <h2>Welcome, <span className="username">{fullName}</span> 👋</h2>
              <h3>Account Number: <span className="username">{accountNumber}</span></h3>
              <p className="balance">Balance: KES {balance !== null ? balance.toFixed(2) : "Loading..."}</p>
                <div className="profile-menu">
                    <button className="profile-button" onClick={() => setMenuOpen(!menuOpen)}>⚙</button>
                    {menuOpen && (
                        <div className="dropdown-menu">
                            <button onClick={() => navigate("/profile")}>Profile</button>
                            <button onClick={() => navigate("/settings")}>Settings</button>
                            <button onClick={() => { logout(); navigate("/login"); }}>Logout</button>
                        </div>
                    )}
                </div>
            </div>

            <div className="action-buttons">
                <button
                    className={`action-btn ${showDepositForm ? "active" : ""}`}
                    onClick={() => {
                        setShowDepositForm(true);
                        setShowWithdrawForm(false);
                        setShowTransferForm(false);
                    }}
                >
                    Deposit
                </button>

                <button
                    className={`action-btn ${showWithdrawForm ? "active" : ""}`}
                    onClick={() => {
                        setShowWithdrawForm(true);
                        setShowDepositForm(false);
                        setShowTransferForm(false);
                    }}
                >
                    Withdraw
                </button>

                <button
                    className={`action-btn ${showTransferForm ? "active" : ""}`}
                    onClick={() => {
                        setShowTransferForm(true);
                        setShowDepositForm(false);
                        setShowWithdrawForm(false);
                    }}
                >
                    Transfer
                </button>
            </div>

            {showDepositForm && (
                <div className="transaction-form">
                    <input type="number" placeholder="Amount" value={depositAmount} onChange={(e) => setDepositAmount(e.target.value)} />
                    <input type="text" value="AGENT12345" disabled />
                    <input type="password" placeholder="Transaction PIN" value={pinInput} onChange={(e) => setPinInput(e.target.value)} />
                    <button onClick={handleDeposit}>Submit</button>
                </div>
            )}

            {showWithdrawForm && (
                <div className="transaction-form">
                    <input type="text" value="AGENT12345" disabled />
                    <input type="number" placeholder="Amount" value={withdrawAmount} onChange={(e) => setWithdrawAmount(e.target.value)} />
                    <input type="password" placeholder="Transaction PIN" value={pinInput} onChange={(e) => setPinInput(e.target.value)} />
                    <button onClick={handleWithdraw}>Submit</button>
                </div>
            )}

            {showTransferForm && (
                <div className="transaction-form">
                    <input
                        type="number"
                        placeholder="Recipient Account Number"
                        value={transferRecipient}
                        onChange={(e) => setTransferRecipient(e.target.value)}
                    />
                    <input
                        type="number"
                        placeholder="Amount"
                        value={transferAmount}
                        onChange={(e) => setTransferAmount(e.target.value)}
                    />
                    <input
                        type="password"
                        placeholder="Transaction PIN"
                        value={transferPin}
                        onChange={(e) => setTransferPin(e.target.value)}
                    />
                    <button onClick={handleTransfer}>Submit</button>
                </div>
            )}

            {message && <p className="message">{message}</p>}

            <div className="recent-activity">
                <Transactions transactions={transactions} />
                <p>See when money comes in, and when it goes out.</p>
            </div>

            <div className="logout-section">
                <button className="logout-button" onClick={() => { logout(); navigate("/login"); }}>Logout</button>
            </div>
        </div>
    );
};

export default Dashboard;
