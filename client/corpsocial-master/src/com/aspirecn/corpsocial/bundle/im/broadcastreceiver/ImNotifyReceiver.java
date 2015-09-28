package com.aspirecn.corpsocial.bundle.im.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aspirecn.corpsocial.bundle.addrbook.facade.FindCorpService;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.bundle.im.event.LoginRespNotifyEvent;
import com.aspirecn.corpsocial.bundle.im.event.LoginStartEvent;
import com.aspirecn.corpsocial.bundle.im.event.NetStatueChangeEvent;
import com.aspirecn.corpsocial.bundle.im.event.NewFriendChatEvent;
import com.aspirecn.corpsocial.bundle.im.event.RefreshImMainTabEvent;
import com.aspirecn.corpsocial.bundle.im.event.SyncCorpGroupEvent;
import com.aspirecn.corpsocial.bundle.im.event.SyncCustomGroupEvent;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.AppBroadcastReceiver;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.util.List;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

@AppBroadcastReceiver(intentFilter = "imNotifyReceiver")
public class ImNotifyReceiver extends BroadcastReceiver {

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        Bundle bundle = arg1.getExtras();
        String action = bundle.getString("action");
        LogUtil.i("aspire-----------imaction:" + action);
        if ("loginStartNotify".equals(action)) {// 开始登录通知
            eventListener.notifyListener(new LoginStartEvent());

        } else if ("syncUserGroupList".equals(action)) {// 同步用户自定义群组列表通知
            // 下载用户群组列表
            String corpId = Config.getInstance().getCorpId();
            UiEventHandleFacade.getInstance().handle(new SyncCustomGroupEvent(corpId));

        } else if ("syncCorpGroup".equals(action)) {// 同步公司群组列表通知
            // 下载公司群组列表
//            UserServiceParam userServiceParam = new UserServiceParam();
//            userServiceParam.setServie("FindCorpService");
//            @SuppressWarnings("unchecked")
//            UserServiceResult result = (UserServiceResult) OsgiServiceLoader.getInstance()
//                    .getService(UserService.class).invoke(userServiceParam);
//            @SuppressWarnings("unchecked")
//            List<UserCorp> userCorps = (List<UserCorp>) result.getData();

            List<UserCorp> userCorps = (List<UserCorp>) OsgiServiceLoader.getInstance().getService(FindCorpService.class).invoke(new Null());

            for (UserCorp uc : userCorps) {
                UiEventHandleFacade.getInstance().handle(new SyncCorpGroupEvent(uc.getCorpId()));
            }

        } else if ("loginRespNotify".equals(action)) {// 登录结果反馈通知
            Bundle loginRespData = bundle.getBundle("data");
            LoginRespNotifyEvent loginRespNotifyEvent = new LoginRespNotifyEvent();
            loginRespNotifyEvent.setRespCode(loginRespData.getInt("errorCode"));
            loginRespNotifyEvent.setMessage(loginRespData.getString("message"));
            eventListener.notifyListener(loginRespNotifyEvent);

        } else if ("netStateChange".equals(action)) {// 网络状态通知
            Bundle netStateChangeData = bundle.getBundle("data");
            boolean networkAvaileble = netStateChangeData.getBoolean("networkAvaileble");
            NetStatueChangeEvent netStatueChangeEvent = new NetStatueChangeEvent();
            netStatueChangeEvent.setNetworkAvaileble(networkAvaileble);
            if (!networkAvaileble) {
                netStatueChangeEvent.setPromptMsg("网络连接不可用");
            }
            eventListener.notifyListener(netStatueChangeEvent);

        } else if ("refreshPubAccountChats".equals(action)) {//公众号有会话更新,刷新消息列表
            eventListener.notifyListener(new RefreshImMainTabEvent());
        } else if ("newFriend".equals(action)) {//new friend
            String userid = bundle.getString("userid");
            UiEventHandleFacade.getInstance().handle(new NewFriendChatEvent(userid));

        }

    }
}
