package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetDepartListRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.GetDepartListRespSubject.GetDepartListRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by Amos on 15-6-16.
 */
@EventBusAnnotation.EventListenerSubject
public class GetDepartListRespSubject extends
        EventSubject<GetDepartListRespEventListener, GetDepartListRespEvent> {

    @Override
    public void notifyListener(GetDepartListRespEvent event) {

        for (GetDepartListRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleGetDepartListRespEvent(event);
        }
    }

    public interface GetDepartListRespEventListener extends EventListener {
        void onHandleGetDepartListRespEvent(
                GetDepartListRespEvent event);
    }
}
