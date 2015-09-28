package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.LoginStartEvent;
import com.aspirecn.corpsocial.bundle.im.listener.LoginStartSubject.LoginStartEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class LoginStartSubject extends
        EventSubject<LoginStartEventListener, LoginStartEvent> {

    @Override
    public void notifyListener(LoginStartEvent event) {
        for (LoginStartEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleLoginStartEvent(event);
        }
    }

    public interface LoginStartEventListener extends EventListener {
        void onHandleLoginStartEvent(LoginStartEvent event);
    }
}
