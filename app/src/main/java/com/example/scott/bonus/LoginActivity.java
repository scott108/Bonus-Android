package com.example.scott.bonus;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by Scott on 15/5/7.
 */
public class LoginActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText emailEditText = (EditText)findViewById(R.id.emailEditText);
        EditText passwordEditText = (EditText)findViewById(R.id.passwordEditText);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
