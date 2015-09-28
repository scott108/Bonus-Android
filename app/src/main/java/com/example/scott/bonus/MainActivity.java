package com.example.scott.bonus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scott.bonus.fragment.CouponFragment;
import com.example.scott.bonus.fragment.InvoiceFragment;
import com.example.scott.bonus.fragment.MyCouponFragment;
import com.example.scott.bonus.fragment.UserFragment;
import com.example.scott.bonus.fragmentcontrol.CouponFragmentControl;
import com.example.scott.bonus.fragmentcontrol.InvoiceFragmentControl;
import com.example.scott.bonus.session.SessionManager;
import com.example.scott.bonus.sharepreference.LoginSharePreference;
import com.example.scott.bonus.sqlite.doa.InvoiceDAO;
import com.example.scott.bonus.sqlite.doa.InvoiceGoodsDAO;
import com.example.scott.bonus.sqlite.entity.InvoiceGoodsItem;
import com.example.scott.bonus.sqlite.entity.InvoiceItem;
import com.example.scott.bonus.sqlite.MyDBHelper;
import com.example.scott.bonus.user.UserAccount;
import com.example.scott.bonus.utility.BackgroundLoginTask;
import com.example.scott.bonus.utility.BackgroundLogoutTask;
import com.example.scott.bonus.utility.utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity implements CreateNdefMessageCallback, OnNdefPushCompleteCallback {
    private static Intent origIntent;
    private Toolbar toolbar;
    private DrawerLayout Drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Dialog invoiceDetailDialog;
    private NfcAdapter mNfcAdapter;
    private NdefMessage ndefMessage;
    protected PendingIntent nfcPendingIntent;
    private IntentFilter[] tagFilters;

    private TextView title;
    private LinearLayout loginClick;
    private Intent loginActivityIntent;

    private LinearLayout invoiceMenuBtn;
    private LinearLayout couponMenuBtn;
    private LinearLayout myCouponMenuBtn;
    private LinearLayout settingMenuBtn;
    private LinearLayout logoutMenuBtn;

    //Sqlite dao
    private InvoiceDAO invoiceDAO;
    private InvoiceGoodsDAO invoiceGoodsDAO;

    //Fragment controller
    private InvoiceFragmentControl invoiceFragmentControl;
    private CouponFragmentControl couponFragmentControl;

    //fragment
    private FragmentManager fragmentManager;
    private Fragment invoiceFragment;
    private Fragment couponFragment;
    private Fragment userFragment;
    private Fragment myCouponFragment;
    private Fragment currentFragment;

    private ClickEventHandler clickEventHandler;

    private SharedPreferences sharePreference;

    //NFC domain
    final String domain = "com.example.scott.androidbream";
    final String type = "icheedata";

    int width;
    int height;

    private TextView userName;
    private TextView welcome;

    public FragmentManager getMyFragmentManager() {
        return fragmentManager;
    }

    public Fragment getCouponFragment() {
        return couponFragment;
    }

    public CouponFragmentControl getCouponFragmentControl() {
        return couponFragmentControl;
    }

    public Fragment getInvoiceFragment() {
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

    public static void setUser(String userName) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initDrawer(toolbar);
        title = (TextView) findViewById(R.id.tool_bar_title);

        EventBus.getDefault().register(this);

        buildSQLite();


        initUI();

        origIntent = getIntent();

        loginCheck();

        Context.setMainActivity(this);




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
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
            setIntent(origIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mNfcAdapter.disableForegroundDispatch(this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initUI() {
        fragmentManager = getSupportFragmentManager();

        invoiceFragmentControl = new InvoiceFragmentControl(this);
        couponFragmentControl = new CouponFragmentControl(this);


        invoiceFragment = new InvoiceFragment();
        couponFragment = new CouponFragment();
        userFragment = new UserFragment();
        myCouponFragment = new MyCouponFragment();



        clickEventHandler = new ClickEventHandler();

        initDialog();

        initNFCAdapter();

        switchFragment(invoiceFragment);
        title.setText("發票匣");

        setOnDrawerMenuClickListener();

        loginActivityIntent = new Intent(this, LoginActivity.class);
        loginClick = (LinearLayout) findViewById(R.id.loginClick);
        loginClick.setOnClickListener(clickEventHandler);

        userName = (TextView) findViewById(R.id.name);
        welcome = (TextView) findViewById(R.id.gotoLoginPage);
    }

    private void initDrawer(Toolbar toolbar) {

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar, 0, 0){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
    }

    private void switchFragment(Fragment nextFragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, nextFragment).commit();

        Drawer.closeDrawers();
    }

    private void setOnDrawerMenuClickListener() {
        invoiceMenuBtn = (LinearLayout) findViewById(R.id.invoice_menu_btn);
        couponMenuBtn = (LinearLayout) findViewById(R.id.coupon_menu_btn);
        myCouponMenuBtn = (LinearLayout) findViewById(R.id.my_coupon_menu_btn);
        settingMenuBtn = (LinearLayout) findViewById(R.id.my_acount_menu_btn);
        logoutMenuBtn = (LinearLayout) findViewById(R.id.logout_menu_btn);

        invoiceMenuBtn.setOnClickListener(clickEventHandler);
        couponMenuBtn.setOnClickListener(clickEventHandler);
        myCouponMenuBtn.setOnClickListener(clickEventHandler);
        settingMenuBtn.setOnClickListener(clickEventHandler);
        logoutMenuBtn.setOnClickListener(clickEventHandler);
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
        invoiceDetailDialog.setContentView(R.layout.invoice_detail);

        JSONObject jsonObject = utility.Base64ByteToJson(invoiceByte);

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
            invoiceDateline.setText(jsonObject.getString("Deadline"));
            invoiceNum.setText(jsonObject.getString("InvoiceNum"));
            invoiceCurrentTime.setText(jsonObject.getString("CurrentTime"));
            invoiceStoreNum.setText("賣方" + jsonObject.getString("StoreNum"));
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
            invoiceTotalMoney.setText(jsonObject.getString("GoodsQuantity") + "項    合計" + jsonObject.getString("TotalMoney"));
            invoicePayDetail.setText("現金    " + "$" + jsonObject.getString("PayDetail") + "找零    " + "$" + jsonObject.getString("PayBack"));
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
            invoiceItem.setDeadline(jsonObject.getString("Deadline"));
            invoiceItem.setInvoiceNum(jsonObject.getString("InvoiceNum"));
            invoiceItem.setCurrentTime(jsonObject.getString("CurrentTime"));
            invoiceItem.setStoreNum(jsonObject.getString("StoreNum"));
            invoiceItem.setStorePhone(jsonObject.getString("StorePhone"));
            invoiceItem.setGoodsQuantity(jsonObject.getString("GoodsQuantity"));
            invoiceItem.setTotalMoney(jsonObject.getString("TotalMoney"));
            invoiceItem.setPayDetail(jsonObject.getString("PayDetail"));
            invoiceItem.setPayBack(jsonObject.getString("PayBack"));
            invoiceItem.setSignature(jsonObject.getString("Signature"));
            invoiceItem.setGoodsHash(jsonObject.getString("GoodsHash"));

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

    public void loginCheck() {
        sharePreference = getSharedPreferences(LoginSharePreference.LOGIN_DATA, 0);
        UserAccount userAccount = LoginSharePreference.getInstance().getLoginData(sharePreference);
        if(!userAccount.getEmail().equals("") && !userAccount.getPassword().equals("")) {
            new BackgroundLoginTask().execute(userAccount.getEmail(), userAccount.getPassword());
        }
    }

    public void onEvent(UserInfoManager userInfoManager) {

        if(!userInfoManager.getUserName().equals("")) {
            userName.setText("Hello, " + userInfoManager.getUserName());
            welcome.setText("歡迎使用iBonus");
        } else {
            userName.setText("遊客");
            welcome.setText("按此登入");
        }
    }

    private void showLogoutDialog()
    {
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("登出");
        MyAlertDialog.setMessage("確定要登出?");
        DialogInterface.OnClickListener cancelClick = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
            }
        };

        DialogInterface.OnClickListener oKClick = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String islogout = new BackgroundLogoutTask().execute().get();
                    if(islogout.equals("true")) {
                        switchFragment(invoiceFragment);
                        toolbar.setElevation(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                sharePreference = getSharedPreferences(LoginSharePreference.LOGIN_DATA, 0);
                LoginSharePreference.getInstance().clearLoginData(sharePreference);
            }
        };
        MyAlertDialog.setPositiveButton("確定", oKClick );
        MyAlertDialog.setNegativeButton("取消", cancelClick);
        MyAlertDialog.show();
    }

    class ClickEventHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.invoice_menu_btn:
                    //invoiceFragment = new InvoiceFragment();
                    switchFragment(invoiceFragment);
                    title.setText("發票匣");
                    toolbar.setElevation(0);
                    System.out.println("Click invoice menu");
                    break;

                case R.id.coupon_menu_btn:
                    switchFragment(couponFragment);
                    title.setText("優惠卷列表");
                    toolbar.setElevation(8);
                    System.out.println("Click coupon menu");
                    break;

                case R.id.my_coupon_menu_btn:
                    Drawer.closeDrawers();
                    if(SessionManager.hasAttribute()) {
                        switchFragment(myCouponFragment);
                        title.setText("我的優惠卷");
                        toolbar.setElevation(8);
                    } else {
                        Toast.makeText(getApplication(), "尚未登入", Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.my_acount_menu_btn:
                    Drawer.closeDrawers();
                    if(SessionManager.hasAttribute()) {
                        switchFragment(userFragment);
                        title.setText("帳戶設定");
                        toolbar.setElevation(8);
                    } else {
                        Toast.makeText(getApplication(), "尚未登入", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.logout_menu_btn:
                    Drawer.closeDrawers();
                    if(SessionManager.hasAttribute()) {
                        showLogoutDialog();
                    } else {
                        Toast.makeText(getApplication(), "尚未登入", Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.loginClick:
                    Drawer.closeDrawers();
                    if(SessionManager.hasAttribute()) {

                    } else {
                        startActivity(loginActivityIntent);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    //Test
    private void insertTestInvoiceToSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("StoreName", "麥當勞");
        jsonObject.put("Dateline", "12月");
        jsonObject.put("InvoiceNum", Math.round(Math.random() * 1000000 + 1));
        jsonObject.put("CurrentTime", "10:10:10");
        jsonObject.put("StoreNum", "123");
        jsonObject.put("StorePhone", "321");
        jsonObject.put("TotalMoney", "109");
        jsonObject.put("PayDetail", "test");
        jsonObject.put("Signature", "test");
        jsonObject.put("Goods0", "麥香雞,109,1,109");

        InvoiceItem invoiceItem = new InvoiceItem();
        try {
            invoiceItem.setStoreName(jsonObject.getString("StoreName"));
            invoiceItem.setDeadline(jsonObject.getString("Dateline"));
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

}
