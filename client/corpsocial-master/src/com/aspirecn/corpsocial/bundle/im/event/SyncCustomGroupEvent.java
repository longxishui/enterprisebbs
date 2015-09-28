package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

public class SyncCustomGroupEvent extends BusEvent {

    private String corpId;

    public SyncCustomGroupEvent() {
    }

    public SyncCustomGroupEvent(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
}
