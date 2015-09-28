package com.aspirecn.corpsocial.bundle.workgrp.listener;

import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSListRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.listener.GetBBSListRespSubject.GetBBSListRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class GetBBSListRespSubject extends EventSubject<GetBBSListRespEventListener, GetBBSListRespEvent> {

    @Override
    public void notifyListener(GetBBSListRespEvent event) {
        for (GetBBSListRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleGetBBSListRespEvent(event);
        }
    }

    public interface GetBBSListRespEventListener extends EventListener {
        void onHandleGetBBSListRespEvent(GetBBSListRespEvent event);
    }
}
