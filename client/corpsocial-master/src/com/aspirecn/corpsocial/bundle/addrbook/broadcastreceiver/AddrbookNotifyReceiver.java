package com.aspirecn.corpsocial.bundle.addrbook.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aspirecn.corpsocial.bundle.addrbook.event.NotifyDismissGroupEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.NotifyKickOutGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.NotifyRefreshMyGroupEvent;
import com.aspirecn.corpsocial.bundle.common.event.GetSelfCorpListEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.AppBroadcastReceiver;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;

@AppBroadcastReceiver(intentFilter = "addrbookNotifyReceiver")
public class AddrbookNotifyReceiver extends BroadcastReceiver {

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        Bundle bundle = arg1.getExtras();
        String action = bundle.getString("action");
        if ("updateAddrbook".equals(action)) {// 更新通讯录通知


            UiEventHandleFacade.getInstance().handle(new GetSelfCorpListEvent());
//            // 下载通讯录
//            UiEventHandleFacade.getInstance().handle(new RefreshAddrbookEvent());

        } else if ("refreshMyGroup".equals(action)) {//刷新群组
            eventListener.notifyListener(new NotifyRefreshMyGroupEvent());

        } else if ("dismissGroup".equals(action)) {//解散群
            eventListener.notifyListener(new NotifyDismissGroupEvent());

        } else if ("kickoutGroupMember".equals(action)) {//踢出群
            eventListener.notifyListener(new NotifyKickOutGroupMemberEvent());

        } else if ("refreshMyGroup".equals(action)) {//刷新我的群组通知

        } else if ("refreshFriend".equals(action)) {
            /**
             * @todo
             */
        }
//		else if ("corpChange".equalsIgnoreCase(action)) {
//			LogUtils.d("登录用户所在公司发生变化，清空通讯录，重新下载。");
//
//			//公司变了，清空通讯录，重新下载。
//			TruncateTableUtils.truncateContactTables();
//			AddressBookConfig.setContactLastTime(0);
//			AddressBookConfig.setDeptLastTime(0);
//			AddressBookConfig.setContactSyn(false);
//
//			// 下载部门
//			UiEventHandleFacade.getInstance().handle(
//					new RefreshDepartmentEvent());
//			// 下载通讯录
//			UiEventHandleFacade.getInstance()
//					.handle(new RefreshAddrbookEvent());
//		} else if("userChange".equalsIgnoreCase(action)){
//            TruncateTableUtils.truncateContactTables2();
//        }

    }
}
