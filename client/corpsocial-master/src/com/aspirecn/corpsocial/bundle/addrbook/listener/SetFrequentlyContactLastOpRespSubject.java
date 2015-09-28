package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.SetFrequentlyContactLastOpRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.SetFrequentlyContactLastOpRespSubject.SetFrequentlyContactLastOpRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class SetFrequentlyContactLastOpRespSubject
        extends
        EventSubject<SetFrequentlyContactLastOpRespEventListener, SetFrequentlyContactLastOpRespEvent> {

    @Override
    public void notifyListener(SetFrequentlyContactLastOpRespEvent event) {
        for (SetFrequentlyContactLastOpRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleSetLastOpEvent(event);
        }
    }

    public interface SetFrequentlyContactLastOpRespEventListener extends EventListener {
        void onHandleSetLastOpEvent(
                SetFrequentlyContactLastOpRespEvent event);
    }
}
