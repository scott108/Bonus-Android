package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.gategory.CategoryAdapter;
import com.example.scott.bonus.sqlite.doa.InvoiceDAO;
import com.example.scott.bonus.sqlite.doa.InvoiceGoodsDAO;
import com.example.scott.bonus.sqlite.entity.InvoiceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 15/4/20.
 */
public class InvoiceFragment extends Fragment {
    ListView categoryList;
    MainActivity mainActivity;
    LayoutInflater inflater;
    CategoryAdapter categoryAdapter;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.invoice, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        categoryAdapter = mainActivity.getInvoiceFragmentControl().getmCategoryAdapter();
        mainActivity.getActionBar().setCustomView(R.layout.invoice_title);
        categoryList = (ListView) this.getView().findViewById(R.id.invoiceList);
        categoryList.setAdapter(categoryAdapter);

        //mCategoryAdapter.addCategory("2015年 1~2月", new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, mContacts));

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                //Toast.makeText(mainActivity, "myPos " + i, Toast.LENGTH_LONG).show();
                mainActivity.getInvoiceFragmentControl().showInvoiceDetailDialog(categoryAdapter.getInvoiceNum(i));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        categoryList.setAdapter(categoryAdapter);
    }

}
