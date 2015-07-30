package com.example.scott.bonus.utility;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.scott.bonus.interfaces.Login;
import com.example.scott.bonus.session.SessionManager;
import com.example.scott.bonus.sharepreference.LoginSharePreference;

import retrofit.RestAdapter;

/**
 * Created by Scott on 15/7/30.
 */
public class BackgroundLoginTask extends AsyncTask<String, Integer, String> {
    private final String serverURL = "http://140.120.15.80:8080/iBonus-server";
    private RestAdapter restAdapter;

    public BackgroundLoginTask() {
        restAdapter = new RestAdapter.Builder().setEndpoint(serverURL).build();
    }

    @Override
    protected String doInBackground(String... params) {
        Login login = restAdapter.create(Login.class);
        String result = "";
        if(!params[0].equals("") && !params[1].equals("")) {

            result = login.userLogin(params[0], params[1]);
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {

    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
        if(result.equals("true")) {
            SessionManager.setAttribute(true);
        }
    }
}
