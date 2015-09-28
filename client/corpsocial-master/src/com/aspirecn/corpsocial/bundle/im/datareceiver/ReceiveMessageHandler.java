/**
 * @(#) ImReceiveHandler.java Created on 2013-11-14
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.im.datareceiver;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.common.uitils.UserUtil;
import com.aspirecn.corpsocial.bundle.im.event.CalculateImUnReadMsgCountEvent;
import com.aspirecn.corpsocial.bundle.im.event.ReceiveMessageEvent;
import com.aspirecn.corpsocial.bundle.im.event.RefreshImMainTabEvent;
import com.aspirecn.corpsocial.bundle.im.notify.MsgNotificationControllers;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.GroupDao;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity.ChatType;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity.MsgStatus;
import com.aspirecn.corpsocial.bundle.im.utils.ContentType;
import com.aspirecn.corpsocial.bundle.im.utils.ImFileUtil;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.CommandData;
import com.aspirecn.corpsocial.common.eventbus.CommandHead;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.DataReceiveHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao.TransactionCallback;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.trinea.android.common.util.JSONUtils;

/**
 * 接收消息处理类
 *
 * @author lizhuo_a
 */
@DataReceiveHandler(commandType = CommandData.CommandType.DIALOG_MESSAGE)
public class ReceiveMessageHandler implements IHandler<Null, CommandData> {

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader.getInstance();

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    private GroupDao groupDao = new GroupDao();

    private ChatDao chatDao = new ChatDao();

    private MsgDao msgDao = new MsgDao();

    @Override
    public synchronized Null handle(CommandData args) {
        CommandHead commandHeader = args.getCommandHeader();
        String senderId = commandHeader.userId;
        // P2P时为接收人ID，群组聊天时为群组ID
        String targetId = commandHeader.targetId;

        boolean groupMsg = commandHeader.groupMsg;
        String chatId = "";
        if (groupMsg) {
            chatId = targetId;
        } else {
            chatId = senderId;
        }

        String senderName = senderId;
        User user = UserUtil.getUser(senderId);
        if (user != null) {
            senderName = user.getName();
        }

        // 保存会话
        handleChat(commandHeader, senderName, chatId);
        handleTextMsg(commandHeader, args.getContent(), senderName, chatId);

        // 通知界面刷新数据
        eventListener.notifyListener(new RefreshImMainTabEvent());
        return new Null();
    }

    /**
     * 处理会话信息
     *
     * @param commandHeader
     */
    private void handleChat(final CommandHead commandHeader,
                            final String senderName, final String chatId) {

        // 开始排他事务，防止出现重复群组问题
        chatDao.executeTransaction(new TransactionCallback() {
            @Override
            public void execute() {
                boolean groupMsg = commandHeader.groupMsg;

                Map<String, Object> wheres = new HashMap<String, Object>();
                //wheres.put("corpId", commandHeader.corpId);
                wheres.put("chatId", chatId);
                String userId = Config.getInstance().getUserId();
                wheres.put("belongUserId", userId);
                ChatEntity chatEntity = chatDao.findByWhere(wheres);

                Date createTime = new Date(commandHeader.sendTime);
                if (chatEntity == null) {// 会话不存在，创建一个新会话

                    if (groupMsg) {// 群会话创建
                        ChatEntity newGroupChatEntity = new ChatEntity();
                        //newGroupChatEntity.setCorpId(commandHeader.corpId);
                        newGroupChatEntity.setChatId(chatId);
                        newGroupChatEntity.setChatType(ChatType.GROUP.value);
                        newGroupChatEntity.setDisplay(true);

                        Map<String, Object> groupWhere = new HashMap<String, Object>();
                        //groupWhere.put("corpId", commandHeader.corpId);
                        groupWhere.put("groupId", chatId);
                        groupWhere.put("belongUserId", userId);
                        GroupEntity groupEntity = groupDao.findByWhere(groupWhere);
                        if (groupEntity != null) {// 群组已经创建
                            String chatName = groupEntity.getName();

                            JSONObject groupChatSetting = new JSONObject();
                            try {
                                groupChatSetting.put("groupId", groupEntity.getGroupId());
                                List<String> adminList = groupEntity.getAdminList();
                                groupChatSetting.put("isAdmin", adminList.contains(userId));
                                // 默认接收新消息
                                groupChatSetting.put("newMsgNotify", true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            newGroupChatEntity.setSetting(groupChatSetting.toString());
                            newGroupChatEntity.setChatName(chatName);
                            newGroupChatEntity.setNoticeCount(newGroupChatEntity.getNoticeCount() + 1);
                            newGroupChatEntity.setBelongUserId(Config.getInstance().getUserId());
                            newGroupChatEntity.setCreateTime(createTime);
                            newGroupChatEntity.setLastUpdateTime(createTime);

                            chatDao.insert(newGroupChatEntity);
                        } else {// 群组还未创建，需要先更新
                            // 不创建会话，等群组更新完成后才能创建
                        }

                    } else {// 点对点会话创建
                        ChatEntity newP2PChatEntity = new ChatEntity();
                        //newP2PChatEntity.setCorpId(commandHeader.corpId);
                        newP2PChatEntity.setChatId(chatId);
                        newP2PChatEntity.setChatType(ChatType.P2P.value);

                        JSONObject p2pChatSetting = new JSONObject();
                        try {
                            // 默认接收新消息
                            p2pChatSetting.put("newMsgNotify", true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        newP2PChatEntity.setSetting(p2pChatSetting.toString());
                        newP2PChatEntity.setChatName(senderName);

                        newP2PChatEntity.setNoticeCount(newP2PChatEntity.getNoticeCount() + 1);
                        newP2PChatEntity.setBelongUserId(Config.getInstance().getUserId());
                        newP2PChatEntity.setCreateTime(createTime);
                        newP2PChatEntity.setLastUpdateTime(createTime);
                        newP2PChatEntity.setDisplay(true);

                        chatDao.insert(newP2PChatEntity);
                    }

                } else {// 会话已经存在，更新会话状态
                    if (groupMsg) {// 群会话修改

                    } else {
                        chatEntity.setChatName(senderName);
                    }
                    int noticeCount = chatEntity.getNoticeCount();
                    chatEntity.setNoticeCount(noticeCount + 1);
                    chatEntity.setLastUpdateTime(createTime);
                    chatDao.update(chatEntity);
                }
            }
        });
    }

    /**
     * 处理系统消息
     *
     * @param ch
     */
    private void handleSystemMsg(CommandHead ch, String content, String senderName, String chatId) {
        MsgEntity msgEntity = new MsgEntity();

        //msgEntity.setCorpId(commandHeader.corpId);
        msgEntity.setChatId(chatId);
        msgEntity.setContentType(ContentType.SYSTEM.getValue());
        msgEntity.setContent(content);
        msgEntity.setOwned(false);
        msgEntity.setOwnedUserId(ch.userId);
        msgEntity.setMsgId(ch.messageId);
        msgEntity.setStatus(MsgStatus.Success.value);
        Date createTime = new Date(ch.sendTime);
        msgEntity.setCreateTime(createTime);
        boolean showTime = showTime(chatId, createTime);
        msgEntity.setShowCreateTime(showTime);
        msgEntity.setBelongUserId(Config.getInstance().getUserId());
        msgEntity.setOwnedUserName(senderName);

        MsgEntity inMsgEntity = msgDao.insert(msgEntity);

        //通知界面
        notifyUIHandleMsgSuccess(inMsgEntity);

        notifyCommonModuleAddUnReadMsgCount();

        notifyMessage(ch.groupMsg, inMsgEntity);
    }

    /**
     * 处理文本消息
     *
     * @param ch
     */
    private void handleTextMsg(CommandHead ch,String content, String senderName, String chatId) {
        MsgEntity msgEntity = new MsgEntity();

        //msgEntity.setCorpId(commandHeader.corpId);
        msgEntity.setChatId(chatId);
        msgEntity.setContentType(ContentType.TEXT.getValue());
        msgEntity.setContent(content);
        msgEntity.setOwned(false);
        msgEntity.setOwnedUserId(ch.userId);
        msgEntity.setMsgId(ch.messageId);
        msgEntity.setStatus(MsgStatus.Success.value);
        Date createTime = new Date(ch.sendTime);
        msgEntity.setCreateTime(createTime);
        boolean showTime = showTime(chatId, createTime);
        msgEntity.setShowCreateTime(showTime);
        msgEntity.setBelongUserId(Config.getInstance().getUserId());
        msgEntity.setOwnedUserName(senderName);

        MsgEntity inMsgEntity = msgDao.insert(msgEntity);

        //通知界面
        notifyUIHandleMsgSuccess(inMsgEntity);

        notifyCommonModuleAddUnReadMsgCount();

        notifyMessage(ch.groupMsg, inMsgEntity);
    }

    private boolean showTime(String chatId, Date createTime) {
        Map<String, Object> msgWheres = new HashMap<String, Object>();
        msgWheres.put("chatId", chatId);
        msgWheres.put("belongUserId", Config.getInstance().getUserId());
        msgWheres.put("showCreateTime", true);
        List<MsgEntity> msgs = msgDao.findAllByWhereAndIndex(msgWheres, 0, 1, "seqNo DESC");
        if (msgs.size() > 0) {
            Date lastMsgCreateTime = msgs.get(0).getCreateTime();
            long time2 = lastMsgCreateTime.getTime();
            long time1 = createTime.getTime();
            long gapTime = time1 - time2;
            return gapTime > 5 * 60 * 1000;
        }
        return true;
    }

    /**
     * 发出通知栏消息通知
     *
     * @param msgEntity
     */
    private void notifyMessage(boolean groupMsg, MsgEntity msgEntity) {
        MsgNotificationControllers.getInstance().showNotify(groupMsg, msgEntity);
    }

    private void notifyUIHandleMsgSuccess(MsgEntity msgEntity) {
        ReceiveMessageEvent receiveMessageEvent = new ReceiveMessageEvent();
        receiveMessageEvent.setErrorCode(ErrorCode.SUCCESS.getValue());
        receiveMessageEvent.setPercent(1);
        receiveMessageEvent.setMsgEntity(msgEntity);
        eventListener.notifyListener(receiveMessageEvent);
    }

    private void notifyCommonModuleAddUnReadMsgCount() {
//        Intent unReadCountIntent = new Intent("commonNotifyReceiver");
//        Bundle unReadCountIntentBundle = new Bundle();
//        unReadCountIntentBundle.putString("action", "unReadCountNotify");
//
//        Bundle unReadCountData = new Bundle();
//        unReadCountData.putBoolean("totalCount", false);
//        unReadCountData.putInt("unReadCount", 1);
//        unReadCountIntentBundle.putBundle("data", unReadCountData);
//
//        unReadCountIntent.putExtras(unReadCountIntentBundle);
//        AppBroadcastManager appBroadcastManager = AppBroadcastManager.getInstance();
//        appBroadcastManager.sendBroadcast(unReadCountIntent);
        UiEventHandleFacade.getInstance().handle(new CalculateImUnReadMsgCountEvent());
    }

}
