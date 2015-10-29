package com.example.scott.bonus.fragmentcontrol;

import android.app.Dialog;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scott.bonus.API;
import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.UserInfoManager;
import com.example.scott.bonus.session.SessionManager;
import com.example.scott.bonus.sqlite.entity.CouponItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;


import de.greenrobot.event.EventBus;
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
    Gson gson = new Gson();

    public CouponFragmentControl(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        //get screen size
        DisplayMetrics dm = new DisplayMetrics();
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        couponDetailDialog = new Dialog(mainActivity);
    }

    public void showCouponDetail(final CouponItem couponItem) {
        couponDetailDialog.setContentView(R.layout.coupon_detail);
        TextView couponName = (TextView) couponDetailDialog.findViewById(R.id.couponNameTextView);
        TextView couponID = (TextView) couponDetailDialog.findViewById(R.id.couponIDTextView);
        TextView couponContent = (TextView) couponDetailDialog.findViewById(R.id.couponContentTextView);
        TextView couponBonus = (TextView) couponDetailDialog.findViewById(R.id.couponBonusTextView);
        TextView startTime = (TextView) couponDetailDialog.findViewById(R.id.startTimeTextView);
        TextView endTime = (TextView) couponDetailDialog.findViewById(R.id.endTimeTextView);
        Button couponExchangeBbutton = (Button) couponDetailDialog.findViewById(R.id.coupon_exchange_button);
        ImageView couponImage = (ImageView) couponDetailDialog.findViewById(R.id.coupon_image);

        couponName.setText(couponItem.getStoreName() + " " + couponItem.getCouponName());
        couponID.setText("品號 : " + "coupon_" + couponItem.getCouponID());
        couponContent.setText("商品內容 : " + couponItem.getCouponContent());
        couponBonus.setText("優惠卷扣點 : " + couponItem.getCouponBonus());
        startTime.setText("起始時間 : " + couponItem.getStartTime());
        endTime.setText("結束時間 : " + couponItem.getEndTime());

        if (couponItem.getStoreName().equals("7-11")) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.citycafe, couponImage);
        } else if (couponItem.getStoreName().equals("全家便利商店")) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.familymartcoupon, couponImage);
        } else if (couponItem.getStoreName().equals("萊爾富超商")) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.lirfo, couponImage);
        } else if (couponItem.getStoreName().equals("大買家")) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.damija, couponImage);
        } else if (couponItem.getStoreName().equals("星巴克")) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.startbucks, couponImage);
        }

        couponExchangeBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionManager.hasAttribute()) {
                    System.out.println("Exchange!!");
                    new GetCouponTask().execute(couponItem.getCouponID());
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

    public void showMyCouponDetail(final CouponItem couponItem) {
        couponDetailDialog.setContentView(R.layout.coupon_detail);
        TextView couponName = (TextView) couponDetailDialog.findViewById(R.id.couponNameTextView);
        TextView couponID = (TextView) couponDetailDialog.findViewById(R.id.couponIDTextView);
        TextView couponContent = (TextView) couponDetailDialog.findViewById(R.id.couponContentTextView);
        TextView couponBonus = (TextView) couponDetailDialog.findViewById(R.id.couponBonusTextView);
        TextView startTime = (TextView) couponDetailDialog.findViewById(R.id.startTimeTextView);
        TextView endTime = (TextView) couponDetailDialog.findViewById(R.id.endTimeTextView);
        Button couponExchangeBbutton = (Button) couponDetailDialog.findViewById(R.id.coupon_exchange_button);
        ImageView couponImage = (ImageView) couponDetailDialog.findViewById(R.id.coupon_image);

        couponName.setText(couponItem.getStoreName() + " " + couponItem.getCouponName());
        couponID.setText("品號 : " + "coupon_" + couponItem.getCouponID());
        couponContent.setText("商品內容 : " + couponItem.getCouponContent());
        couponBonus.setText("優惠卷扣點 : " + couponItem.getCouponBonus());
        startTime.setText("起始時間 : " + couponItem.getStartTime());
        endTime.setText("結束時間 : " + couponItem.getEndTime());

        if (couponItem.getStoreName().equals("7-11")) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.citycafe, couponImage);
        } else if (couponItem.getStoreName().equals("全家便利商店")) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.familymartcoupon, couponImage);
        } else if (couponItem.getStoreName().equals("萊爾富超商")) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.lirfo, couponImage);
        } else if (couponItem.getStoreName().equals("大買家")) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.damija, couponImage);
        } else if (couponItem.getStoreName().equals("星巴克")) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.startbucks, couponImage);
        }

        couponExchangeBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            /*API.getInstance().getHttp().getCoupon(SessionManager.getSessionID(), params[0], new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    System.out.println(jsonObject);
                    if(jsonObject.get("exchangedResponse").getAsBoolean()) {
                        UserInfoManager.getInstance().setBonus(jsonObject.get("bonus").getAsInt());

                        CouponItem couponItem = gson.fromJson(jsonObject.get("coupon"), CouponItem.class);

                        mainActivity.getCouponDAO().insert(couponItem);

                        EventBus.getDefault().post(UserInfoManager.getInstance());

                        Toast.makeText(mainActivity.getApplication(), "兌換成功", Toast.LENGTH_LONG).show();

                        couponDetailDialog.dismiss();
                    } else {
                        Toast.makeText(mainActivity.getApplication(), "點數不夠", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });*/
            return null;
        }

        @Override
        protected void onPostExecute(String r) {
            UserInfoManager.getInstance().setBonus(UserInfoManager.getInstance().getBonus() - 3000);

            CouponItem couponItem = new CouponItem();
            couponItem.setCouponID(Math.round(Math.random()* 9999) + Math.round(Math.random()* 9999) + "");
            couponItem.setStoreName("7-11");
            couponItem.setCouponName("飲料買一送一");
            couponItem.setCouponContent("來電任意飲料買一送一");
            couponItem.setImageUrl("");
            couponItem.setCouponBonus(3000);
            couponItem.setStartTime("2015/10/01");
            couponItem.setEndTime("2016/10/01");

            mainActivity.getCouponDAO().insert(couponItem);

            EventBus.getDefault().post(UserInfoManager.getInstance());

            Toast.makeText(mainActivity.getApplication(), "兌換成功", Toast.LENGTH_LONG).show();

            couponDetailDialog.dismiss();
        }
    }
}
