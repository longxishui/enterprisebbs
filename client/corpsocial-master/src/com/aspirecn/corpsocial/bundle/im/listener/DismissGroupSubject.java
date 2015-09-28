package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.DismissGroupEvent;
import com.aspirecn.corpsocial.bundle.im.listener.DismissGroupSubject.DismissGroupEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class DismissGroupSubject extends
        EventSubject<DismissGroupEventListener, DismissGroupEvent> {

    @Override
    public void notifyListener(DismissGroupEvent event) {
        for (DismissGroupEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleDismissGroupEvent(event);
        }
    }

    public interface DismissGroupEventListener extends EventListener {
        void onHandleDismissGroupEvent(DismissGroupEvent event);
    }
}
