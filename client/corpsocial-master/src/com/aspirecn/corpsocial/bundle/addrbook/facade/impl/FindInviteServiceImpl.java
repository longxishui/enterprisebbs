package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.FriendInvite;
import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindInviteParam;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindInviteService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.FriendInviteDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenbin on 2015/8/10.
 * 获取好友邀请信息列表
 */
@OsgiAnnotation.OsgiService(serviceType = FindInviteService.class)
public class FindInviteServiceImpl implements FindInviteService {

    @EventBusAnnotation.Component
    private FriendInviteDao inviteDao ;

    @EventBusAnnotation.Component
    private UserDao userDao;

    @Override
    public List<FriendInvite> invoke(FindInviteParam params) {

        List<FriendInvite> invites = new ArrayList<>();
        try {
            List<FriendInvite> list=inviteDao.queryInvite(Config.getInstance().getUserId(), params.getStart(), params.getCount());
            for(FriendInvite invite:list){
                User user=userDao.findUserDetail(invite.getUserid());
                if(user!=null){
                    invite.setSmallUrl(user.getSmallUrl());
                }
            }
            invites.addAll(list);

        } catch (SQLException ex) {
            LogUtil.e("FindInviteServiceImpl查询出错", ex);
        }
        return invites;
    }
}
