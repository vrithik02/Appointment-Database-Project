package com.appointmentProject.desktop;

/**
 * Simple session helper for desktop client.
 * Right now we only care whether the user is an Admin.
 */
public class UserSession {

    // You can update this from the login screen later.
    private static String userType = "Admin"; // default for now

    public static void setUserType(String type) {
        userType = type;
    }

    public static String getUserType() {
        return userType;
    }

    public static boolean isAdmin() {
        return "Admin".equalsIgnoreCase(userType);
    }
}
