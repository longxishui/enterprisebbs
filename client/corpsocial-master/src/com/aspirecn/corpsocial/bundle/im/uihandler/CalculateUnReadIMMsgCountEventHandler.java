package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.CalculateImUnReadMsgCountEvent;
import com.aspirecn.corpsocial.bundle.im.event.ImUnReadMsgCountRespEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;


/**
 * Created by yinghuihong on 15/8/27.
 */
@EventBusAnnotation.UIEventHandler(eventType = CalculateImUnReadMsgCountEvent.class)
public class CalculateUnReadIMMsgCountEventHandler implements IHandler<Null, CalculateImUnReadMsgCountEvent> {

    private ChatDao chatDao = new ChatDao();


    @Override
    public Null handle(CalculateImUnReadMsgCountEvent args) {
        int imUnReadCount = chatDao.queryUnReadCount();
        ImUnReadMsgCountRespEvent event = new ImUnReadMsgCountRespEvent();
        event.setUnReadCount(imUnReadCount + 0);
        EventListenerSubjectLoader.getInstance().notifyListener(event);
        return null;
    }
}
