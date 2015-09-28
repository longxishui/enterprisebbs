package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by yinghuihong on 15/3/31.
 */
public class ConnectEvent extends BusEvent {

    /**
     * 验证码
     */
    private String veritifyCode;

    public String getVeritifyCode() {
        return veritifyCode;
    }

    public void setVeritifyCode(String veritifyCode) {
        this.veritifyCode = veritifyCode;
    }
}
