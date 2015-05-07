package com.example.scott.bonus.fragmentcontrol;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;

import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.fragmentcontrol.couponadapter.CouponAdapter;
import com.example.scott.bonus.fragmentcontrol.couponadapter.CouponInfo;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Scott on 15/5/7.
 */
public class CouponFragmentControl {

    private List<CouponInfo> applicationList = new ArrayList<CouponInfo>();
    private CouponAdapter mAdapter;
    private MainActivity mainActivity;
    private LinearLayoutManager linearLayoutManager;

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    public CouponAdapter getmAdapter() {
        return mAdapter;
    }

    public CouponFragmentControl(MainActivity mainActivity) {

        this.mainActivity = mainActivity;
        mAdapter = new CouponAdapter(new ArrayList<CouponInfo>(), R.layout.coupon_cardview_item, mainActivity);
        //linearLayoutManager = new LinearLayoutManager(mainActivity);
        new InitializeApplicationsTask().execute();
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

            super.onPostExecute(result);
        }
    }
}
