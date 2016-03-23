package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.fragmentcontrol.couponadapter.CouponAdapter;
import com.example.scott.bonus.itemanimator.CustomItemAnimator;
import com.example.scott.bonus.sqlite.entity.CouponItem;
import com.example.scott.bonus.utility.ApiType;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Scott on 15/9/28.
 */
public class MyCouponFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerView mRecyclerView;
    private CouponAdapter mAdapter;
    private TextView mHintTextView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_mycoupon, container, false);
        mAdapter = new CouponAdapter(new ArrayList<CouponItem>(), R.layout.coupon_cardview_item) {
            @Override
            protected void onViewHolder(ViewHolder viewHolder, int i) {
                final CouponItem couponItem = getCoupons().get(i);

                viewHolder.getCouponName().setText(couponItem.getStoreName() + "\n" + couponItem.getCouponName());
                if(couponItem.getIsUsed() == 0) {
                    viewHolder.getIsUsed().setText("未使用");
                    viewHolder.getIsUsed().setTextColor(mainActivity.getResources().getColor(R.color.green));
                } else {
                    viewHolder.getIsUsed().setText("已使用");
                    viewHolder.getIsUsed().setTextColor(mainActivity.getResources().getColor(R.color.red));
                }

                viewHolder.getIsUsed().setVisibility(View.VISIBLE);

                viewHolder.getCouponBonus().setVisibility(View.GONE);

                viewHolder.getBnp().setVisibility(View.GONE);

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

                viewHolder.getCardViewItemLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainActivity.getCouponFragmentControl().showMyCouponDetail(couponItem);
                    }
                });
            }
        };
        mHintTextView = (TextView) layout.findViewById(R.id.hintTextView);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycleList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        mRecyclerView.setItemAnimator(new CustomItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        getAllMyCoupon();

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

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 300, 300, false);
        return new BitmapDrawable(mainActivity.getResources(), bitmapResized);
    }

    private void getAllMyCoupon() {
        mAdapter.getCoupons().clear();
        mAdapter.addApplications(mainActivity.getCouponDAO().getAll());
        mAdapter.notifyDataSetChanged();

        if(mAdapter.getCoupons().size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mHintTextView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mHintTextView.setVisibility(View.VISIBLE);
        }
    }
}
