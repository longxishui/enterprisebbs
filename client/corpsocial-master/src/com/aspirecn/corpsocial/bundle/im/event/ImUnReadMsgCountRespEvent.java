package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by yinghuihong on 15/8/27.
 */
public class ImUnReadMsgCountRespEvent extends BusEvent {
    /**
     * im chat 列表消息未读总数
     */
    private int unReadCount;

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
}
