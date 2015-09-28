package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.RegisterActiveRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.RegisterActiveRespSubject.RegisterActiveRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class RegisterActiveRespSubject extends
        EventSubject<RegisterActiveRespEventListener, RegisterActiveRespEvent> {

    @Override
    public void notifyListener(RegisterActiveRespEvent event) {
        for (RegisterActiveRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleRegisterActiveRespEvent(event);
        }
    }

    public interface RegisterActiveRespEventListener extends EventListener {
        void onHandleRegisterActiveRespEvent(RegisterActiveRespEvent event);
    }
}
