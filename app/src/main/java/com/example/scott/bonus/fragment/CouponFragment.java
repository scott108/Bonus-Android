package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.scott.bonus.API;
import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.UserInfoManager;
import com.example.scott.bonus.fragmentcontrol.couponadapter.CouponAdapter;
import com.example.scott.bonus.itemanimator.CustomItemAnimator;
import com.example.scott.bonus.sqlite.entity.CouponItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LimitedAgeMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
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
    private List<CouponItem> applicationList = new ArrayList<CouponItem>();
    private CouponAdapter mAdapter;
    private TextView userBonusTextView;

    private int currentBonus = 0;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_coupon, container, false);
        mAdapter = new CouponAdapter(new ArrayList<CouponItem>(), R.layout.coupon_cardview_item) {
            @Override
            protected void onViewHolder(ViewHolder viewHolder, int i) {
                final CouponItem couponItem = getCoupons().get(i);

                viewHolder.getCouponName().setText(couponItem.getStoreName() + "\n" + couponItem.getCouponName());
                viewHolder.getCouponBonus().setText(couponItem.getCouponBonus() + " 點");

                int progress = 0;

                if(currentBonus >= couponItem.getCouponBonus()) {
                    progress = 100;
                } else {
                    progress = ((currentBonus * 100 )/ couponItem.getCouponBonus());
                }
                if(progress < 20) {
                    setProgressBarColor(viewHolder.getBnp(), mainActivity.getResources().getColor(R.color.blue));
                } else if(progress > 20 && progress <= 50) {
                    setProgressBarColor(viewHolder.getBnp(), mainActivity.getResources().getColor(R.color.green));
                } else if(progress > 50 && progress <= 80) {
                    setProgressBarColor(viewHolder.getBnp(), mainActivity.getResources().getColor(R.color.dark_green));
                } else if(progress > 80 && progress <= 99) {
                    setProgressBarColor(viewHolder.getBnp(), mainActivity.getResources().getColor(R.color.orange));
                } else {
                    setProgressBarColor(viewHolder.getBnp(), mainActivity.getResources().getColor(R.color.red));
                }

                if (couponItem.getStoreName().equals("7-11")) {
                    ImageLoader.getInstance().displayImage("drawable://" + R.drawable.citycafe, viewHolder.getImage());
                } else if (couponItem.getStoreName().equals("全家便利商店")) {
                    ImageLoader.getInstance().displayImage("drawable://" + R.drawable.familymartcoupon, viewHolder.getImage());
                } else if (couponItem.getStoreName().equals("萊爾富超商")) {
                    ImageLoader.getInstance().displayImage("drawable://" + R.drawable.lirfo, viewHolder.getImage());
                } else if (couponItem.getStoreName().equals("大買家")) {
                    ImageLoader.getInstance().displayImage("drawable://" + R.drawable.damija, viewHolder.getImage());
                } else if (couponItem.getStoreName().equals("星巴克")) {
                    ImageLoader.getInstance().displayImage("drawable://" + R.drawable.startbucks, viewHolder.getImage());
                }


                viewHolder.getBnp().setProgress(progress);

                viewHolder.getCardViewItemLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainActivity.getCouponFragmentControl().showCouponDetail(couponItem);
                    }
                });
            }
        };

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycleList);
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

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 300, 300, false);
        return new BitmapDrawable(mainActivity.getResources(), bitmapResized);
    }

    private void setProgressBarColor(NumberProgressBar numberProgressBar, int color) {
        numberProgressBar.setProgressTextColor(color);
        numberProgressBar.setReachedBarColor(color);
    }

    public void onEvent(UserInfoManager userInfoManager) {
        System.out.println("onEvent");
        userBonusTextView.setText(userInfoManager.getBonus() + "");
        setCurrentBonus(userInfoManager.getBonus());
        mAdapter.notifyDataSetChanged();
    }

    public void setCurrentBonus(int currentBonus) {
        this.currentBonus = currentBonus;
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
                        CouponItem couponItem = new CouponItem();
                        couponItem.setCouponID(jsonObject.get("couponID").getAsString());
                        couponItem.setStoreName(jsonObject.get("storeName").getAsString());
                        couponItem.setCouponName(jsonObject.get("couponName").getAsString());
                        couponItem.setCouponContent(jsonObject.get("couponContent").getAsString());
                        couponItem.setImageUrl(jsonObject.get("couponImgUrl").getAsString());
                        couponItem.setCouponBonus(jsonObject.get("couponBonus").getAsInt());
                        couponItem.setStartTime(jsonObject.get("startTime").getAsString());
                        couponItem.setEndTime(jsonObject.get("endTime").getAsString());

                        applicationList.add(couponItem);

                    }
                    //set data for list
                    mAdapter.addApplications(applicationList);
                    swipeRefreshLayout.setRefreshing(false);
                    setCurrentBonus(UserInfoManager.getInstance().getBonus());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            return null;
        }
    }

}
