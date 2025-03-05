package services;

public class UserSession {

    // This will store the current logged-in user ID
    private static int currentUserId;

    // Set the user ID when the user logs in
    public static void setUserId(int userId) {
        currentUserId = userId;
    }

    // Get the current logged-in user ID
    public static int getUserId() {
        return currentUserId;
    }
}
