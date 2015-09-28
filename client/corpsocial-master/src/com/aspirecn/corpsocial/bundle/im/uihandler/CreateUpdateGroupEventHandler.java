package com.aspirecn.corpsocial.bundle.im.uihandler;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.GroupDao;
import com.aspirecn.corpsocial.bundle.im.repository.GroupMemberDao;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;


/**
 * 创建群组处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = CreateUpdateGroupEvent.class)
public class CreateUpdateGroupEventHandler implements
        IHandler<Null, CreateUpdateGroupEvent> {

    @Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    private GroupMemberDao groupMemberDao = new GroupMemberDao();

    @Autowired
    private GroupDao groupDao = new GroupDao();

    private ChatDao chatDao = new ChatDao();

    private OsgiServiceLoader osgiServiceLoader = OsgiServiceLoader
            .getInstance();

    @Override
    public Null handle(CreateUpdateGroupEvent busEvent) {

        return new Null();
    }

    /**
     * 根据userId获取用户
     *
     * @param userId
     * @return
     */
    private User getUser(String userId) {
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
