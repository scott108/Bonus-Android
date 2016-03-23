package com.example.scott.bonus.fragmentcontrol.couponadapter;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.UserInfoManager;
import com.example.scott.bonus.sqlite.entity.CouponItem;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Scott on 15/5/7.
 */
public abstract class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder>{

    private List<CouponItem> coupons;
    private int rowLayout;


    public List<CouponItem> getCoupons() {
        return coupons;
    }

    public CouponAdapter(List<CouponItem> coupons, int rowLayout) {
        this.coupons = coupons;
        this.rowLayout = rowLayout;
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

    public void addApplications(List<CouponItem> applications) {
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
        onViewHolder(viewHolder, i);
    }

    @Override
    public int getItemCount() {
        return coupons == null ? 0 : coupons.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView couponName, deadline, couponBonus, isUsed;
        private ImageView image;
        private NumberProgressBar bnp;

        LinearLayout cardViewItemLayout;

        public ImageView getImage() {
            return image;
        }

        public LinearLayout getCardViewItemLayout() {
            return cardViewItemLayout;
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

        public TextView getIsUsed() {
            return isUsed;
        }

        public ViewHolder(final View itemView) {
            super(itemView);
            couponName = (TextView) itemView.findViewById(R.id.coupon_name);
            couponBonus = (TextView) itemView.findViewById(R.id.couponBonus);
            isUsed = (TextView) itemView.findViewById(R.id.isUsed);
            bnp = (NumberProgressBar) itemView.findViewById(R.id.number_progress_bar);

            image = (ImageView) itemView.findViewById(R.id.countryImage);

            cardViewItemLayout = (LinearLayout) itemView.findViewById(R.id.cardViewItemLayout);
        }
    }

    protected abstract void onViewHolder(final ViewHolder viewHolder, int i);
}
