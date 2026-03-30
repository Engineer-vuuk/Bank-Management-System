import axios from "axios";

const API_URL = "http://localhost:8080/api"; // ✅ Base API URL

const api = axios.create({
  baseURL: API_URL,
  headers: { "Content-Type": "application/json" },
});

// ✅ Attach Authorization Token to Requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; // ✅ Add Bearer token
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// ✅ Handle API Errors (including 401 Unauthorized)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      if (error.response.status === 401) {
        console.warn("Session expired. Redirecting to login...");
        localStorage.removeItem("token"); // ✅ Clear token on unauthorized access
        window.location.href = "/login"; // ✅ Redirect to login
      } else {
        console.error(`API Error: ${error.response.status} - ${error.response.data?.message || "Unknown error"}`);
      }
    } else {
      console.error("Network Error:", error.message);
    }
    return Promise.reject(error);
  }
);

export default api;
