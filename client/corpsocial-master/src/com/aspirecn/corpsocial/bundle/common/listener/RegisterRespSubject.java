package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.RegisterRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.RegisterRespSubject.RegisterRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class RegisterRespSubject extends
        EventSubject<RegisterRespEventListener, RegisterRespEvent> {

    @Override
    public void notifyListener(RegisterRespEvent event) {
        for (RegisterRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleRegisterRespEvent(event);
        }
    }

    public interface RegisterRespEventListener extends EventListener {
        void onHandleRegisterRespEvent(RegisterRespEvent event);
    }
}
