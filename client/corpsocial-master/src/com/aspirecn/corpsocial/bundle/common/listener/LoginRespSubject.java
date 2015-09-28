package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.LoginRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.LoginRespSubject.LoginRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class LoginRespSubject extends
        EventSubject<LoginRespEventListener, LoginRespEvent> {

    @Override
    public void notifyListener(LoginRespEvent event) {
        for (LoginRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleLoginRespEvent(event);
        }
    }

    public interface LoginRespEventListener extends EventListener {
        void onHandleLoginRespEvent(LoginRespEvent event);
    }
}
