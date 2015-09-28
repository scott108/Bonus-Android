package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
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
 * Created by Scott on 15/9/28.
 */
public class MyCouponFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<CouponInfo> applicationList = new ArrayList<CouponInfo>();
    private CouponAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_mycoupon, container, false);
        mAdapter = new CouponAdapter(new ArrayList<CouponInfo>(), R.layout.coupon_cardview_item) {
            @Override
            protected void onViewHolder(ViewHolder viewHolder, int i) {
                final CouponInfo couponInfo = getCoupons().get(i);

                viewHolder.getCouponName().setText(couponInfo.getStoreName() + "\n" + couponInfo.getCouponName());
                viewHolder.getCouponBonus().setVisibility(View.GONE);

                viewHolder.getBnp().setVisibility(View.GONE);

                viewHolder.getImage().setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.ic_card_giftcard_black_24dp));

                viewHolder.getCardViewItemLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainActivity.getCouponFragmentControl().showMyCouponDetail(couponInfo);
                    }
                });
            }
        };

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycleList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        mRecyclerView.setItemAnimator(new CustomItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setProgressBarColor(NumberProgressBar numberProgressBar, int color) {
        numberProgressBar.setProgressTextColor(color);
        numberProgressBar.setReachedBarColor(color);
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
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            return null;
        }
    }

}
