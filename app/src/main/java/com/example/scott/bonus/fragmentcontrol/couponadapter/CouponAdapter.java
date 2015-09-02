package com.example.scott.bonus.fragmentcontrol.couponadapter;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.UserInfoManager;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Scott on 15/5/7.
 */
public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder>{

    private List<CouponInfo> coupons;
    private int rowLayout;
    private MainActivity mAct;

    private int currentBonus = 0;


    public void onEvent(int currentBonus) {
        this.currentBonus = currentBonus;
    }

    public CouponAdapter(List<CouponInfo> coupons, int rowLayout, MainActivity act) {
        this.coupons = coupons;
        this.rowLayout = rowLayout;
        this.mAct = act;
    }

    public void clearApplications() {
        int size = this.coupons.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                coupons.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addApplications(List<CouponInfo> applications) {
        this.coupons.addAll(applications);
        this.notifyItemRangeInserted(0, applications.size() - 1);


    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final CouponInfo couponInfo = coupons.get(i);

        viewHolder.getStoreName().setText("店家：" + couponInfo.getStoreName());
        viewHolder.getCouponName().setText("優惠卷名稱：" + couponInfo.getCouponName());
        viewHolder.getCouponBonus().setText("紅利：" + couponInfo.getCouponBonus() + " 點");

        int progress = 0;

        if(currentBonus >= couponInfo.getCouponBonus()) {
            progress = 100;
        } else {
            progress = ((currentBonus * 100 )/ couponInfo.getCouponBonus());
        }
        if(progress < 20) {
            setProgressBarColor(viewHolder.bnp, mAct.getResources().getColor(R.color.blue));
        } else if(progress > 20 && progress <= 50) {
            setProgressBarColor(viewHolder.bnp, mAct.getResources().getColor(R.color.green));
        } else if(progress > 50 && progress <= 80) {
            setProgressBarColor(viewHolder.bnp, mAct.getResources().getColor(R.color.dark_green));
        } else if(progress > 80 && progress <= 99) {
            setProgressBarColor(viewHolder.bnp, mAct.getResources().getColor(R.color.orange));
        } else {
            setProgressBarColor(viewHolder.bnp, mAct.getResources().getColor(R.color.red));
        }

        viewHolder.bnp.setProgress(progress);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return coupons == null ? 0 : coupons.size();
    }

    private void setProgressBarColor(NumberProgressBar numberProgressBar, int color) {
        numberProgressBar.setProgressTextColor(color);
        numberProgressBar.setReachedBarColor(color);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView storeName, couponName, deadline, couponBonus;
        private ImageView image;
        private NumberProgressBar bnp;

        public TextView getStoreName() {
            return storeName;
        }

        public TextView getCouponName() {
            return couponName;
        }

        public TextView getCouponBonus() {
            return couponBonus;
        }

        public NumberProgressBar getBnp() {
            return bnp;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            storeName = (TextView) itemView.findViewById(R.id.storeName);
            couponName = (TextView) itemView.findViewById(R.id.couponName);
            couponBonus = (TextView) itemView.findViewById(R.id.couponBonus);
            bnp = (NumberProgressBar) itemView.findViewById(R.id.number_progress_bar);

            //image = (ImageView) itemView.findViewById(R.id.countryImage);
        }
    }
}
