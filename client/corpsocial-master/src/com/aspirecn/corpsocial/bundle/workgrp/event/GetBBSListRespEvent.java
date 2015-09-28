package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.ArrayList;

public class GetBBSListRespEvent extends BusEvent {
    /**
     * 错误码
     */
    private int errCode;
    /**
     * 返回的帖子的集合
     */
    private ArrayList<BBSItem> bbsItems;
    /**
     * 帖子的模块栏目id
     */
    private String groupId;

    public GetBBSListRespEvent() {
        super();
    }

    public GetBBSListRespEvent(int errCode, ArrayList<BBSItem> bbsItems,
                               String groupId) {
        super();
        this.errCode = errCode;
        this.bbsItems = bbsItems;
        this.groupId = groupId;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public ArrayList<BBSItem> getBbsItems() {
        return bbsItems;
    }

    public void setBbsItems(ArrayList<BBSItem> bbsItems) {
        this.bbsItems = bbsItems;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "GetBBSListRespEvent [errCode=" + errCode + ", bbsItems="
                + bbsItems + ", groupId=" + groupId + "]";
    }

}
