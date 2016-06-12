package com.koterwong.weather.commons.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public class SavedCityDBManager {

    private static SavedCityDBManager mDatabaseManager;
    private SQLiteDatabase mDatabase;
    public static final String TABLE_NAME = "city";

    private SavedCityDBManager(Context context) {
        SavedCityDBHelper dbHelper = new SavedCityDBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
    }

    public synchronized static SavedCityDBManager getInstance(Context context) {
        if (mDatabaseManager == null) {
            synchronized (SavedCityDBManager.class){
                if (mDatabaseManager==null){
                    mDatabaseManager = new SavedCityDBManager(context);
                }
            }
        }
        return mDatabaseManager;
    }

    public void addCity(String cityName) {
        if (cityName == null) {
            return;
        }
        ContentValues mValues = new ContentValues();
        mValues.put("saved_city", cityName);
        mDatabase.insert(TABLE_NAME, null, mValues);
    }

    public List<String> queryCities(){
        Cursor mCursor = mDatabase.query(TABLE_NAME, new String[]{"saved_city"}, null, null, null, null, null);
        if (mCursor.moveToFirst()){
            List<String> mCityList = new ArrayList<>();
            do {
                String mCity = mCursor.getString(0);
                mCityList.add(mCity);
            }while (mCursor.moveToNext());
            mCursor.close();
            return mCityList;
        }
        return null;
    }

    public boolean isExistCity(String city){
        Cursor cursor = mDatabase.query(TABLE_NAME, new String[]{"saved_city"}, "saved_city=?", new String[]{city}, null, null, null);
        boolean isExist = cursor.moveToFirst();
        cursor.close();
        return isExist;
    }

    public void deleteCity(String city){
        mDatabase.delete(TABLE_NAME,"saved_city=?",new String[]{city});
    }
}
