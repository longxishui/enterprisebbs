package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindMyEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindMyRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.utils.AddrbookConfig;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
import com.aspirecn.corpsocial.common.config.Config;
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
 * Created by amos on 15-7-1.
 */
@EventBusAnnotation.UIEventHandler(eventType = FindMyEvent.class)
public class FindMyEventHandler extends EventHandler implements IHandler<Null, FindMyEvent> {

    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public Null handle(final FindMyEvent findMyEvent) {
        findMyEvent.setUserid(Config.getInstance().getUserId());
        LogUtil.i("请求的json数据为：" + findMyEvent.getJson());
//        HttpRequest.request(AddrbookConfig.FIND_CONTACT, findMyEvent.getJson(), new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//                FindMyRespEvent findMyRespEvent = null;
//                if (result != null && result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    LogUtil.d("获得用户数据为：" + result.getMessage());
//                    findMyRespEvent = handleRespData(result.getMessage());
//                    instance.notifyListener(findMyRespEvent);
//                } else {
//                    findMyRespEvent = new FindMyRespEvent();
//                    findMyRespEvent.setErrorCode(result == null ? ErrorCode.NETWORK_FAILED.getValue() : result.getErrorCode());
//                    LogUtil.e("同步通讯录失败");
//                    netFaiNotify(findMyRespEvent);
//                }
//            }
//        });
        return new Null();
    }

    private FindMyRespEvent handleRespData(String jsonResult) {
        FindMyRespEvent findMyRespEvent = new FindMyRespEvent();
        try {
            JSONObject jsonRoot = new JSONObject(jsonResult);
            //JSONObject jsonData = jsonRoot.getJSONObject("data");
            ArrayList<User> userItems = new ArrayList<User>();
            if (jsonRoot.has("data")) {
                JSONArray jsonListItem = jsonRoot.getJSONArray("data");


                JSONObject jsonItem = null;

                int itemsLen = jsonListItem.length();
                findMyRespEvent.setErrorCode(ErrorCode.SUCCESS.getValue());

                for (int i = 0; i < itemsLen; i++) {
                    jsonItem = jsonListItem.getJSONObject(i);

                    User userItem = (new Gson()).fromJson(jsonItem.toString(), User.class);
                    userItems.add(userItem);
                    userItem.setIsSameCorp(1);
                }
                if (userItems.size() > 0) {
                    List<User> users = dao.findByUserId(userItems.get(0).getUserid());

                    if (users.size() == 0) {
                        dao.createOrUpdataContact(userItems);
                    }
                }
            }

        } catch (Exception e) {
            LogUtil.i("获取列表解析失败");

            findMyRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
        }
        return findMyRespEvent;
    }
}
