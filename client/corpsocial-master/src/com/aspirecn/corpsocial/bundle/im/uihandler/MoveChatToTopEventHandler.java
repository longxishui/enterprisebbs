package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.MoveChatToTopEvent;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息置顶处理类
 *
 * @author lihaiqiang
 */
@UIEventHandler(eventType = MoveChatToTopEvent.class)
public class MoveChatToTopEventHandler implements
        IHandler<Null, MoveChatToTopEvent> {

    @Autowired
    private ChatDao chatDao = new ChatDao();

    @Autowired
    private MsgDao msgDao = new MsgDao();

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(MoveChatToTopEvent busEvent) {

        String chatId = busEvent.getChatId();
        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("chatId", chatId);
        wheres.put("belongUserId", Config.getInstance().getUserId());

        // 更新最后更新时间
        ChatEntity chatEntity = chatDao.findByWhere(wheres);
        if (chatEntity != null) {
            chatEntity.setMoveToTopTime(new Date(System.currentTimeMillis()));
            chatDao.update(chatEntity);
        }

        eventListener.notifyListener(new RefreshImMainTabEvent());

        return new Null();

    }

}
