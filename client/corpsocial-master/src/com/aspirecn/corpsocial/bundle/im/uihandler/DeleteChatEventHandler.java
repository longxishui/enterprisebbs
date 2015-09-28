package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.DeleteChatEvent;
import com.aspirecn.corpsocial.bundle.im.event.RefreshImMainTabEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

import java.util.HashMap;
import java.util.Map;

/**
 * 加载单个本地对话处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = DeleteChatEvent.class)
public class DeleteChatEventHandler implements IHandler<Null, DeleteChatEvent> {

    @Autowired
    private ChatDao chatDao = new ChatDao();

    @Autowired
    private MsgDao msgDao = new MsgDao();

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(DeleteChatEvent busEvent) {

        String chatId = busEvent.getChatId();
        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("chatId", chatId);
        wheres.put("belongUserId", Config.getInstance().getUserId());
//		List<MsgEntity> deleteMsgs = msgDao.findAllByWhere(wheres, "");
//
//		// 删除会话消息
//		msgDao.detele(deleteMsgs);

        // 删除会话
        ChatEntity chatEntity = chatDao.findByWhere(wheres);
        chatDao.detele(chatEntity);

        eventListener.notifyListener(new RefreshImMainTabEvent());

        return new Null();

    }

}
