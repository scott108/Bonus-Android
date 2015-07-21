package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Scott on 15/4/20.
 */
public class CouponFragment extends Fragment{

    MainActivity mainActivity;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_coupon, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity.getActionBar().setCustomView(R.layout.coupon_title);
        mRecyclerView = (RecyclerView) this.getView().findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        mRecyclerView.setAdapter(mainActivity.getCouponFragmentControl().getmAdapter());
    }

}
