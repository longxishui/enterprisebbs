package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.GetVisitAccountRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.GetVisitAccountRespSubject.GetVisitAccountRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class GetVisitAccountRespSubject extends
        EventSubject<GetVisitAccountRespEventListener, GetVisitAccountRespEvent> {

    @Override
    public void notifyListener(GetVisitAccountRespEvent event) {
        for (GetVisitAccountRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleGetVisitAccountRespEvent(event);
        }
    }

    public interface GetVisitAccountRespEventListener extends EventListener {
        void onHandleGetVisitAccountRespEvent(GetVisitAccountRespEvent event);
    }
}
