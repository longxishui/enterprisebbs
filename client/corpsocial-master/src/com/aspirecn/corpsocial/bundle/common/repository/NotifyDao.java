package com.aspirecn.corpsocial.bundle.common.repository;

import com.aspirecn.corpsocial.bundle.common.repository.entity.NotifyEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinghuihong on 15/7/1.
 */
public class NotifyDao extends SqliteDao<NotifyEntity, String> {


    public List<NotifyEntity> queryUnHandled() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("belongUserId", Config.getInstance().getUserId());
        map.put("isHandled", false);
        return findAllByWhere(map);
    }
}
