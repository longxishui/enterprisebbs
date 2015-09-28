package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.FindContactBatchRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.FindContactBatchRespSubject.FindContactBatchRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by yinghuihong on 15/7/15.
 */
@EventBusAnnotation.EventListenerSubject
public class FindContactBatchRespSubject extends EventSubject<FindContactBatchRespEventListener, FindContactBatchRespEvent> {

    @Override
    public void notifyListener(FindContactBatchRespEvent event) {

        for (FindContactBatchRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleFindContactRespEvent(event);
        }
    }

    public interface FindContactBatchRespEventListener extends EventListener {
        void onHandleFindContactRespEvent(FindContactBatchRespEvent event);
    }
}
