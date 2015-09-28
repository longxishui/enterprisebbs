package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by Amos on 15-6-25.
 */
public class FindFriendEvent extends BusEvent {

    private String key;
    //initialKey
    private String sort;

    public FindFriendEvent() {
    }

    public FindFriendEvent(String key) {
        this.key = key;
    }

    public FindFriendEvent(String key, String sort) {
        this.key = key;
        this.sort = sort;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
