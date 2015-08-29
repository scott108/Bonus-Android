package com.example.scott.bonus.session;

/**
 * Created by Scott on 15/7/30.
 */
public class SessionManager {
    private static boolean attribute = false;

    private static String sessionID = "";

    public static boolean hasAttribute() {
        return attribute;
    }

    public static void setAttribute(boolean attribute) {
        SessionManager.attribute = attribute;
    }

    public static String getSessionID() {
        return sessionID;
    }

    public static void setSessionID(String sessionID) {
        SessionManager.sessionID = sessionID;
    }

    public static void clearAttribute() {
        sessionID = "";
        attribute = false;
    }
}
