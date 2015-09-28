package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindContactBatchEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindContactBatchRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
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
 * Created by yinghuihong on 15/7/15.
 */
@EventBusAnnotation.UIEventHandler(eventType = FindContactBatchEvent.class)
public class FindContactBatchEventHandle extends EventHandler implements IHandler<Null, FindContactBatchEvent> {

    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public Null handle(FindContactBatchEvent event) {
        LogUtil.i("aspire--请求的json数据为：" + event.getJson());
//        HttpRequest.request(AddrbookConfig.FIND_CONTACT_BATCH, event.getJson(), new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//                FindContactBatchRespEvent findContactRespEvent = null;
//                if (result != null && result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    LogUtil.d("aspire--获得用户数据为：" + result.getMessage());
//                    findContactRespEvent = handleRespData(result.getMessage());
//                    instance.notifyListener(findContactRespEvent);
//                } else {
//                    findContactRespEvent = new FindContactBatchRespEvent();
//                    findContactRespEvent.setErrorCode(result == null ? ErrorCode.NETWORK_FAILED.getValue() : result.getErrorCode());
//                    LogUtil.e("aspire--同步通讯录失败");
//                    netFaiNotify(findContactRespEvent);
//                }
//            }
//        });
        return null;
    }

    private FindContactBatchRespEvent handleRespData(String jsonResult) {
        FindContactBatchRespEvent findContactRespEvent = new FindContactBatchRespEvent();
        try {
            JSONObject jsonRoot = new JSONObject(jsonResult);

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
                for (User user : userItems) {
                    user.setIsSameCorp(0);
                    user.setIsTemp(0);
                }
                dao.createOrUpdataContact(userItems);

            }

        } catch (Exception e) {
            LogUtil.i("获取列表解析失败", e);
            findContactRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
        }
        return findContactRespEvent;
    }
}
