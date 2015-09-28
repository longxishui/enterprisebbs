package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.im.event.LocalP2PChatSettingEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.P2PChatSetting;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;

import java.util.HashMap;
import java.util.Map;

import cn.trinea.android.common.util.JSONUtils;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

/**
 * @author lizhuo_a
 */
@UIEventHandler(eventType = LocalP2PChatSettingEvent.class)
public class LoadP2PChatSettingEventHandler implements
        IHandler<P2PChatSetting, LocalP2PChatSettingEvent> {

    private ChatDao chatDao = new ChatDao();

    @Override
    public P2PChatSetting handle(LocalP2PChatSettingEvent busEvent) {
        P2PChatSetting p2pChatSetting = new P2PChatSetting();

        String userId = busEvent.getUserId();
        p2pChatSetting.setChatId(userId);

        User user = getUser(userId);
        p2pChatSetting.setUser(user);

        Map<String, Object> chatWhere = new HashMap<String, Object>();
        chatWhere.put("chatId", userId);
        chatWhere.put("belongUserId", Config.getInstance().getUserId());
        ChatEntity chatEntity = chatDao.findByWhere(chatWhere);

        if (chatEntity != null) {
            String setting = chatEntity.getSetting();
            boolean newMsgNotify = JSONUtils.getBoolean(setting, "newMsgNotify", true);
            p2pChatSetting.setNewMsgNotify(newMsgNotify);
        }

        return p2pChatSetting;
    }

    /**
     * 根据userId获取用户
     *
     * @param userId
     * @return
     */
    private User getUser(String userId) {

        return (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(userId);
    }

}
