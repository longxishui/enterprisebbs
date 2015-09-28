package com.aspirecn.corpsocial.common.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;



public class NetworkService extends Service {
    private static String TAG = "NetworkService";
    private WakeLock wakeLock = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.acquireWakeLock();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        this.releaseWakeLock();
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(TAG, "网络检测服务启动");
//        new Thread() {
//            public void run() {
//                while (true) {
//                    IoSession session = IMNetClientImpl.getIoSession();
//                    if (session != null) {
//                        AndroidClientIoHandler.getIoHandler().sessionIdleCheck(
//                                session);
//                    }
//                    try {
//                        Thread.sleep(30 * 1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
    }

    private void acquireWakeLock() {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, getClass()
                    .getCanonicalName());
            if (null != wakeLock) {
                Log.i(TAG, "申请电源锁成功");
                wakeLock.acquire();
            }
        }
    }

    private void releaseWakeLock() {
        if (null != wakeLock && wakeLock.isHeld()) {
            Log.i(TAG, "释放电源锁");
            wakeLock.release();
            wakeLock = null;
        }
    }
}
