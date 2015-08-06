package com.example.scott.bonus;

/**
 * Created by Scott on 15/8/6.
 */
public class Context {
    public static MainActivity mainActivity;
    public static LoginActivity loginActivity;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        Context.mainActivity = mainActivity;
    }

    public static LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public static void setLoginActivity(LoginActivity loginActivity) {
        Context.loginActivity = loginActivity;
    }
}
