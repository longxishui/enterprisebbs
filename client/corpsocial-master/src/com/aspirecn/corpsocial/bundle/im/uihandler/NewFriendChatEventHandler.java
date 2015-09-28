package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.event.NewFriendChatEvent;
import com.aspirecn.corpsocial.bundle.im.event.RefreshImMainTabEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Amos on 15-6-26.
 */
@EventBusAnnotation.UIEventHandler(eventType = NewFriendChatEvent.class)
public class NewFriendChatEventHandler implements
        IHandler<Null, NewFriendChatEvent> {

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @EventBusAnnotation.Autowired
    private ChatDao chatDao = new ChatDao();

    @EventBusAnnotation.Autowired
    private MsgDao msgDao = new MsgDao();

    @Override
    public Null handle(NewFriendChatEvent busEvent) {

        String userid = busEvent.getUserid();

        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("chatId", userid);
        wheres.put("belongUserId", Config.getInstance().getUserId());
        ChatEntity chatEntity = chatDao.findByWhere(wheres);
        Chat chat = new Chat();
        if (chatEntity == null) {
            chatEntity = new ChatEntity();
        }
        chatEntity.setCreateTime(new Date(System.currentTimeMillis()));
        chatEntity.setBelongUserId(Config.getInstance().getUserId());
        chatEntity.setNoticeCount(0);
        chatEntity.setDisplay(false);
        JSONObject chatSetting = new JSONObject();
        try {
            // 默认接收新消息
            chatSetting.put("newMsgNotify", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        chatEntity.setSetting(chatSetting.toString());
        if (chatEntity.getSeqNo() == 0) {
            ChatEntity insertChatEntity = chatDao.insert(chatEntity);
            chat.setChatEntity(insertChatEntity);
        } else {
            ChatEntity updateChatEntity = chatDao.update(chatEntity);
            chat.setChatEntity(updateChatEntity);
        }


        // 有可能存在属于此会话的消息
        Map<String, Object> msgWheres = new HashMap<String, Object>();
        msgWheres.put("chatId", userid);
        msgWheres.put("belongUserId", Config.getInstance().getUserId());
        List<MsgEntity> msgs = msgDao.findAllByWhereAndIndex(msgWheres,
                0, 30, "createTime DESC");
        LinkedList<MsgEntity> msgList = new LinkedList<MsgEntity>();
        for (MsgEntity msg : msgs) {
            msgList.addFirst(msg);
        }

        chat.setMsgList(msgList);
        chat.setSpeeker(true);
        eventListener.notifyListener(new RefreshImMainTabEvent());

        return new Null();

    }

}
