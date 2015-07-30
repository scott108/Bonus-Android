package com.example.scott.bonus.session;

/**
 * Created by Scott on 15/7/30.
 */
public class SessionManager {
    private static boolean attribute = false;

    public static boolean hasAttribute() {
        return attribute;
    }

    public static void setAttribute(boolean attribute) {
        SessionManager.attribute = attribute;
    }
}
