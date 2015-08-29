package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.fragmentcontrol.invoiceAdapter.InvoiceAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Scott on 15/7/22.
 */
public class InvoiceExchangedFragment extends Fragment {
    ListView categoryList;
    MainActivity mainActivity;
    LayoutInflater inflater;
    InvoiceAdapter invoiceAdapter;
    TextView invoiceExchangedTextView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        if(savedInstanceState == null) {
            EventBus.getDefault().register(this);
        }
        return inflater.inflate(R.layout.fragment_invoice_exchanged, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        invoiceAdapter = mainActivity.getInvoiceFragmentControl().getIsExchangedInvoiceAdapter();
        invoiceExchangedTextView = (TextView) this.getView().findViewById(R.id.invoiceExchangedTextView);

        categoryList = (ListView) this.getView().findViewById(R.id.invoiceExchangedList);
        categoryList.setAdapter(invoiceAdapter);

        if(categoryList.getCount() == 0 ){
            invoiceExchangedTextView.setVisibility(View.VISIBLE);
        } else {
            invoiceExchangedTextView.setVisibility(View.GONE);
        }

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                //Toast.makeText(mainActivity, "myPos " + i, Toast.LENGTH_LONG).show();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(invoiceAdapter.getItem(i).toString());
                    mainActivity.getInvoiceFragmentControl().showInvoiceDetailDialog(jsonObject.getString("發票統編"), i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    }

    public void onEvent(String trigger) {
        if(invoiceAdapter.getCount() == 0 ){
            invoiceExchangedTextView.setVisibility(View.VISIBLE);
        } else {
            invoiceExchangedTextView.setVisibility(View.GONE);
        }
    }
}
