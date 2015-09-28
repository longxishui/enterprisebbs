package com.aspirecn.corpsocial.bundle.workgrp.listener;

import com.aspirecn.corpsocial.bundle.workgrp.event.BBSReplyRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.listener.BBSReplyRespSubject.BBSReplyRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class BBSReplyRespSubject extends EventSubject<BBSReplyRespEventListener, BBSReplyRespEvent> {

    @Override
    public void notifyListener(BBSReplyRespEvent event) {
        for (BBSReplyRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleBBSReplyRespEvent(event);
        }
    }

    public interface BBSReplyRespEventListener extends EventListener {
        void onHandleBBSReplyRespEvent(BBSReplyRespEvent bbsReplyRespEvent);
    }
}
