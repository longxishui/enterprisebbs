package com.aspirecn.corpsocial.bundle.settings.utils;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

/**
 * Created by lihaiqiang on 2015/7/15.
 */
public class SettingUserUtil {

    /**
     * 根据userId获取用户
     *
     * @param userId
     * @return
     */
    public static User getUser(String userId) {
//        UserService service = (UserService) OsgiServiceLoader.getInstance()
//                .getService(UserService.class);
//        UserServiceParam param = new UserServiceParam();
//        param.setServie("FindContactService");
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("userid", userId);
//        param.setParams(map);
//        UserServiceResult<List<User>> result = service.invoke(param);
//        List<User> users = result.getData();
//        if (users != null && users.size() > 0) {
//            return users.get(0);
//        }
//        return null;
        return (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(userId);
    }

}
