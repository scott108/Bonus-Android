package com.example.scott.bonus.utility;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scott.bonus.Context;
import com.example.scott.bonus.HttpSetting;
import com.example.scott.bonus.R;
import com.example.scott.bonus.UserInfoManager;
import com.example.scott.bonus.session.SessionManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Scott on 15/8/6.
 */
public class BackgroundLogoutTask extends AsyncTask<String, Integer, String> {
    private ProgressDialog progressDialog = new ProgressDialog(Context.getMainActivity());
    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("登出中請稍候...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        System.out.println(SessionManager.getSessionID());
        String result = HttpSetting.getInstance().getHttp().userLogout(SessionManager.getSessionID());
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("true")) {
            SessionManager.setAttribute(false);

            UserInfoManager.getInstance().setUserName("");
            UserInfoManager.getInstance().setEmail("");
            UserInfoManager.getInstance().setBonus(0);

            EventBus.getDefault().post(UserInfoManager.getInstance());
            Toast.makeText(Context.getMainActivity(), "登出成功", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(Context.getMainActivity(), "登出失敗", Toast.LENGTH_LONG).show();
        }
        progressDialog.dismiss();
    }
}
