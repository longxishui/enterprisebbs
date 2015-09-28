package com.aspirecn.corpsocial.bundle.im.presenter.impl;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.addrbook.event.AddFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindContactBatchRespEvent;
import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupEvent;
import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.DeleteMsgEvent;
import com.aspirecn.corpsocial.bundle.im.event.DismissGroupEvent;
import com.aspirecn.corpsocial.bundle.im.event.KickOutGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.event.LoadChatEvent;
import com.aspirecn.corpsocial.bundle.im.event.QuitGroupEvent;
import com.aspirecn.corpsocial.bundle.im.event.ReceiveMessageEvent;
import com.aspirecn.corpsocial.bundle.im.event.SendMessageEvent;
import com.aspirecn.corpsocial.bundle.im.event.SendMessageRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.SyncMessageRespEvent;
import com.aspirecn.corpsocial.bundle.im.presenter.ChatPresenter;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.bundle.im.utils.ContentType;
import com.aspirecn.corpsocial.bundle.im.utils.ImFileUtil;
import com.aspirecn.corpsocial.bundle.im.view.ChatView;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.util.BitmapUtil;
import com.aspirecn.corpsocial.common.util.ColorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 聊天对话页面
 * <p/>
 * Created by yinghuihong on 15/7/16.
 */
public class ChatPresenterImpl implements ChatPresenter {


    private ChatView iView;
    private Chat chat = new Chat();
    private String mCorpId;
    private String mChatId;
    private String mChatName;
    private int mChatType;
    private int mThemeColor;
    /**
     * 文件存储路径
     */
    private String pictureFilePath;
    /**
     * 语音存储路径
     */
    private String voiceFilePath;

    public ChatPresenterImpl(ChatView chatView) {
        this.iView = chatView;
    }


    //==========================================以下为回调=========================================//

    /**
     * 处理发送消息反馈
     */
    @Override
    public void onHandleSendMessageRespEvent(SendMessageRespEvent event) {
        // 刷新数据
        refreshMsgList(false);

        if (event.getErrorCode() != ErrorCode.SUCCESS.getValue()) {
            String errorMsg = event.getErrorMsg();
            if (!TextUtils.isEmpty(errorMsg)) {
                iView.showToast(errorMsg);
            }
        }
    }


    /**
     * 处理接收消息
     */
    @Override
    public void onHandleReceiveMessageEvent(final ReceiveMessageEvent event) {
        MsgEntity msgEntity = event.getMsgEntity();
        // chatId有可能为空，因为chat对象还未初始化完毕
        String chatId = chat.doGetChatId();
        // 显示属于本会话的消息
        if (msgEntity != null && chatId != null) {
            if (chatId.equals(msgEntity.getChatId())
                    && msgEntity.getBelongUserId().equals(
                    chat.doGetBelongUserId())) {
                boolean exist = false;
                for (int i = 0; i < chat.getMsgList().size(); i++) {
                    MsgEntity msg = chat.getMsgList().get(i);
                    if (msg.getMsgId().equals(msgEntity.getMsgId())) {
                        String extraInfo = msgEntity.getExtraInfo();
                        msg.setExtraInfo(extraInfo);
                        msgEntity = msg;
                        exist = true;
                    }
                }
                if (!exist) {
                    LinkedList<MsgEntity> msgList = chat.getMsgList();
                    msgList.add(msgEntity);
                    chat.clearUnReadCount();
                }
                if (msgEntity.getContentType() == ContentType.PICTURE
                        .getValue()) {
                    if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
                        float percent = event.getPercent();
                        if (percent == 1) {
                            msgEntity.setStatus(MsgEntity.MsgStatus.Success.value);
                        }
                    } else {
                        msgEntity.setStatus(MsgEntity.MsgStatus.Fail.value);
                    }
                } else if (msgEntity.getContentType() == ContentType.POSITION
                        .getValue()) {
                    if (ErrorCode.SUCCESS.getValue() == event.getErrorCode()) {
                        float percent = event.getPercent();
                        if (percent == 1) {
                            msgEntity.setStatus(MsgEntity.MsgStatus.Success.value);
                        }
                    } else {
                        msgEntity.setStatus(MsgEntity.MsgStatus.Fail.value);
                    }
                } else if (msgEntity.getContentType() == ContentType.VIDIO.getValue()) {
                    if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
                        float percent = event.getPercent();
                        if (percent == 1) {
                            msgEntity.setStatus(MsgEntity.MsgStatus.Success.value);
                        }
                    } else {
                        msgEntity.setStatus(MsgEntity.MsgStatus.Fail.value);
                    }
                }

                // 刷新数据
                iView.notifyDataChanged(chat, !exist);
            }
        }
    }

    /**
     * 群组信息-允许发现配置变更
     */
    @Override
    public void onHandleCreateUpdateGroupEvent(CreateUpdateGroupEvent event) {
        GroupEntity groupEntity = event.getGroupEntity();

        if (groupEntity != null) {
            List<String> speekList = groupEntity.getSpeekList();
            String limitType = groupEntity.getLimitType();
            if (GroupEntity.LimitType.Limit.equals(limitType)) {
                String userId = Config.getInstance().getUserId();
                if (speekList.contains(userId)) {
                    chat.setSpeeker(true);
                    iView.showToast("您被管理员设置为允许发言");
                    iView.setSpeakPower(chat, true);
                } else {
                    chat.setSpeeker(false);
                    iView.setSpeakPower(chat, false);
                    iView.showToast("您被管理员设置为禁止发言");
                }
            }
        }
    }

    @Override
    public void onHandleDismissGroupEvent(DismissGroupEvent event) {
        if (mChatId.equals(event.getGroupId())) {
            iView.doFinish();
        }
    }

    @Override
    public void onHandleKickOutGroupMemberEvent(KickOutGroupMemberEvent event) {
        if (mChatId.equals(event.getGroupId())) {
            iView.doFinish();
        }
    }


    @Override
    public void onHandleSyncMessageRespEvent(SyncMessageRespEvent event) {
        MsgEntity msgEntity = event.getMsgEntity();
        LinkedList<MsgEntity> msgList = chat.getMsgList();
        // chatId有可能为空，因为chat对象还未初始化完毕
        String chatId = chat.doGetChatId();
        // 显示属于本会话的消息
        if (msgEntity != null && chatId != null) {
            if (chatId.equals(msgEntity.getChatId())
                    && msgEntity.getBelongUserId().equals(
                    chat.doGetBelongUserId())) {
                boolean exist = false;
                for (int i = 0; i < chat.getMsgList().size(); i++) {
                    MsgEntity msg = chat.getMsgList().get(i);
                    if (msg.getMsgId().equals(msgEntity.getMsgId())) {
                        String extraInfo = msgEntity.getExtraInfo();
                        msg.setExtraInfo(extraInfo);
                        msgEntity = msg;
                        exist = true;
                    }
                }
                if (!exist) {
                    msgList.addFirst(msgEntity);
                    chat.clearUnReadCount();
                }
                if (msgEntity.getContentType() == ContentType.PICTURE
                        .getValue()) {
                    if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
                        float percent = event.getPercent();
                        if (percent == 1) {
                            msgEntity.setStatus(MsgEntity.MsgStatus.Success.value);
                        }
                    } else {
                        msgEntity.setStatus(MsgEntity.MsgStatus.Fail.value);
                    }
                } else if (msgEntity.getContentType() == ContentType.POSITION
                        .getValue()) {
                    if (ErrorCode.SUCCESS.getValue() == event.getErrorCode()) {
                        float percent = event.getPercent();
                        if (percent == 1) {
                            msgEntity.setStatus(MsgEntity.MsgStatus.Success.value);
                        }
                    } else {
                        msgEntity.setStatus(MsgEntity.MsgStatus.Fail.value);
                    }
                }
                // 刷新数据
                iView.notifyDataChanged(chat, false);
            }
        }
    }

    @Override
    public void onHandleCreateUpdateGroupRespEvent(CreateUpdateGroupRespEvent event) {
        int errorCode = event.getErrorCode();
        if (errorCode == ErrorCode.SUCCESS.getValue()
                && !event.getChatEntity().getChatId().equals(mChatId)) {
            iView.doFinish();
        }
    }

    @Override
    public void onHandleAddFriendRespEvent(AddFriendRespEvent event) {
        iView.closeCustomDialog();
        if (event.getStatus() == ErrorCode.SUCCESS.getValue()) {
            iView.showToast("已发送");
        } else {
            iView.showToast("已发失败");
        }
    }

    @Override
    public void onHandleQuitGroupEvent(QuitGroupEvent event) {
        if (mChatId.equals(event.getGroupId())) {
            iView.doFinish();
        }
    }

    @Override
    public void onHandleFindContactRespEvent(FindContactBatchRespEvent event) {
        iView.notifyDataChanged(chat, false);
    }


    //==========================================以上为回调=========================================//

    @Override
    public void initData(String corpId, String chatId, String chatName, int chatType) {
        mCorpId = corpId;
        mChatId = chatId;
        mChatName = chatName;
        mChatType = chatType;
        mThemeColor = getThemeColor();
    }

    @Override
    public int getThemeColor() {
        return ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor);
    }

    /**
     * 加载会话消息
     */
    @Override
    public void doLoadChat() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setChatId(mChatId);
        chatEntity.setChatName(mChatName);
        chatEntity.setChatType(mChatType);

        LoadChatEvent loadChatEvent = new LoadChatEvent();
        loadChatEvent.setChatEntity(chatEntity);

        chat = (Chat) uiEventHandleFacade.handle(loadChatEvent);

        // 清空未读消息条数
        chat.clearUnReadCount();

        // 设置发言权限
        iView.setSpeakPower(chat, chat.isSpeeker());

        iView.initPullToRefreshListView(chat);
    }

    @Override
    public void onClickActionbarRightBtn() {
        if (ChatEntity.ChatType.P2P.value == mChatType) {
            iView.startP2PChatSettingActivity(mChatId, mCorpId, mChatName);
        } else if (ChatEntity.ChatType.GROUP.value == mChatType) {
            // 群聊，会话ID即为群ID
            iView.startGroupChatSettingActivity(mChatId, mCorpId, mChatName);
        }
    }

    /**
     * 发送文本消息
     */
    @Override
    public void sendTextMsg(String textMst) {
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.setChatId(mChatId);
        msgEntity.setOwned(true);
        String userId = Config.getInstance().getUserId();
        msgEntity.setOwnedUserId(userId);
//        msgEntity.setMsgId(CommonUtils.getUUID());
        msgEntity.setContentType(ContentType.TEXT.getValue());
        msgEntity.setContent(textMst);
        msgEntity.setBelongUserId(userId);
        msgEntity.setCreateTime(new Date(Config.getInstance().getIntervalTime() + System.currentTimeMillis()));
        msgEntity.setStatus(MsgEntity.MsgStatus.Sending.value);
        // 刷新数据
        sendMessageDataChanged(chat, msgEntity, true);

        doSendMessage(chat, msgEntity);
    }

    @Override
    public void takeAPhoto() {
        pictureFilePath = ImFileUtil.createPicFilePath(mChatId);
        iView.takeAPhoto(pictureFilePath);
    }

    @Override
    public void sendPictureMsg() {
        sendPictureMsg(pictureFilePath);
    }

    @Override
    public void sendPictureMsg(String path) {
        BitmapUtil.correction(path);
        MsgEntity msgEntity = new MsgEntity();
        // String doGetChatId = chat.doGetChatId();
        //msgEntity.setCorpId(mCorpId);
        msgEntity.setChatId(mChatId);
        msgEntity.setOwned(true);
        String userId = Config.getInstance().getUserId();
        msgEntity.setOwnedUserId(userId);
//        msgEntity.setMsgId(CommonUtils.getUUID());
        msgEntity.setContentType(ContentType.PICTURE.getValue());
        msgEntity.setContent(path);
        msgEntity.setBelongUserId(userId);
        msgEntity.setCreateTime(new Date(Config.getInstance().getIntervalTime() + System.currentTimeMillis()));
        msgEntity.setStatus(MsgEntity.MsgStatus.Sending.value);

        // 刷新数据
        sendMessageDataChanged(chat, msgEntity, true);

        doSendMessage(chat, msgEntity);
    }

    @Override
    public void sendLocationMsg(String imagePath, String latitude, String longitude) {
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.setChatId(mChatId);
        msgEntity.setOwned(true);
        String userId = Config.getInstance().getUserId();
        msgEntity.setOwnedUserId(userId);
//        msgEntity.setMsgId(CommonUtils.getUUID());
        msgEntity.setContentType(ContentType.POSITION.getValue());
        msgEntity.setContent(longitude + "," + latitude);
        msgEntity.setBelongUserId(userId);
        msgEntity.setCreateTime(new Date(Config.getInstance().getIntervalTime() + System.currentTimeMillis()));
        msgEntity.setStatus(MsgEntity.MsgStatus.Sending.value);

        JSONObject extraInfo = new JSONObject();
        try {
            extraInfo.put("nativeUrl", imagePath);
        } catch (JSONException e) {
//            logger.error("发消息", e);
        }
        msgEntity.setExtraInfo(extraInfo.toString());

        // 刷新数据
        sendMessageDataChanged(chat, msgEntity, true);

        doSendMessage(chat, msgEntity);
    }

    @Override
    public void sendVideoMsg(String videoFilePath) {
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.setChatId(mChatId);
        msgEntity.setOwned(true);
        msgEntity.setOwnedUserId(Config.getInstance().getUserId());
//        msgEntity.setMsgId(CommonUtils.getUUID());
        msgEntity.setContent(videoFilePath);
        msgEntity.setContentType(ContentType.VIDIO.getValue());
        msgEntity.setBelongUserId(Config.getInstance().getUserId());
        msgEntity.setCreateTime(new Date(Config.getInstance().getIntervalTime() + System.currentTimeMillis()));
        msgEntity.setStatus(MsgEntity.MsgStatus.Sending.value);
        // 刷新数据
        sendMessageDataChanged(chat, msgEntity, true);

        doSendMessage(chat, msgEntity);
    }

    private void sendMessageDataChanged(Chat chat, MsgEntity msgEntity, boolean showLastItem) {
        chat.getMsgList().add(msgEntity);

        iView.notifyDataChanged(chat, showLastItem);
    }

    /**
     * 发送消息
     */
    private void doSendMessage(Chat chat, MsgEntity msgEntity) {
        SendMessageEvent sendMessageEvent = new SendMessageEvent();
        sendMessageEvent.setMsgEntity(msgEntity);
        int chatType = chat.doGetChatType();
        if (ChatEntity.ChatType.GROUP.value == chatType) {
            sendMessageEvent.setGroupMsg(true);
        }
        uiEventHandleFacade.handle(sendMessageEvent);
    }

    @Override
    public void refreshMsgList(boolean showLastItem) {
        List<MsgEntity> doRefreshMsgList = chat.doRefreshMsgList();
        LinkedList<MsgEntity> msgList = chat.getMsgList();

        msgList.clear();
        for (MsgEntity msgEntity : doRefreshMsgList) {
            msgList.addFirst(msgEntity);
        }

        iView.notifyDataChanged(chat, showLastItem);
    }

    @Override
    public void onClickSendAgain(int position) {
        MsgEntity msgEntity = chat.getMsgList().remove(position);
        msgEntity.setStatus(MsgEntity.MsgStatus.Sending.value);
        msgEntity.setCreateTime(new Date(Config.getInstance().getIntervalTime() + System.currentTimeMillis()));
        chat.getMsgList().add(msgEntity);
        iView.notifyDataChanged(chat, true);

        doSendMessage(chat, msgEntity);
    }

    @Override
    public void startRecordVoice() {
        voiceFilePath = ImFileUtil.createVoiceFilePath(mChatId);
        iView.startRecordVoice(mThemeColor, voiceFilePath);
    }

    @Override
    public void stopRecordVoice() {
        iView.stopRecordVoice(mThemeColor);
    }

    @Override
    public void sendVoiceMsg(int time) {
        MsgEntity msgEntity = new MsgEntity();
        //msgEntity.setCorpId(mCorpId);
        msgEntity.setChatId(mChatId);
        msgEntity.setOwned(true);
        String userId = Config.getInstance().getUserId();
        msgEntity.setOwnedUserId(userId);
//        msgEntity.setMsgId(CommonUtils.getUUID());
        msgEntity.setContentType(ContentType.VOICE.getValue());
        msgEntity.setContent(voiceFilePath);
        msgEntity.setBelongUserId(userId);
        msgEntity.setCreateTime(new Date(Config.getInstance().getIntervalTime() + System.currentTimeMillis()));
        msgEntity.setStatus(MsgEntity.MsgStatus.Sending.value);

        JSONObject jsonObject = new JSONObject();

        try {
            // 录音时长
            jsonObject.put("voiceTime", time + "\"");
        } catch (JSONException e) {
//            logger.error("发消息", e);
        }
        msgEntity.setExtraInfo(jsonObject.toString());
        // 刷新数据
        sendMessageDataChanged(chat, msgEntity, true);

        doSendMessage(chat, msgEntity);
    }

    @Override
    public void onClickLocationSelectBtn() {
        iView.startLocationActivity(ImFileUtil.createLocationFilePath(mChatId));
    }

    @Override
    public void onRestart() {
        iView.notifyDataChanged(chat, false);
    }

    @Override
    public void doDeleteMessage(String msgId) {
        List<String> msgIds = new ArrayList<String>();
        msgIds.add(msgId);
        DeleteMsgEvent deleteMsgEvent = new DeleteMsgEvent();
        deleteMsgEvent.setChatId(mChatId);
        deleteMsgEvent.setMsgIds(msgIds);
        uiEventHandleFacade.handle(deleteMsgEvent);
    }


}
