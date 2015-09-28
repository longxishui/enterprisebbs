package com.aspirecn.corpsocial.bundle.addrbook.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aspirecn.corpsocial.bundle.addrbook.event.CustomerServiceEvent;
import com.aspirecn.corpsocial.bundle.addrbook.utils.TruncateTableUtils;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;

/**
 * Created by chenziqiang on 15-1-29.
 */
@EventBusAnnotation.AppBroadcastReceiver(intentFilter = "customerNotifyReceiver")
public class CustomerNotifyReceiver extends BroadcastReceiver {
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        Bundle bundle = arg1.getExtras();
        String action = bundle.getString("action");
        String corpId = bundle.getString("corpId");
        if (corpId == null) {
            corpId = ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED);
        }
        if ("updateCustomer".equals(action)) {
            TruncateTableUtils.truncateCustomerTables();
            UiEventHandleFacade.getInstance().handle(new CustomerServiceEvent(corpId));
        }
    }
}
