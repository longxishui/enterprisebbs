package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import android.content.Intent;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.domain.Users;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetAddressListEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetAddressListRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RefreshAddrbookRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.CorpModified;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindCorpService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.utils.AddrbookConfig;
import com.aspirecn.corpsocial.bundle.common.domain.GetSelfCorpListRespBean;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.bundle.common.service.NotifyService;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.AppBroadcastManager;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.util.BadgeUtil;
import com.aspirecn.corpsocial.common.util.HttpRequest;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

/**
 * Created by Amos on 15-6-16.
 */

@EventBusAnnotation.UIEventHandler(eventType = GetAddressListEvent.class)
public class GetAddressListEventHandler extends EventHandler implements IHandler<Null, GetAddressListEvent> {

    @EventBusAnnotation.Component
    private UserDao dao ;
    private AppBroadcastManager appBroadcastManager = AppBroadcastManager
            .getInstance();

    @Override
    public Null handle(final GetAddressListEvent getAddressListEvent) {

        List<CorpModified> cms = getCorpModified();
        getAddressListEvent.setLimit(200);
        getAddressListEvent.setLastModifiedList(cms);

        processTask(getAddressListEvent);

        return new Null();
    }

    private void processTask(final GetAddressListEvent getAddressListEvent) {
        LogUtil.i("aspire---请求的json数据为：" + getAddressListEvent.getJson());
//        HttpRequest.request(AddrbookConfig.GET_ADDRESS_LIST, getAddressListEvent.getJson(), true, new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//
//                if (result != null && result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    LogUtil.i("aspire---获得通讯录数据为：" + result.getMessage());
//                    handleRespData(result.getMessage(), getAddressListEvent);
//                } else {
//                    processFailed(getAddressListEvent);
//                    GetAddressListRespEvent getAddressListRespEvent = new GetAddressListRespEvent();
//                    getAddressListRespEvent.setErrorCode(result == null ? ErrorCode.NETWORK_FAILED.getValue() : result.getErrorCode());
//                    LogUtil.e("aspire---获取通讯录失败");
//                    AddrbookConfig.reset();
//                    netFaiNotify(getAddressListRespEvent);
//                }
//            }
//        });
    }

    private GetAddressListRespEvent handleRespData(String jsonResult, GetAddressListEvent getAddressListEvent) {
        GetAddressListRespEvent getAddressListRespEvent = new GetAddressListRespEvent();
        try {
            JSONObject jsonRoot = new JSONObject(jsonResult);
            if (jsonRoot.has("data")) {
                JSONArray jsonListItem = jsonRoot.getJSONArray("data");
                ArrayList<User> userItems = new ArrayList<User>();
                getAddressListRespEvent.setErrorCode(ErrorCode.SUCCESS.getValue());
                userItems.addAll((new Gson()).fromJson(jsonRoot.toString(), Users.class).getData());
                process(userItems, getAddressListEvent);

            } else {
                process(new ArrayList<User>(), getAddressListEvent);

            }
        } catch (Exception e) {
            LogUtil.e("aspire---获取列表解析失败", e);

            getAddressListRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
        }
        return getAddressListRespEvent;
    }

    /**
     * 通知其它模块同步数据
     */
    private void notifySync() {
        // 同步群组
        notifyImModuleSyncUserGroupList();
        // 通知工作台的模块

        List<UserCorp> userCorps = (List<UserCorp>) OsgiServiceLoader.getInstance().getService(FindCorpService.class).invoke(new Null());

        // 遍历企业
        for (UserCorp userCorp : userCorps) {
            String corpId = userCorp.getCorpId();
            notifySimpleProcessUpdate(corpId);
            notifyTaskUpdate(corpId);
            notifyDailyUpdate(corpId);
            notifyAppCenterUpdate(corpId);
        }
    }

    private boolean isEnd(int downloadRecordsCount, int pageSize) {
        return downloadRecordsCount < pageSize;
    }

    private void filter(List<User> data) {
        try {
            List<UserCorp> userCorps = GetSelfCorpListRespBean.find(Config.getInstance().getUserId());
            for (User user : data) {
                boolean exist = false;
                for (UserCorp uc : user.getCorpList()) {
                    for (UserCorp euc : userCorps) {
                        if ("0".equals(uc.getStatus()) && euc.getCorpId().equals(uc.getCorpId())) {
                            exist = true;
                            break;
                        }
                    }
                    if (exist) break;
                }
                if (!exist) {
                    for (UserCorp uc : user.getCorpList()) {
                        uc.setStatus("1");
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e("", e);
        }
    }

    private void processFailed(GetAddressListEvent getAddressListEvent) {

        // 启动NotifyService
        AspirecnCorpSocial.getContext().startService(new Intent(AspirecnCorpSocial.getContext(), NotifyService.class));

        GetAddressListRespEvent getAddressListRespEvent = new GetAddressListRespEvent();
        getAddressListRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
        instance.notifyListener(getAddressListRespEvent);

        RefreshAddrbookRespEvent event = new RefreshAddrbookRespEvent();
        event.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
        instance.notifyListener(event);

        runNextevents(GetAddressListEvent.class.getSimpleName());
        notifySync();
        //标识拉取通讯录过程结束
        AddrbookConfig.reset();


    }

    private void process(ArrayList<User> userItems, GetAddressListEvent getAddressListEvent) {

        LogUtil.i("aspire--start save user:" + userItems.size());
        filter(userItems);

        if (ConfigPersonal.getInstance().getFirstTime() == 1) {
            for (User user : userItems) {
                user.setIsSameCorp(1);
            }
            dao.createOrUpdataContact(userItems);
        } else {
            dao.createContact(userItems);

        }
        LogUtil.i("aspire---end save user");
        Map<String, Long> modifiedCops = new HashMap();
        long last = 0;
        for (User userItem : userItems) {
            List<UserCorp> ucs = userItem.getCorpList();
            for (UserCorp uc : ucs) {
                if (modifiedCops.containsKey(uc.getCorpId())) {
                    if (modifiedCops.get(uc.getCorpId()) < userItem.getLastModifiedTime()) {
                        modifiedCops.put(uc.getCorpId(), userItem.getLastModifiedTime());
                    }
                } else {
                    modifiedCops.put(uc.getCorpId(), userItem.getLastModifiedTime());
                }
            }

            if (userItem.getLastModifiedTime() > last) {
                last = userItem.getLastModifiedTime();
            }
        }
        Iterator<String> iterator = modifiedCops.keySet().iterator();
        while (iterator.hasNext()) {
            String corpId = iterator.next();
            try {
                GetSelfCorpListRespBean.updateUserLastModifedTime(corpId, modifiedCops.get(corpId));
            } catch (Exception e) {
                LogUtil.e("更新用户企业最新时间出错", e);
            }
        }

        if (last > 1) {
            ConfigPersonal.getInstance().setContactLastTime(last);
        }

        if (isEnd(userItems.size(), getAddressListEvent.getLimit())) {
            if (ConfigPersonal.getInstance().getFirstTime() == 0) {
                ConfigPersonal.getInstance().setFirstTime();
            }
            ConfigPersonal.getInstance().setAddrStatus(1);
            // 启动NotifyService
            AspirecnCorpSocial.getContext().startService(new Intent(AspirecnCorpSocial.getContext(), NotifyService.class));

            GetAddressListRespEvent getAddressListRespEvent = new GetAddressListRespEvent();
            getAddressListRespEvent.setErrorCode(ErrorCode.SUCCESS.getValue());
//            GetAddressListRespEvent getAddressListRespEvent2 = new GetAddressListRespEvent();
//            getAddressListRespEvent2.setErrorCode(ErrorCode.SUCCESS.getValue());
            instance.notifyListener(getAddressListRespEvent);
            //instance.notifyListener(getAddressListRespEvent2);

            RefreshAddrbookRespEvent event = new RefreshAddrbookRespEvent();
            event.setErrorCode(ErrorCode.SUCCESS.getValue());
            instance.notifyListener(event);

            runNextevents(GetAddressListEvent.class.getSimpleName());
            BadgeUtil.setBadgeCount(AspirecnCorpSocial.getContext(), 10);
            notifySync();
            AddrbookConfig.reset();
        } else {
            getAddressListEvent.setStart(getAddressListEvent.getStart() + getAddressListEvent.getLimit());
            processTask(getAddressListEvent);
        }
    }


    private void notifySimpleProcessUpdate(String corpId) {
        Intent i = new Intent("simpleProcessNotifyReceiver");
        i.putExtra("action", "updateSimpleProcess");
        i.putExtra("corpId", corpId);
        appBroadcastManager.sendBroadcast(i);
    }

    private void notifyImModuleSyncUserGroupList() {
        Intent i = new Intent("imNotifyReceiver");
        i.putExtra("action", "syncUserGroupList");
        appBroadcastManager.sendBroadcast(i);
    }

    private void notifyTaskUpdate(String corpId) {
        Intent i = new Intent("taskNotifyReceiver");
        i.putExtra("action", "updateTask");
        i.putExtra("corpId", corpId);
        appBroadcastManager.sendBroadcast(i);
    }

    private void notifyDailyUpdate(String corpId) {
        Intent i = new Intent("dailyNotifyReceiver");
        i.putExtra("action", "updateDaily");
        i.putExtra("corpId", corpId);
        appBroadcastManager.sendBroadcast(i);
    }

    private void notifyAppCenterUpdate(String corpId) {
        Intent i = new Intent("appDefNotifyReceiver");
        i.putExtra("action", "updateAppDef");
        i.putExtra("corpId", corpId);
        appBroadcastManager.sendBroadcast(i);
    }

}
