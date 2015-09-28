package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.utils.AddrbookConfig;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.HttpRequest;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amos on 15-6-16.
 */
@EventBusAnnotation.UIEventHandler(eventType = FindContactEvent.class)
public class FindContactEventHandler extends EventHandler implements IHandler<Null, FindContactEvent> {

    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public Null handle(final FindContactEvent findContactEvent) {
        LogUtil.i("aspire--请求的json数据为：" + findContactEvent.getJson());
//        HttpRequest.request(AddrbookConfig.FIND_CONTACT, findContactEvent.getJson(), new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//                FindContactRespEvent findContactRespEvent = null;
//                if (result != null && result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    LogUtil.d("aspire--获得用户数据为：" + result.getMessage());
//                    findContactRespEvent = handleRespData(result.getMessage());
//                    instance.notifyListener(findContactRespEvent);
//                } else {
//                    findContactRespEvent = new FindContactRespEvent();
//                    findContactRespEvent.setErrorCode(result == null ? ErrorCode.NETWORK_FAILED.getValue() : result.getErrorCode());
//                    LogUtil.e("aspire--同步通讯录失败");
//                    netFaiNotify(findContactRespEvent);
//                }
//            }
//        });
        return new Null();
    }

    private FindContactRespEvent handleRespData(String jsonResult) {
        FindContactRespEvent findContactRespEvent = new FindContactRespEvent();
        try {
            JSONObject jsonRoot = new JSONObject(jsonResult);
            //JSONObject jsonData = jsonRoot.getJSONObject("data");
            ArrayList<User> userItems = new ArrayList<User>();
            if (jsonRoot.has("data")) {
                JSONArray jsonListItem = jsonRoot.getJSONArray("data");


                JSONObject jsonItem = null;

                int itemsLen = jsonListItem.length();
                findContactRespEvent.setErrorCode(ErrorCode.SUCCESS.getValue());

                for (int i = 0; i < itemsLen; i++) {
                    jsonItem = jsonListItem.getJSONObject(i);

                    User userItem = (new Gson()).fromJson(jsonItem.toString(), User.class);
                    userItems.add(userItem);
                }
                if (userItems.size() > 0) {
                    List<User> users = dao.findByUserId(userItems.get(0).getUserid());
                    //存在本地用户
                    if (users.size() > 0) {
                        for (User user : userItems) {
                            //非临时用户
                            if (user.getIsTemp() == 0) {
                                user.setIsFriend(users.get(0).getIsFriend());
                                user.setIsSameCorp(users.get(0).getIsSameCorp());
                            }
                        }
                    }

                    for (User user : userItems) {
                        List<UserCorp> ucs = user.getCorpList();
                        for (int i = ucs.size() - 1; i >= 0; i--) {
                            if ("1".equals(ucs.get(i).getStatus())) {
                                ucs.remove(i);
                            }
                        }
                    }
                }
            }
            findContactRespEvent.setUsers(userItems);
        } catch (Exception e) {
            LogUtil.i("获取列表解析失败", e);
            findContactRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
        }
        return findContactRespEvent;
    }

}
