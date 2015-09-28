/**
 * @(#) ImReceiveEvent.java Created on 2013-11-11
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 单条IM 消息
 * <p/>
 * The class <code>ImReceiveEvent</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class SendMessageRespEvent extends BusEvent {

    private int errorCode;

    private String errorMsg;

    private float percent;

    private String msgId;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

}
