package com.aspirecn.corpsocial.common.eventbus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author lizhuo_a
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
        // Intent newIntent = new Intent(context, CoreService.class);
        // newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //
        // 注意，必须添加这个标记，否则启动会失败
        //
        // context.startService(newIntent);
        // }

    }
}
