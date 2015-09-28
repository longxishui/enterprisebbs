package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.LoginRespNotifyEvent;
import com.aspirecn.corpsocial.bundle.im.listener.LoginRespNotifySubject.LoginRespNotifyEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class LoginRespNotifySubject extends
        EventSubject<LoginRespNotifyEventListener, LoginRespNotifyEvent> {

    @Override
    public void notifyListener(LoginRespNotifyEvent event) {
        for (LoginRespNotifyEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleLoginRespNotifyEvent(event);
        }
    }

    public interface LoginRespNotifyEventListener extends EventListener {
        void onHandleLoginRespNotifyEvent(LoginRespNotifyEvent event);
    }
}
