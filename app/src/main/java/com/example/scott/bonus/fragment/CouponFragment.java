package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.scott.bonus.API;
import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.UserInfoManager;
import com.example.scott.bonus.fragmentcontrol.couponadapter.CouponAdapter;
import com.example.scott.bonus.fragmentcontrol.couponadapter.CouponInfo;
import com.example.scott.bonus.itemanimator.CustomItemAnimator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Scott on 15/4/20.
 */
public class CouponFragment extends Fragment{

    private MainActivity mainActivity;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<CouponInfo> applicationList = new ArrayList<CouponInfo>();
    private CouponAdapter mAdapter;
    private TextView userBonusTextView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_coupon, container, false);
        mAdapter = new CouponAdapter(new ArrayList<CouponInfo>(), R.layout.coupon_cardview_item, mainActivity);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        mRecyclerView.setItemAnimator(new CustomItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        userBonusTextView = (TextView) layout.findViewById(R.id.userBonusTextView);
        userBonusTextView.setText(UserInfoManager.getInstance().getBonus() + "");

        new InitializeApplicationsTask().execute();

        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(mainActivity.getResources().getColor(R.color.primary));
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new InitializeApplicationsTask().execute();
            }
        });

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(UserInfoManager userInfoManager) {
        System.out.println("onEvent");
        userBonusTextView.setText(userInfoManager.getBonus() + "");
        mAdapter.onEvent(userInfoManager.getBonus());
        mAdapter.notifyDataSetChanged();
    }

    private class InitializeApplicationsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mAdapter.clearApplications();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            applicationList.clear();
            API.getInstance().getHttp().getAllCoupon(new Callback<JsonArray>() {
                @Override
                public void success(JsonArray jsonElements, Response response) {

                    for (int i = 0; i < jsonElements.size(); i++) {
                        System.out.println(jsonElements.get(i));
                        JsonObject jsonObject = (JsonObject)jsonElements.get(i);
                        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        CouponInfo couponInfo = new CouponInfo();
                        couponInfo.setCouponID(jsonObject.get("couponID").getAsString());
                        couponInfo.setStoreName(jsonObject.get("storeName").getAsString());
                        couponInfo.setCouponName(jsonObject.get("couponName").getAsString());
                        couponInfo.setCouponContent(jsonObject.get("couponContent").getAsString());
                        couponInfo.setImageUrl(jsonObject.get("couponImgUrl").getAsString());
                        couponInfo.setCouponBonus(jsonObject.get("couponBonus").getAsInt());
                        couponInfo.setStartTime(jsonObject.get("startTime").getAsString());
                        couponInfo.setEndTime(jsonObject.get("endTime").getAsString());

                        applicationList.add(couponInfo);

                    }
                    //set data for list
                    mAdapter.addApplications(applicationList);
                    swipeRefreshLayout.setRefreshing(false);
                    mAdapter.onEvent(UserInfoManager.getInstance().getBonus());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            return null;
        }
    }

}
