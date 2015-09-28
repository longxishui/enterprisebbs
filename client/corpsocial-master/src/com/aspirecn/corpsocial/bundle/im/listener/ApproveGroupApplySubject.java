package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.ApproveGroupApplyEvent;
import com.aspirecn.corpsocial.bundle.im.listener.ApproveGroupApplySubject.ApproveGroupApplyEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class ApproveGroupApplySubject extends
        EventSubject<ApproveGroupApplyEventListener, ApproveGroupApplyEvent> {

    @Override
    public void notifyListener(ApproveGroupApplyEvent event) {
        for (ApproveGroupApplyEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleApproveGroupApplyEvent(event);
        }
    }

    public interface ApproveGroupApplyEventListener extends EventListener {
        void onHandleApproveGroupApplyEvent(ApproveGroupApplyEvent event);
    }
}
