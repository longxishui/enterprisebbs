package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSItemEntity;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.FileInfoEntity;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

public class CreateOrModifyBBSRespEvent extends BusEvent {
    /**
     * 错误码
     */
    private int errorCode;
    /**
     * 帖子的id
     */
    private String itemId;
    /**
     * 帖子的模块id
     */
    private String groupId;
    /**
     * 实体对象
     */
    private BBSItemEntity bbsItemEntity;
    private List<FileInfoEntity> listFileInfoEntity;

    public CreateOrModifyBBSRespEvent() {
        super();
    }

    public CreateOrModifyBBSRespEvent(int errorCode, String itemId,
                                      String groupId) {
        super();
        this.errorCode = errorCode;
        this.itemId = itemId;
        this.groupId = groupId;
    }
    public List<FileInfoEntity> getListFileInfoEntity() {
        return listFileInfoEntity;
    }

    public void setListFileInfoEntity(List<FileInfoEntity> listFileInfoEntity) {
        this.listFileInfoEntity = listFileInfoEntity;
    }

    public BBSItemEntity getBbsItemEntity() {
        return bbsItemEntity;
    }

    public void setBbsItemEntity(BBSItemEntity bbsItemEntity) {
        this.bbsItemEntity = bbsItemEntity;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "CreateOrModifyBBSRespEvent [errorCode=" + errorCode
                + ", itemId=" + itemId + ", groupId=" + groupId + "]";
    }

}
