package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 踢出群成员反馈事件
 *
 * @author lizhuo_a
 */
public class KickOutGroupMemberRespEvent extends BusEvent {

    /**
     * 返回码
     */
    private int errorCode;

    /**
     * 错误描述
     */
    private String errorInfo;

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
