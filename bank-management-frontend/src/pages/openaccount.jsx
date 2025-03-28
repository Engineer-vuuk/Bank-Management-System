import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./openAccount.css";

const OpenAccount = () => {
    const [formData, setFormData] = useState({
        fullname: "",
        email: "",
        dob: "",
        nextOfKin: "",
        phoneNumber: "",
        kinPhoneNumber: "",
        transactionPin: "",
        confirmTransactionPin: "",
        idNumber: "",
    });

    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    // Redirect existing users to dashboard
    useEffect(() => {
        const existingAccount = localStorage.getItem("accountNumber");
        if (existingAccount) {
            navigate("/dashboard");
        }
    }, [navigate]);

    // Handle form input change
    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    // Generate a 13-digit account number
    const generateAccountNumber = () => {
        return Math.floor(1000000000000 + Math.random() * 9000000000000).toString();
    };

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validate transaction pin
        if (formData.transactionPin !== formData.confirmTransactionPin) {
            setMessage("Transaction PINs do not match!");
            return;
        }

        // Generate account number
        const accountNumber = generateAccountNumber();

        try {
            const response = await axios.post("http://localhost:8080/api/open-account", {
                ...formData,
                accountNumber,
            });

            if (response.status === 201) {
                setMessage("Account created successfully!");
                localStorage.setItem("accountNumber", accountNumber); // Store in local storage
                setTimeout(() => navigate("/dashboard"), 2000); // Redirect to dashboard
            } else {
                setMessage("Failed to open account.");
            }
        } catch (error) {
            setMessage("Error opening account.");
            console.error(error);
        }
    };

    return (
        <div className="open-account-container">
            <h2>Open a New Account</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" name="fullname" placeholder="Full Name" onChange={handleChange} required />
                <input type="email" name="email" placeholder="Email" onChange={handleChange} required />
                <input type="date" name="dob" onChange={handleChange} required />
                <input type="text" name="nextOfKin" placeholder="Next of Kin Name" onChange={handleChange} required />
                <input type="tel" name="phoneNumber" placeholder="Phone Number" onChange={handleChange} required />
                <input type="tel" name="kinPhoneNumber" placeholder="Next of Kin Phone Number" onChange={handleChange} required />
                <input type="number" name="idNumber" placeholder="ID Number (KYC)" onChange={handleChange} required />
                <input type="password" name="transactionPin" placeholder="Transaction PIN" onChange={handleChange} required />
                <input type="password" name="confirmTransactionPin" placeholder="Confirm Transaction PIN" onChange={handleChange} required />

                <button type="submit">Open Account</button>
            </form>
            <p>{message}</p>
        </div>
    );
};

export default OpenAccount;
