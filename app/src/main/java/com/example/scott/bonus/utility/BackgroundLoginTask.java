package com.example.scott.bonus.utility;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.scott.bonus.Context;
import com.example.scott.bonus.API;
import com.example.scott.bonus.R;
import com.example.scott.bonus.UserInfoManager;
import com.example.scott.bonus.session.SessionManager;
import com.example.scott.bonus.sharepreference.LoginSharePreference;
import com.google.gson.JsonObject;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Scott on 15/7/30.
 */
public class BackgroundLoginTask extends AsyncTask<String, Integer, Boolean> {

    boolean result = false;

    @Override
    protected Boolean doInBackground(String... params) {

        if(!params[0].equals("") && !params[1].equals("")) {

            API.getInstance().getHttp().userLogin(params[0], params[1], new Callback<JsonObject>() {
               @Override
               public void success(JsonObject jsonObject, Response response) {
                   result = jsonObject.get("response").getAsBoolean();
                   if (result) {
                       SessionManager.setAttribute(true);

                       SessionManager.setSessionID("JSESSIONID=" + jsonObject.get("sessionID").getAsString());

                       UserInfoManager.getInstance().setUserName(jsonObject.get("name").getAsString());
                       UserInfoManager.getInstance().setEmail(jsonObject.get("email").getAsString());
                       UserInfoManager.getInstance().setBonus(jsonObject.get("bonus").getAsInt());

                       EventBus.getDefault().post(UserInfoManager.getInstance());
                   } else {
                       SessionManager.setAttribute(false);
                   }
                   EventBus.getDefault().post(jsonObject);
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
    protected void onPostExecute(Boolean result) {

    }
}
