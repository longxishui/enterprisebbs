package com.aspirecn.corpsocial.bundle.workgrp.domain;

import android.os.Parcel;

import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSItemEntity;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.FileInfoEntity;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * @author chenping
 */
public class BBSItem {
    private BBSItemEntity bbsItemEntity;
    private ArrayList<String> praiseUseridList;
    private ArrayList<KeyValue> praisedUserinfos;
    private FileInfoEntity fileInfoEntity;

    public BBSItem() {
        super();
    }

    public BBSItemEntity getBbsItemEntity() {
        return bbsItemEntity;
    }

    public void setBbsItemEntity(BBSItemEntity bbsItemEntity) {
        this.bbsItemEntity = bbsItemEntity;
    }

    public ArrayList<String> getPraiseUseridList() {
        return praiseUseridList;
    }

    public void setPraiseUseridList(ArrayList<String> praiseUseridList) {
        this.praiseUseridList = praiseUseridList;
    }

    public void setPraisedUserinfos(ArrayList<KeyValue> praisedUserinfos) {
        this.praisedUserinfos = praisedUserinfos;
    }

    public FileInfoEntity getFileInfoEntity() {
        if(bbsItemEntity!=null&&bbsItemEntity.getFileInfo()!=null&&!bbsItemEntity.getFileInfo().equals("")){
            fileInfoEntity = new Gson().fromJson(bbsItemEntity.getFileInfo(),FileInfoEntity.class);
        }
        return fileInfoEntity;
    }
}
