package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.DeleteMsgEvent;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

import java.util.List;

/**
 * 删除会话消息处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = DeleteMsgEvent.class)
public class DeleteMsgEventHandler implements IHandler<Null, DeleteMsgEvent> {

    @Autowired
    private MsgDao msgDao = new MsgDao();

    @Override
    public Null handle(DeleteMsgEvent busEvent) {
        String chatId = busEvent.getChatId();
        List<String> msgIds = busEvent.getMsgIds();

        msgDao.deleteMsgs(chatId, msgIds);

        return new Null();
    }
}
