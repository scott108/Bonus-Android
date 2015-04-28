package com.example.scott.bonus.gategory;

/**
 * Created by Scott on 15/4/20.
 */
import android.widget.Adapter;
import android.widget.ArrayAdapter;

public class Category {
    private String mTitle;
    private ArrayAdapter mAdapter;
    public Category(String title, ArrayAdapter adapter) {
        mTitle = title;
        mAdapter = adapter;
    }
    public void setTile(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setAdapter(ArrayAdapter adapter) {
        mAdapter = adapter;
    }

    public ArrayAdapter getAdapter() {
        return mAdapter;
    }

}
