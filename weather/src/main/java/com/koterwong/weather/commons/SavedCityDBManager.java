package com.koterwong.weather.commons;

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


   //数据库管理者对象，操作数据库通过该对象。
    private static SavedCityDBManager mDatabaseManager;

    private SQLiteDatabase mDatabase;
    public static final String TABLE_NAME = "city";

    //将构造方法私有化
    private SavedCityDBManager(Context context) {
        SavedCityDBHelper dbHelper = new SavedCityDBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB的实例。
     */
    public synchronized static SavedCityDBManager getInstance(Context context) {
        if (mDatabaseManager == null) {
            mDatabaseManager = new SavedCityDBManager(context);
        }
        return mDatabaseManager;
    }

    /**
     * 将城市信息保存到数据库
     * @param cityName
     */
    public void addCity(String cityName) {
        if (cityName == null) {
            return;
        }
        ContentValues mValues = new ContentValues();
        mValues.put("saved_city", cityName);
        mDatabase.insert(TABLE_NAME, null, mValues);
    }

    /**
     * 加载已经保存的城市
     * @return 没有数据返回null
     */
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

    /**
     * 判断数据库是否存在该城市
     * @param city
     * @return
     */
    public boolean isExistCity(String city){
        Cursor cursor = mDatabase.query(TABLE_NAME, new String[]{"saved_city"}, "saved_city=?", new String[]{city}, null, null, null);
        return cursor.moveToFirst();
    }

    /**
     * 删除
     * @param city
     */
    public void deleteCity(String city){
        mDatabase.delete(TABLE_NAME,"saved_city=?",new String[]{city});
    }
}
