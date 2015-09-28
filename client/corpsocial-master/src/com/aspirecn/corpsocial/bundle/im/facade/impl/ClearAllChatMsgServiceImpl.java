package com.aspirecn.corpsocial.bundle.im.facade.impl;

import com.aspirecn.corpsocial.bundle.im.event.ClearChatMsgEvent;
import com.aspirecn.corpsocial.bundle.im.facade.ClearAllChatMsgService;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation.OsgiService;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;

import java.util.List;

@OsgiService(serviceType = ClearAllChatMsgService.class)
public class ClearAllChatMsgServiceImpl implements ClearAllChatMsgService {

    private ChatDao chatDao = new ChatDao();

    @Override
    public Void invoke(Void params) {

        List<ChatEntity> chatList = chatDao.findAllByWhere("belongUserId", Config.getInstance().getUserId(), "");
        for (ChatEntity chatEntity : chatList) {
            String chatId = chatEntity.getChatId();

            ClearChatMsgEvent clearChatMsgEvent = new ClearChatMsgEvent();
            clearChatMsgEvent.setChatId(chatId);

            UiEventHandleFacade facade = UiEventHandleFacade.getInstance();
            facade.handle(clearChatMsgEvent);
        }

        return null;
    }

}
