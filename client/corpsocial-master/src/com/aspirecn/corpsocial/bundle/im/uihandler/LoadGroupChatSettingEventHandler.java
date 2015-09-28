package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.LocalGroupChatSettingEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.GroupDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupChatSetting;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.trinea.android.common.util.JSONUtils;

/**
 * 加载单个本地对话处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = LocalGroupChatSettingEvent.class)
public class LoadGroupChatSettingEventHandler implements
        IHandler<GroupChatSetting, LocalGroupChatSettingEvent> {


    private GroupDao groupDao = new GroupDao();

    private ChatDao chatDao = new ChatDao();

    @Override
    public GroupChatSetting handle(LocalGroupChatSettingEvent busEvent) {

        GroupChatSetting groupChatSetting = new GroupChatSetting();

        Map<String, Object> where = new HashMap<String, Object>();
        String groupId = busEvent.getGroupId();
        where.put("groupId", groupId);
        String userId = Config.getInstance().getUserId();
        where.put("belongUserId", userId);
        GroupEntity groupEntity = groupDao.findByWhere(where);

        groupChatSetting.setGroupEntity(groupEntity);
        groupChatSetting.setNewMsgNotify(true);
        List<String> adminList = groupEntity.getAdminList();
        if (adminList != null) {
            groupChatSetting.setAdmin(adminList.contains(userId));
        }

        Map<String, Object> chatWhere = new HashMap<String, Object>();
        chatWhere.put("chatId", groupId);
        chatWhere.put("belongUserId", Config.getInstance().getUserId());
        ChatEntity chatEntity = chatDao.findByWhere(chatWhere);

        if (chatEntity != null) {
            String setting = chatEntity.getSetting();
            boolean newMsgNotify = JSONUtils.getBoolean(setting, "newMsgNotify", true);
            groupChatSetting.setNewMsgNotify(newMsgNotify);
        }

        return groupChatSetting;
    }

}
