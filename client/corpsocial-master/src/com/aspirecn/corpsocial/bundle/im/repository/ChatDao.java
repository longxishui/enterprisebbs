package com.aspirecn.corpsocial.bundle.im.repository;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatDao extends SqliteDao<ChatEntity, Integer> {

    public List<ChatEntity> searchChatByKeyword(String belongUserId, int chatType,
                                                String keyword, String orderBy) {
        List<ChatEntity> result = new ArrayList<ChatEntity>();
        QueryBuilder<ChatEntity, Integer> queryBuilder = dao.queryBuilder();
        try {
            Where<ChatEntity, Integer> where = queryBuilder.where();

            where.eq("belongUserId", belongUserId);
            where.and().eq("chatType", chatType);
            if (!TextUtils.isEmpty(keyword)) {
                where.and().like("chatName", "%" + keyword + "%");
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


    /**
     * 获取未读详细数
     */
    public int queryUnReadCount() {
        int unReadCount = 0;
        try {
            QueryBuilder<ChatEntity, Integer> queryBuilder = dao.queryBuilder();
            Where<ChatEntity, Integer> where = queryBuilder.where();
            where.eq("belongUserId", Config.getInstance().getUserId());
            where.and();
            where.eq("display", true);
            List<ChatEntity> chatEntities = queryBuilder.query();
            for (ChatEntity chatEntity : chatEntities) {
                unReadCount += chatEntity.getNoticeCount();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unReadCount;
    }

}
