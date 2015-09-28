package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSPraiseInfo;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSReplyInfoEntity;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.ArrayList;
import java.util.List;

public class GetBBSDetailRespEvent extends BusEvent {
    /**
     * 帖子的id
     */
    private String bbsId;
    /**
     * 错误码
     */
    private int errorCode;
    /**
     * 点赞的信息
     */
    private ArrayList<String> praiseInfos;
    /**
     * 评论的信息
     */
    private List<BBSReplyInfoEntity> bbsReplyInfoEntities;

    public GetBBSDetailRespEvent() {
        super();
    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public ArrayList<String> getPraiseInfos() {
        return praiseInfos;
    }

    public void setPraiseInfos(ArrayList<String> praiseInfos) {
        this.praiseInfos = praiseInfos;
    }


    public String getBbsId() {
        return bbsId;
    }

    public void setBbsId(String bbsId) {
        this.bbsId = bbsId;
    }

    public List<BBSReplyInfoEntity> getBbsReplyInfoEntities() {
        return bbsReplyInfoEntities;
    }

    public void setBbsReplyInfoEntities(List<BBSReplyInfoEntity> bbsReplyInfoEntities) {
        this.bbsReplyInfoEntities = bbsReplyInfoEntities;
    }

    @Override
    public String toString() {
        return "GetBBSDetailRespEvent{" +
                "bbsId='" + bbsId + '\'' +
                ", errorCode=" + errorCode +
                ", praiseInfos=" + praiseInfos +
                ", bbsReplyInfoEntities=" + bbsReplyInfoEntities +
                '}';
    }
}
