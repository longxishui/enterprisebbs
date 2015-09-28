package com.aspirecn.corpsocial.bundle.workgrp.repository;

import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSReplyInfoEntity;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BBSReplyInfoDao extends SqliteDao<BBSReplyInfoEntity, String> {

    public boolean insertEntity(BBSReplyInfoEntity replyInfoEntity) {
        boolean res = false;
        try {
            dao.createOrUpdate(replyInfoEntity);
            res = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<BBSReplyInfoEntity> findAllReplyInfos(String itemId) {
        List<BBSReplyInfoEntity> entitys = findAllByWhere("itemId", itemId, "time");
        if (entitys == null) {
            return new ArrayList<BBSReplyInfoEntity>();
        }else{
            return entitys;
        }
    }

    public List<BBSReplyInfoEntity> findByBBSId(String bbsitemId) {
        // TODO Auto-generated method stub
        List<BBSReplyInfoEntity> entitys = findAllByWhere("itemId", bbsitemId, "time");
        return entitys;
    }
}
