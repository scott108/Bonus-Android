package com.example.scott.bonus.fragmentcontrol;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scott.bonus.API;
import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.fragmentcontrol.couponadapter.CouponAdapter;
import com.example.scott.bonus.fragmentcontrol.couponadapter.CouponInfo;
import com.example.scott.bonus.session.SessionManager;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Scott on 15/5/7.
 */
public class CouponFragmentControl {

    private MainActivity mainActivity;
    private Dialog couponDetailDialog;
    int width;
    int height;

    public CouponFragmentControl(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        //get screen size
        DisplayMetrics dm = new DisplayMetrics();
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        couponDetailDialog = new Dialog(mainActivity);
    }

    public void showCouponDetail(final CouponInfo couponInfo) {
        couponDetailDialog.setContentView(R.layout.coupon_detail);
        TextView couponName = (TextView) couponDetailDialog.findViewById(R.id.couponNameTextView);
        TextView couponID = (TextView) couponDetailDialog.findViewById(R.id.couponIDTextView);
        TextView couponContent = (TextView) couponDetailDialog.findViewById(R.id.couponContentTextView);
        TextView couponBonus = (TextView) couponDetailDialog.findViewById(R.id.couponBonusTextView);
        TextView startTime = (TextView) couponDetailDialog.findViewById(R.id.startTimeTextView);
        TextView endTime = (TextView) couponDetailDialog.findViewById(R.id.endTimeTextView);
        Button couponExchangeBbutton = (Button) couponDetailDialog.findViewById(R.id.coupon_exchange_button);

        couponName.setText(couponInfo.getStoreName() + " " + couponInfo.getCouponName());
        couponID.setText("品號 : " + "coupon_" + couponInfo.getCouponID());
        couponContent.setText("商品內容 : " + couponInfo.getCouponContent());
        couponBonus.setText("優惠卷扣點 : " + couponInfo.getCouponBonus());
        startTime.setText("起始時間 : " + couponInfo.getStartTime());
        endTime.setText("結束時間 : " + couponInfo.getEndTime());

        couponExchangeBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionManager.hasAttribute()) {
                    System.out.println("Exchange!!");
                    new GetCouponTask().execute(couponInfo.getCouponID());
                } else {
                    Toast.makeText(mainActivity.getApplication(), "尚未登入", Toast.LENGTH_LONG).show();
                }
            }
        });

        couponDetailDialog.show();

        WindowManager.LayoutParams params = couponDetailDialog.getWindow()
                .getAttributes();
        params.width = width;
        params.height = height * 4 / 5;
        params.windowAnimations = R.style.PauseDialogAnimation;
        couponDetailDialog.getWindow().setAttributes(params);
    }

    class GetCouponTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            API.getInstance().getHttp().getCoupon(SessionManager.getSessionID(), params[0], new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    System.out.println(jsonObject);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
