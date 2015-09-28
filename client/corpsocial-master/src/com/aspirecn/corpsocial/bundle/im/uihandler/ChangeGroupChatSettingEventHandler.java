package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.ChangeGroupChatSettingEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupChatSetting;
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
@UIEventHandler(eventType = ChangeGroupChatSettingEvent.class)
public class ChangeGroupChatSettingEventHandler implements
        IHandler<Null, ChangeGroupChatSettingEvent> {

    private ChatDao chatDao = new ChatDao();

    @Override
    public Null handle(ChangeGroupChatSettingEvent busEvent) {

        GroupChatSetting groupChatSetting = busEvent.getGroupChatSetting();
        String chatId = groupChatSetting.doGetGroupId();

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
                    jsonObject.put("newMsgNotify", groupChatSetting.isNewMsgNotify());

                    String updateSetting = jsonObject.toString();
                    chatEntity.setSetting(updateSetting);

                } else {
                    jsonObject = new JSONObject();
                    jsonObject.put("newMsgNotify", groupChatSetting.isNewMsgNotify());

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
