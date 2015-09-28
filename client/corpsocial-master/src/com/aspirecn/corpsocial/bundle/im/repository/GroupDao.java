package com.aspirecn.corpsocial.bundle.im.repository;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupDao extends SqliteDao<GroupEntity, Integer> {

    /**
     * 聊天记录搜索
     *
     * @param belongUserId
     * @param keyword
     * @param orderBy
     * @return
     */
    public List<GroupEntity> searchGroupByKeyword(String belongUserId,
                                                  String keyword, String orderBy) {
        List<GroupEntity> result = new ArrayList<GroupEntity>();
        QueryBuilder<GroupEntity, Integer> queryBuilder = dao.queryBuilder();
        try {
            Where<GroupEntity, Integer> where = queryBuilder.where();

            where.eq("belongUserId", belongUserId);
            if (!TextUtils.isEmpty(keyword)) {
                where.and().like("name", "%" + keyword + "%");
            }

            if (!TextUtils.isEmpty(orderBy)) {
                queryBuilder.orderByRaw(orderBy);
            }

            result = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void insertGroup(final GroupEntity groupEntity) {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() {
                Map<String, Object> groupWhere = new HashMap<String, Object>();
                groupWhere.put("groupId", groupEntity.getGroupId());
                groupWhere.put("belongUserId", groupEntity.getBelongUserId());
                GroupEntity findGroupEntity = findByWhere(groupWhere);
                if (findGroupEntity == null)
                    insert(groupEntity);
            }


        });
    }
}
