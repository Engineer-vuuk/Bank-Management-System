import { useEffect, useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { useNavigate } from "react-router-dom";
import { getAdminDashboardData, resetUserPassword } from "@/api/admin";
import { Menu, X } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import "./AdminDashboard.css";

export default function AdminDashboard() {
  const [dashboardData, setDashboardData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [menuOpen, setMenuOpen] = useState(false);
  const [resetEmail, setResetEmail] = useState("");
  const [resetStatus, setResetStatus] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    setLoading(true);
    setError("");

    try {
      const token = localStorage.getItem("token");
      if (!token) {
        setError("Session expired. Please log in again.");
        return;
      }

      const data = await getAdminDashboardData();
      setDashboardData(data);
    } catch (error) {
      console.error("Error fetching dashboard data:", error);
      setError("Failed to load data. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async () => {
    if (!resetEmail.trim()) {
      setResetStatus("Please enter a valid email.");
      return;
    }

    try {
      await resetUserPassword(resetEmail.trim());
      setResetStatus(`Password reset link sent to ${resetEmail}`);
      setResetEmail("");
    } catch (err) {
      console.error(err);
      setResetStatus("Failed to send reset link. User may not exist.");
    }
  };

  return (
    <div className="admin-dashboard">
      <button
        className="menu-toggle"
        onClick={() => setMenuOpen(!menuOpen)}
      >
        {menuOpen ? <X size={24} /> : <Menu size={24} />}
      </button>

      {menuOpen && (
        <div className="dropdown-menu">
          <ul>
            <li>
              <button>Profile</button>
            </li>
            <li>
              <button>Settings</button>
            </li>
            <li>
              <button
                className="logout-button"
                onClick={() => {
                  localStorage.removeItem("token");
                  navigate("/login");
                }}
              >
                Logout
              </button>
            </li>
          </ul>
        </div>
      )}

      {error && <p className="status-message">{error}</p>}
      {loading ? (
        <p className="text-center text-gray-500 animate-pulse">Loading dashboard...</p>
      ) : (
        <>
          <div className="card-grid">
            <Card className="shadow-lg border border-gray-200 hover:shadow-xl transition">
              <CardContent className="p-5 text-center">
                <h2 className="text-lg font-semibold text-gray-700">Total Users</h2>
                <p className="text-3xl font-bold text-green-600">{dashboardData?.totalUsers ?? 0}</p>
              </CardContent>
            </Card>
          </div>

          <Card className="shadow-lg border border-gray-200 my-6">
            <CardContent className="p-6">
              <h2 className="text-lg font-semibold mb-4 text-gray-700">Recent Transactions</h2>
              {dashboardData?.recentTransactions?.length > 0 ? (
                <table className="transactions-table">
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Description</th>
                      <th>Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    {dashboardData.recentTransactions.map((tx, index) => (
                      <tr key={index}>
                        <td>{index + 1}</td>
                        <td>{tx.description || tx}</td>
                        <td>{tx.date || "N/A"}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              ) : (
                <p className="text-sm text-gray-500">No recent transactions.</p>
              )}
            </CardContent>
          </Card>

          <Card className="shadow-lg border border-gray-200 max-w-lg mx-auto">
            <CardContent className="p-6">
              <h2 className="text-lg font-semibold mb-3">Reset User Password</h2>
              <div className="flex flex-col sm:flex-row gap-2">
                <Input
                  type="email"
                  placeholder="Enter user's email"
                  value={resetEmail}
                  onChange={(e) => setResetEmail(e.target.value)}
                />
                <Button onClick={handleResetPassword}>Send Reset Link</Button>
              </div>
              {resetStatus && <p className="mt-2 text-sm text-gray-600">{resetStatus}</p>}
            </CardContent>
          </Card>
        </>
      )}
    </div>
  );
}
