package com.example.scott.bonus;


/**
 * Created by Scott on 15/8/12.
 */
public class UserInfoManager {
    private static String email = "";
    private static String userName = "";
    private static int bonus = 0;

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserInfoManager.email = email;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserInfoManager.userName = userName;
    }

    public static int getBonus() {
        return bonus;
    }

    public static void setBonus(int bonus) {
        UserInfoManager.bonus = bonus;
    }
}
