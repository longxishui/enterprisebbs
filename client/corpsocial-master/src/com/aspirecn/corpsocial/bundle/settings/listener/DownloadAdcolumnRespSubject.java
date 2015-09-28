package com.aspirecn.corpsocial.bundle.settings.listener;

import com.aspirecn.corpsocial.bundle.settings.event.DownloadAdcolumnRespEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by chenziqiang on 15-3-24.
 */
@EventBusAnnotation.EventListenerSubject
public class DownloadAdcolumnRespSubject extends EventSubject
        <DownloadAdcolumnRespSubject.DownloadAdcolumnRespEventListener, DownloadAdcolumnRespEvent> {

    @Override
    public void notifyListener(DownloadAdcolumnRespEvent event) {
        for (DownloadAdcolumnRespEventListener eventListener : eventListenerQueue) {
            eventListener.onDownloadAdcolumnRespEventListener(event);
        }
    }


    public interface DownloadAdcolumnRespEventListener extends EventListener {
        void onDownloadAdcolumnRespEventListener(DownloadAdcolumnRespEvent event);
    }
}
