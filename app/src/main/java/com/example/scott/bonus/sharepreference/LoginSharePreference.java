package com.example.scott.bonus.sharepreference;

import android.content.SharedPreferences;

import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.user.User;

/**
 * Created by Scott on 15/7/30.
 */
public class LoginSharePreference {
    private SharedPreferences sharePreference;
    public static final String LOGIN_DATA = "LOGIN_DATA";
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private User user;

    private static LoginSharePreference loginSharePreference = new LoginSharePreference();

    public static LoginSharePreference getInstance() {
        return loginSharePreference;
    }

    private LoginSharePreference() {
        user = new User();
    }

    public void setLoginData(SharedPreferences sharePreference, String email, String password) {
        sharePreference.edit()
                .putString(LoginSharePreference.EMAIL, email)
                .putString(LoginSharePreference.PASSWORD, password)
                .commit();
    }

    public User getLoginData(SharedPreferences sharePreference) {
        user.setEmail(sharePreference.getString(EMAIL, ""));
        user.setPassword(sharePreference.getString(PASSWORD, ""));
        return user;
    }

}
