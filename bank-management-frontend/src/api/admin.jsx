import api from "./api"; // ✅ Import the configured Axios instance

// ✅ Fetch Admin Dashboard Data
export const getAdminDashboardData = async () => {
  try {
    const response = await api.get("/admin/dashboard");
    return response.data;
  } catch (error) {
    console.error("Error fetching admin dashboard data:", error);
    throw error;
  }
};

// ✅ Fetch the latest transactions (limit 5)
export const getRecentTransactions = async () => {
  try {
    const response = await api.get("/admin/recent-transactions");
    return response.data;
  } catch (error) {
    console.error("Error fetching recent transactions:", error);
    throw error;
  }
};

// ✅ Reset User Password
export const resetUserPassword = async (userId) => {
  try {
    const response = await api.post(`/admin/reset-password/${userId}`);
    return response.data;
  } catch (error) {
    console.error("Error resetting password:", error);
    throw error;
  }
};
