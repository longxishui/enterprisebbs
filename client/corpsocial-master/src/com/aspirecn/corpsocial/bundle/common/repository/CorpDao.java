package com.aspirecn.corpsocial.bundle.common.repository;

import com.aspirecn.corpsocial.bundle.common.domain.Corp;
import com.aspirecn.corpsocial.bundle.common.repository.entity.CorpEntity;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amos on 15-6-18.
 */

@EventBusAnnotation.Dao(name = "corpDao")
public class CorpDao extends SqliteDao<CorpEntity, String> {

    public List<Corp> find(String belongUserId) throws SQLException {

        List<CorpEntity> entities = dao.queryBuilder().where().eq("belongUserId", belongUserId).query();
        List<Corp> corps = new ArrayList();
        for (CorpEntity entity : entities) {
            Corp corp = new Corp();
            corp.setCorpId(entity.getCorpId());
            corp.setName(entity.getCorpName());
            corps.add(corp);
        }
        return corps;
    }
}
