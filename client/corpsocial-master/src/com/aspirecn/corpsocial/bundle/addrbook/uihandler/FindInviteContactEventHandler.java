package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.FriendInvite;
import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindInviteContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindInviteContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.InviteFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.FriendInviteDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.FriendInviteEntity;
import com.aspirecn.corpsocial.bundle.addrbook.utils.AddrbookConfig;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amos on 15-6-24.
 * 为接收到好友邀请上服务器查找用户信息
 */
@EventBusAnnotation.UIEventHandler(eventType = FindInviteContactEvent.class)
public class FindInviteContactEventHandler extends EventHandler implements IHandler<Null, FindInviteContactEvent> {

    @EventBusAnnotation.Component
    private FriendInviteDao friendInviteDao;
    @EventBusAnnotation.Component
    private UserDao userDao ;
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader.getInstance();

    @Override
    public Null handle(final FindInviteContactEvent findInviteContactEvent) {
        LogUtil.i("请求的json数据为：" + findInviteContactEvent.getJson());
//        HttpRequest.request(AddrbookConfig.FIND_CONTACT, findInviteContactEvent.getJson(), new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//                FindInviteContactRespEvent findInviteContactRespEvent = null;
//                if (result != null && result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    LogUtil.d("获得用户数据为：" + result.getMessage());
//                    findInviteContactRespEvent = handleRespData(result.getMessage());
//                    instance.notifyListener(findInviteContactRespEvent);
//                } else {
//                    LogUtil.e("同步通讯录失败");
//                }
//            }
//        });
        return new Null();
    }

    private FindInviteContactRespEvent handleRespData(String jsonResult) {
        FindInviteContactRespEvent findInviteContactRespEvent = new FindInviteContactRespEvent();
        try {
            JSONObject jsonRoot = new JSONObject(jsonResult);
            //JSONObject jsonData = jsonRoot.getJSONObject("data");
            JSONArray jsonListItem = jsonRoot.getJSONArray("data");
            ArrayList<User> userItems = new ArrayList<User>();

            JSONObject jsonItem = null;

            int itemsLen = jsonListItem.length();
            findInviteContactRespEvent.setErrorCode(ErrorCode.SUCCESS.getValue());

            for (int i = 0; i < itemsLen; i++) {
                jsonItem = jsonListItem.getJSONObject(i);

                User userItem = (new Gson()).fromJson(jsonItem.toString(), User.class);
                userItems.add(userItem);
            }
            if (userItems.size() > 0) {
                /**
                 * need transaction
                 */
                //保存为临时用户
                for (User user : userItems) {
                    user.setIsTemp(1);
                    user.setIsSameCorp(0);
                }
                userDao.createOrUpdataContact(userItems);
                FriendInviteEntity invite = friendInviteDao.findUnaccept(userItems.get(0).getUserid());
                if (invite != null) {
                    invite.setUserInfo((new Gson()).toJson(userItems.get(0), User.class));
                    friendInviteDao.updateUser(invite);

                    List<FriendInvite> invites = new ArrayList();
                    invites.add(friendInviteDao.build(invite));
                    findInviteContactRespEvent.setInvites(invites);
                    eventListener.notifyListener(new InviteFriendEvent(friendInviteDao.build(invite)));
                }


            }


        } catch (Exception e) {
            LogUtil.e("获取列表解析失败", e);

            findInviteContactRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
        }
        return findInviteContactRespEvent;
    }
}
