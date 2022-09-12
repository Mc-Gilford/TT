package com.example.domu.controller;

public class UserController {
    /**
     * Data class that captures user information for logged in users retrieved from LoginRepository
     */
    public static class LoggedInUser {

        private String userId;
        private String displayName;

        public LoggedInUser(String userId, String displayName) {
            this.userId = userId;
            this.displayName = displayName;
        }

        public String getUserId() {
            return userId;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
