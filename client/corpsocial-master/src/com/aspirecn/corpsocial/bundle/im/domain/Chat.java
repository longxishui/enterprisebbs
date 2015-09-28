/**
 * @(#) Chat.java Created on 2013-10-31
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.im.domain;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.im.event.SyncMessageListEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.GroupDao;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity.ChatType;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatSetting;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupChatSetting;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupMemberEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.P2PChatSetting;
import com.aspirecn.corpsocial.bundle.im.utils.ContentType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 会话对象
 *
 * @author lizhuo_a
 */
public class Chat {

    /**
     * 对话信息
     */
    private ChatEntity chatEntity = new ChatEntity();

    /**
     * 对话所属消息列表
     */
    private LinkedList<MsgEntity> msgList = new LinkedList<MsgEntity>();

    private MsgDao msgDao = new MsgDao();

    private ChatDao chatDao = new ChatDao();

    private GroupDao groupDao = new GroupDao();

    private boolean speeker;

    /**
     * 申请发言管理员
     */
    private GroupMemberEntity applySpeekAdmin;

    /**
     * 加载更多消息
     *
     * @param index
     * @param count
     */
    public List<MsgEntity> doLoadMoreMsg(int index, int count) {
        // 根据条件从数据库中加载新的消息列表
        List<MsgEntity> msgs = new ArrayList<MsgEntity>();
        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("chatId", chatEntity.getChatId());
        wheres.put("belongUserId", Config.getInstance().getUserId());
        msgs = msgDao
                .findAllByWhereAndIndex(wheres, index, count, "createTime DESC");
        int size = msgs.size();
        //如果本地群没有消息，初始化同步30条信息
        if (size < count && chatEntity != null) {
            SyncMessageListEvent syncUserMessageListEvent = new SyncMessageListEvent();

            syncUserMessageListEvent.setGroupMsg(chatEntity.getChatType() == ChatType.GROUP.value);
            syncUserMessageListEvent.setTargetId(chatEntity.getChatId());
            long fromTime = System.currentTimeMillis();
            if (size > 0) {
                fromTime = msgs.get(0).getCreateTime().getTime();
            } else if (size == 0 && index != 0) {
                List<MsgEntity> lastMsg = msgDao.findAllByWhereAndIndex(wheres, 0, 1, "createTime ASC");
                if (lastMsg.size() > 0) {
                    fromTime = lastMsg.get(0).getCreateTime().getTime();
                }
            }
            syncUserMessageListEvent.setFromTime(fromTime);
            syncUserMessageListEvent.setCount(count - size);

            UiEventHandleFacade.getInstance().handle(syncUserMessageListEvent);
        }

        return msgs;
    }

    public String doRefreshChatName() {
        Map<String, Object> chatWheres = new HashMap<String, Object>();
        chatWheres.put("chatId", chatEntity.getChatId());
        chatWheres.put("belongUserId", Config.getInstance().getUserId());
        chatEntity = chatDao.findByWhere(chatWheres);

        if (chatEntity != null) {
            return chatEntity.getChatName();
        }

        return "";
    }

    public List<MsgEntity> doRefreshMsgList() {
        int count = msgList.size();
        List<MsgEntity> msgs = new ArrayList<MsgEntity>();
        Map<String, Object> wheres = new HashMap<String, Object>();
        String chatId = chatEntity.getChatId();
        if (chatId != null) {
            wheres.put("chatId", chatId);
            wheres.put("belongUserId", Config.getInstance().getUserId());
            msgs = msgDao
                    .findAllByWhereAndIndex(wheres, 0, count, "createTime DESC");

            return msgs;

        }
        return msgs;
    }

    public int doGetGroupType() {
        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("groupId", chatEntity.getChatId());
        wheres.put("belongUserId", Config.getInstance().getUserId());
        GroupEntity findByWhere = groupDao.findByWhere(wheres);

        return findByWhere == null ? -1 : findByWhere.getGroupType();

    }

    /**
     * 会话时间
     *
     * @return
     */
    public Date doGetChatTime() {
        Date chatTime = new Date(System.currentTimeMillis());
        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("chatId", chatEntity.getChatId());
        wheres.put("belongUserId", Config.getInstance().getUserId());
        List<MsgEntity> msgList = msgDao.findAllByWhereAndIndex(wheres, 0, 1,
                "createTime DESC");

        if (msgList != null && msgList.size() > 0) {
            MsgEntity msgEntity = msgList.get(0);
            chatTime = msgEntity.getCreateTime();
        } else {
            chatTime = chatEntity.getCreateTime();
        }
        return chatTime;
    }

    /**
     * 获取置顶时间
     *
     * @return
     */
    public long doGetMoveToTopTime() {
        Date time = chatEntity.getMoveToTopTime();
        if (time != null) {
            return time.getTime();
        } else {
            return 0;
        }
    }

    /**
     *
     */
    public void clearUnReadCount() {
        Map<String, Object> chatWheres = new HashMap<String, Object>();
        chatWheres.put("chatId", chatEntity.getChatId());
        chatWheres.put("belongUserId", Config.getInstance().getUserId());
        chatEntity = chatDao.findByWhere(chatWheres);

        chatEntity.setNoticeCount(0);
        chatDao.update(chatEntity);
    }

    public int doGetChatType() {
        return chatEntity.getChatType();
    }

    /**
     * 会话简介
     *
     * @return
     */
    public String doGetChatIntro() {
        String chatIntro = "";

        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("chatId", chatEntity.getChatId());
        wheres.put("belongUserId", Config.getInstance().getUserId());
        List<MsgEntity> msgs = msgDao.findAllByWhereAndIndex(wheres, 0, 1, "createTime DESC");

        if (msgs.size() > 0) {
            MsgEntity last = msgs.get(0);
            String speaker = TextUtils.isEmpty(last.getOwnedUserName()) ?
                    "" : last.getOwnedUserName() + ": ";

            int contentType = last.getContentType();
            if (contentType == ContentType.TEXT.getValue()) {
                chatIntro = speaker + last.getContent();
            } else if (contentType == ContentType.VOICE.getValue()) {
                chatIntro = speaker + "[语音]";
            } else if (contentType == ContentType.PICTURE.getValue()) {
                chatIntro = speaker + "[图片]";
            } else if (contentType == ContentType.POSITION.getValue()) {
                chatIntro = speaker + "[位置]";
            } else if (contentType == ContentType.SYSTEM.getValue()) {
                chatIntro = last.getContent();
            } else if (contentType == ContentType.VIDIO.getValue()) {
                chatIntro = speaker + "[视频]";
            } else {
                chatIntro = "[暂未支持的消息类型]";
            }
        } else if (!TextUtils.isEmpty(chatEntity.getDescription())) {
            chatIntro = chatEntity.getDescription();

        } else if (chatEntity.getChatType() == ChatType.GROUP.value) {
            chatIntro = "【" + chatEntity.getChatName() + "】群已创建了，欢迎大家踊跃发言！";
        }

        return chatIntro;
    }

    public int doGetUnReadCount() {
        return chatEntity.getNoticeCount();
    }

    public String doGetChatId() {
        return chatEntity.getChatId();
    }

    public String doGeChatName() {
        return chatEntity.getChatName();
    }

    /**
     * 得到会话设置
     *
     * @return
     */
    public ChatSetting doGetChatSetting() {
        int chatType = chatEntity.getChatType();
        String setting = chatEntity.getSetting();

        try {
            JSONTokener jsonParser = new JSONTokener(setting);
            JSONObject chatSetting = (JSONObject) jsonParser.nextValue();

            if (ChatType.P2P.value == chatType) {
                P2PChatSetting p2pChatSetting = new P2PChatSetting();

                boolean newMsgNotify = chatSetting.getBoolean("newMsgNotify");
                p2pChatSetting.setNewMsgNotify(newMsgNotify);
                return p2pChatSetting;

            } else if (ChatType.GROUP.value == chatType) {
                GroupChatSetting groupChatSetting = new GroupChatSetting();

                boolean newMsgNotify = chatSetting.getBoolean("newMsgNotify");
                groupChatSetting.setNewMsgNotify(newMsgNotify);

                return groupChatSetting;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存会话设置
     *
     * @param chatSetting
     */
    public void doSaveChatSetting(ChatSetting chatSetting) {

        int chatType = chatEntity.getChatType();
        JSONObject chatSettingText = new JSONObject();
        try {
            if (ChatType.P2P.value == chatType) {
                P2PChatSetting p2pChatSetting = (P2PChatSetting) chatSetting;

                chatSettingText.put("newMsgNotify",
                        p2pChatSetting.isNewMsgNotify());

            } else if (ChatType.GROUP.value == chatType) {
                GroupChatSetting groupChatSetting = new GroupChatSetting();

                chatSettingText.put("newMsgNotify",
                        groupChatSetting.isNewMsgNotify());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        chatEntity.setSetting(chatSettingText.toString());

        chatDao.update(chatEntity);
    }

    /**
     * 判断是否设置置顶
     *
     * @return
     */
    public boolean doGetIsMoveToTop() {
        Date date = chatEntity.getMoveToTopTime();
        return date != null && date.getTime() > 0;

    }

    public String doGetBelongUserId() {
        return chatEntity.getBelongUserId();
    }

    public ChatEntity getChatEntity() {
        return chatEntity;
    }

    public void setChatEntity(ChatEntity chatEntity) {
        this.chatEntity = chatEntity;
    }

    public LinkedList<MsgEntity> getMsgList() {
        return msgList;
    }

    public void setMsgList(LinkedList<MsgEntity> msgList) {
        this.msgList = msgList;
    }

    public boolean isSpeeker() {
        return speeker;
    }

    public void setSpeeker(boolean speeker) {
        this.speeker = speeker;
    }

    public GroupMemberEntity getApplySpeekAdmin() {
        return applySpeekAdmin;
    }

    public void setApplySpeekAdmin(GroupMemberEntity applySpeekAdmin) {
        this.applySpeekAdmin = applySpeekAdmin;
    }

}
