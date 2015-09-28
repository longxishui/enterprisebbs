package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by yinghuihong on 15/5/21.
 */
public class GetCorpViewDefEvent extends BusEvent {

    private String corpId;

    public GetCorpViewDefEvent(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpId() {
        return corpId;
    }

    @Override
    public String toString() {
        return "GetCorpViewDefEvent{" +
                "corpId='" + corpId + '\'' +
                '}';
    }
}
