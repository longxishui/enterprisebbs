package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.FindContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.FindContactRespSubject.FindContactRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by Amos on 15-6-16.
 */
@EventBusAnnotation.EventListenerSubject
public class FindContactRespSubject extends
        EventSubject<FindContactRespEventListener, FindContactRespEvent> {

    @Override
    public void notifyListener(FindContactRespEvent event) {

        for (FindContactRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleFindContactRespEvent(event);
        }
    }

    public interface FindContactRespEventListener extends EventListener {
        void onHandleFindContactRespEvent(
                FindContactRespEvent event);
    }
}
