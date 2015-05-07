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

import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;

import java.util.List;

/**
 * Created by Scott on 15/5/7.
 */
public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder>{

    private List<CouponInfo> coupons;
    private int rowLayout;
    private MainActivity mAct;

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

        viewHolder.name.setText(couponInfo.getName());
        viewHolder.number.setText(couponInfo.getNumber());

        viewHolder.image.setImageBitmap(couponInfo.getIcon());

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, number, deadline, content;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            number = (TextView) itemView.findViewById(R.id.number);
            image = (ImageView) itemView.findViewById(R.id.countryImage);
        }

    }
}
