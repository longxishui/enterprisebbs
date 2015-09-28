package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.NotifyRefreshMyGroupEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.NotifyRefreshMyGroupSubject.NotifyRefreshMyGroupEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class NotifyRefreshMyGroupSubject extends EventSubject<NotifyRefreshMyGroupEventListener, NotifyRefreshMyGroupEvent> {

    @Override
    public void notifyListener(NotifyRefreshMyGroupEvent event) {
        for (NotifyRefreshMyGroupEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleRefreshMyGroup(event);
        }
    }

    public interface NotifyRefreshMyGroupEventListener extends EventListener {
        void onHandleRefreshMyGroup(NotifyRefreshMyGroupEvent event);
    }

}
