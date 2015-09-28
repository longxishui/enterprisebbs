package com.aspirecn.corpsocial.bundle.settings.uihandler;

import com.aspirecn.corpsocial.bundle.settings.event.ModifySignatureEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

import java.io.IOException;

/**
 * 修改个性签名
 *
 * @author wangdeng
 */
@UIEventHandler(eventType = ModifySignatureEvent.class)
public class ModifySignatureEventHandler implements
        IHandler<Null, ModifySignatureEvent> {


    private EventListenerSubjectLoader eventListenSubject = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(final ModifySignatureEvent args) {

        return new Null();
    }

}
