package com.koterwong.weather.commons;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public class SavedCityDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "saveCities";

    public static final int VERSION = 1;

    public static String CREATE_TABLE_CITY = "create table city(" +
            "_id integer primary key autoincrement," +
            "saved_city text)";

    public SavedCityDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    /**
     * 创建表
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CITY);
    }

    /**
     * 更新表
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
