package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.SearchChatMsgEvent;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import java.util.List;

/**
 * @author lizhuo_a
 */
@UIEventHandler(eventType = SearchChatMsgEvent.class)
public class SearchChatMsgEventHandler implements
        IHandler<List<MsgEntity>, SearchChatMsgEvent> {

    private MsgDao msgDao = new MsgDao();

    @Override
    public List<MsgEntity> handle(SearchChatMsgEvent busEvent) {
        String chatId = busEvent.getChatId();

        String userId = Config.getInstance().getUserId();
        String searchKeyWord = busEvent.getSearchKeyWord();

        List<MsgEntity> searchedMsgs = msgDao.searchMsgByKeyword(chatId, userId,
                searchKeyWord, "createTime ASC");

        return searchedMsgs;
    }
}
