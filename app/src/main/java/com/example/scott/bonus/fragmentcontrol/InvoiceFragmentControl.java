package com.example.scott.bonus.fragmentcontrol;

import android.app.Dialog;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scott.bonus.API;
import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.UserInfoManager;
import com.example.scott.bonus.customcheckbox.TouchCheckBox;
import com.example.scott.bonus.fragmentcontrol.entities.InvoiceInfo;
import com.example.scott.bonus.fragmentcontrol.invoiceAdapter.InvoiceAdapter;
import com.example.scott.bonus.session.SessionManager;
import com.example.scott.bonus.sqlite.entity.InvoiceGoodsItem;
import com.example.scott.bonus.sqlite.entity.InvoiceItem;
import com.example.scott.bonus.utility.BackgroundLoginTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Scott on 15/4/27.
 */
public class InvoiceFragmentControl {

    private MainActivity mainActivity;
    private ArrayList<String> tempInvoiceList;
    private Dialog invoiceDetailDialog;
    int width;
    int height;

    private InvoiceAdapter isNotExchangedInvoiceAdapter = new InvoiceAdapter() {
        @Override
        protected View getTitleView(String title, int index, View convertView, ViewGroup parent) {
            TextView titleView;

            if (convertView == null) {
                titleView = (TextView)mainActivity.getLayoutInflater().inflate(R.layout.invoice_list_group_item, null);
            } else {
                titleView = (TextView)convertView;
            }
            titleView.setText(title);

            return titleView;
        }

        @Override
        protected View getItemView(String caption, int index, View convertView, ViewGroup parent) {
            LinearLayout listItem;

            if (convertView == null) {
                listItem = (LinearLayout) mainActivity.getLayoutInflater().inflate(R.layout.invoice_list_view_item, null);
            } else {
                listItem = (LinearLayout) convertView;
            }
            TextView invoiceItemTitle = (TextView) listItem.findViewById(R.id.invoice_item_title);
            JSONObject jsonObject = null;
            String itemTitle = "";
            try {
                jsonObject = new JSONObject(caption);
                itemTitle = "\n發票統編:" + jsonObject.getString("發票統編") + "\n" +
                            "店名:" + jsonObject.getString("店名") + "\n" +
                            "消費時間:" + jsonObject.get("消費時間") + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            invoiceItemTitle.setText(itemTitle);
            ImageView invoiceIcon = (ImageView) listItem.findViewById(R.id.invoice_icon);
            invoiceIcon.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_launcher));

            TouchCheckBox touchCheckBox = (TouchCheckBox) listItem.findViewById(R.id.checkbox);
            touchCheckBox.setCircleColor(mainActivity.getResources().getColor(R.color.primary));

            System.out.println(touchCheckBox.isChecked());

            return listItem;
        }
    };

    private InvoiceAdapter isExchangedInvoiceAdapter = new InvoiceAdapter() {
        @Override
        protected View getTitleView(String title, int index, View convertView, ViewGroup parent) {
            TextView titleView;

            if (convertView == null) {
                titleView = (TextView)mainActivity.getLayoutInflater().inflate(R.layout.invoice_list_group_item, null);
            } else {
                titleView = (TextView)convertView;
            }
            titleView.setText(title);

            return titleView;
        }

        @Override
        protected View getItemView(String caption, int index, View convertView, ViewGroup parent) {
            LinearLayout listItem;

            if (convertView == null) {
                listItem = (LinearLayout) mainActivity.getLayoutInflater().inflate(R.layout.invoice_list_view_item, null);
            } else {
                listItem = (LinearLayout) convertView;
            }
            TextView invoiceItemTitle = (TextView) listItem.findViewById(R.id.invoice_item_title);
            JSONObject jsonObject = null;
            String itemTitle = "";
            try {
                jsonObject = new JSONObject(caption);
                itemTitle = "\n發票統編:" + jsonObject.getString("發票統編") + "\n" +
                        "店名:" + jsonObject.getString("店名") + "\n" +
                        "消費時間:" + jsonObject.get("消費時間") + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            invoiceItemTitle.setText(itemTitle);
            ImageView invoiceIcon = (ImageView) listItem.findViewById(R.id.invoice_icon);
            invoiceIcon.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_launcher));
            TouchCheckBox touchCheckBox = (TouchCheckBox) listItem.findViewById(R.id.checkbox);
            touchCheckBox.setVisibility(View.INVISIBLE);

            return listItem;
        }
    };

    public InvoiceAdapter getIsNotExchangedInvoiceAdapter() {
        return isNotExchangedInvoiceAdapter;
    }

    public InvoiceAdapter getIsExchangedInvoiceAdapter() {
        return isExchangedInvoiceAdapter;
    }

    public InvoiceFragmentControl(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        //get screen size
        DisplayMetrics dm = new DisplayMetrics();
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width=dm.widthPixels;
        height=dm.heightPixels;
        invoiceDetailDialog = new Dialog(mainActivity);
        //invoiceDetailDialog.getWindow().setLayout(width, height);

        //get invoice data to add into listView
        List<InvoiceItem> invoiceItems =  mainActivity.getInvoiceDAO().getAll();
        JSONObject jsonObject = new JSONObject();
        for(int i = 0; i < invoiceItems.size(); i++) {
            try {
                jsonObject.put("發票統編", invoiceItems.get(i).getInvoiceNum());
                jsonObject.put("店名", invoiceItems.get(i).getStoreName());
                jsonObject.put("消費時間", invoiceItems.get(i).getCurrentTime());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String listContentTitle = jsonObject.toString();
            if(invoiceItems.get(i).getIsExchanged() == 0) {
                if(!isNotExchangedInvoiceAdapter.getGroupNames().contains(invoiceItems.get(i).getStoreName())) {
                    tempInvoiceList = new ArrayList<String>();
                    tempInvoiceList.add(listContentTitle);
                    isNotExchangedInvoiceAdapter.addInvoice(invoiceItems.get(i).getStoreName(), new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, tempInvoiceList));
                } else {
                    isNotExchangedInvoiceAdapter.addInvoiceInExistGroup(invoiceItems.get(i).getStoreName(), listContentTitle);
                }
                isNotExchangedInvoiceAdapter.notifyDataSetChanged();
                String trigger = "";
                EventBus.getDefault().post(trigger);
            } else {
                if(!isExchangedInvoiceAdapter.getGroupNames().contains(invoiceItems.get(i).getStoreName())) {
                    tempInvoiceList = new ArrayList<String>();
                    tempInvoiceList.add(listContentTitle);
                    isExchangedInvoiceAdapter.addInvoice(invoiceItems.get(i).getStoreName(), new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, tempInvoiceList));
                } else {
                    isExchangedInvoiceAdapter.addInvoiceInExistGroup(invoiceItems.get(i).getStoreName(), listContentTitle);
                }
                isExchangedInvoiceAdapter.notifyDataSetChanged();
                String trigger = "";
                EventBus.getDefault().post(trigger);
            }

        }
    }

    private void addIntoIsExchangedList(String inoviceTitle) {
        try {
            JSONObject jsonObject = new JSONObject(inoviceTitle);

            if(!isExchangedInvoiceAdapter.getGroupNames().contains(jsonObject.getString("店名"))) {
                tempInvoiceList = new ArrayList<String>();
                tempInvoiceList.add(inoviceTitle);
                isExchangedInvoiceAdapter.addInvoice(jsonObject.getString("店名"), new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, tempInvoiceList));
            } else {
                isExchangedInvoiceAdapter.addInvoiceInExistGroup(jsonObject.getString("店名"), inoviceTitle);
            }
            isExchangedInvoiceAdapter.notifyDataSetChanged();
            String trigger = "";
            EventBus.getDefault().post(trigger);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void addNewInvoiceIntoAdapter(InvoiceItem invoiceItem) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("發票統編", invoiceItem.getInvoiceNum());
            jsonObject.put("店名", invoiceItem.getStoreName());
            jsonObject.put("消費時間", invoiceItem.getCurrentTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String listContentTitle = jsonObject.toString();

        if(!isNotExchangedInvoiceAdapter.getGroupNames().contains(invoiceItem.getStoreName())) {
            tempInvoiceList = new ArrayList<String>();
            tempInvoiceList.add(listContentTitle);
            isNotExchangedInvoiceAdapter.addInvoice(invoiceItem.getStoreName(), new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, tempInvoiceList));

        } else {
            isNotExchangedInvoiceAdapter.addInvoiceInExistGroup(invoiceItem.getStoreName(), listContentTitle);
        }
        isNotExchangedInvoiceAdapter.notifyDataSetChanged();
        String trigger = "";
        EventBus.getDefault().post(trigger);
    }

    public void showInvoiceDetailDialog(String invoiceNum, final int position) {
        final InvoiceItem invoiceItem = mainActivity.getInvoiceDAO().get(invoiceNum);
        if (invoiceItem != null) {
            List<InvoiceGoodsItem> invoiceGoodsItems = mainActivity.getInvoiceGoodsDAO().get(invoiceNum);
            invoiceDetailDialog.setContentView(R.layout.invoice_detail);
            TextView storeNameTextView = (TextView)invoiceDetailDialog.findViewById(R.id.storeNameTextView);
            TextView datelineTextView = (TextView)invoiceDetailDialog.findViewById(R.id.datelineTextView);
            TextView invoiceNumTextView = (TextView)invoiceDetailDialog.findViewById(R.id.invoiceNumTextView);
            TextView currentTimeTextView = (TextView)invoiceDetailDialog.findViewById(R.id.currentTimeTextView);
            TextView storeNumTextView = (TextView)invoiceDetailDialog.findViewById(R.id.storeNumTextView);
            TextView storePhoneTextView = (TextView)invoiceDetailDialog.findViewById(R.id.storePhoneTextView);
            TextView goodsDetailTextView = (TextView)invoiceDetailDialog.findViewById(R.id.goodsDetailTextView);
            TextView totalMoneyTextView = (TextView)invoiceDetailDialog.findViewById(R.id.totalMoneyTextView);
            TextView payDetailTextView = (TextView)invoiceDetailDialog.findViewById(R.id.payDetailTextView);

            storeNameTextView.setText(invoiceItem.getStoreName());
            datelineTextView.setText(invoiceItem.getDeadline());
            invoiceNumTextView.setText(invoiceItem.getInvoiceNum());
            currentTimeTextView.setText(invoiceItem.getCurrentTime());
            storeNumTextView.setText("賣方"+ invoiceItem.getStoreNum());
            storePhoneTextView.setText(invoiceItem.getStorePhone());
            goodsDetailTextView.setText("");
            for(int i = 0; i < invoiceGoodsItems.size(); i++) {
                String goods = invoiceGoodsItems.get(i).getGoodsName() + "    " + invoiceGoodsItems.get(i).getGoodsPrice() + "*" + "    "+
                        invoiceGoodsItems.get(i).getGoodsQuantity() + "    " + invoiceGoodsItems.get(i).getGoodsTotalPrice() + "T";
                if(i == 0) {
                    goodsDetailTextView.setText(goodsDetailTextView.getText() + goods);
                } else {
                    goodsDetailTextView.setText(goodsDetailTextView.getText() + "\n" + goods);
                }
            }
            totalMoneyTextView.setText(invoiceItem.getGoodsQuantity() + "項    合計" + invoiceItem.getTotalMoney());
            payDetailTextView.setText("現金    " + "$" + invoiceItem.getPayDetail() + "找零    " + "$" + invoiceItem.getPayBack());

            //query SQLite to add to dialog

            invoiceDetailDialog.show();
            WindowManager.LayoutParams params = invoiceDetailDialog.getWindow()
                    .getAttributes();
            params.width = width;
            params.height = height * 4 / 5;
            params.windowAnimations = R.style.PauseDialogAnimation;
            invoiceDetailDialog.getWindow().setAttributes(params);

            Button send = (Button) invoiceDetailDialog.findViewById(R.id.invoice_exchange_button);
            if(invoiceItem.getIsExchanged() == 1) {
                send.setVisibility(View.INVISIBLE);
            } else {
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Gson gson = new Gson();
                        String json = gson.toJson(invoiceItem);
                        System.out.println("Print local value : " + json);
                        new GetBonusTask().execute(json, String.valueOf(position));
                    }
                });
            }

            // Toast.makeText(mainActivity, "簽章：" + invoiceItem.getSignature(), Toast.LENGTH_LONG).show();
        }
    }

    class GetBonusTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            final String param = params[0];
            final int position = Integer.valueOf(params[1]);
            API.getInstance().getHttp().getBonus(SessionManager.getSessionID(), params[0], new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    System.out.println(jsonObject);
                    if(jsonObject.get("response").getAsString().equals("true")) {
                        UserInfoManager.getInstance().setBonus(jsonObject.get("bonus").getAsInt());

                        EventBus.getDefault().post(UserInfoManager.getInstance());
                        Gson gson = new Gson();
                        InvoiceItem invoiceItem  = gson.fromJson(param, InvoiceItem.class);
                        invoiceItem.setIsExchanged(1);
                        boolean result = mainActivity.getInvoiceDAO().update(invoiceItem);

                        String isExchangedInvoice = isNotExchangedInvoiceAdapter.removeInvoice(position);
                        isNotExchangedInvoiceAdapter.notifyDataSetChanged();

                        addIntoIsExchangedList(isExchangedInvoice);

                        invoiceDetailDialog.dismiss();
                        Toast.makeText(mainActivity, "兌換成功", Toast.LENGTH_LONG).show();
                    } else if(jsonObject.get("response").getAsString().equals("fail")){
                        invoiceDetailDialog.dismiss();
                        Toast.makeText(mainActivity, "兌換失敗", Toast.LENGTH_LONG).show();
                    } else {
                        mainActivity.loginCheck();
                        if(SessionManager.hasAttribute()) {
                            new GetBonusTask().execute(param);
                        } else {
                            Toast.makeText(mainActivity, "尚未登入", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
            return null;
        }
    }
}
