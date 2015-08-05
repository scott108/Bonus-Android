package com.example.scott.bonus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scott.bonus.interfaces.SignUp;

import retrofit.RestAdapter;

/**
 * Created by Scott on 15/7/30.
 */
public class SignUpActivity extends Activity{
    private EditText userNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button signUpBtn;

    private ProgressDialog signUpProgress;

    private RestAdapter restAdapter;
    private final String serverURL = "http://140.120.15.80:8080/iBonus-server";

    private ClickEventHandler clickEventHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
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
        userNameEditText = (EditText) findViewById(R.id.signUpNameEditText);
        emailEditText = (EditText) findViewById(R.id.signUpEmailEditText);
        passwordEditText = (EditText) findViewById(R.id.signUpPasswordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.signUpConfirmPasswordEditText);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);

        signUpProgress = new ProgressDialog(this);

        signUpBtn.setOnClickListener(clickEventHandler);
    }

    private void signUp() {
        new AddUserTask().execute(userNameEditText.getText().toString(),
                                    emailEditText.getText().toString(),
                                    passwordEditText.getText().toString(),
                                    confirmPasswordEditText.getText().toString());
    }

    class ClickEventHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signUpBtn:
                    signUp();
                    break;
            }
        }
    }

    private class AddUserTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            signUpProgress.setMessage("申請中請稍候...");
            signUpProgress.show();
        }
        @Override
        protected String doInBackground(String... params) {
            SignUp signUp = restAdapter.create(SignUp.class);
            String result = "";
            System.out.println("doInBackground...");

            if(!params[0].equals("") && !params[1].equals("") && !params[2].equals("") && !params[3].equals("")) {
                result = signUp.userAdd(params[0], params[1], params[2], params[3]);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("true")) {
                Toast.makeText(SignUpActivity.this, "申請帳號密碼成功", Toast.LENGTH_LONG).show();
                finish();
            } else if(result.equals("passwordfail")) {
                Toast.makeText(SignUpActivity.this, "密碼不一致", Toast.LENGTH_LONG).show();
            } else if(result.equals("emailfail")) {
                Toast.makeText(SignUpActivity.this, "E-mail格式錯誤", Toast.LENGTH_LONG).show();
            } else if(result.equals("false")) {
                Toast.makeText(SignUpActivity.this, "申請失敗", Toast.LENGTH_LONG).show();
            }
            signUpProgress.dismiss();
        }
    }
}
