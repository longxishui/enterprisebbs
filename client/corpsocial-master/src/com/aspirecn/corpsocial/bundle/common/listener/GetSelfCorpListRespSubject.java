package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.GetSelfCorpListRespEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by Amos on 15-6-17.
 */
@EventBusAnnotation.EventListenerSubject
public class GetSelfCorpListRespSubject extends EventSubject<GetSelfCorpListRespSubject.GetSelfCorpListRespListener, GetSelfCorpListRespEvent> {
    @Override
    public void notifyListener(GetSelfCorpListRespEvent event) {
        for (GetSelfCorpListRespListener eventListener : eventListenerQueue) {
            eventListener.onGetSelfCorpListRespEvent(event);
        }
    }

    public interface GetSelfCorpListRespListener extends EventListener {
        void onGetSelfCorpListRespEvent(GetSelfCorpListRespEvent event);
    }
}
