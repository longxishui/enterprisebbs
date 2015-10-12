package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.Dept;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetDepartListEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetDepartListRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.CorpModified;
import com.aspirecn.corpsocial.bundle.addrbook.repository.DepartDao;
import com.aspirecn.corpsocial.bundle.addrbook.utils.AddrbookConfig;
import com.aspirecn.corpsocial.bundle.common.domain.GetSelfCorpListRespBean;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Amos on 15-6-16.
 */
@EventBusAnnotation.UIEventHandler(eventType = GetDepartListEvent.class)
public class GetDepartListEventHandler extends EventHandler implements IHandler<Null, GetDepartListEvent> {

    @EventBusAnnotation.Component
    private DepartDao dao ;

    @Override
    public Null handle(final GetDepartListEvent getDepartListEvent) {

        List<CorpModified> cms = getCorpModified();
        getDepartListEvent.setLastModifiedList(cms);
        getDepartListEvent.setLimit(1000);

        processTask(getDepartListEvent);


        return new Null();
    }

    private void processTask(final GetDepartListEvent getDepartListEvent) {
        LogUtil.i("aspire---请求的json数据为：" + getDepartListEvent.getJson());

//        HttpRequest.request(AddrbookConfig.GET_DEPART_LIST, getDepartListEvent.getJson(), true, new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//                GetDepartListRespEvent getDepartListRespEvent = null;
//                if (result != null && result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    LogUtil.d("aspire---获得组织数据为：" + result.getMessage());
//                    getDepartListRespEvent = handleRespData(result.getMessage());
//                    instance.notifyListener(getDepartListRespEvent);
//                } else {
//                    AddrbookConfig.reset();
//                    getDepartListRespEvent = new GetDepartListRespEvent();
//                    getDepartListRespEvent.setErrorCode(result == null ? ErrorCode.NETWORK_FAILED.getValue() : result.getErrorCode());
//                    LogUtil.e("aspire---获取组织数据失败");
//
//                    netFaiNotify(getDepartListRespEvent);
//                }
//            }
//        });
    }

    private GetDepartListRespEvent handleRespData(String jsonResult) {
        GetDepartListRespEvent getDepartListRespEvent = new GetDepartListRespEvent();
        try {
            JSONObject jsonRoot = new JSONObject(jsonResult);
            //JSONObject jsonData = jsonRoot.getJSONObject("data");
            if (jsonRoot.has("data")) {
                JSONArray jsonListItem = jsonRoot.getJSONArray("data");
                ArrayList<Dept> deptItems = new ArrayList<Dept>();

                JSONObject jsonItem = null;

                int itemsLen = jsonListItem.length();
                getDepartListRespEvent.setErrorCode(ErrorCode.SUCCESS.getValue());
                Map<String, Long> modifiedCops = new HashMap();
                for (int i = 0; i < itemsLen; i++) {
                    jsonItem = jsonListItem.getJSONObject(i);

                    Dept deptItem = (new Gson()).fromJson(jsonItem.toString(), Dept.class);
                    deptItems.add(deptItem);
                    if (modifiedCops.containsKey(deptItem.getCorpId())) {
                        if (modifiedCops.get(deptItem.getCorpId()) < deptItem.getLastModifiedTime()) {
                            modifiedCops.put(deptItem.getCorpId(), deptItem.getLastModifiedTime());
                        }
                    } else {
                        modifiedCops.put(deptItem.getCorpId(), deptItem.getLastModifiedTime());
                    }
                }
                dao.createOrUpdataDept(deptItems);
                /**
                 * update every corp lasmodifiedtime
                 */
                Iterator<String> iterator = modifiedCops.keySet().iterator();
                while (iterator.hasNext()) {
                    String corpId = iterator.next();
                    GetSelfCorpListRespBean.updateLastModifedTime(corpId, modifiedCops.get(corpId));
                }

                getDepartListRespEvent.setDepts(deptItems);

                /**
                 * 通知拉通讯录信息
                 */
                runNextevents(GetDepartListEvent.class.getSimpleName());
            } else {
                runNextevents(GetDepartListEvent.class.getSimpleName());
            }
        } catch (Exception e) {
            LogUtil.e("获取列表解析失败", e);
            getDepartListRespEvent.setErrorCode(ErrorCode.OTHER_ERROR.getValue());
        }

        return getDepartListRespEvent;
    }
}
