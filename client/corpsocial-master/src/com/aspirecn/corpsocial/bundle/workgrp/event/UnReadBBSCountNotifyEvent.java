package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

public class UnReadBBSCountNotifyEvent extends BusEvent {

    private int unReadCount;

    public UnReadBBSCountNotifyEvent() {
        super();
    }

    public UnReadBBSCountNotifyEvent(int unReadCount) {
        super();
        this.unReadCount = unReadCount;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    @Override
    public String toString() {
        return "UnReadBBSCountNotifyEvent [unReadCount=" + unReadCount + "]";
    }

}
