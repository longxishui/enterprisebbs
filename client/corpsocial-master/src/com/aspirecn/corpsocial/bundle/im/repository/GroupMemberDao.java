package com.aspirecn.corpsocial.bundle.im.repository;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupMemberEntity;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupMemberDao extends SqliteDao<GroupMemberEntity, Integer> {

    public List<GroupMemberEntity> searchGroupMemberByKeyword(String groupId,
                                                              String belongUserId, String keyword, String orderBy) {
        List<GroupMemberEntity> result = new ArrayList<GroupMemberEntity>();
        QueryBuilder<GroupMemberEntity, Integer> queryBuilder = dao
                .queryBuilder();
        try {
            Where<GroupMemberEntity, Integer> where = queryBuilder.where();

            // where.eq("groupId", groupId);
            // where.and().eq("belongUserId", belongUserId);
            if (!TextUtils.isEmpty(keyword)) {
                // where.and().like("memberName", "%" + keyword + "%").or()
                // .like("initialCode", "%" + keyword + "%");
                where.like("memberName", "%" + keyword + "%").or()
                        .like("initialCode", "%" + keyword + "%").or().like("spellKey", "%" + keyword + "%").or().raw("length('" + keyword + "') > 3 and cellphone like '" + "%" + keyword + "%" + "'");
            }
            where.and().eq("groupId", groupId);
            where.and().eq("belongUserId", belongUserId);

            if (!TextUtils.isEmpty(orderBy)) {
                queryBuilder.orderByRaw(orderBy);
            }

            result = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
