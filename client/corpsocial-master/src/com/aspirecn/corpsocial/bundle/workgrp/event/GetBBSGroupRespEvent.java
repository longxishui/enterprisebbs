package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSGroup;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.ArrayList;

public class GetBBSGroupRespEvent extends BusEvent {
    /**
     * 错误码
     */
    private int errcode;
    /**
     * 栏目模块 信息
     */
    private ArrayList<BBSGroup> bbsGroups;
    /**
     * 是否与上次获得有更改（同步时使用）
     */
    private boolean isChange;

    public GetBBSGroupRespEvent() {
        super();
    }

    public GetBBSGroupRespEvent(int errcode, ArrayList<BBSGroup> bbsGroups) {
        super();
        this.errcode = errcode;
        this.bbsGroups = bbsGroups;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public ArrayList<BBSGroup> getBbsGroups() {
        return bbsGroups;
    }

    public void setBbsGroups(ArrayList<BBSGroup> bbsGroups) {
        this.bbsGroups = bbsGroups;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean isChange) {
        this.isChange = isChange;
    }

    @Override
    public String toString() {
        return "GetBBSGroupRespEvent [errcode=" + errcode + ", bbsGroups="
                + bbsGroups + "]";
    }

}
