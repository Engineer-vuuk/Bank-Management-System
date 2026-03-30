package com.bms.bank.management.system.dto;

import java.util.List;

public class AdminDashboardResponse {
    private int pendingApprovals;
    private int totalUsers;
    private List<String> recentTransactions;
    private List<UserDTO> usersPendingApproval;

    // ✅ Constructor
    public AdminDashboardResponse(int pendingApprovals, int totalUsers, List<String> recentTransactions, List<UserDTO> usersPendingApproval) {
        this.pendingApprovals = pendingApprovals;
        this.totalUsers = totalUsers;
        this.recentTransactions = recentTransactions;
        this.usersPendingApproval = usersPendingApproval;
    }

    // ✅ Getters and Setters
    public int getPendingApprovals() { return pendingApprovals; }
    public void setPendingApprovals(int pendingApprovals) { this.pendingApprovals = pendingApprovals; }

    public int getTotalUsers() { return totalUsers; }
    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }

    public List<String> getRecentTransactions() { return recentTransactions; }
    public void setRecentTransactions(List<String> recentTransactions) { this.recentTransactions = recentTransactions; }

    public List<UserDTO> getUsersPendingApproval() { return usersPendingApproval; }
    public void setUsersPendingApproval(List<UserDTO> usersPendingApproval) { this.usersPendingApproval = usersPendingApproval; }
}
