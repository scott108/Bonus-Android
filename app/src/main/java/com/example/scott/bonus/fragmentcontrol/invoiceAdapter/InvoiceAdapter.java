package com.example.scott.bonus.fragmentcontrol.invoiceAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Scott on 15/4/20.
 */
public abstract class InvoiceAdapter extends BaseAdapter {
    private List<Invoice> invoices = new ArrayList<Invoice>();
    private List<String> groupNames = new ArrayList<String>();

    private HashMap<String, Invoice> classHashMap = new HashMap<String, Invoice>();

    public List<String> getGroupNames() {
        return groupNames;
    }

    public void addInvoice(String title, ArrayAdapter adapter) {
        Invoice invoice = new Invoice(title, adapter);
        invoices.add(invoice);
        classHashMap.put(title, invoice);
        groupNames.add(title);
    }

    public String removeInvoice(int position) {
        for (Invoice invoice : invoices) {
            int size = invoice.getAdapter().getCount() + 1;
            if (position < size) {
                String invoiceTitle = invoice.getAdapter().getItem(position - 1).toString();
                invoice.getAdapter().remove(invoice.getAdapter().getItem(position - 1));
                if (invoice.getAdapter().getCount() == 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(invoiceTitle);
                        String groupName = jsonObject.getString("店名");
                        System.out.println(groupName);
                        classHashMap.remove(groupName);
                        groupNames.remove(groupName);
                        for(int i = 0;;i++) {
                            if(invoices.get(i).getTitle().equals(groupName)) {
                                invoices.remove(i);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return invoiceTitle;
            }
            position -= size;
        }
        return null;
    }

    public void addInvoiceInExistGroup(String title, String listContentTitle) {
        Invoice invoice = classHashMap.get(title);
        ArrayAdapter adapter = invoice.getAdapter();
        adapter.add(listContentTitle);
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
                return getTitleView(invoice.getTitle(), invoiceIndex, convertView, parent);
            }
            int size = invoice.getAdapter().getCount()+1;
            if (position < size) {
                return getItemView(invoice.getAdapter().getItem(position - 1).toString(), invoiceIndex, convertView, parent);
                //return invoice.getAdapter().getView(position - 1, convertView, parent);
            }
            position -= size;

            invoiceIndex++;
        }

        return null;
    }

    protected abstract View getTitleView(String caption,int index,View convertView,ViewGroup parent);
    protected abstract View getItemView(String caption,int index,View convertView,ViewGroup parent);
}
