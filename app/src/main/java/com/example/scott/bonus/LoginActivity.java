package com.example.scott.bonus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scott.bonus.session.SessionManager;
import com.example.scott.bonus.sharepreference.LoginSharePreference;
import com.google.gson.JsonObject;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Scott on 15/5/7.
 */
public class LoginActivity extends Activity{
    private SharedPreferences sharePreference;
    private ProgressDialog loginProgress;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private TextView signUpLink;

    private ClickEventHandler clickEventHandler;

    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();

        Context.setLoginActivity(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initUI() {
        clickEventHandler = new ClickEventHandler();
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signUpLink = (TextView) findViewById(R.id.signUpLinkTextView);

        emailEditText.setText("scott@gmail.com");
        passwordEditText.setText("asdf123");

        loginProgress = new ProgressDialog(this);
        loginBtn.setOnClickListener(clickEventHandler);
        signUpLink.setOnClickListener(clickEventHandler);

        intent = new Intent(this, SignUpActivity.class);
    }

    private class ClickEventHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginBtn:
                    new LoginTask().execute(emailEditText.getText().toString(),
                                            passwordEditText.getText().toString());
                    break;
                case R.id.signUpLinkTextView:
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }


    public void loginResult(JsonObject jsonObject) {
        String result = jsonObject.toString();
        if(!jsonObject.has("loginfail")) {
            SessionManager.setAttribute(true);

            SessionManager.setSessionID("JSESSIONID=" + jsonObject.get("sessionID").getAsString());

            sharePreference = getSharedPreferences(LoginSharePreference.LOGIN_DATA, 0);

            LoginSharePreference.getInstance().setLoginData(sharePreference,
                    emailEditText.getText().toString(),
                    passwordEditText.getText().toString());

            UserInfoManager.getInstance().setUserName(jsonObject.get("name").getAsString());
            UserInfoManager.getInstance().setEmail(jsonObject.get("email").getAsString());
            UserInfoManager.getInstance().setBonus(jsonObject.get("bonus").getAsInt());



            TextView welcom = (TextView) Context.getMainActivity().findViewById(R.id.name);
            welcom.setText("Hello, " + jsonObject.get("name").getAsString());
            TextView loginPage = (TextView) Context.getMainActivity().findViewById(R.id.gotoLoginPage);
            loginPage.setText("歡迎使用iBonus");
            LoginActivity.this.finish();

        } else {
            Toast.makeText(LoginActivity.this, "帳號密碼組合錯誤", Toast.LENGTH_LONG).show();
        }
        loginProgress.dismiss();
    }

    private class LoginTask extends AsyncTask<String, Integer, Void> {
        @Override
        protected void onPreExecute() {
            loginProgress.setMessage("登入中請稍候...");
            loginProgress.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            if(!params[0].equals("") && !params[1].equals("")) {
                HttpSetting.getInstance().getHttp().userLogin(params[0], params[1], new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        System.out.println(response.getHeaders());
                        loginResult(jsonObject);

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
            return null;
        }
    }
}
