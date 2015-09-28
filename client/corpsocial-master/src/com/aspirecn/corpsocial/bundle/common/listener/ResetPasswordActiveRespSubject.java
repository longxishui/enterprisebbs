package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.ResetPasswordActiveRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.ResetPasswordActiveRespSubject.RegisterActiveRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class ResetPasswordActiveRespSubject extends
        EventSubject<RegisterActiveRespEventListener, ResetPasswordActiveRespEvent> {

    @Override
    public void notifyListener(ResetPasswordActiveRespEvent event) {
        for (RegisterActiveRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleRegisterActiveRespEvent(event);
        }
    }

    public interface RegisterActiveRespEventListener extends EventListener {
        void onHandleRegisterActiveRespEvent(ResetPasswordActiveRespEvent event);
    }
}
