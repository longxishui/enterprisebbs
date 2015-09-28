package com.aspirecn.corpsocial.bundle.settings.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by chenziqiang on 15-3-24.
 */
public class GetAdcolumnListEvent extends BusEvent {
    private int index;
    private int count;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
