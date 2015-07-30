package com.example.scott.bonus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scott.bonus.interfaces.Login;
import com.example.scott.bonus.session.SessionManager;
import com.example.scott.bonus.sharepreference.LoginSharePreference;

import retrofit.RestAdapter;


/**
 * Created by Scott on 15/5/7.
 */
public class LoginActivity extends Activity{
    private SharedPreferences sharePreference;
    private ProgressDialog loginProgress;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private ClickEventHandler clickEventHandler;
    private RestAdapter restAdapter;
    private final String serverURL = "http://140.120.15.80:8080/iBonus-server";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        restAdapter = new RestAdapter.Builder().setEndpoint(serverURL).build();
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
        loginProgress = new ProgressDialog(this);
        loginBtn.setOnClickListener(clickEventHandler);
    }

    private class ClickEventHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginBtn:
                    new LoginTask().execute();
                    break;
                default:
                    break;
            }
        }
    }

    private class LoginTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            loginProgress.setMessage("登入中請稍候");
            loginProgress.show();
        }
        @Override
        protected String doInBackground(String... params) {
            Login login = restAdapter.create(Login.class);
            String result = "";
            if(!emailEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals("")) {

                result = login.userLogin(emailEditText.getText().toString(), passwordEditText.getText().toString());
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
                sharePreference = getSharedPreferences(LoginSharePreference.LOGIN_DATA, 0);
                LoginSharePreference.getInstance().setLoginData(sharePreference,
                                                    emailEditText.getText().toString(),
                                                    passwordEditText.getText().toString());
                LoginActivity.this.finish();
            } else {
                Toast.makeText(LoginActivity.this, "帳號密碼組合錯誤", Toast.LENGTH_LONG).show();
            }
            loginProgress.dismiss();
        }
    }
}
