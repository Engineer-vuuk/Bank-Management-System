import axios from "axios";

const API_URL = "http://localhost:8080/api/auth/";

// ✅ Register User
export const register = async (username, email, password) => {
    const response = await axios.post(`${API_URL}register`, {
        username,
        email,
        password,
    });

    if (response.data.token) {
        localStorage.setItem("token", response.data.token);
        localStorage.setItem("accountNumber", response.data.accountNumber);
        localStorage.setItem("role", response.data.role || "USER"); // Default role
    }

    return response.data;
};

// ✅ Login User
export const login = async (email, password) => {
    const response = await axios.post(`${API_URL}login`, { email, password });

    if (response.data.token) {
        localStorage.setItem("token", response.data.token);
        localStorage.setItem("accountNumber", response.data.accountNumber);
        localStorage.setItem("role", response.data.role || "USER");
    }

    return response.data;
};

// ✅ Logout User
export const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("accountNumber");
    localStorage.removeItem("role");
};

// ✅ Get Current User (from localStorage)
export const getCurrentUser = () => {
    return localStorage.getItem("token") ? {
        token: localStorage.getItem("token"),
        accountNumber: localStorage.getItem("accountNumber"),
        role: localStorage.getItem("role"),
    } : null;
};

// ✅ Attach Token to API Requests
export const authHeader = () => {
    const token = localStorage.getItem("token");
    return token ? { Authorization: `Bearer ${token}` } : {};
};
