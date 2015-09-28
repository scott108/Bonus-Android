package com.example.scott.bonus.sqlite.doa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.scott.bonus.sqlite.MyDBHelper;
import com.example.scott.bonus.sqlite.entity.CouponItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 15/9/28.
 */
public class CouponDAO {
    // 表格名稱
    public static final String TABLE_NAME = "coupon_item";

    // 其它表格欄位名稱
    public static final String COUPONID_COLUMN = "_coupon_id";
    public static final String STORENAME_COLUMN = "store_name";
    public static final String COUPONNAME_COLUMN = "coupon_name";
    public static final String COUPONCOTENT_COLUMN = "coupon_content";
    public static final String IMAGEURL_COLUMN = "image_url";
    public static final String COUPONBONUS_COLUMN = "coupon_bonus";
    public static final String STARTTIME_COLUMN = "start_time";
    public static final String ENDTIME_COLUMN = "end_time";
    public static final String ISUSED_COLUMN = "is_used";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COUPONID_COLUMN + " TEXT PRIMARY KEY, " +
                    STORENAME_COLUMN + " TEXT NOT NULL, " +
                    COUPONNAME_COLUMN + " TEXT NOT NULL, " +
                    COUPONCOTENT_COLUMN + " TEXT NOT NULL, " +
                    IMAGEURL_COLUMN + " TEXT NOT NULL, " +
                    COUPONBONUS_COLUMN + " INTEGER NOT NULL, " +
                    STARTTIME_COLUMN + " TEXT NOT NULL, " +
                    ENDTIME_COLUMN + " TEXT NOT NULL," +
                    ISUSED_COLUMN + " INTEGER DEFAULT 0)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public CouponDAO(Context context) {
        db = MyDBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public CouponItem insert(CouponItem couponItem) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(COUPONID_COLUMN, couponItem.getCouponID());
        cv.put(STORENAME_COLUMN, couponItem.getStoreName());
        cv.put(COUPONNAME_COLUMN, couponItem.getCouponName());
        cv.put(COUPONCOTENT_COLUMN, couponItem.getCouponContent());
        cv.put(IMAGEURL_COLUMN, couponItem.getImageUrl());
        cv.put(COUPONBONUS_COLUMN, couponItem.getCouponBonus());
        cv.put(STARTTIME_COLUMN, couponItem.getStartTime());
        cv.put(ENDTIME_COLUMN, couponItem.getEndTime());

        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        db.insert(TABLE_NAME, null, cv);

        // 回傳結果
        return couponItem;
    }

    // 修改參數指定的物件
    public boolean update(CouponItem couponItem) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(COUPONID_COLUMN, couponItem.getCouponID());
        cv.put(STORENAME_COLUMN, couponItem.getStoreName());
        cv.put(COUPONNAME_COLUMN, couponItem.getCouponName());
        cv.put(COUPONCOTENT_COLUMN, couponItem.getCouponContent());
        cv.put(IMAGEURL_COLUMN, couponItem.getImageUrl());
        cv.put(COUPONBONUS_COLUMN, couponItem.getCouponBonus());
        cv.put(STARTTIME_COLUMN, couponItem.getStartTime());
        cv.put(ENDTIME_COLUMN, couponItem.getEndTime());
        cv.put(ISUSED_COLUMN, couponItem.getIsUsed());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = COUPONID_COLUMN + "=" + "'" + couponItem.getCouponID() + "'" ;

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = COUPONID_COLUMN + "=" + "'" + id + "'";
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    // 讀取所有記事資料
    public List<CouponItem> getAll() {
        List<CouponItem> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public CouponItem get(String id) {
        // 準備回傳結果用的物件
        CouponItem couponItem = null;
        // 使用編號為查詢條件
        String where = COUPONID_COLUMN + "=" + "'" + id + "'" ;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            couponItem = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return couponItem;
    }

    // 把Cursor目前的資料包裝為物件
    public CouponItem getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        CouponItem result = new CouponItem();

        result.setCouponID(cursor.getString(0));
        result.setStoreName(cursor.getString(1));
        result.setCouponName(cursor.getString(2));
        result.setCouponContent(cursor.getString(3));
        result.setImageUrl(cursor.getString(4));
        result.setCouponBonus(cursor.getInt(5));
        result.setStartTime(cursor.getString(6));
        result.setEndTime(cursor.getString(7));
        result.setIsUsed(cursor.getInt(8));

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
