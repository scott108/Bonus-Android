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
import com.example.scott.bonus.fragmentcontrol.entities.InvoiceInfo;
import com.example.scott.bonus.fragmentcontrol.invoiceAdapter.InvoiceAdapter;
import com.example.scott.bonus.session.SessionManager;
import com.example.scott.bonus.sqlite.entity.InvoiceGoodsItem;
import com.example.scott.bonus.sqlite.entity.InvoiceItem;
import com.example.scott.bonus.utility.BackgroundLoginTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
            invoiceItemTitle.setText(caption);
            ImageView invoiceIcon = (ImageView) listItem.findViewById(R.id.invoice_icon);
            invoiceIcon.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_launcher));
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
            invoiceItemTitle.setText(caption);
            ImageView invoiceIcon = (ImageView) listItem.findViewById(R.id.invoice_icon);
            invoiceIcon.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_launcher));
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
        for(int i = 0; i < invoiceItems.size(); i++) {
            String listContentTitle = "\n發票統編：" + invoiceItems.get(i).getInvoiceNum() + "\n店名：" + invoiceItems.get(i).getStoreName()  + "\n消費時間："+ invoiceItems.get(i).getCurrentTime() + "\n";
            if(!isNotExchangedInvoiceAdapter.getGroupNames().contains(invoiceItems.get(i).getStoreName())
                    || !isExchangedInvoiceAdapter.getGroupNames().contains(invoiceItems.get(i).getStoreName())) {
                tempInvoiceList = new ArrayList<String>();
                tempInvoiceList.add(listContentTitle);
                if(invoiceItems.get(i).getIsExchanged() == 0) {
                    isNotExchangedInvoiceAdapter.addInvoice(invoiceItems.get(i).getStoreName(), invoiceItems.get(i).getInvoiceNum(), new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, tempInvoiceList));
                } else {
                    isExchangedInvoiceAdapter.addInvoice(invoiceItems.get(i).getStoreName(), invoiceItems.get(i).getInvoiceNum(), new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, tempInvoiceList));
                }
            } else {
                if(invoiceItems.get(i).getIsExchanged() == 0) {
                    isNotExchangedInvoiceAdapter.addInvoiceInExistGroup(invoiceItems.get(i).getStoreName(), invoiceItems.get(i).getInvoiceNum(), listContentTitle);
                } else {
                    isExchangedInvoiceAdapter.addInvoiceInExistGroup(invoiceItems.get(i).getStoreName(), invoiceItems.get(i).getInvoiceNum(), listContentTitle);
                }
            }
        }
    }


    public void addNewInvoiceIntoAdapter(InvoiceItem invoiceItem) {

        //get invoice data to add into listView
        String listContentTitle = "\n發票統編：" + invoiceItem.getInvoiceNum() + "\n店名：" + invoiceItem.getStoreName()  + "\n消費時間："+ invoiceItem.getCurrentTime() + "\n";
        if(!isNotExchangedInvoiceAdapter.getGroupNames().contains(invoiceItem.getStoreName())) {
            tempInvoiceList = new ArrayList<String>();
            tempInvoiceList.add(listContentTitle);
            isNotExchangedInvoiceAdapter.addInvoice(invoiceItem.getStoreName(), invoiceItem.getInvoiceNum(), new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, tempInvoiceList));
        } else {
            isNotExchangedInvoiceAdapter.addInvoiceInExistGroup(invoiceItem.getStoreName(), invoiceItem.getInvoiceNum(), listContentTitle);
        }
    }

    public void showInvoiceDetailDialog(String invoiceNum) {
        InvoiceItem invoiceItem = mainActivity.getInvoiceDAO().get(invoiceNum);
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
            datelineTextView.setText(invoiceItem.getDateline());
            invoiceNumTextView.setText(invoiceItem.getInvoiceNum());
            currentTimeTextView.setText(invoiceItem.getCurrentTime());
            storeNumTextView.setText(invoiceItem.getStoreNum());
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
            totalMoneyTextView.setText(invoiceItem.getTotalMoney());
            payDetailTextView.setText(invoiceItem.getPayDetail());

            //query SQLite to add to dialog

            invoiceDetailDialog.show();
            WindowManager.LayoutParams params = invoiceDetailDialog.getWindow()
                    .getAttributes();
            params.width = width;
            params.height = height * 4 / 5;
            params.windowAnimations = R.style.PauseDialogAnimation;
            invoiceDetailDialog.getWindow().setAttributes(params);

            Button send = (Button) invoiceDetailDialog.findViewById(R.id.invoice_exchange_button);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    InvoiceInfo invoiceInfo = new InvoiceInfo();
                    invoiceInfo.setTestString("TEST");
                    invoiceInfo.setTestSignature("1234567890");
                    String json = gson.toJson(invoiceInfo);
                    System.out.println("Print local value : " + json);
                    new GetBonusTask().execute(json);
                }
            });

            // Toast.makeText(mainActivity, "簽章：" + invoiceItem.getSignature(), Toast.LENGTH_LONG).show();
        }
    }

    class GetBonusTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            final String param = params[0];
            API.getInstance().getHttp().getBonus(SessionManager.getSessionID(), params[0], new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    System.out.println(jsonObject);
                    if(jsonObject.get("response").getAsString().equals("true")) {
                        UserInfoManager.getInstance().setBonus(jsonObject.get("bonus").getAsInt());

                        EventBus.getDefault().post(UserInfoManager.getInstance());
                        invoiceDetailDialog.dismiss();
                        Toast.makeText(mainActivity, "兌換成功", Toast.LENGTH_LONG).show();
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
