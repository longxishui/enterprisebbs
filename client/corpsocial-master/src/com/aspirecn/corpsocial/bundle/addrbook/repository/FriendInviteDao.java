package com.aspirecn.corpsocial.bundle.addrbook.repository;

import com.aspirecn.corpsocial.bundle.addrbook.domain.FriendInvite;
import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.FriendInviteEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amos on 15-6-19.
 */
@EventBusAnnotation.Dao(name = "inviteDao")
public class FriendInviteDao extends SqliteDao<FriendInviteEntity, String> {

    public List<FriendInvite> queryInvite(String belongUserId, int start, int count) throws SQLException {

        QueryBuilder<FriendInviteEntity, String> queryBuilder = dao.queryBuilder();
        Where<FriendInviteEntity, String> condition = queryBuilder.where().eq("belongUserId", belongUserId);
        queryBuilder.orderByRaw("createTime desc");
        if (count > 0) {
            queryBuilder.limit(count);
            queryBuilder.offset(start);
        }
        List<FriendInvite> invites = new ArrayList<FriendInvite>();
        List<FriendInviteEntity> entities = queryBuilder.query();
        for (FriendInviteEntity entity : entities) {
            FriendInvite invite = new FriendInvite();
            invites.add(invite);
            invite.setSeqNo(entity.getSeqNo());
            invite.setUserid(entity.getUserid());
            invite.setSmallUrl(entity.getSmallUrl());
            invite.setUsername(entity.getUsername());
            invite.setCorpName(entity.getCorpName());
            invite.setCreateTime(entity.getCreateTime());
            invite.setStatus(entity.getStatus());
            invite.setIsNew(entity.getIsNew());
            invite.setCorpInfo(entity.getCorpInfo());
            invite.setUser((new Gson()).fromJson(entity.getUserInfo(), User.class));
        }
        return invites;
    }

    public FriendInvite build(FriendInviteEntity entity) {
        FriendInvite invite = new FriendInvite();
        invite.setSeqNo(entity.getSeqNo());
        invite.setUsername(entity.getUsername());
        invite.setUserid(entity.getUserid());
        invite.setCorpName(entity.getCorpName());
        invite.setCreateTime(entity.getCreateTime());
        invite.setStatus(entity.getStatus());
        invite.setIsNew(entity.getIsNew());
        invite.setCorpInfo(entity.getCorpInfo());
        if (entity.getUserInfo() != null && entity.getUserInfo().length() > 0)
            invite.setUser((new Gson()).fromJson(entity.getUserInfo(), User.class));
        return invite;
    }

    public void create(FriendInvite invite) throws SQLException {
        FriendInviteEntity entity = new FriendInviteEntity();
        entity.setUserid(invite.getUserid());
        entity.setUsername(invite.getUsername());
        entity.setStatus(invite.getStatus());
        entity.setCreateTime(invite.getCreateTime());
        entity.setBelongUserId(Config.getInstance().getUserId());
        entity.setCorpName(invite.getCorpName());
        entity.setCorpInfo(invite.getCorpInfo());
        entity.setIsNew(invite.getIsNew());
        if (invite.getUser() != null) {
            entity.setUserInfo((new Gson()).toJson(invite.getUser()));
        }
        entity.setSmallUrl(invite.getSmallUrl());
        dao.create(entity);
    }

    public FriendInviteEntity findById(int seqNo) {
        FriendInviteEntity entity = null;
        QueryBuilder<FriendInviteEntity, String> queryBuilder = dao.queryBuilder();
        Where<FriendInviteEntity, String> where = queryBuilder.where();
        try {
            where.eq("belongUserId", Config.getInstance().getUserId());
            where.and();
            where.eq("seqNo", seqNo);
            entity = queryBuilder.queryForFirst();
        } catch (SQLException e) {

        }

        return entity;
    }

    public FriendInviteEntity findByUserid(String userid) {
        FriendInviteEntity entity = null;
        QueryBuilder<FriendInviteEntity, String> queryBuilder = dao.queryBuilder();
        Where<FriendInviteEntity, String> where = queryBuilder.where();
        try {
            where.eq("belongUserId", Config.getInstance().getUserId()).and().eq("userid", userid);
            entity = queryBuilder.queryForFirst();
        } catch (SQLException e) {

        }

        return entity;
    }

    public FriendInviteEntity findUnaccept(String userid) {
        FriendInviteEntity entity = null;
        QueryBuilder<FriendInviteEntity, String> queryBuilder = dao.queryBuilder();
        Where<FriendInviteEntity, String> where = queryBuilder.where();
        try {
            where.eq("belongUserId", Config.getInstance().getUserId()).and().eq("userid", userid).and().eq("status", 0);
            entity = queryBuilder.queryForFirst();
        } catch (SQLException e) {

        }

        return entity;
    }

    public FriendInviteEntity findAccept(String userid) {
        FriendInviteEntity entity = null;
        QueryBuilder<FriendInviteEntity, String> queryBuilder = dao.queryBuilder();
        Where<FriendInviteEntity, String> where = queryBuilder.where();
        try {
            where.eq("belongUserId", Config.getInstance().getUserId()).and().eq("userid", userid).and().eq("status", 1);
            entity = queryBuilder.queryForFirst();
        } catch (SQLException e) {

        }

        return entity;
    }

    public void update(int seqNo, int status) throws SQLException {
        FriendInviteEntity entity = this.findById(seqNo);
        if (entity != null) {
            entity.setStatus(status);
            dao.update(entity);
        }
    }

    public void updateReaded(int seqNo) throws SQLException {
        FriendInviteEntity entity = this.findById(seqNo);
        if (entity != null) {
            entity.setIsNew(0);
            dao.update(entity);
        }
    }

    public void deleteInvite(int seqNo) throws SQLException {
        FriendInviteEntity entity = this.findById(seqNo);
        if (entity != null) {
            dao.delete(entity);
        }
    }

    public void updateUser(FriendInviteEntity entity) throws SQLException {
        dao.update(entity);
    }


    public void createAndRemoveInvite(final FriendInvite invite, final FriendInviteEntity old) throws SQLException {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {
                FriendInviteEntity entity = new FriendInviteEntity();
                entity.setUserid(invite.getUserid());
                entity.setUsername(invite.getUsername());
                entity.setStatus(invite.getStatus());
                entity.setCreateTime(invite.getCreateTime());
                entity.setBelongUserId(Config.getInstance().getUserId());
                entity.setCorpName(invite.getCorpName());
                entity.setCorpInfo(invite.getCorpInfo());
                entity.setIsNew(invite.getIsNew());
                entity.setSignature(invite.getSignature());
                if (invite.getUser() != null) {
                    entity.setUserInfo((new Gson()).toJson(invite.getUser()));
                }
                entity.setSmallUrl(invite.getSmallUrl());
                dao.create(entity);

                if (old != null) {
                    dao.delete(old);
                }
            }
        });
    }

}
