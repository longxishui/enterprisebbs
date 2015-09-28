package com.aspirecn.corpsocial.bundle.workgrp.repository;

import android.text.TextUtils;
import android.util.Log;

import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSItemEntity;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSReplyInfoEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BBSItemDao extends SqliteDao<BBSItemEntity, String> {

    public boolean insertEntity(BBSItemEntity itemEntity) {
        boolean res = false;
        try {
            dao.createOrUpdate(itemEntity);
            res = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<BBSItem> findAllItems(String groupId, int index, int count) {
        List<BBSItem> items = new ArrayList<BBSItem>();
//		List<BBSItemEntity> entitys = findAllByWhere("groupId", groupId, "createTime desc");
        Map<String, Object> columns = new HashMap<String, Object>();
        columns.put("groupId", groupId);
        columns.put("userid", Config.getInstance().getUserId());
        List<BBSItemEntity> entitys = findAllByWhereAndIndex(columns, index, count, "createTime desc");
//		replyInfoDao = new BBSReplyInfoDao();
        for (BBSItemEntity entity : entitys) {
            BBSItem item = new BBSItem();
            item.setBbsItemEntity(entity);
            if (Integer.valueOf(entity.getPraiseTimes()) == 0) {
                item.setPraiseUseridList(new ArrayList<String>());
            } else {
                ArrayList<String> praiseIds = new ArrayList<String>(Arrays.asList(entity.getPraiseUserIds().split("-")));
                item.setPraiseUseridList(praiseIds);
            }
//			item.setBbsReplyInfoList(replyInfoDao.findAllReplyInfos(entity.getId()));
            items.add(item);
        }
        Log.e("BBSItemDao","获取到的数据数目为："+items.size());
        return items;
    }

    public BBSItem findItemById(String bbsId) {
        List<BBSItemEntity> listentity = null;
        BBSItemEntity entity = null;
        try {
            listentity = dao.queryBuilder().where().eq("id", bbsId).and().eq("userid", Config.getInstance().getUserId()).query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (listentity == null || listentity.size() == 0) {
            return null;
        } else {
            entity = listentity.get(0);
        }
        BBSItem item = new BBSItem();
        item.setBbsItemEntity(entity);
        if (TextUtils.isEmpty(entity.getPraiseUserIds())) {
            item.setPraiseUseridList(new ArrayList<String>());
        } else {
            ArrayList<String> praiseIds = new ArrayList<String>(Arrays.asList(entity.getPraiseUserIds().split("-")));
            item.setPraiseUseridList(praiseIds);
        }
        return item;
    }

    public void updatePraiseUserIds(String itemId, String listPraiseUserID, String praiseTimes) {
        String sql = "UPDATE bbs_item SET praiseUserIds=?,praiseTimes=? where id= ? and userid= ?";
        try {
            dao.executeRaw(sql, listPraiseUserID, praiseTimes, itemId, Config.getInstance().getUserId());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void updateReplyTimes(String id, String replyTimes) {
        // TODO Auto-generated method stub
        String sql = "UPDATE bbs_item SET replyTimes=? where id= ? and userid= ?";
        try {
            dao.executeRaw(sql, replyTimes, id, Config.getInstance().getUserId());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 让回复数加1或减1用做评论的提交和删除评论
     */
    public void refreshReplyTimes(String id, int i) {
        String sql = null;
        switch (i) {
            case 1:
                sql = "UPDATE bbs_item SET replyTimes= replyTimes + 1 where id= ? and userid= ?";

                break;
            case -1:
                sql = "UPDATE bbs_item SET replyTimes= replyTimes - 1 where id= ? and userid= ?";
                break;
            default:
                return;
        }
        try {
            dao.executeRaw(sql, id, Config.getInstance().getUserId());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public long getLastModifyTime() {
        // TODO Auto-generated method stub
        QueryBuilder<BBSItemEntity, String> queryBuilder = dao.queryBuilder();
        queryBuilder.orderByRaw(" lastModifiedTime desc ");
        queryBuilder.limit(1);
        try {
            List<BBSItemEntity> list = queryBuilder.where().eq("userid", Config.getInstance().getUserId()).query();
            if (list != null && list.size() > 0) {
                return list.get(0).getLastModifiedTime();
            }
        } catch (SQLException e) {

        }
        return 0;
    }

    public List<BBSItem> findConcernMeItemsPartin(String corpId) {
        // TODO Auto-generated method stub
        List<BBSItemEntity> listBBSItemEntity = new ArrayList<BBSItemEntity>();
        String userid = Config.getInstance().getUserId();
        Map<String, Object[]> map = new HashMap<String, Object[]>();
        String[] bbsid = getBBSIDsByCreateId(userid, corpId);
        map.put("id", bbsid);
        Map<String, Object> useridmap = new HashMap<String, Object>();
        useridmap.put("userid", Config.getInstance().getUserId());
        useridmap.put("corpId", corpId);
        listBBSItemEntity.addAll(findAllByWhereAndIndex(useridmap,
                null, map,
                null, 0, 100,
                "createTime desc"));
        return getBBSItemsByBBSItemEntitys(listBBSItemEntity);
    }

    /**
     * 与我相关我创建的帖子
     */
    public List<BBSItem> findConcernMeItemsCreate(String corpId) {
        List<BBSItemEntity> listBBSItemEntity = new ArrayList<BBSItemEntity>();
        try {
            listBBSItemEntity.addAll(dao.queryBuilder().orderByRaw("createTime desc").where().eq("creatorId", Config.getInstance().getUserId()).and().eq("userid", Config.getInstance().getUserId()).and().eq("corpId", corpId).query());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getBBSItemsByBBSItemEntitys(listBBSItemEntity);
    }

    public List<BBSItem> findAllConcernMeItems(int index, String corpId) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 根据bbsItemEntitys获取bbsItems
     */
    private List<BBSItem> getBBSItemsByBBSItemEntitys(List<BBSItemEntity> listBBSItemEntitys) {
        List<BBSItem> listBBSItem = new ArrayList<BBSItem>();
        if (listBBSItemEntitys == null) {
            return listBBSItem;
        }
        for (BBSItemEntity entity : listBBSItemEntitys) {
            BBSItem item = new BBSItem();
            item.setBbsItemEntity(entity);
            if (Integer.valueOf(entity.getPraiseTimes()) == 0) {
                item.setPraiseUseridList(new ArrayList<String>());
            } else {
                ArrayList<String> praiseIds = new ArrayList<String>(Arrays.asList(entity.getPraiseUserIds().split("-")));
                item.setPraiseUseridList(praiseIds);
            }
            listBBSItem.add(item);
        }
        return listBBSItem;
    }

    /**
     * 查询自己回复参与过的帖子的id集合
     */
    private String[] getBBSIDsByCreateId(String createId, String corpId) {
        BBSReplyInfoDao replyInfoDao = new BBSReplyInfoDao();
        Map<String, Object> useridmap = new HashMap<String, Object>();
        useridmap.put("replyerId", createId);
        useridmap.put("corpId", corpId);
        //List<BBSReplyInfoEntity> listBBSReplyInfos = replyInfoDao.findAllByWhere("replyerId",createId,null);
        List<BBSReplyInfoEntity> listBBSReplyInfos = replyInfoDao.findAllByWhere(useridmap, null);
        List<String> listbbsid = new ArrayList<String>();
        if (listBBSReplyInfos != null) {
            for (BBSReplyInfoEntity entity : listBBSReplyInfos) {
                listbbsid.add(entity.getItemId());
            }
        }
        return listbbsid.toArray(new String[listbbsid.size()]);
    }
}
