package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

public class SyncMessageListEvent extends BusEvent {

    private boolean groupMsg;

    private String targetId;

    private int count;

    private long fromTime;

    public boolean isGroupMsg() {
        return groupMsg;
    }

    public void setGroupMsg(boolean groupMsg) {
        this.groupMsg = groupMsg;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getFromTime() {
        return fromTime;
    }

    public void setFromTime(long fromTime) {
        this.fromTime = fromTime;
    }
}
