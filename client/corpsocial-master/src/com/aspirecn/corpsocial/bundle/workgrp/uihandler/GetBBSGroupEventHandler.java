package com.aspirecn.corpsocial.bundle.workgrp.uihandler;

import com.aspirecn.corpsocial.bundle.net.HttpCallBack;
import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSGroup;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSGroupEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSGroupRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSGroupDao;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSGroupEntity;
import com.aspirecn.corpsocial.bundle.workgrp.util.HttpMessage;
import com.aspirecn.corpsocial.bundle.workgrp.util.WorkgrpConfig;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.HttpRequest;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@UIEventHandler(eventType = GetBBSGroupEvent.class)
public class GetBBSGroupEventHandler implements IHandler<Null, GetBBSGroupEvent> {

    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(final GetBBSGroupEvent getGroupListEvent) {
        LogUtil.i("bbsGroupHandler开始请求");
        if (getGroupListEvent.getCorpId() == null) {
            getGroupListEvent.setCorpId(ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED));
        }
        HttpRequest.request(WorkgrpConfig.GET_BBS_GROUP, getGroupListEvent.getJson(), new HttpCallBack() {
            @Override
            public void notifyResult(int errorCode,String message) {
                GetBBSGroupRespEvent getBBSGroupRespEvent = new GetBBSGroupRespEvent();
                if (errorCode == ErrorCode.SUCCESS.getValue()) {
                    LogUtil.i("获得同事圈数据为：" + message);
                    HttpMessage httpMessage = WorkgrpConfig.getHttpMessage(message);
                    if (httpMessage.httpResult == 0) {
                        getBBSGroupRespEvent = handleRespData(httpMessage.data, getGroupListEvent.getCorpId());
                        getBBSGroupRespEvent.setErrcode(errorCode);
                    } else {
                        getBBSGroupRespEvent.setErrcode(ErrorCode.NETWORK_FAILED.getValue());
                    }
                } else {
                    getBBSGroupRespEvent.setErrcode(errorCode);
                }
                instance.notifyListener(getBBSGroupRespEvent);
            }
        });

        return new Null();
    }

    private GetBBSGroupRespEvent handleRespData(String jsonString, String corpId) {
        GetBBSGroupRespEvent bbsGroupRespEvent = new GetBBSGroupRespEvent();
        bbsGroupRespEvent.setChange(false);
        LogUtil.i("获取bbsgroup:" + jsonString);
        try {
            JSONObject jsonRoot = new JSONObject(jsonString);
            JSONArray jsonGroupList = jsonRoot.getJSONArray("list");
            ArrayList<BBSGroup> mBbsGroups = new ArrayList<BBSGroup>();
            BBSGroupDao bbsGroupDao = new BBSGroupDao();
            BBSGroupEntity entity = null;
            BBSGroup mGroup = null;
            boolean isChangeGroup = false;
            /** 判断group是否有更新 */
            if (jsonGroupList.length() == 0 || jsonGroupList.length() != bbsGroupDao.findAllGroups(corpId).size()) {
                isChangeGroup = true;
            }
            if (!isChangeGroup) {
                /** 判断group是否有修改 */
                for (int i = 0; i < jsonGroupList.length(); i++) {
                    JSONObject jsonGroup = jsonGroupList.getJSONObject(i);
                    BBSGroupEntity bbsGroupEntity = bbsGroupDao.findByGroupId(jsonGroup.getString("id"));
                    if (bbsGroupEntity == null) {
                        isChangeGroup = true;
                        break;
                    } else {
                        if (!(bbsGroupEntity.getLastModifiedTime() == jsonGroup.getLong("lastModifiedTime"))) {
                            isChangeGroup = true;
                            break;
                        }
                    }
                }
            }
            bbsGroupDao.clearAllData(corpId);
            for (int j = 0; j < jsonGroupList.length(); j++) {
                mGroup = new BBSGroup();
                JSONObject jsonGroupItem = jsonGroupList.getJSONObject(j);
                entity = new BBSGroupEntity();
                entity.setBelongUserId(Config.getInstance().getUserId());
                entity.setBelongCorpId(corpId);
                entity.setId(jsonGroupItem.getString("id"));
                entity.setName(jsonGroupItem.getString("name"));
                entity.setIconurl(jsonGroupItem.getString("iconurl"));
                entity.setSortNo(jsonGroupItem.getString("sortNo"));
                entity.setLastModifiedTime(jsonGroupItem.getLong("lastModifiedTime"));
                JSONArray jsonAdminList = jsonGroupItem.getJSONArray("adminList");
                List<String> admins = new ArrayList<String>();
                mGroup.setBbsGroupEntity(entity);
                int len = jsonAdminList.length();
                StringBuffer bbsGroupAdmins = new StringBuffer();
                for (int ai = 0; ai < len; ai++) {
                    String adminId = jsonAdminList.getString(ai);
                    bbsGroupAdmins.append(adminId);
                    admins.add(adminId);
                    if (ai != (len - 1)) {
                        bbsGroupAdmins.append("-");
                    }
                }
                mGroup.setAdmins(admins);
                mBbsGroups.add(mGroup);
                entity.setAdminList(bbsGroupAdmins.toString());
//                entity.setLimitType(jsonGroupItem.getInt("limitType"));
                entity.setLimitType(0);
                bbsGroupDao.insertEntity(entity);
            }
            bbsGroupRespEvent.setBbsGroups(mBbsGroups);
            bbsGroupRespEvent.setChange(isChangeGroup);
            bbsGroupRespEvent.setErrcode(ErrorCode.SUCCESS.getValue());
        } catch (Exception e) {
            bbsGroupRespEvent.setChange(false);
            e.printStackTrace();
        }
        return bbsGroupRespEvent;
    }

}
