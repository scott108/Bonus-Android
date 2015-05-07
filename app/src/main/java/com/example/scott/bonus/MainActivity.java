package com.example.scott.bonus;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scott.bonus.fragment.CouponFragment;
import com.example.scott.bonus.fragment.InvoiceFragment;
import com.example.scott.bonus.fragment.UserFragment;
import com.example.scott.bonus.fragmentcontrol.InvoiceFragmentControl;
import com.example.scott.bonus.gategory.CategoryAdapter;
import com.example.scott.bonus.sqlite.doa.InvoiceDAO;
import com.example.scott.bonus.sqlite.doa.InvoiceGoodsDAO;
import com.example.scott.bonus.sqlite.entity.InvoiceGoodsItem;
import com.example.scott.bonus.sqlite.entity.InvoiceItem;
import com.example.scott.bonus.sqlite.MyDBHelper;
import com.example.scott.bonus.utility.utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements CreateNdefMessageCallback, OnNdefPushCompleteCallback {
    private Dialog invoiceDetailDialog;
    private NfcAdapter mNfcAdapter;
    private NdefMessage ndefMessage;
    protected PendingIntent nfcPendingIntent;
    private IntentFilter[] tagFilters;
    private Intent origIntent;
    private InvoiceDAO invoiceDAO;
    private InvoiceGoodsDAO invoiceGoodsDAO;
    private InvoiceFragment invoiceFragment;
    private CouponFragment couponFragment;
    private UserFragment userFragment;
    private InvoiceFragmentControl invoiceFragmentControl;
    String domain = "com.example.scott.androidbream";
    String type = "icheedata";
    int width;
    int height;

    public InvoiceFragment getInvoiceFragment() {
        return invoiceFragment;
    }

    public InvoiceFragmentControl getInvoiceFragmentControl() {
        return invoiceFragmentControl;
    }

    public InvoiceDAO getInvoiceDAO() {
        return invoiceDAO;
    }

    public InvoiceGoodsDAO getInvoiceGoodsDAO() {
        return invoiceGoodsDAO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set custom action bar title
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);

        invoiceFragment = new InvoiceFragment();
        couponFragment  = new CouponFragment();
        userFragment = new UserFragment();

        buildSQLite();

        invoiceFragmentControl = new InvoiceFragmentControl(this);

        initTabFragment();

        initDialog();

        initNFCAdapter();


        origIntent = getIntent();

    }

    @Override
    public void onNewIntent(Intent intent) {
        System.out.println("On New Inten Thread : " + Thread.currentThread().getName().toString());
        String appSpecificPath = intent.getDataString();
        System.out.println("appSpecificPath :" + appSpecificPath + intent);
        setIntent(intent);
        /*
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) && getIntent().getDataString().equals("vnd.android.nfc://ext/" + domain + ":" + type)) {
            processIntent(getIntent());
        }
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("On Resume Thread : " + Thread.currentThread().getName().toString());
        mNfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, tagFilters, null);
        // Check to see that the Activity started due to an Android Beam
        System.out.println("intent :" + getIntent().getAction().toString());
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
            setIntent(origIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        NdefMessage msg = ndefMessage;
        System.out.println("detected");
        return msg;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        System.out.println("Finish Pushed");
        ndefMessage = null;
    }

    private void initTabFragment() {
        //Fragment tab setting
        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //1
        tabHost.addTab(tabHost.newTabSpec("電子發票")
                        .setIndicator("電子發票"),
                invoiceFragment.getClass(),
                null);
        //2
        tabHost.addTab(tabHost.newTabSpec("優惠卷")
                        .setIndicator("優惠卷"),
                couponFragment.getClass(),
                null);
        //3
        tabHost.addTab(tabHost.newTabSpec("帳戶")
                        .setIndicator("帳戶"),
                userFragment.getClass(),
                null);

    }

    private void initDialog() {
        //get screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width=dm.widthPixels;
        height=dm.heightPixels;
        invoiceDetailDialog = new Dialog(this);
    }

    private void initNFCAdapter() {
        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Register Android Beam callback
        mNfcAdapter.setNdefPushMessageCallback(this, this);
        // Register callback to listen for message-sent success
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            this.finish();
            return;
        }

        // Register callback
        mNfcAdapter.setNdefPushMessageCallback(this, this);

        nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        tagDetected.addDataScheme("vnd.android.nfc");
        tagDetected.addDataAuthority("ext", null);
        tagDetected.addDataPath("/" + domain + ":" + type, 0);
        tagFilters = new IntentFilter[]{tagDetected};
    }

    private void buildSQLite() {

        MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext(), MyDBHelper.DATABASE_NAME, null, MyDBHelper.VERSION);

        // 建立資料庫物件
        invoiceDAO = new InvoiceDAO(getApplicationContext());
        invoiceGoodsDAO = new InvoiceGoodsDAO(getApplicationContext());

        // 取得所有記事資料
        List<InvoiceItem> items = invoiceDAO.getAll();

        for(int i = 0; i < items.size(); i++) {
            System.out.println(items.get(i).getInvoiceNum());
        }

        List<InvoiceGoodsItem> goods = invoiceGoodsDAO.getAll();

        for(int i = 0; i < goods.size(); i++) {
            System.out.println(goods.get(i).getInvoiceNum() + "    " + goods.get(i).getGoodsName());
        }
    }

    private void showInvoiceDetailDialog(byte[] invoiceByte) {
        // custom dialog
        invoiceDetailDialog.setContentView(R.layout.activity_invoice_detail);

        JSONObject jsonObject = utility.Base64ByteToJson(invoiceByte);

        //System.out.println("FUK" + message);

        TextView invoiceStore = (TextView) invoiceDetailDialog.findViewById(R.id.storeNameTextView);
        TextView invoiceDateline = (TextView) invoiceDetailDialog.findViewById(R.id.datelineTextView);
        TextView invoiceNum = (TextView) invoiceDetailDialog.findViewById(R.id.invoiceNumTextView);
        TextView invoiceCurrentTime = (TextView) invoiceDetailDialog.findViewById(R.id.currentTimeTextView);
        TextView invoiceStoreNum = (TextView) invoiceDetailDialog.findViewById(R.id.storeNumTextView);
        TextView invoiceStorePhone = (TextView) invoiceDetailDialog.findViewById(R.id.storePhoneTextView);
        TextView invoiceGoodsList = (TextView) invoiceDetailDialog.findViewById(R.id.goodsDetailTextView);
        TextView invoiceTotalMoney = (TextView) invoiceDetailDialog.findViewById(R.id.totalMoneyTextView);
        TextView invoicePayDetail = (TextView) invoiceDetailDialog.findViewById(R.id.payDetailTextView);

        try {
            invoiceStore.setText(jsonObject.getString("StoreName"));
            invoiceDateline.setText(jsonObject.getString("Dateline"));
            invoiceNum.setText(jsonObject.getString("InvoiceNum"));
            invoiceCurrentTime.setText(jsonObject.getString("CurrentTime"));
            invoiceStoreNum.setText(jsonObject.getString("StoreNum"));
            invoiceStorePhone.setText(jsonObject.getString("StorePhone"));
            invoiceGoodsList.setText("");
            for(int i = 0;; i++) {
                if(!jsonObject.has("Goods" + i)) {
                    break;
                }

                String[] token = jsonObject.getString("Goods" + i).split(",");
                if(i == 0) {
                    invoiceGoodsList.setText(invoiceGoodsList.getText() + token[0] + "    " + token[1] + "*" + "    "
                            + token[2] + "    " + token[3] + "T");
                }
                else {
                    invoiceGoodsList.setText(invoiceGoodsList.getText() + "\n" + token[0] + "    " + token[1] + "*" + "    "
                                                                        + token[2] + "    " + token[3] + "T");
                }
            }
            invoiceTotalMoney.setText(jsonObject.getString("TotalMoney"));
            invoicePayDetail.setText(jsonObject.getString("PayDetail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        invoiceDetailDialog.show();
        WindowManager.LayoutParams params = invoiceDetailDialog.getWindow()
                .getAttributes();
        params.width = width;
        params.height = height * 4 / 5;
        params.windowAnimations = R.style.PauseDialogAnimation;
        invoiceDetailDialog.getWindow().setAttributes(params);
    }

    void insertToSQLite(byte[] invoiceByte) {
        JSONObject jsonObject = utility.Base64ByteToJson(invoiceByte);
        InvoiceItem invoiceItem = new InvoiceItem();
        try {
            invoiceItem.setStoreName(jsonObject.getString("StoreName"));
            invoiceItem.setDateline(jsonObject.getString("Dateline"));
            invoiceItem.setInvoiceNum(jsonObject.getString("InvoiceNum"));
            invoiceItem.setCurrentTime(jsonObject.getString("CurrentTime"));
            invoiceItem.setStoreNum(jsonObject.getString("StoreNum"));
            invoiceItem.setStorePhone(jsonObject.getString("StorePhone"));
            invoiceItem.setTotalMoney(jsonObject.getString("TotalMoney"));
            invoiceItem.setPayDetail(jsonObject.getString("PayDetail"));
            invoiceItem.setSignature(jsonObject.getString("Signature"));
            for(int i = 0;; i++) {
                if(!jsonObject.has("Goods" + i)) {
                    break;
                }
                InvoiceGoodsItem invoiceGoodsItem = new InvoiceGoodsItem();
                String[] token = jsonObject.getString("Goods" + i).split(",");
                invoiceGoodsItem.setInvoiceNum(jsonObject.getString("InvoiceNum"));
                invoiceGoodsItem.setGoodsName(token[0]);
                invoiceGoodsItem.setGoodsPrice(token[1]);
                invoiceGoodsItem.setGoodsQuantity(token[2]);
                invoiceGoodsItem.setGoodsTotalPrice(token[3]);
                invoiceGoodsDAO.insert(invoiceGoodsItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        invoiceDAO.insert(invoiceItem);

        invoiceFragmentControl.addNewInvoiceIntoAdapter(invoiceItem);
    }



    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];

        // record 0 contains the MIME type, record 1 is the AAR, if present
        Toast.makeText(this, "收到新的發票", Toast.LENGTH_LONG).show();
        showInvoiceDetailDialog(msg.getRecords()[0].getPayload());
        insertToSQLite(msg.getRecords()[0].getPayload());
    }

    public void setNfcMessage(byte[] mimeData) {
        ndefMessage = new NdefMessage(new NdefRecord[]{NdefRecord.createExternal(domain, type, mimeData)});
    }
}
