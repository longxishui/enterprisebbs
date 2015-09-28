package com.aspirecn.corpsocial.bundle.common.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aspirecn.corpsocial.bundle.common.event.GetSelfCorpListEvent;
import com.aspirecn.corpsocial.bundle.common.event.QuitEvent;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.AppBroadcastReceiver;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;

@AppBroadcastReceiver(intentFilter = "commonNotifyReceiver")
public class CommonNotifyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent intent) {
        String action = intent.getStringExtra("action");
        if ("quitNotify".equals(action)) {// 退出登录通知
            // 修改账号状态
            Config.getInstance().setAccountStatus(false);
            // 断开连接
            UiEventHandleFacade instance = UiEventHandleFacade.getInstance();
            instance.handle(new QuitEvent());
        } else if ("corpChange".equals(action)) {
            if (ConfigPersonal.getInt(ConfigPersonal.Key.CORP_STATUS) == 1) {
                UiEventHandleFacade instance = UiEventHandleFacade.getInstance();
                instance.handle(new GetSelfCorpListEvent());
            }
        }

    }
}
