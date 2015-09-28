package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.ClearChatMsgEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 清空会话消息处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = ClearChatMsgEvent.class)
public class ClearChatMsgEventHandler implements IHandler<Null, ClearChatMsgEvent> {

    @Autowired
    private MsgDao msgDao = new MsgDao();

    private ChatDao chatDao = new ChatDao();

    @Override
    public Null handle(ClearChatMsgEvent busEvent) {

        String chatId = busEvent.getChatId();
        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("chatId", chatId);
        wheres.put("belongUserId", Config.getInstance().getUserId());
        List<MsgEntity> deleteMsgs = msgDao.findAllByWhere(wheres, "");

        for (MsgEntity msgEntity : deleteMsgs) {
            int contentType = msgEntity.getContentType();
            //图片，语音，位置消息，删除对应文件信息
            if (MsgEntity.MsgContentType.PICTURE == contentType) {
                String content = msgEntity.getContent();
                if (content.startsWith(Constant.ROOT_PATH)) {
                    File file = new File(content);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } else if (MsgEntity.MsgContentType.VOICE == contentType) {
                String content = msgEntity.getContent();
                File file = new File(content);
                if (file.exists()) {
                    file.delete();
                }
            } else if (MsgEntity.MsgContentType.POSITION == contentType) {
                String nativeUrl = null;
                try {
                    nativeUrl = new JSONObject(msgEntity.getExtraInfo())
                            .optString("nativeUrl");
                    File file = new File(nativeUrl);
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (JSONException e) {
                    LogUtil.e("删除位置信息错误", e);
                }
            }
        }

        // 删除会话消息
        msgDao.detele(deleteMsgs);

        //更新会话未读消息条数
        ChatEntity chat = chatDao.findByWhere(wheres);
        if (chat != null) {
            chat.setNoticeCount(0);
            chatDao.update(chat);
        }

        return new Null();
    }

}
