package com.aspirecn.corpsocial.bundle.common.uitils;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindCorpService;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;

import java.util.List;

/**
 * 用户工具类
 * Created by lihaiqiang on 2015/7/16.
 */
public class UserUtil {

    /**
     * 根据userId获取用户
     *
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public static User getUser(String userId) {
        return (User) OsgiServiceLoader.getInstance()
                .getService(FindContactService.class).invoke(userId);
    }

    /**
     * 获取用户所属企业
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<UserCorp> getUserCorp() {
        return (List<UserCorp>) OsgiServiceLoader.getInstance()
                .getService(FindCorpService.class).invoke(new Null());
    }

}
