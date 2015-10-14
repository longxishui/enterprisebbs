package com.aspirecn.corpsocial.bundle.workgrp.domain;

import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSItemEntity;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.FileInfoEntity;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author duyz
 */
public class BBSItem {
    private BBSItemEntity bbsItemEntity;
    private List<FileInfoEntity> fileInfoEntities = new ArrayList<FileInfoEntity>();

    public BBSItem() {
        super();
    }

    public BBSItemEntity getBbsItemEntity() {
        return bbsItemEntity;
    }

    public void setBbsItemEntity(BBSItemEntity bbsItemEntity) {
        this.bbsItemEntity = bbsItemEntity;
    }

    public List<FileInfoEntity> getFileInfoList() {
        if(bbsItemEntity!=null&&bbsItemEntity.getFileInfoString()!=null&&!bbsItemEntity.getFileInfoString().equals("")){
            try{
                JSONArray jsonArray = new JSONArray(bbsItemEntity.getFileInfoString());
                if(jsonArray.length()==0){
                    return null;
                }
                for(int i=0;i<jsonArray.length();i++){
                    FileInfoEntity fileInfoEntity = new Gson().fromJson(jsonArray.get(i).toString(),FileInfoEntity.class);
                    fileInfoEntities.add(fileInfoEntity);
                }
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.e("BBSItem转换失败");
            }
//            fileInfoEntity = new Gson().fromJson(bbsItemEntity.getFileInfo(),FileInfoEntity.class);
        }
        return fileInfoEntities;
    }
}
