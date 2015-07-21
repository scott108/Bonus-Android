package com.example.scott.bonus.fragmentcontrol.invoiceAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

/**
 * Created by Scott on 15/4/20.
 */
public abstract class InvoiceAdapter extends BaseAdapter {
    private List<Invoice> invoices = new ArrayList<Invoice>();
    private List<String> groupNames = new ArrayList<String>();

    private HashMap<String, Invoice> classHashMap = new HashMap<String, Invoice>();
    private HashMap<String, String> invoiceNumHashMap = new HashMap<String, String>();

    public List<String> getGroupNames() {
        return groupNames;
    }

    public void addInvoice(String title, String invoiceNum, ArrayAdapter adapter) {
        Invoice invoice = new Invoice(title, adapter);
        invoices.add(invoice);
        classHashMap.put(title, invoice);
        groupNames.add(title);
        invoiceNumHashMap.put(adapter.getItem(0).toString(), invoiceNum);
    }

    public void addInvoiceInExistGroup(String title, String invoiceNum, String listContentTitle) {
        Invoice invoice = classHashMap.get(title);
        ArrayAdapter adapter = invoice.getAdapter();
        System.out.println(adapter.getItem(0));
        adapter.add(listContentTitle);
        invoiceNumHashMap.put(adapter.getItem(adapter.getCount() - 1).toString(), invoiceNum);
    }

    public String getInvoiceNum(int position) {
        String invoiceNum = invoiceNumHashMap.get(getItem(position));
        return invoiceNum;
    }

    @Override
    public int getCount() {
        int total = 0;

        for (Invoice invoice : invoices) {
            total += invoice.getAdapter().getCount() + 1;
        }

        return total;
    }

    @Override
    public Object getItem(int position) {
        for (Invoice invoice : invoices) {
            if (position == 0) {
                return invoice;
            }

            int size = invoice.getAdapter().getCount() + 1;
            if (position < size) {
                return invoice.getAdapter().getItem(position-1);
            }
            position -= size;
        }

        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getViewTypeCount() {
        int total = 1;

        for (Invoice invoice : invoices) {
            total += invoice.getAdapter().getViewTypeCount();
        }

        return total;
    }
    public int getItemViewType(int position) {
        int typeOffset = 1;

        for (Invoice invoice : invoices) {
            if (position == 0) {
                return 0;
            }

            int size = invoice.getAdapter().getCount() + 1;
            if (position < size) {
                return typeOffset + invoice.getAdapter().getItemViewType(position - 1);
            }
            position -= size;

            typeOffset += invoice.getAdapter().getViewTypeCount();
        }

        return -1;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int invoiceIndex = 0;

        for (Invoice invoice : invoices) {
            if (position == 0) {
                return getTitleView(invoice.getTitle(), invoiceIndex,convertView, parent);
            }
            int size = invoice.getAdapter().getCount()+1;
            if (position < size) {
                return invoice.getAdapter().getView(position - 1, convertView, parent);
            }
            position -= size;

            invoiceIndex++;
        }

        return null;
    }

    protected abstract View getTitleView(String caption,int index,View convertView,ViewGroup parent);

}
