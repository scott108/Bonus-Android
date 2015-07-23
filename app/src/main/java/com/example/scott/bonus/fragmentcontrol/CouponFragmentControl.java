package com.example.scott.bonus.fragmentcontrol;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
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


    public CouponAdapter getmAdapter() {
        return mAdapter;
    }

    public CouponFragmentControl(MainActivity mainActivity) {

        this.mainActivity = mainActivity;

    }


}
