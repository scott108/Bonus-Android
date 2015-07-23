package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.content.Intent;
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

import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.fragmentcontrol.couponadapter.CouponAdapter;
import com.example.scott.bonus.fragmentcontrol.couponadapter.CouponInfo;
import com.example.scott.bonus.itemanimator.CustomItemAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 15/4/20.
 */
public class CouponFragment extends Fragment{

    MainActivity mainActivity;
    private RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
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
        View layout = inflater.inflate(R.layout.fragment_coupon, container, false);
        mAdapter = new CouponAdapter(new ArrayList<CouponInfo>(), R.layout.coupon_cardview_item, mainActivity);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        mRecyclerView.setItemAnimator(new CustomItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        new InitializeApplicationsTask().execute();

        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(mainActivity.getResources().getColor(R.color.primary));
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new InitializeApplicationsTask().execute();
            }
        });

        return layout;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

            //Query the applications
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            for (int i = 0; i < 10; i++) {
                CouponInfo couponInfo = new CouponInfo();
                couponInfo.setName("店名：7-ELEVEN");
                couponInfo.setNumber("優惠項目：飲料買一送一");
                Bitmap bitmap = ((BitmapDrawable)mainActivity.getResources().getDrawable( R.drawable.icon )).getBitmap();
                int oldwidth = bitmap.getWidth();
                int oldheight = bitmap.getHeight();
                float scaleWidth = 200 / (float)oldwidth;
                float scaleHeight = 200 / (float)oldheight;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // create the new Bitmap object
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, oldwidth,oldheight, matrix, true);
                couponInfo.setIcon(resizedBitmap);
                applicationList.add(couponInfo);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //handle visibility

            //set data for list
            mAdapter.addApplications(applicationList);
            swipeRefreshLayout.setRefreshing(false);

            super.onPostExecute(result);
        }
    }

}
