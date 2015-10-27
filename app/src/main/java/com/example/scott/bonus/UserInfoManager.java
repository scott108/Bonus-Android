package com.example.scott.bonus;


/**
 * Created by Scott on 15/8/12.
 */
public class UserInfoManager {
    private static String email = "";
    private static String userName = "";
    private static int bonus = 0;
    private static UserInfoManager userInfoManager = new UserInfoManager();
    private static int experience = 0;

    private UserInfoManager() {

    }

    public void clearUserInfo() {
        email = "";
        userName = "";
        bonus = 0;
    }

    public static UserInfoManager getInstance() {
        return userInfoManager;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        UserInfoManager.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        UserInfoManager.userName = userName;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        UserInfoManager.bonus = bonus;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        UserInfoManager.experience = experience;
    }
}
