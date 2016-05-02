package com.koterwong.weather.choicecity.Model;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.koterwong.weather.base.BaseApplication;
import com.koterwong.weather.beans.City;
import com.koterwong.weather.beans.Province;
import com.koterwong.weather.R;
import com.koterwong.weather.utils.L;
import com.koterwong.weather.utils.ThreadManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/25.
 * Description:
 */
public class CityModelImp implements CityModel {


//    private static String DB_Name = "china_city.db";

    /**
     * 数据库Copy的位置
     */
    static StringBuilder builder = new StringBuilder();
    private static String path = builder.append(File.separator)
            .append("data")
            .append(Environment.getDataDirectory().getAbsolutePath())
            .append(File.separator)
            .append(BaseApplication.getApplication().getPackageName())
            .append(File.separator)
            .append("china_city.db")
            .toString();

    @Override
    public void queryProvince(CityModelImp.QueryProListener listener) {
        SQLiteDatabase cityDb = DBManager.getCityDataBase(path);
        Cursor cursor = cityDb.query("T_Province", null, null, null, null, null, null);
        if (cursor == null) {
            listener.queryFailed(new FileNotFoundException("数据库文件不存在"));
            return;
        }
        List<Province> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.ProName = cursor.getString(cursor.getColumnIndex("ProName"));
                province.ProShort = cursor.getString(cursor.getColumnIndex("ProSort"));
                province.proRemeark = cursor.getString(cursor.getColumnIndex("ProRemark"));
                list.add(province);
            } while (cursor.moveToNext());
            cursor.close();
            listener.querySuccess(list);
        }
    }

    @Override
    public void queryCity(String ProID, CityModelImp.QueryCityListener listener) {
        SQLiteDatabase cityDb = DBManager.getCityDataBase(path);

        Cursor cursor = cityDb.query("T_City", null, "ProID = ?", new String[]{ProID}, null, null, null);
        if (cursor == null) {
            listener.queryFailed(new FileNotFoundException("数据库文件不存在"));
            return;
        }
        List<City> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.CityName = cursor.getString(cursor.getColumnIndex("CityName"));
                city.ProID = ProID;
                list.add(city);
            } while (cursor.moveToNext());
            listener.querySuccess(list);
        }
        cursor.close();
    }

    @Override
    public void loadCityDataBase(final CityModelImp.LoadCityDBListener listener) {
        File file = new File(path);
        if (file.exists()) {
            listener.copySuccess();
            return;
        }
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                Resources resources = BaseApplication.getApplication().getResources();
                InputStream is = resources.openRawResource(R.raw.china_city);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(path);
                    byte[] buf = new byte[1024 * 8];
                    int len;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    listener.copySuccess();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    L.e("该路径不是一个文件");
                    listener.copyFailed(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    L.e("读取文件异常");
                    listener.copyFailed(e);
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface LoadCityDBListener {

        void copySuccess();

        void copyFailed(Exception e);
    }

    public interface QueryCityListener {

        void querySuccess(List<City> cityList);

        void queryFailed(Exception e);
    }

    public interface QueryProListener {

        void querySuccess(List<Province> provinceList);

        void queryFailed(Exception e);
    }
}
