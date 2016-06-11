package com.koterwong.weather.ui.choicecity.Model;

import android.database.sqlite.SQLiteDatabase;

import com.koterwong.weather.MyApp;

import java.io.File;

/**
 * Author：Koterwong，Data：2016/4/25.
 * Description:
 */
public class CityDbManager {

    private static SQLiteDatabase db;
    private static final String CITY_DB_NAME = "china_city.db";

    public synchronized static SQLiteDatabase getCityDataBase(String path) {
        if (db != null) {
            return db;
        } else {
            db = SQLiteDatabase.openOrCreateDatabase(path, null);
        }
        return db;
    }

    /**
     * 获取数据库Copy的位置.
     */
    public static String getDBCopyPath() {
        StringBuilder builder = new StringBuilder();
        return builder.append(File.separator)
                .append(MyApp.getApp().getFilesDir().getAbsolutePath())
                .append(File.separator)
                .append(CITY_DB_NAME)
                .toString();
//         builder.append(File.separator)
//                .append("data")
//                .append(Environment.getDataDirectory().getAbsolutePath())
//                .append(File.separator)
//                .append(MyApp.getApp().getPackageName())
//                .append(File.separator)
//                .append("databases")
//                .append(File.separator)
//                .append("")
//                .toString();
    }
}
