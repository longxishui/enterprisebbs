package com.aspirecn.corpsocial.bundle.settings.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by chenziqiang on 15-3-24.
 */
public class DownloadAdcolumnRespEvent extends BusEvent {
    private int errorCode;

    public int getError() {
        return errorCode;
    }

    public void setError(int errorCode) {
        this.errorCode = errorCode;
    }
}
