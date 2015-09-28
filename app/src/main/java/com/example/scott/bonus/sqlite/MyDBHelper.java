package com.example.scott.bonus.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.example.scott.bonus.sqlite.doa.CouponDAO;
import com.example.scott.bonus.sqlite.doa.InvoiceDAO;
import com.example.scott.bonus.sqlite.doa.InvoiceGoodsDAO;

/**
 * Created by Scott on 15/4/26.
 */

public class MyDBHelper extends SQLiteOpenHelper {

    // 資料庫名稱
    public static final String DATABASE_NAME = "invoiceDB.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;
    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;

    // 建構子，在一般的應用都不需要修改
    public MyDBHelper(Context context, String name, CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new MyDBHelper(context, DATABASE_NAME,
                    null, VERSION).getWritableDatabase();
        }

        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立應用程式需要的表格
        System.out.println("create a Database");
        db.execSQL(InvoiceDAO.CREATE_TABLE);
        db.execSQL(InvoiceGoodsDAO.CREATE_TABLE);
        db.execSQL(CouponDAO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 刪除原有的表格
        db.execSQL("DROP TABLE IF EXISTS " + InvoiceDAO.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + InvoiceGoodsDAO.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CouponDAO.CREATE_TABLE);

        // 呼叫onCreate建立新版的表格
        onCreate(db);
    }

}
