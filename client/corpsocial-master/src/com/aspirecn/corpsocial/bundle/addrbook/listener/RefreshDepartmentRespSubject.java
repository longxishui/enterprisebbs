package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.RefreshDepartmentRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RefreshDepartmentRespSubject.RefreshDepartmentRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class RefreshDepartmentRespSubject
        extends
        EventSubject<RefreshDepartmentRespEventListener, RefreshDepartmentRespEvent> {

    @Override
    public void notifyListener(RefreshDepartmentRespEvent event) {
        for (RefreshDepartmentRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleRefreshDepartmentRespEvent(event);
        }
    }

    public interface RefreshDepartmentRespEventListener extends EventListener {
        void onHandleRefreshDepartmentRespEvent(
                RefreshDepartmentRespEvent event);
    }
}
