package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.CreateUpdateGroupRespSubject.CreateUpdateGroupRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class CreateUpdateGroupRespSubject extends
        EventSubject<CreateUpdateGroupRespEventListener, CreateUpdateGroupRespEvent> {

    @Override
    public void notifyListener(CreateUpdateGroupRespEvent event) {
        for (CreateUpdateGroupRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleCreateUpdateGroupRespEvent(event);
        }
    }

    public interface CreateUpdateGroupRespEventListener extends EventListener {
        void onHandleCreateUpdateGroupRespEvent(CreateUpdateGroupRespEvent event);
    }
}
