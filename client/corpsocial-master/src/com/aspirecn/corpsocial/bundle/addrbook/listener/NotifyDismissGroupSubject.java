package com.aspirecn.corpsocial.bundle.addrbook.listener;


import com.aspirecn.corpsocial.bundle.addrbook.event.NotifyDismissGroupEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.NotifyDismissGroupSubject.NotifyDismissGroupEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class NotifyDismissGroupSubject extends
        EventSubject<NotifyDismissGroupEventListener, NotifyDismissGroupEvent> {

    @Override
    public void notifyListener(NotifyDismissGroupEvent event) {
        for (NotifyDismissGroupEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleDismissGroupEvent(event);
        }
    }

    public interface NotifyDismissGroupEventListener extends EventListener {
        void onHandleDismissGroupEvent(NotifyDismissGroupEvent event);
    }
}
