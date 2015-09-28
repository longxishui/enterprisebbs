package com.aspirecn.corpsocial.bundle.workgrp.repository;

import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSGroup;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSGroupEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public ArrayList<BBSGroup> findAllGroups(String corpId) {
        ArrayList<BBSGroup> groups = new ArrayList<BBSGroup>();
        List<BBSGroupEntity> entitys;
        try {
            entitys = dao.queryBuilder().orderByRaw("sortNo").where().eq("belongUserId", Config.getInstance().getUserId()).and().eq("belongCorpId", corpId).query();
            BBSGroup group = null;
            for (BBSGroupEntity entity : entitys) {
                group = new BBSGroup();
                group.setBbsGroupEntity(entity);
                String[] admins = entity.getAdminList().split("-");
                group.setAdmins(new ArrayList<String>(Arrays.asList(admins)));
                groups.add(group);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return groups;
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
