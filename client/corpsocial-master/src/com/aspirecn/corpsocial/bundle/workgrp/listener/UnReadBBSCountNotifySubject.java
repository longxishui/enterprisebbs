package com.aspirecn.corpsocial.bundle.workgrp.listener;

import com.aspirecn.corpsocial.bundle.workgrp.event.UnReadBBSCountNotifyEvent;
import com.aspirecn.corpsocial.bundle.workgrp.listener.UnReadBBSCountNotifySubject.UnReadBBSCountNotifyEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class UnReadBBSCountNotifySubject extends EventSubject<UnReadBBSCountNotifyEventListener, UnReadBBSCountNotifyEvent> {

    @Override
    public void notifyListener(UnReadBBSCountNotifyEvent event) {
        for (UnReadBBSCountNotifyEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleUnReadBBSCountNotifyEvent(event);
        }
    }

    public interface UnReadBBSCountNotifyEventListener extends EventListener {
        void onHandleUnReadBBSCountNotifyEvent(UnReadBBSCountNotifyEvent event);
    }
}
