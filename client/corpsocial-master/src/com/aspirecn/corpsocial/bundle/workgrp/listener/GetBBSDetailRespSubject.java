package com.aspirecn.corpsocial.bundle.workgrp.listener;

import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSDetailRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.listener.GetBBSDetailRespSubject.GetBBSDetailRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class GetBBSDetailRespSubject extends EventSubject<GetBBSDetailRespEventListener, GetBBSDetailRespEvent> {

    @Override
    public void notifyListener(GetBBSDetailRespEvent event) {
        for (GetBBSDetailRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleGetBBSDetailRespEvent(event);
        }
    }

    public interface GetBBSDetailRespEventListener extends EventListener {
        void onHandleGetBBSDetailRespEvent(GetBBSDetailRespEvent event);
    }
}
