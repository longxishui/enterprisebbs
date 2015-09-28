package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindGroupMemberContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.utils.AddrbookConfig;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
import com.aspirecn.corpsocial.bundle.im.facade.UpdateGroupMemberService;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.util.HttpRequest;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenbin on 2015/7/25.
 */
@EventBusAnnotation.UIEventHandler(eventType = FindGroupMemberContactEvent.class)
public class FindGroupMemberContactEventHandler extends EventHandler implements IHandler<Null, FindGroupMemberContactEvent> {

    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public Null handle(FindGroupMemberContactEvent event) {
//        HttpRequest.request(AddrbookConfig.FIND_CONTACT_BATCH, event.getJson(), new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//
//                if (result != null && result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    handleRespData(result.getMessage());
//
//                }
//            }
//        });
        return null;
    }

    private void handleRespData(String jsonResult) {

        try {
            JSONObject jsonRoot = new JSONObject(jsonResult);

            ArrayList<User> userItems = new ArrayList<User>();
            if (jsonRoot.has("data")) {
                JSONArray jsonListItem = jsonRoot.getJSONArray("data");


                JSONObject jsonItem = null;

                int itemsLen = jsonListItem.length();

                List<String> ids = new ArrayList<>();
                for (int i = 0; i < itemsLen; i++) {
                    jsonItem = jsonListItem.getJSONObject(i);

                    User userItem = (new Gson()).fromJson(jsonItem.toString(), User.class);
                    userItems.add(userItem);
                    ids.add(userItem.getUserid());
                }
                for (User user : userItems) {
                    user.setIsSameCorp(0);
                    user.setIsTemp(0);
                }
                dao.createOrUpdataContact(userItems);
                OsgiServiceLoader.getInstance()
                        .getService(UpdateGroupMemberService.class).invoke(ids);
            }

        } catch (Exception e) {
            LogUtil.i("��ȡ�б����ʧ��", e);

        }

    }
}
