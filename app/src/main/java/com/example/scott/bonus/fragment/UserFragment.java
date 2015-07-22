package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scott.bonus.LoginActivity;
import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;

/**
 * Created by Scott on 15/4/20.
 */
public class UserFragment extends Fragment{

    MainActivity mainActivity;
    Intent loginActivityIntent;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
        loginActivityIntent = new Intent(mainActivity, LoginActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView loginClickText =(TextView)this.getView().findViewById(R.id.loginClickText);
        loginClickText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginActivityIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
