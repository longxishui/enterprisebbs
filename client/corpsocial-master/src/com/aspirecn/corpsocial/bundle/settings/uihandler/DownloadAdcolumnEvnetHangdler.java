package com.aspirecn.corpsocial.bundle.settings.uihandler;

import android.util.Log;

import com.aspirecn.corpsocial.bundle.settings.event.DownloadAdcolumnEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;



/**
 * Created by chenziqiang on 15-3-24.
 */
@EventBusAnnotation.UIEventHandler(eventType = DownloadAdcolumnEvent.class)
public class DownloadAdcolumnEvnetHangdler implements IHandler<Null, DownloadAdcolumnEvent> {


    @Override
    public Null handle(DownloadAdcolumnEvent args) {
//        HttpRequest.request(AdcolumnUtils.GET_ADCOLUMN_LIST,
//                args.getData(), this);
        return new Null();
    }

}
