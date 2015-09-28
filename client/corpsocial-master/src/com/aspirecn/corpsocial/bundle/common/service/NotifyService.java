package com.aspirecn.corpsocial.bundle.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.aspirecn.corpsocial.bundle.common.event.GetConfigListEvent;
import com.aspirecn.corpsocial.bundle.common.repository.NotifyDao;
import com.aspirecn.corpsocial.bundle.common.repository.entity.NotifyEntity;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.AppBroadcastManager;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;

import java.util.List;

/**
 * 消息通知服务
 * <p/>
 * Created by yinghuihong on 15/7/1.
 */
public class NotifyService extends Service {

    private IBinder binder = new NotifyService.LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // limit after sync address book
        if (ConfigPersonal.getInstance().getAddrStatus() != 1) {
            return START_STICKY;
        }

        if (ConfigPersonal.getInt(ConfigPersonal.Key.CORP_STATUS) != 1) {
            return START_STICKY;
        }

        // 取出待通知消息
        NotifyDao dao = new NotifyDao();
        List<NotifyEntity> entities = dao.queryUnHandled();
        for (NotifyEntity entity : entities) {
//            notifyMsg(entity);
            entity.isHandled = true;
            dao.update(entity);
        }

        return START_STICKY;
    }

//    private void notifyMsg(NotifyEntity entity) {
//        if (entity.modelId == ModelType.ADDRESSBOOK.getValue()) {
//            notifyAddrbookModuleChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.TASK.getValue()) {
//            notifyTaskModuleChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.NEWS.getValue()) {
//            notifyNewsModuleChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.BBS.getValue()) {
//            notifyBBSModuleChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.PROCESS.getValue()) {
//            notifyProcessModuleChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.APPDEF.getValue()) {
//            notifyAppDefModuleChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.CONFIGED.getValue()) {
//            notifyConfigChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.APP_COMMON_DATA.getValue()) {
//            notifyAppCommonDataChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.SIGNED_CONFIG.getValue()) {
//            notifyCheckinConfigChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.SIMPLE_PROCESS.getValue()) {
//            notifySimpleProcessChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.CUSTOMER_SERVICE.getValue()) {
//            notifyCustomerServiceChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.REPORT.getValue()) {
//            notifyDailyServiceChange(entity.belongCorpId);
//        } else if (entity.modelId == ModelType.GROUP_LIST.getValue()) {
//            notifyCorpChange(entity.belongCorpId);
//        }
//    }

    private void notifyCorpChange(String corpId) {

        Intent intent = new Intent("commonNotifyReceiver");
        intent.putExtra("action", "corpChange");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void notifyTaskModuleChange(String corpId) {
        Intent intent = new Intent("taskNotifyReceiver");
        intent.putExtra("action", "updateTask");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void notifyAppDefModuleChange(String corpId) {
        Intent intent = new Intent("appDefNotifyReceiver");
        intent.putExtra("action", "updateAppDef");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void notifyAddrbookModuleChange(String corpId) {
        Intent intent = new Intent("addrbookNotifyReceiver");
        intent.putExtra("action", "updateAddrbook");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void notifyNewsModuleChange(String corpId) {
        Intent intent = new Intent("newsNotifyReceiver");
        intent.putExtra("action", "updateNews");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void notifyBBSModuleChange(String corpId) {
        Intent intent = new Intent("bbsNotifyReceiver");
        intent.putExtra("action", "updateBbs");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void notifyProcessModuleChange(String corpId) {
        Intent intent = new Intent("processNotifyReceiver");
        intent.putExtra("action", "updateProcess");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void notifyAppCommonDataChange(String corpId) {
        Intent intent = new Intent("appDefNotifyReceiver");
        intent.putExtra("action", "appCommonDataChange");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void notifyCheckinConfigChange(String corpId) {
        Intent intent = new Intent("checkinNotifyReceiver");
        intent.putExtra("action", "checkinConfigChange");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void notifySimpleProcessChange(String corpId) {
        Intent intent = new Intent("simpleProcessNotifyReceiver");
        intent.putExtra("action", "updateSimpleProcess");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void notifyConfigChange(String corpId) {
        UiEventHandleFacade.getInstance().handle(new GetConfigListEvent(corpId));
    }

    private void notifyCustomerServiceChange(String corpId) {
        Intent intent = new Intent("customerNotifyReceiver");
        intent.putExtra("action", "updateCustomer");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void notifyDailyServiceChange(String corpId) {
        Intent intent = new Intent("dailyNotifyReceiver");
        intent.putExtra("action", "updateDaily");
        intent.putExtra("corpId", corpId);
        AppBroadcastManager.getInstance().sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }
}
