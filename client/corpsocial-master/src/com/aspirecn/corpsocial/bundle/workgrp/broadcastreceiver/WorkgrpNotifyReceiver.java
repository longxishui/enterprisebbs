package com.aspirecn.corpsocial.bundle.workgrp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aspirecn.corpsocial.bundle.workgrp.event.UnReadBBSCountNotifyEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.AppBroadcastReceiver;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;

@AppBroadcastReceiver(intentFilter = "bbsNotifyReceiver")
public class WorkgrpNotifyReceiver extends BroadcastReceiver {

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String action = bundle.getString("action");
        if ("updateBbs".equals(action)) {
            System.out.println(action);
            UnReadBBSCountNotifyEvent event = new UnReadBBSCountNotifyEvent();
            eventListener.notifyListener(event);
        }
    }

}
