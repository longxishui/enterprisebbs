package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.bundle.workgrp.util.DeleteType;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

public class BBSDeleteRespEvent extends BusEvent {
    /**
     * 返回的错误码
     */
    private int errorCode;
    /**
     * 删除的类型  删除的是帖子类型为DeleteType.ITEM 删除的是评论类型为DeleteType.REPLY
     */
    private DeleteType deleteType;
    /**
     * 评论的id
     */
    private String replyId;
    /**
     * bbsItem的id
     */
    private String bbsItemId;
    /**
     * bbs模块的id
     */
    private String groupId;

    public BBSDeleteRespEvent() {
        super();
    }

    public BBSDeleteRespEvent(int errorCode, DeleteType deleteType,
                              String replyId, String bbsItemId, String groupId) {
        super();
        this.errorCode = errorCode;
        this.deleteType = deleteType;
        this.replyId = replyId;
        this.bbsItemId = bbsItemId;
        this.groupId = groupId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public DeleteType getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(DeleteType deleteType) {
        this.deleteType = deleteType;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getBbsItemId() {
        return bbsItemId;
    }

    public void setBbsItemId(String bbsItemId) {
        this.bbsItemId = bbsItemId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "BBSDeleteRespEvent [errorCode=" + errorCode + ", deleteType="
                + deleteType + ", replyId=" + replyId + ", bbsItemId="
                + bbsItemId + ", groupId=" + groupId + "]";
    }

}
