package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.common.event.RefreshAvatarEvent;
import com.aspirecn.corpsocial.bundle.common.uihandler.RefreshAvatarSubject.RefreshAvatarListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class RefreshAvatarSubject extends EventSubject<RefreshAvatarListener, RefreshAvatarEvent> {

    @Override
    public void notifyListener(RefreshAvatarEvent event) {
        for (RefreshAvatarListener eventListener : eventListenerQueue) {
            eventListener.onHandleRefreshAvatarEvent(event);
        }
    }

    public interface RefreshAvatarListener extends EventListener {
        void onHandleRefreshAvatarEvent(RefreshAvatarEvent event);
    }
}
