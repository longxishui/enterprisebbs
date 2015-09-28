package com.aspirecn.corpsocial.bundle.im.repository;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.bundle.im.utils.ContentType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MsgDao extends SqliteDao<MsgEntity, Integer> {

    /**
     * 聊天记录搜索
     *
     * @param chatId
     * @param belongUserId
     * @param keyword
     * @param orderBy
     * @return
     */
    public List<MsgEntity> searchMsgByKeyword(String chatId,
                                              String belongUserId, String keyword, String orderBy) {
        // List<MsgEntity> result = new ArrayList<MsgEntity>();
        List<MsgEntity> result = new LinkedList<MsgEntity>();
        QueryBuilder<MsgEntity, Integer> queryBuilder = dao.queryBuilder();
        try {
            Where<MsgEntity, Integer> where = queryBuilder.where();

            where.eq("chatId", chatId);
            where.and().eq("belongUserId", belongUserId);
            where.and().eq("contentType", ContentType.TEXT.getValue());
            if (!TextUtils.isEmpty(keyword)) {
                where.and().like("content", "%" + keyword + "%");
            }

            if (!TextUtils.isEmpty(orderBy)) {
                queryBuilder.orderByRaw(orderBy);
            }

            List<MsgEntity> msgs = queryBuilder.query();
            for (MsgEntity msg : msgs) {
                result.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除聊天消息
     *
     * @param chatId
     * @param msgIds
     * @return
     */
    public boolean deleteMsgs(String chatId, List<String> msgIds) {
        QueryBuilder<MsgEntity, Integer> queryBuilder = dao.queryBuilder();
        try {
            Where<MsgEntity, Integer> where = queryBuilder.where();
            where.eq("chatId", chatId);
            where.and().eq("belongUserId", Config.getInstance().getUserId());
            where.and().in("msgId", msgIds);

            List<MsgEntity> msgs = queryBuilder.query();

            for (MsgEntity msgEntity : msgs) {
                int contentType = msgEntity.getContentType();
                //图片，语音，位置消息，删除对应文件信息
                if (MsgEntity.MsgContentType.PICTURE == contentType) {
                    String content = msgEntity.getContent();
                    if (content.startsWith(Constant.ROOT_PATH)) {
                        File file = new File(content);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                } else if (MsgEntity.MsgContentType.VOICE == contentType) {
                    String content = msgEntity.getContent();
                    File file = new File(content);
                    if (file.exists()) {
                        file.delete();
                    }
                } else if (MsgEntity.MsgContentType.POSITION == contentType) {
                    String nativeUrl = null;
                    try {
                        nativeUrl = new JSONObject(msgEntity.getExtraInfo())
                                .optString("nativeUrl");
                        File file = new File(nativeUrl);
                        if (file.exists()) {
                            file.delete();
                        }
                    } catch (JSONException e) {
                        LogUtil.e("删除位置信息错误", e);
                    }
                }
            }

            if (msgs != null) {
                detele(msgs);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
