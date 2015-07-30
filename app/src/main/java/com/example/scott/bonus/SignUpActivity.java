package com.example.scott.bonus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    private ClickEventHandler clickEventHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initUI();
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

        signUpBtn.setOnClickListener(clickEventHandler);
    }

    class ClickEventHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signUpBtn:
                    break;
            }
        }
    }
}
