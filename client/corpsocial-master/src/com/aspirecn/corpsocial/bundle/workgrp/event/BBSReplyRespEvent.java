package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.bundle.workgrp.util.ReplyType;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

public class BBSReplyRespEvent extends BusEvent {
    /**
     * 错误码
     */
    private int errorCode;
    /**
     * 帖子回应的类型，分为点赞、评论
     */
    private ReplyType type;
    /**
     * 回复的id，点赞不涉及
     */
    private String replyId;
    /**
     * 回应对应的帖子id
     */
    private String bbsId;
    /**
     * 回应对应的帖子的模块id
     */
    private String groupId;

    public BBSReplyRespEvent() {
        super();
    }

    public BBSReplyRespEvent(int errorCode, ReplyType type, String replyId,
                             int actId) {
        super();
        this.errorCode = errorCode;
        this.type = type;
        this.replyId = replyId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public ReplyType getType() {
        return type;
    }

    public void setType(ReplyType type) {
        this.type = type;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }


    public String getBbsId() {
        return bbsId;
    }

    public void setBbsId(String bbsId) {
        this.bbsId = bbsId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
