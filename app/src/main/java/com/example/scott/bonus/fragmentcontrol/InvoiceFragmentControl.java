package com.example.scott.bonus.fragmentcontrol;

import android.app.Dialog;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.gategory.CategoryAdapter;
import com.example.scott.bonus.sqlite.entity.InvoiceGoodsItem;
import com.example.scott.bonus.sqlite.entity.InvoiceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 15/4/27.
 */
public class InvoiceFragmentControl {

    private MainActivity mainActivity;
    private ArrayList<String> tempInvoiceList;
    private Dialog invoiceDetailDialog;
    int width;
    int height;
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
            if(!mCategoryAdapter.getClassName().contains(invoiceItems.get(i).getStoreName())) {
                tempInvoiceList = new ArrayList<String>();
                tempInvoiceList.add(listContentTitle);
                mCategoryAdapter.addCategory(invoiceItems.get(i).getStoreName(), invoiceItems.get(i).getInvoiceNum(), new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, tempInvoiceList));
            } else {
                mCategoryAdapter.addCategoryInExistClass(invoiceItems.get(i).getStoreName(), invoiceItems.get(i).getInvoiceNum(), listContentTitle);
            }
        }
    }

    private CategoryAdapter mCategoryAdapter = new CategoryAdapter() {
        @Override
        protected View getTitleView(String title, int index, View convertView, ViewGroup parent) {
            TextView titleView;

            if (convertView == null) {
                titleView = (TextView)mainActivity.getLayoutInflater().inflate(R.layout.invoice_list_title, null);
            } else {
                titleView = (TextView)convertView;
            }
            titleView.setText(title);

            return titleView;
        }
    };

    public CategoryAdapter getmCategoryAdapter() {
        return mCategoryAdapter;
    }

    public void addNewInvoiceIntoAdapter(InvoiceItem invoiceItem) {

        //get invoice data to add into listView
        String listContentTitle = "\n發票統編：" + invoiceItem.getInvoiceNum() + "\n店名：" + invoiceItem.getStoreName()  + "\n消費時間："+ invoiceItem.getCurrentTime() + "\n";
        if(!mCategoryAdapter.getClassName().contains(invoiceItem.getStoreName())) {
            tempInvoiceList = new ArrayList<String>();
            tempInvoiceList.add(listContentTitle);
            mCategoryAdapter.addCategory(invoiceItem.getStoreName(), invoiceItem.getInvoiceNum(), new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, tempInvoiceList));
        } else {
            mCategoryAdapter.addCategoryInExistClass(invoiceItem.getStoreName(), invoiceItem.getInvoiceNum(), listContentTitle);
        }
    }

    public void showInvoiceDetailDialog(String invoiceNum) {
        // custom dialog
        InvoiceItem invoiceItem = mainActivity.getInvoiceDAO().get(invoiceNum);

        if (invoiceItem != null) {
            List<InvoiceGoodsItem> invoiceGoodsItems = mainActivity.getInvoiceGoodsDAO().get(invoiceNum);
            invoiceDetailDialog.setContentView(R.layout.activity_invoice_detail);
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
            // Toast.makeText(mainActivity, "簽章：" + invoiceItem.getSignature(), Toast.LENGTH_LONG).show();
        }
    }
}
