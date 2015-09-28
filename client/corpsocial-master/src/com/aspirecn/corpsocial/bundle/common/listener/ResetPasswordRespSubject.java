package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.ResetPasswordRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.ResetPasswordRespSubject.ResetPasswordRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class ResetPasswordRespSubject extends
        EventSubject<ResetPasswordRespEventListener, ResetPasswordRespEvent> {

    @Override
    public void notifyListener(ResetPasswordRespEvent event) {
        for (ResetPasswordRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleResetPasswordRespEvent(event);
        }
    }

    public interface ResetPasswordRespEventListener extends EventListener {
        void onHandleResetPasswordRespEvent(ResetPasswordRespEvent event);
    }
}
