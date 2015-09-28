package com.aspirecn.corpsocial.bundle.settings.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by chenziqiang on 15-3-24.
 */
public class GetAdcolumnDetailEvent extends BusEvent {
    private String adcolumnId;

    public String getAdcolumnId() {
        return adcolumnId;
    }

    public void setAdcolumnId(String adcolumnId) {
        this.adcolumnId = adcolumnId;
    }
}
