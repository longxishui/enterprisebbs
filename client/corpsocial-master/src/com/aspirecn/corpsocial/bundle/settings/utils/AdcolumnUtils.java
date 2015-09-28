package com.aspirecn.corpsocial.bundle.settings.utils;

import com.aspirecn.corpsocial.bundle.settings.event.DownloadAdcolumnRespEvent;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;

/**
 * Created by chenziqiang on 15-3-24.
 */
public class AdcolumnUtils {
    public static String GET_ADCOLUMN_LIST = "GET_ADCOLUMN_LIST";

    public static String ADCOLUMNID = "ADCOLUMNID";

    public static long getAdcolumnLastTime() {
        return ConfigPersonal.getInstance().getAdcolumnLastTime();
    }

    public static void setAdcolumnLastTime(long lastTime) {
        ConfigPersonal.getInstance().setAdcolumnLastTime(lastTime);
    }

    public static void notiy(int errorCode) {
        EventListenerSubjectLoader eventListenSubject = EventListenerSubjectLoader
                .getInstance();
        DownloadAdcolumnRespEvent event = new DownloadAdcolumnRespEvent();
        event.setError(errorCode);
        eventListenSubject.notifyListener(event);
    }
}
