package com.example.scott.bonus.utility;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.scott.bonus.Context;
import com.example.scott.bonus.HttpSetting;
import com.example.scott.bonus.R;
import com.example.scott.bonus.session.SessionManager;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Scott on 15/7/30.
 */
public class BackgroundLoginTask extends AsyncTask<String, Integer, String> {

    String result = "false";

    @Override
    protected String doInBackground(String... params) {

        if(!params[0].equals("") && !params[1].equals("")) {

            HttpSetting.getInstance().getHttp().userLogin(params[0], params[1], new Callback<JsonObject>() {
               @Override
               public void success(JsonObject jsonObject, Response response) {
                   result = jsonObject.toString();
                   System.out.println(result);
                   if (!result.equals("false")) {
                       SessionManager.setAttribute(true);
                       TextView welcom = (TextView) Context.getMainActivity().findViewById(R.id.name);
                       welcom.setText("Hello, " + jsonObject.get("name").getAsString());
                       TextView loginPage = (TextView) Context.getMainActivity().findViewById(R.id.gotoLoginPage);
                       loginPage.setText("歡迎使用iBonus");
                   } else {
                       SessionManager.setAttribute(false);
                   }
               }

               @Override
               public void failure(RetrofitError error) {

               }
           });
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {

    }

    @Override
    protected void onPostExecute(String result) {

    }
}
