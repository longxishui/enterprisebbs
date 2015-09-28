package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.DismissGroupEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

/**
 * 解散群处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = DismissGroupEvent.class)
public class DismissGroupEventHandler implements
        IHandler<Null, DismissGroupEvent> {

    @Override
    public Null handle(final DismissGroupEvent busEvent) {

        return new Null();
    }
}
