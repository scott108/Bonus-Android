package com.example.scott.bonus.fragmentcontrol.storesAdapter;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Scott on 15/9/30.
 */
public class Store {
    private String name;
    private String address;
    private String phoneNum;
    private ArrayList<String> products;

    public Store() {
        products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }
}
