package com.aspirecn.corpsocial.bundle.workgrp.listener;

import com.aspirecn.corpsocial.bundle.workgrp.event.BBSDeleteRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.listener.BBSDeleteRespSubject.BBSDeleteRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class BBSDeleteRespSubject extends EventSubject<BBSDeleteRespEventListener, BBSDeleteRespEvent> {

    @Override
    public void notifyListener(BBSDeleteRespEvent event) {
        for (BBSDeleteRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleBBSDeleteRespEvent(event);
        }
    }

    public interface BBSDeleteRespEventListener extends EventListener {
        void onHandleBBSDeleteRespEvent(BBSDeleteRespEvent bbsDeleteRespEvent);
    }
}
