package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.ChangeP2PChatSettingEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.P2PChatSetting;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lizhuo_a
 */
@UIEventHandler(eventType = ChangeP2PChatSettingEvent.class)
public class ChangeP2PChatSettingEventHandler implements
        IHandler<Null, ChangeP2PChatSettingEvent> {

    private ChatDao chatDao = new ChatDao();

    @Override
    public Null handle(ChangeP2PChatSettingEvent busEvent) {

        P2PChatSetting p2pChatSetting = busEvent.getP2pChatSetting();
        String chatId = p2pChatSetting.getChatId();

        Map<String, Object> chatWhere = new HashMap<String, Object>();
        chatWhere.put("chatId", chatId);
        chatWhere.put("belongUserId", Config.getInstance().getUserId());
        ChatEntity chatEntity = chatDao.findByWhere(chatWhere);

        if (chatEntity != null) {
            String setting = chatEntity.getSetting();

            JSONObject jsonObject;
            try {
                if (setting != null) {
                    jsonObject = new JSONObject(setting);
                    jsonObject.put("newMsgNotify", p2pChatSetting.isNewMsgNotify());

                    String updateSetting = jsonObject.toString();
                    chatEntity.setSetting(updateSetting);

                } else {
                    jsonObject = new JSONObject();
                    jsonObject.put("newMsgNotify", p2pChatSetting.isNewMsgNotify());

                    String updateSetting = jsonObject.toString();
                    chatEntity.setSetting(updateSetting);
                }
                chatDao.update(chatEntity);

            } catch (JSONException e) {
                LogUtil.e("更新个人聊天设置失败", e);
            }
        }

        return new Null();
    }

}
