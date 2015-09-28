package com.aspirecn.corpsocial.bundle.im.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.im.facade.UpdateGroupMemberService;
import com.aspirecn.corpsocial.bundle.im.repository.GroupMemberDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupMemberEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenbin on 2015/7/25.
 */
@OsgiAnnotation.OsgiService(serviceType = UpdateGroupMemberService.class)
public class UpdateGroupMemberServiceImpl implements UpdateGroupMemberService {

    private GroupMemberDao groupMemberDao = new GroupMemberDao();

    private UserDao userDao = new UserDao();

    @Override
    public Void invoke(List<String> ids) {

        for (String userId : ids) {
            try {
                Map<String, Object> where = new HashMap<String, Object>();
                where.put("memberId", userId);
                where.put("belongUserId", Config.getInstance().getUserId());
                List<GroupMemberEntity> entities = groupMemberDao.findAllByWhere(where);
                User user = userDao.findUserDetail(userId);
                if (user != null) {
                    for (GroupMemberEntity entity : entities) {
                        entity.setStatus(user.getImStatus());
                        entity.setHeadImgUrl(user.getUrl());
                        entity.setMemberName(user.getName());
                        entity.setInitialCode(user.getInitialKey());
                        entity.setCellphone(user.getCellphone());
                        entity.setSpellKey(user.getSpellKey());
                        groupMemberDao.update(entity);
                    }
                }
            } catch (Exception e) {
                LogUtil.e("update group error ", e);
            }
        }
        return null;
    }
}
