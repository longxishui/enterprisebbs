package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 网络状态变更通知事件
 *
 * @author lizhuo_a
 */
public class NetStatueChangeEvent extends BusEvent {

    /**
     * 网络类型：wifi
     */
    public static final int NETWORK_WIFI = 1;
    /**
     * 网络状态：有或无
     */
    private boolean isNetworkAvaileble;
    /**
     * 网络类型
     */
    private int networkType;
    /**
     * 提示信息
     */
    private String promptMsg;

    public boolean isNetworkAvaileble() {
        return isNetworkAvaileble;
    }

    public void setNetworkAvaileble(boolean isNetworkAvaileble) {
        this.isNetworkAvaileble = isNetworkAvaileble;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public String getPromptMsg() {
        return promptMsg;
    }

    public void setPromptMsg(String promptMsg) {
        this.promptMsg = promptMsg;
    }

}
