package com.example.scott.bonus.sqlite.doa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.scott.bonus.sqlite.MyDBHelper;
import com.example.scott.bonus.sqlite.entity.InvoiceGoodsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 15/4/27.
 */
public class InvoiceGoodsDAO {
    // 表格名稱
    public static final String TABLE_NAME = "invoice_goods_item";

    // 其它表格欄位名稱
    public static final String INVOICENUM_COLUMN = "invoice_num";
    public static final String GOODSNAME_COLUMN = "goods_name";
    public static final String GOODSPRICE_COLUMN = "goods_price";
    public static final String GOODSQUANTITY_COLUMN = "goods_quantity";
    public static final String GOODSTOTALPRICE_COLUMN = "goods_total_price";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    INVOICENUM_COLUMN + " TEXT NOT NULL, " +
                    GOODSNAME_COLUMN + " TEXT NOT NULL, " +
                    GOODSPRICE_COLUMN + " TEXT NOT NULL, " +
                    GOODSQUANTITY_COLUMN + " TEXT NOT NULL, " +
                    GOODSTOTALPRICE_COLUMN + " TEXT NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public InvoiceGoodsDAO(Context context) {
        db = MyDBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public InvoiceGoodsItem insert(InvoiceGoodsItem invoiceGoodsItem) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(INVOICENUM_COLUMN, invoiceGoodsItem.getInvoiceNum());
        cv.put(GOODSNAME_COLUMN, invoiceGoodsItem.getGoodsName());
        cv.put(GOODSPRICE_COLUMN, invoiceGoodsItem.getGoodsPrice());
        cv.put(GOODSQUANTITY_COLUMN, invoiceGoodsItem.getGoodsQuantity());
        cv.put(GOODSTOTALPRICE_COLUMN, invoiceGoodsItem.getGoodsTotalPrice());

        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        db.insert(TABLE_NAME, null, cv);

        // 回傳結果
        return invoiceGoodsItem;
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
    public List<InvoiceGoodsItem> getAll() {
        List<InvoiceGoodsItem> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public List<InvoiceGoodsItem> get(String id) {
        // 準備回傳結果用的物件
        List<InvoiceGoodsItem> result = new ArrayList<>();
        // 使用編號為查詢條件
        String where = INVOICENUM_COLUMN + "=" + "'" + id + "'";
        // 執行查詢
        Cursor cursor = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        // 關閉Cursor物件
        cursor.close();
        // 回傳結果
        return result;
    }

    // 把Cursor目前的資料包裝為物件
    public InvoiceGoodsItem getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        InvoiceGoodsItem result = new InvoiceGoodsItem();

        result.setInvoiceNum(cursor.getString(0));
        result.setGoodsName(cursor.getString(1));
        result.setGoodsPrice(cursor.getString(2));
        result.setGoodsQuantity(cursor.getString(3));
        result.setGoodsTotalPrice(cursor.getString(4));


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
