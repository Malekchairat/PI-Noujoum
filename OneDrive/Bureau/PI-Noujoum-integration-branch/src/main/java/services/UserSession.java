package services;

public class UserSession {
    private static int currentUserId; // Store only the user ID

    public static void setUserId(int userId) {
        currentUserId = userId;
    }

    public static int getUserId() {
        return currentUserId;
    }

    public static boolean isLoggedIn() {
        return currentUserId > 0; // Check if a user is logged in
    }

    public static void logout() {
        currentUserId = 0;
    }
}
