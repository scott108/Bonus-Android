package com.example.scott.bonus.sharepreference;

import android.content.SharedPreferences;

import com.example.scott.bonus.user.UserAccount;

/**
 * Created by Scott on 15/7/30.
 */
public class LoginSharePreference {
    private SharedPreferences sharePreference;
    public static final String LOGIN_DATA = "LOGIN_DATA";
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private UserAccount userAccount;

    private static LoginSharePreference loginSharePreference = new LoginSharePreference();

    public static LoginSharePreference getInstance() {
        return loginSharePreference;
    }

    private LoginSharePreference() {
        userAccount = new UserAccount();
    }

    public void setLoginData(SharedPreferences sharePreference, String email, String password) {
        sharePreference.edit()
                .putString(LoginSharePreference.EMAIL, email)
                .putString(LoginSharePreference.PASSWORD, password)
                .commit();
    }

    public UserAccount getLoginData(SharedPreferences sharePreference) {
        userAccount.setEmail(sharePreference.getString(EMAIL, ""));
        userAccount.setPassword(sharePreference.getString(PASSWORD, ""));
        return userAccount;
    }

    public void clearLoginData(SharedPreferences sharePreference) {
        sharePreference.edit()
                .putString(LoginSharePreference.EMAIL, "")
                .putString(LoginSharePreference.PASSWORD, "")
                .commit();
    }

}
