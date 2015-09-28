package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.common.event.UpdateThemeEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

/**
 * Created by hongyinghui on 15/4/13.
 */
@EventBusAnnotation.UIEventHandler(eventType = UpdateThemeEvent.class)
public class UpdateThemeEventHandler implements IHandler<Null, UpdateThemeEvent> {


    @Override
    public Null handle(UpdateThemeEvent args) {
        //TODO request config from server
        //TODO save the config json data into Config.java
        //TODO download the logo (这里可变动，将图片下载放到UI层去), after the logo had downloaded
        //TODO notify the RefreshThemeListener


        //TODO IN UI
        //TODO fetch the config json data and convert to object by GSon.jar
        //TODO change the properties of Views in java code

        return null;
    }
}
