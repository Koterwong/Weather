package com.koterwong.weather.choicecity.Model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Author：Koterwong，Data：2016/4/25.
 * Description:
 */
public class DBManager {

    private static SQLiteDatabase db ;

    public synchronized static SQLiteDatabase getCityDataBase(String path){
        if (db!=null){
            return db;
        }else{
            db = SQLiteDatabase.openOrCreateDatabase(path,null);
        }
        return db;
    }
}
