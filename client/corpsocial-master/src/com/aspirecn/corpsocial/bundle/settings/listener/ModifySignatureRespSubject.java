package com.aspirecn.corpsocial.bundle.settings.listener;

import com.aspirecn.corpsocial.bundle.settings.event.ModifySignatureRespEvent;
import com.aspirecn.corpsocial.bundle.settings.listener.ModifySignatureRespSubject.ModifySignatureRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class ModifySignatureRespSubject
        extends
        EventSubject<ModifySignatureRespEventListener, ModifySignatureRespEvent> {

    @Override
    public void notifyListener(ModifySignatureRespEvent event) {
        for (ModifySignatureRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleModifySignatureRespEvent(event);
        }
    }

    public interface ModifySignatureRespEventListener extends EventListener {
        void onHandleModifySignatureRespEvent(
                ModifySignatureRespEvent event);
    }
}
