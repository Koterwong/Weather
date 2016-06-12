package com.koterwong.weather.ui.choicecity.Model;

import android.database.sqlite.SQLiteDatabase;

import com.koterwong.weather.MyApp;

import java.io.File;

/**
 * Author：Koterwong，Data：2016/4/25.
 * Description:
 */
public class ChinaCityDataBase {

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
        return MyApp.getApp().getFilesDir().getAbsolutePath() + File.separator + CITY_DB_NAME;
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
