package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.fragmentcontrol.invoiceAdapter.InvoiceAdapter;

/**
 * Created by Scott on 15/7/22.
 */
public class InvoiceUnExchangedFragment extends Fragment {
    ListView categoryList;
    MainActivity mainActivity;
    LayoutInflater inflater;
    InvoiceAdapter invoiceAdapter;
    TextView invoiceUnexchangedTextView;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_invoice_unexchanged, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        invoiceAdapter = mainActivity.getInvoiceFragmentControl().getInvoiceAdapter();
        invoiceUnexchangedTextView = (TextView) this.getView().findViewById(R.id.invoiceUnexchangedTextView);
        categoryList = (ListView) this.getView().findViewById(R.id.invoiceUnexchangedList);
        categoryList.setAdapter(invoiceAdapter);

        if(categoryList.getCount() == 0 ){
            invoiceUnexchangedTextView.setVisibility(View.VISIBLE);
        } else {
            invoiceUnexchangedTextView.setVisibility(View.GONE);
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
        //invoiceAdapter.notifyDataSetChanged();

        if(invoiceAdapter.getCount() == 0 ){
            invoiceUnexchangedTextView.setVisibility(View.VISIBLE);
        } else {
            invoiceUnexchangedTextView.setVisibility(View.GONE);
        }
    }
}
