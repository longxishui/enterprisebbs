package com.aspirecn.corpsocial.bundle.workgrp.listener;

import com.aspirecn.corpsocial.bundle.workgrp.event.CreateOrModifyBBSRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.listener.CreateOrModifyBBSRespSubject.CreateOrModifyBBSRespListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class CreateOrModifyBBSRespSubject extends EventSubject<CreateOrModifyBBSRespListener, CreateOrModifyBBSRespEvent> {

    @Override
    public void notifyListener(CreateOrModifyBBSRespEvent event) {
        for (CreateOrModifyBBSRespListener eventListener : eventListenerQueue) {
            eventListener.onHandleCreateOrModifyBBSRespEvent(event);
        }
    }

    public interface CreateOrModifyBBSRespListener extends EventListener {
        void onHandleCreateOrModifyBBSRespEvent(CreateOrModifyBBSRespEvent createOrModifyBBSRespEvent);
    }

}
