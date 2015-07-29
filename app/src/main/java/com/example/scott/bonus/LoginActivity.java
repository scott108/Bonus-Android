package com.example.scott.bonus;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scott.bonus.interfaces.Login;

import java.util.concurrent.ExecutionException;

import retrofit.RestAdapter;


/**
 * Created by Scott on 15/5/7.
 */
public class LoginActivity extends Activity{
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
        protected String doInBackground(String... params) {
            Login login;
            String result;
            if(!emailEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals("")) {
                login = restAdapter.create(Login.class);
                result = login.userLogin(emailEditText.getText().toString(), passwordEditText.getText().toString());
                return result;
            }

            return "false";
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {

        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
            if(result.equals("true")) {
                LoginActivity.this.finish();
            } else {
                Toast.makeText(LoginActivity.this, "帳號密碼組合錯誤", Toast.LENGTH_LONG).show();
            }
        }
    }
}
