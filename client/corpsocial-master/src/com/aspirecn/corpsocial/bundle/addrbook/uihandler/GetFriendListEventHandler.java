package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetFriendListEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetFriendListRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Amos on 15-6-16.
 */
@EventBusAnnotation.UIEventHandler(eventType = GetFriendListEvent.class)
public class GetFriendListEventHandler extends EventHandler implements IHandler<Null, GetFriendListEvent> {

    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public Null handle(final GetFriendListEvent getFriendListEvent) {
        getFriendListEvent.setLimit(10000);
        getFriendListEvent.setLastModifiedTime(ConfigPersonal.getInstance().getContactLastTime());
        getFriendListEvent.setLastFriendTime(ConfigPersonal.getInstance().getFriendLastTime());

        LogUtil.i("请求的json数据为：" + getFriendListEvent.getJson());
//        HttpRequest.request(AddrbookConfig.GET_FRIEND_LIST, getFriendListEvent.getJson(), true, new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//                GetFriendListRespEvent getFriendListRespEvent = null;
//                if (result != null && result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    LogUtil.d("获得好友数据为：" + result.getMessage());
//                    getFriendListRespEvent = handleRespData(result.getMessage());
//                    instance.notifyListener(getFriendListRespEvent);
//                } else {
//                    getFriendListRespEvent = new GetFriendListRespEvent();
//                    getFriendListRespEvent.setErrorCode(result == null ? ErrorCode.NETWORK_FAILED.getValue() : result.getErrorCode());
//                    LogUtil.e("同步好友失败");
//                    netFaiNotify(getFriendListRespEvent);
//                }
//            }
//        });
        return new Null();
    }

    private GetFriendListRespEvent handleRespData(String jsonResult) {
        GetFriendListRespEvent getFriendListRespEvent = new GetFriendListRespEvent();
        try {
            JSONObject jsonRoot = new JSONObject(jsonResult);
            //JSONObject jsonData = jsonRoot.getJSONObject("data");
            ArrayList<User> userItems = new ArrayList<User>();
            if (jsonRoot.has("data")) {
                JSONObject jsonData = jsonRoot.getJSONObject("data");
                long lastFriendTime = 0;
                if (jsonData.has("friendList")) {
                    //JSONArray jsonListItem = jsonRoot.getJSONArray("data");

                    JSONArray jsonListItem = jsonData.getJSONArray("friendList");
                    JSONObject jsonItem = null;

                    int itemsLen = jsonListItem.length();

                    getFriendListRespEvent.setErrorCode(ErrorCode.SUCCESS.getValue());
                    long lasttime = 0;
                    for (int i = 0; i < itemsLen; i++) {
                        jsonItem = jsonListItem.getJSONObject(i);

                        User userItem = (new Gson()).fromJson(jsonItem.toString(), User.class);
                        if (userItem.getLastModifiedTime() > lasttime) {
                            lasttime = userItem.getLastModifiedTime();
                        }
                        userItem.setIsFriend(1);
                        userItems.add(userItem);
                    }
                    dao.createOrUpdataFriend(userItems);
                    lastFriendTime = jsonData.getLong("lastFriendTime");
                    if (lastFriendTime > ConfigPersonal.getInstance().getFriendLastTime()) {
                        ConfigPersonal.getInstance().setFriendLastTime(lastFriendTime);

                    }
                }
                if (jsonData.has("removedList")) {
                    try {
                        JSONArray jsonListItem = jsonData.getJSONArray("removedList");
                        for (int i = 0; i < jsonListItem.length(); i++) {
                            String userid = jsonListItem.getString(i);
                            dao.cancelFriend(userid);
                        }
                    } catch (Exception e) {
                        LogUtil.e("", e);
                    }
                }
            }
            getFriendListRespEvent.setUsers(userItems);
        } catch (Exception e) {
            LogUtil.e("获取列表解析失败", e);
            getFriendListRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
        }
        return getFriendListRespEvent;
    }
}
