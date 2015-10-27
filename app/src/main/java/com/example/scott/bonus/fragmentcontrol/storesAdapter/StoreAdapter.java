package com.example.scott.bonus.fragmentcontrol.storesAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Scott on 15/9/30.
 */
public abstract class StoreAdapter extends BaseAdapter {
    private List<Store> stores = new ArrayList<Store>();
    private List<String> groupNames = new ArrayList<String>();

    private HashMap<String, Store> classHashMap = new HashMap<String, Store>();

    public void addStore(Store store) {
        stores.add(store);
    }

    public void removeStore(int position) {
        stores.remove(position);
    }

    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public Object getItem(int position) {
        return stores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getItemView(stores.get(position), position, convertView, parent);
    }

    protected abstract View getItemView(Store store,int index,View convertView,ViewGroup parent);
}

