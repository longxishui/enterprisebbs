package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.CorpChangeRespEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by amos on 15-7-15.
 */
@EventBusAnnotation.EventListenerSubject
public class CorpChangeRespSubject extends EventSubject<CorpChangeRespSubject.CorpChangeRespListener, CorpChangeRespEvent> {

    @Override
    public void notifyListener(CorpChangeRespEvent event) {
        for (CorpChangeRespListener eventListener : eventListenerQueue) {
            eventListener.onCorpChangeRespEvent(event);
        }
    }

    public interface CorpChangeRespListener extends EventListener {
        void onCorpChangeRespEvent(CorpChangeRespEvent event);
    }
}
