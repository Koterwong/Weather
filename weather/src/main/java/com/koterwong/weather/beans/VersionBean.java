package com.koterwong.weather.beans;

/**
 * ================================================
 * Created By：Koterwong; Time: 2016/06/06 15:40
 * <p/>
 * Description:
 * =================================================
 */
public class VersionBean {

    /**
     * name : 天气
     * version : 4
     * changelog : 加入一些新的动画。
     * updated_at : 1465191448
     * versionShort : 1.2
     * build : 4
     * installUrl : http://download.fir.im/v2/app/install/5745b4e6f2fc420cdc000012?download_token=b6c3a6b185d22b968470096a49ddbffb
     * install_url : http://download.fir.im/v2/app/install/5745b4e6f2fc420cdc000012?download_token=b6c3a6b185d22b968470096a49ddbffb
     * direct_install_url : http://download.fir.im/v2/app/install/5745b4e6f2fc420cdc000012?download_token=b6c3a6b185d22b968470096a49ddbffb
     * update_url : http://fir.im/koterweather
     * binary : {"fsize":2783810}
     */
    public String name;
    public int version;
    public String changelog;
    public String updated_at;
    public String versionShort;
    public String build;
    public String installUrl;
    public String install_url;
    public String direct_install_url;
    public String update_url;
    /**
     * fsize : 2783810
     */
    public BinaryBean binary;

    public static class BinaryBean {
        public int fsize;
    }
}
