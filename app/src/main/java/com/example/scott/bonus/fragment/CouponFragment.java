package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;

/**
 * Created by Scott on 15/4/20.
 */
public class CouponFragment extends Fragment{

    MainActivity mainActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.coupon, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity.getActionBar().setCustomView(R.layout.coupon_title);


    }
}
