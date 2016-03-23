package com.example.scott.bonus.fragmentcontrol;

import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Base64;
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
import com.example.scott.bonus.rsa.RSA;
import com.example.scott.bonus.session.SessionManager;
import com.example.scott.bonus.sqlite.entity.CouponItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

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

    final BigInteger modulus = new BigInteger("107241757999324904109676237233998063372282760155581141342752413692887583048814821221030706707167921452158792708120548149058657917192427214697842032427487630966828799686728050147100820423608156536978307513903790660036654957441997168808342303029956119479061610128933276777366502321051631391540621213726816239821");
    final BigInteger publicExponent = new BigInteger("65537");
    RSAPublicKey publicKey = RSA.getPublicKey(modulus, publicExponent);

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
        Button couponVerifyBbutton = (Button) couponDetailDialog.findViewById(R.id.coupon_verify_button);
        ImageView couponImage = (ImageView) couponDetailDialog.findViewById(R.id.coupon_image);

        couponVerifyBbutton.setVisibility(View.VISIBLE);

        couponExchangeBbutton.setText("使用優惠券");

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
                String couponString = gson.toJson(couponItem);
                String base64String = null;
                try {
                    base64String = Base64.encodeToString(couponString.getBytes("UTF-8"), Base64.DEFAULT);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] couponByte = Base64.decode(base64String, Base64.DEFAULT);
                mainActivity.setNfcMessage(couponByte);
            }
        });

        couponVerifyBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String couponContent = couponItem.getCouponID() + couponItem.getCouponName() + couponItem.getCouponContent() + couponItem.getStoreName() +
                        couponItem.getCouponBonus() + couponItem.getStartTime() + couponItem.getEndTime();

                byte[] sign = Base64.decode(couponItem.getSignature(), Base64.DEFAULT);

                boolean result = RSA.verify(couponContent, sign, publicKey);
                if (result) {
                    Toast.makeText(mainActivity, "簽章：" + '\n' + couponItem.getSignature() + '\n' + "驗證結果：驗證成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mainActivity, "簽章：" + '\n' + couponItem.getSignature() + '\n' + "驗證結果：驗證失敗", Toast.LENGTH_LONG).show();

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
            });
            return null;
        }
    }
}
