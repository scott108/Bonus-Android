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
import com.example.scott.bonus.utility.ApiType;
import com.example.scott.bonus.utility.BackgroundLoginTask;
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
        EventBus.getDefault().register(this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                    loginProgress.setMessage("登入中請稍候...");
                    loginProgress.show();
                    new BackgroundLoginTask().execute(emailEditText.getText().toString(),
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

    public void onEvent(JsonObject jsonObject) {
        int type = jsonObject.get("type").getAsInt();
        if(type == ApiType.LOGIN) {
            String result = jsonObject.get("response").getAsString();
            System.out.println("Login!!!");
            if(result.equals("true")) {
                sharePreference = getSharedPreferences(LoginSharePreference.LOGIN_DATA, 0);
                LoginSharePreference.getInstance().setLoginData(sharePreference,
                                                                jsonObject.get("email").getAsString(),
                                                                jsonObject.get("password").getAsString());
                LoginActivity.this.finish();
            } else {
                Toast.makeText(LoginActivity.this, "帳號密碼組合錯誤", Toast.LENGTH_LONG).show();
            }
            loginProgress.dismiss();
        }
    }
}
