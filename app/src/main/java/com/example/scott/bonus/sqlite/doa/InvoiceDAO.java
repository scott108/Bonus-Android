package com.example.scott.bonus.sqlite.doa;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.scott.bonus.sqlite.MyDBHelper;
import com.example.scott.bonus.sqlite.entity.InvoiceItem;

/**
 * Created by Scott on 15/4/26.
 */

// 資料功能類別
public class InvoiceDAO {
    // 表格名稱
    public static final String TABLE_NAME = "invoice_item";

    // 其它表格欄位名稱
    public static final String STORENAME_COLUMN = "store_name";
    public static final String DATELINE_COLUMN = "dateline";
    public static final String INVOICENUM_COLUMN = "_invoice_num";
    public static final String CURRENTTIME_COLUMN = "current_time";
    public static final String STORENUM_COLUMN = "store_num";
    public static final String STOREPHONE_COLUMN = "store_phone";
    public static final String TOTALMOMEY_COLUMN = "total_money";
    public static final String PAYDETAIL_COLUMN = "pay_detail";
    public static final String SIGNATURE_COLUMN = "signature";
    public static final String ISEXCHANGED_COLUMN = "is_exchanged";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    STORENAME_COLUMN + " TEXT NOT NULL, " +
                    DATELINE_COLUMN + " TEXT NOT NULL, " +
                    INVOICENUM_COLUMN + " TEXT PRIMARY KEY, " +
                    CURRENTTIME_COLUMN + " TEXT NOT NULL, " +
                    STORENUM_COLUMN + " TEXT NOT NULL, " +
                    STOREPHONE_COLUMN + " TEXT NOT NULL, " +
                    TOTALMOMEY_COLUMN + " TEXT NOT NULL, " +
                    PAYDETAIL_COLUMN + " TEXT NOT NULL," +
                    SIGNATURE_COLUMN + "  TEXT NOT NULL," +
                    ISEXCHANGED_COLUMN + "  INTEGER DEFAULT 0)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public InvoiceDAO(Context context) {
        db = MyDBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public InvoiceItem insert(InvoiceItem invoiceItem) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(STORENAME_COLUMN, invoiceItem.getStoreName());
        cv.put(DATELINE_COLUMN, invoiceItem.getDateline());
        cv.put(INVOICENUM_COLUMN, invoiceItem.getInvoiceNum());
        cv.put(CURRENTTIME_COLUMN, invoiceItem.getCurrentTime());
        cv.put(STORENUM_COLUMN, invoiceItem.getStoreNum());
        cv.put(STOREPHONE_COLUMN, invoiceItem.getStorePhone());
        cv.put(TOTALMOMEY_COLUMN, invoiceItem.getTotalMoney());
        cv.put(PAYDETAIL_COLUMN, invoiceItem.getPayDetail());
        cv.put(SIGNATURE_COLUMN, invoiceItem.getSignature());

        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        db.insert(TABLE_NAME, null, cv);

        // 回傳結果
        return invoiceItem;
    }

    // 修改參數指定的物件
    /*public boolean update(InvoiceItem invoiceItem) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(STORENAME_COLUMN, invoiceItem.getStoreName());
        cv.put(DATELINE_COLUMN, invoiceItem.getDateline());
        cv.put(CURRENTTIME_COLUMN, invoiceItem.getCurrentTime());
        cv.put(STORENUM_COLUMN, invoiceItem.getStoreNum());
        cv.put(STOREPHONE_COLUMN, invoiceItem.getStorePhone());
        cv.put(TOTALMOMEY_COLUMN, invoiceItem.getTotalMoney());
        cv.put(PAYDETAIL_COLUMN, invoiceItem.getPayDetail());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = INVOICENUM_COLUMN + "=" + invoiceItem.getInvoiceNum();

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }*/

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = INVOICENUM_COLUMN + "=" + "'" + id + "'";
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    // 讀取所有記事資料
    public List<InvoiceItem> getAll() {
        List<InvoiceItem> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public InvoiceItem get(String id) {
        // 準備回傳結果用的物件
        InvoiceItem invoiceItem = null;
        // 使用編號為查詢條件
        String where = INVOICENUM_COLUMN + "=" + "'" + id + "'" ;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            invoiceItem = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return invoiceItem;
    }

    // 把Cursor目前的資料包裝為物件
    public InvoiceItem getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        InvoiceItem result = new InvoiceItem();

        result.setStoreName(cursor.getString(0));
        result.setDateline(cursor.getString(1));
        result.setInvoiceNum(cursor.getString(2));
        result.setCurrentTime(cursor.getString(3));
        result.setStoreNum(cursor.getString(4));
        result.setStorePhone(cursor.getString(5));
        result.setTotalMoney(cursor.getString(6));
        result.setPayDetail(cursor.getString(7));
        result.setSignature(cursor.getString(8));

        // 回傳結果
        return result;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

}
