package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

public class GetConfigListEvent extends BusEvent {

    private String corpId;

    public GetConfigListEvent() {
    }

    public GetConfigListEvent(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
}
