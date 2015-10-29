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
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private ProgressBar progressBar;

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
        progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycleList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        mRecyclerView.setItemAnimator(new CustomItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        userBonusTextView = (TextView) layout.findViewById(R.id.userBonusTextView);
        userBonusTextView.setText(UserInfoManager.getInstance().getBonus() + "");

        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(mainActivity.getResources().getColor(R.color.primary));
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new InitializeApplicationsTask().execute();
            }
        });

        new InitializeApplicationsTask().execute();

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
            /*API.getInstance().getHttp().getAllCoupon(new Callback<JsonArray>() {
                @Override
                public void success(JsonArray jsonElements, Response response) {

                    for (int i = 0; i < jsonElements.size(); i++) {
                        System.out.println(jsonElements.get(i));
                        JsonObject jsonObject = (JsonObject) jsonElements.get(i);
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
                    progressBar.setVisibility(View.GONE);

                    swipeRefreshLayout.setRefreshing(false);
                    setCurrentBonus(UserInfoManager.getInstance().getBonus());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });*/

            return null;
        }

        @Override
        protected void onPostExecute(Void r) {

            JSONObject jsonObject1 = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            JSONObject jsonObject3 = new JSONObject();
            JSONObject jsonObject4 = new JSONObject();
            JSONObject jsonObject5 = new JSONObject();

            try {
                jsonObject1.put("couponID", "1");
                jsonObject1.put("storeName", "7-11");
                jsonObject1.put("couponName", "美式咖啡買一送一");
                jsonObject1.put("couponContent", "美式咖啡買一送一");
                jsonObject1.put("couponImgUrl", "");
                jsonObject1.put("couponBonus", "5000");
                jsonObject1.put("startTime", "2015/10/01");
                jsonObject1.put("endTime", "2016/10/01");

                jsonObject2.put("couponID", "2");
                jsonObject2.put("storeName", "全家便利商店");
                jsonObject2.put("couponName", "咖啡優惠卷");
                jsonObject2.put("couponContent", "美式咖啡買一送一 OR 任意咖啡第二件五折");
                jsonObject2.put("couponImgUrl", "");
                jsonObject2.put("couponBonus", "5000");
                jsonObject2.put("startTime", "2015/10/01");
                jsonObject2.put("endTime", "2016/10/01");

                jsonObject3.put("couponID", "3");
                jsonObject3.put("storeName", "萊爾富超商");
                jsonObject3.put("couponName", "超商優惠卷");
                jsonObject3.put("couponContent", "蘋果牛奶買一送一");
                jsonObject3.put("couponImgUrl", "");
                jsonObject3.put("couponBonus", "3000");
                jsonObject3.put("startTime", "2015/10/01");
                jsonObject3.put("endTime", "2016/10/01");

                jsonObject4.put("couponID", "4");
                jsonObject4.put("storeName", "大買家");
                jsonObject4.put("couponName", "各式商品優惠");
                jsonObject4.put("couponContent", "各式商品優惠");
                jsonObject4.put("couponImgUrl", "");
                jsonObject4.put("couponBonus", "7000");
                jsonObject4.put("startTime", "2015/10/01");
                jsonObject4.put("endTime", "2016/10/01");

                jsonObject5.put("couponID", "5");
                jsonObject5.put("storeName", "星巴克");
                jsonObject5.put("couponName", "星冰樂買一送一");
                jsonObject5.put("couponContent", "任意口味星冰樂買一送一");
                jsonObject5.put("couponImgUrl", "");
                jsonObject5.put("couponBonus", "10000");
                jsonObject5.put("startTime", "2015/10/01");
                jsonObject5.put("endTime", "2016/10/01");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject1);
            jsonArray.put(jsonObject2);
            jsonArray.put(jsonObject3);
            jsonArray.put(jsonObject4);
            jsonArray.put(jsonObject5);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                CouponItem couponItem = new CouponItem();
                try {
                    couponItem.setCouponID(jsonObject.get("couponID").toString());
                    couponItem.setStoreName(jsonObject.get("storeName").toString());
                    couponItem.setCouponName(jsonObject.get("couponName").toString());
                    couponItem.setCouponContent(jsonObject.get("couponContent").toString());
                    couponItem.setImageUrl(jsonObject.get("couponImgUrl").toString());
                    couponItem.setCouponBonus(Integer.valueOf(jsonObject.get("couponBonus").toString()));
                    couponItem.setStartTime(jsonObject.get("startTime").toString());
                    couponItem.setEndTime(jsonObject.get("endTime").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                applicationList.add(couponItem);

            }
            //set data for list
            mAdapter.addApplications(applicationList);
            progressBar.setVisibility(View.GONE);

            swipeRefreshLayout.setRefreshing(false);
            setCurrentBonus(UserInfoManager.getInstance().getBonus());
        }
    }

}
