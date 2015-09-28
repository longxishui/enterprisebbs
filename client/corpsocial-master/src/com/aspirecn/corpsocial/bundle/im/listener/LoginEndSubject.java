package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.LoginEndEvent;
import com.aspirecn.corpsocial.bundle.im.listener.LoginEndSubject.LoginEndEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class LoginEndSubject extends
        EventSubject<LoginEndEventListener, LoginEndEvent> {

    @Override
    public void notifyListener(LoginEndEvent event) {
        for (LoginEndEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleLoginEndEvent(event);
        }
    }

    public interface LoginEndEventListener extends EventListener {
        void onHandleLoginEndEvent(LoginEndEvent event);
    }
}
