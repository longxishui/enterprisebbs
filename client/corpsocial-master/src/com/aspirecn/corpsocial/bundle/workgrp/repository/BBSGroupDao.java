package com.aspirecn.corpsocial.bundle.workgrp.repository;

import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSGroupEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

public class BBSGroupDao extends SqliteDao<BBSGroupEntity, Integer> {

    public boolean insertEntity(BBSGroupEntity entity) {
        boolean res = false;
        try {
            dao.createOrUpdate(entity);
            res = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<BBSGroupEntity> findAllGroups(String corpId) {
        List<BBSGroupEntity> entitys = null;
        try {
//            entitys = dao.queryBuilder().orderByRaw("sortNo").where().eq("belongUserId", Config.getInstance().getUserId()).and().eq("belongCorpId", corpId).query();
            entitys = dao.queryBuilder().orderByRaw("sortNo").query();
            LogUtil.i("得到的BBS_GROUP为"+entitys.size());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return entitys;
    }

    public BBSGroupEntity findByGroupId(String groupId) {
        try {
            List<BBSGroupEntity> listGroupEntity = dao.queryBuilder().where().eq("id", groupId).and().eq("belongUserId", Config.getInstance().getUserId()).query();
            if (listGroupEntity != null && listGroupEntity.size() > 0) {
                return listGroupEntity.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 清除所有的数据
     */
    public int clearAllData(String corpId) {
        DeleteBuilder<BBSGroupEntity, Integer> deleteBuilder = dao.deleteBuilder();
        int result = -1;
        try {
            deleteBuilder.where().eq("belongCorpId", corpId);
            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
