package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.common.uitils.UserUtil;
import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.event.LoadChatEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.GroupDao;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity.ChatType;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity.LimitType;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupMemberEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 加载单个本地对话处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = LoadChatEvent.class)
public class LoadChatEventHandler implements IHandler<Chat, LoadChatEvent> {

    @Autowired
    private ChatDao chatDao = new ChatDao();

    @Autowired
    private MsgDao msgDao = new MsgDao();

    private GroupDao groupDao = new GroupDao();

    @Override
    public Chat handle(LoadChatEvent busEvent) {

        Chat chat = new Chat();

        ChatEntity chatEntity = busEvent.getChatEntity();
        String chatId = chatEntity.getChatId();
        Map<String, Object> wheres = new HashMap<String, Object>();
        //wheres.put("corpId", chatEntity.getCorpId());
        wheres.put("chatId", chatId);
        wheres.put("belongUserId", Config.getInstance().getUserId());
        ChatEntity findByWhere = chatDao.findByWhere(wheres);

        if (findByWhere == null) {// 会话不存在
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
            ChatEntity insertChatEntity = chatDao.insert(chatEntity);
            chat.setChatEntity(insertChatEntity);

            // 有可能存在属于此会话的消息
            Map<String, Object> msgWheres = new HashMap<String, Object>();
            //msgWheres.put("corpId", chatEntity.getCorpId());
            msgWheres.put("chatId", chatId);
            msgWheres.put("belongUserId", Config.getInstance().getUserId());
            List<MsgEntity> msgs = msgDao.findAllByWhereAndIndex(msgWheres,
                    busEvent.getIndex(), busEvent.getCount(), "createTime DESC");
            LinkedList<MsgEntity> msgList = new LinkedList<MsgEntity>();
            for (MsgEntity msg : msgs) {
                msgList.addFirst(msg);
            }

            chat.setMsgList(msgList);
        } else {
            Map<String, Object> msgWheres = new HashMap<String, Object>();
            //msgWheres.put("corpId", chatEntity.getCorpId());
            msgWheres.put("chatId", chatId);
            msgWheres.put("belongUserId", Config.getInstance().getUserId());
            List<MsgEntity> msgs = msgDao.findAllByWhereAndIndex(msgWheres,
                    busEvent.getIndex(), busEvent.getCount(), "createTime DESC");
            LinkedList<MsgEntity> msgList = new LinkedList<MsgEntity>();
            for (MsgEntity msg : msgs) {
                msgList.addFirst(msg);
            }
            chat.setChatEntity(findByWhere);

            chat.setMsgList(msgList);
        }

        int chatType = chatEntity.getChatType();
        boolean isSpeaker = false;
        if (ChatType.GROUP.value == chatType) {
            Map<String, Object> where = new HashMap<String, Object>();
            //where.put("corpId", chatEntity.getCorpId());
            where.put("groupId", chatId);
            String userId = Config.getInstance().getUserId();
            where.put("belongUserId", userId);
            GroupEntity groupEntity = groupDao.findByWhere(where);

            if (groupEntity != null) {
                if (LimitType.Unlimit.equals(groupEntity.getLimitType())) {
                    isSpeaker = true;
                } else if (LimitType.Limit.equals(groupEntity.getLimitType())) {
                    List<String> speakList = groupEntity.getSpeekList();
                    isSpeaker = speakList.contains(userId);
                }

                List<String> adminList = groupEntity.getAdminList();
                if (adminList.size() > 0) {
                    String string = adminList.get(0);
                    User contact = UserUtil.getUser(string);

                    if (contact != null) {
                        GroupMemberEntity admin = new GroupMemberEntity();
                        admin.setMemberId(contact.getUserid());
                        admin.setMemberName(contact.getName());
                        chat.setApplySpeekAdmin(admin);
                    }
                }
            }
        } else if (ChatType.P2P.value == chatType) {
            isSpeaker = true;
        }
        chat.setSpeeker(isSpeaker);

        return chat;
    }

}
