package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.SearchParam;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindFriendsByNameService;
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
 * 根据用户名模糊查询好友
 */
@OsgiAnnotation.OsgiService(serviceType = FindFriendsByNameService.class)
public class FindFriendsByNameServiceImpl implements FindFriendsByNameService {

    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public List<User> invoke(String key) {
        List<SearchParam> sparams = new ArrayList();
        SearchParam sparam = new SearchParam();
        sparam.setName("name");
        sparam.setMisty(true);
        sparam.setValue(key);
        sparams.add(sparam);

        sparam = new SearchParam();
        sparam.setName("isFriend");
        sparam.setMisty(false);
        sparam.setValue(1);
        sparams.add(sparam);
        List<User> users = new ArrayList<User>();
        try {
            users.addAll(dao.find(Config.getInstance().getUserId(), sparams, 0, 0, null));

        } catch (SQLException ex) {
            LogUtil.e("FindFriendsByNameServiceImpl查询出错", ex);
        }
        return users;
    }
}
