package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.DownloadFrequentlyContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.DownloadFrequentlyContactRespSubject.DownloadFrequentlyContactRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class DownloadFrequentlyContactRespSubject
        extends
        EventSubject<DownloadFrequentlyContactRespEventListener, DownloadFrequentlyContactRespEvent> {

    @Override
    public void notifyListener(DownloadFrequentlyContactRespEvent event) {

        for (DownloadFrequentlyContactRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleDownloadFrequentlyContactRespEventListener(event);
        }
    }

    public interface DownloadFrequentlyContactRespEventListener extends EventListener {
        void onHandleDownloadFrequentlyContactRespEventListener(
                DownloadFrequentlyContactRespEvent event);
    }
}
