package com.aspirecn.corpsocial.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class NetTools {

    public static void isLegalHttpUrl(String url) {
        int idx = url.indexOf("://");
        if (idx == -1) {
            throw new IllegalArgumentException(url
                    + " is not an right http url,no '://'");
        }
        if (!url.startsWith("http")) {
            throw new IllegalArgumentException(url
                    + " is not an right http url,no \"http\"");
        }

    }

    /**
     * 检测网络连接是否可用
     *
     * @param context
     * @return 具体的网络模式，一般有wifi，mobile，如果没有网络连通的话则返回null
     */
    public static String checkConnectedNetWork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return null;
        }
        NetworkInfo[] netinfo = cm.getAllNetworkInfo();
        if (netinfo == null) {
            return null;
        }
        for (int i = 0; i < netinfo.length; i++) {
            NetworkInfo info = netinfo[i];
            if (info.isConnected()) {
                return info.getTypeName();
            }
        }
        return null;
    }

    /**
     * 网络传输
     *
     * @param path 路径
     * @return
     */
    public static boolean transmission(String path) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(path)
                    .openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestProperty("content-type", "text/html");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断实际网络是否畅通
     *
     * @param context
     * @return
     */
    public static boolean checkAvailableNetWork(Context context) {
        String netName = checkConnectedNetWork(context);
        if (netName == null) {
            return false;
        }
        netName = netName.toLowerCase(Locale.getDefault());
        if (!netName.equals("wifi")) {
            return true;
        } else {
            if (transmission("http://www.baidu.com/")) {
                return true;
            }
        }
        return false;
    }
}
