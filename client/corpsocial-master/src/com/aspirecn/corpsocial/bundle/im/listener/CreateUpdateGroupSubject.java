package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupEvent;
import com.aspirecn.corpsocial.bundle.im.listener.CreateUpdateGroupSubject.CreateUpdateGroupEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class CreateUpdateGroupSubject extends
        EventSubject<CreateUpdateGroupEventListener, CreateUpdateGroupEvent> {

    @Override
    public void notifyListener(CreateUpdateGroupEvent event) {
        for (CreateUpdateGroupEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleCreateUpdateGroupEvent(event);
        }
    }

    public interface CreateUpdateGroupEventListener extends EventListener {
        void onHandleCreateUpdateGroupEvent(CreateUpdateGroupEvent event);
    }
}
