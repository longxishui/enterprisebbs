package com.aspirecn.corpsocial.bundle.im.uihandler;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.common.uitils.UserUtil;
import com.aspirecn.corpsocial.bundle.im.event.SendMessageEvent;
import com.aspirecn.corpsocial.bundle.im.event.SendMessageRespEvent;
import com.aspirecn.corpsocial.bundle.im.facade.AddSystemMsgService;
import com.aspirecn.corpsocial.bundle.im.facade.SystemMessage;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity.MsgStatus;
import com.aspirecn.corpsocial.bundle.im.utils.ContentType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.CommandData;
import com.aspirecn.corpsocial.common.eventbus.CommandHead;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.util.BitmapUtil;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送消息事件处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = SendMessageEvent.class)
public class SendMessageEventHandler implements IHandler<Null, SendMessageEvent> {

//    @Autowired
//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    @Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader.getInstance();

    @Autowired
    private MsgDao msgDao = new MsgDao();

    private ChatDao chatDao = new ChatDao();

    @Override
    public Null handle(SendMessageEvent busEvent) {
        boolean canCommunicate = true;
        String friendName = "他（她）";
        MsgEntity msgEntity = busEvent.getMsgEntity();

        //是否可发消息(点对点)
        if (!busEvent.isGroupMsg()) {
            User user = UserUtil.getUser(msgEntity.getChatId());
            if (user != null) {
                canCommunicate = user.canCommunicate();
                if (!TextUtils.isEmpty(user.getName())) {
                    friendName = user.getName();
                }
            }
        }

        MsgEntity dbMsgEntity = msgDao.findByWhere("msgId", msgEntity.getMsgId());
        // 第一次发送消息
        if (dbMsgEntity == null) {
            String ownedUserId = msgEntity.getOwnedUserId();
            User contact = UserUtil.getUser(ownedUserId);
            msgEntity.setOwnedUserName(contact == null ? "" : contact.getName());

        } else {// 重发信息
            msgDao.detele(dbMsgEntity);
        }

        if (!canCommunicate) {
            msgEntity.setStatus(MsgStatus.Fail.value);
        }

        // 将信息保存到本地数据库
        dbMsgEntity = msgDao.insert(msgEntity);

        // 更新会话信息
        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("chatId", dbMsgEntity.getChatId());
        wheres.put("belongUserId", Config.getInstance().getUserId());
        ChatEntity chatEntity = chatDao.findByWhere(wheres);
        chatEntity.setLastUpdateTime(new Date(System.currentTimeMillis()));
        chatEntity.setDisplay(true);
        chatDao.update(chatEntity);

        //发消息
        if (canCommunicate) {
            int contentType = dbMsgEntity.getContentType();
            if (contentType == ContentType.TEXT.getValue()) {
                sendTextMsg(busEvent);
            }
        } else {
            //通知界面
            SendMessageRespEvent sendMessageRespEvent = new SendMessageRespEvent();
            sendMessageRespEvent.setErrorCode(0);
            sendMessageRespEvent.setErrorMsg("");
            sendMessageRespEvent.setMsgId(msgEntity.getMsgId());
            eventListener.notifyListener(sendMessageRespEvent);

            //发送非好友系统提示消息
            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setSenderId(msgEntity.getChatId());
            systemMessage.setContent(getContent(friendName));
            OsgiServiceLoader.getInstance().getService(AddSystemMsgService.class).invoke(systemMessage);
        }
        return new Null();
    }

    private String getContent(String name) {
        StringBuffer contentSb = new StringBuffer();
        contentSb.append("您不在" + name + "的好友列表中，请先发送添加好友请求，等对方接受您的请求后，才能聊天。");
        contentSb.append("<add_friend>添加好友</add_friend>");
        return contentSb.toString();
    }

    /**
     * 处理文本消息
     *
     * @param busEvent
     */
    private void sendTextMsg(final SendMessageEvent busEvent) {
        final MsgEntity msgEntity = busEvent.getMsgEntity();

//        CommandData commandData = new CommandData();
//
//        CommandHead.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandData.CommandType.DIALOG_MESSAGE);
//        headBuilder.sendTime(System.currentTimeMillis());
//        headBuilder.compressType(CompressType.UN_COMPRESS);
//        headBuilder.encryptType(EncryptType.UNENCRPT);
//        headBuilder.messageId(msgEntity.getMsgId());
//        headBuilder.userId(Config.getInstance().getUserId());
//        headBuilder.corpId(Config.getInstance().getCorpId());
//        headBuilder.targetId(msgEntity.getChatId());
//        headBuilder.contentType(ContentType.TEXT);
//        headBuilder.groupMsg(busEvent.isGroupMsg());
//        headBuilder.targetId(msgEntity.getChatId());
//        CommandHeader header = headBuilder.build();
//
//        DialogMessage.Builder bodyBuilder = new DialogMessage.Builder();
//        bodyBuilder.content(ByteString.of(msgEntity.getContent().getBytes()));
//
//        commandData.setCommandHeader(header);
//        commandData.setData(bodyBuilder.build().toByteArray());

//        LogUtil.i(String.format("发送文本消息  %s", commandData.toString()));
    }



}
