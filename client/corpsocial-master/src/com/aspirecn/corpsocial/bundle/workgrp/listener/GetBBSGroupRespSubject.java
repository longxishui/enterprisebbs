package com.aspirecn.corpsocial.bundle.workgrp.listener;

import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSGroupRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.listener.GetBBSGroupRespSubject.GetBBSGroupRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class GetBBSGroupRespSubject extends EventSubject<GetBBSGroupRespEventListener, GetBBSGroupRespEvent> {

    @Override
    public void notifyListener(GetBBSGroupRespEvent event) {
        for (GetBBSGroupRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleGetBBSGroupRespEvent(event);
        }
    }

    public interface GetBBSGroupRespEventListener extends EventListener {
        void onHandleGetBBSGroupRespEvent(GetBBSGroupRespEvent getBBSGroupRespEvent);
    }
}
