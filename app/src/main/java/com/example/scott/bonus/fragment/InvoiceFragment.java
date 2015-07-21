package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.fragmentcontrol.invoiceAdapter.Invoice;
import com.example.scott.bonus.fragmentcontrol.invoiceAdapter.InvoiceAdapter;

/**
 * Created by Scott on 15/4/20.
 */
public class InvoiceFragment extends Fragment {
    ListView categoryList;
    MainActivity mainActivity;
    LayoutInflater inflater;
    InvoiceAdapter invoiceAdapter;
    TextView textView;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.activity_invoice, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        invoiceAdapter = mainActivity.getInvoiceFragmentControl().getInvoiceAdapter();
        mainActivity.getActionBar().setCustomView(R.layout.invoice_title);
        textView = (TextView) this.getView().findViewById(R.id.textView);
        categoryList = (ListView) this.getView().findViewById(R.id.invoiceList);
        categoryList.setAdapter(invoiceAdapter);

        if(categoryList.getCount() == 0 ){
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                //Toast.makeText(mainActivity, "myPos " + i, Toast.LENGTH_LONG).show();
                mainActivity.getInvoiceFragmentControl().showInvoiceDetailDialog(invoiceAdapter.getInvoiceNum(i));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        categoryList.setAdapter(invoiceAdapter);

        if(invoiceAdapter.getCount() == 0 ){
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }


}
