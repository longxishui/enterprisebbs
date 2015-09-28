package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.RefreshThemeEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by hongyinghui on 15/4/13.
 */
@EventBusAnnotation.EventListenerSubject
public class RefreshThemeSubject extends EventSubject<RefreshThemeSubject.RefreshThemeEventListener, RefreshThemeEvent> {


    @Override
    public void notifyListener(RefreshThemeEvent event) {
        for (RefreshThemeEventListener eventListener : eventListenerQueue) {
            eventListener.onRefreshThemeEvent(event);
        }
    }

    public interface RefreshThemeEventListener extends EventListener {
        void onRefreshThemeEvent(RefreshThemeEvent event);
    }

}
