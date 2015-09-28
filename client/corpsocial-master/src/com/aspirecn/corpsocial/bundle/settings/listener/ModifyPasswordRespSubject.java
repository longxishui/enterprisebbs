package com.aspirecn.corpsocial.bundle.settings.listener;

import com.aspirecn.corpsocial.bundle.settings.event.ModifyPasswordRespEvent;
import com.aspirecn.corpsocial.bundle.settings.listener.ModifyPasswordRespSubject.ModifyPasswordRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class ModifyPasswordRespSubject extends
        EventSubject<ModifyPasswordRespEventListener, ModifyPasswordRespEvent> {

    @Override
    public void notifyListener(ModifyPasswordRespEvent event) {
        for (ModifyPasswordRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleModifyPasswordRespEvent(event);
        }
    }

    public interface ModifyPasswordRespEventListener extends EventListener {
        void onHandleModifyPasswordRespEvent(ModifyPasswordRespEvent event);
    }
}
