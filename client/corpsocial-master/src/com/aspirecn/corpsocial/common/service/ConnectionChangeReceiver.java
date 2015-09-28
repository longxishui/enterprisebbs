/**
 *
 */
package com.aspirecn.corpsocial.common.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectionChangeReceiver extends BroadcastReceiver{
    private static final long serialVersionUID = 1L;
    private static boolean networkStatus = true;
    //	private static String networkType = "";
    private static String cacheDir = "";

    public ConnectionChangeReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public ConnectionChangeReceiver(Context context) {
        checkNetworkStatus(context);
    }

    public static void checkNetworkStatus(Context context) {
        networkStatus = NetworkProber.isNetworkAvailable(context);
//		logger.debug("开机启动网络状态，网络可用状态：" + networkStatus);
//		NetStatusCheckThread.checkThread.setNeedCheck(networkStatus);
//        if (!NetStatusCheckThread.checkThread.isAlive()) {
//            NetStatusCheckThread.checkThread.start();
//        }
        cacheDir = context.getExternalCacheDir().getAbsolutePath();
//        IMNetClientImpl.setCacheFileDir(cacheDir);
//		checkNetWorkType(context);
    }


}