package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import android.content.Intent;
import android.os.Bundle;

import com.aspirecn.corpsocial.bundle.addrbook.event.CustomerServiceEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.CustomerServiceRespEvent;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.AppBroadcastManager;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by chenziqiang on 15-1-14.
 */
@UIEventHandler(eventType = CustomerServiceEvent.class)
public class DownloadCustomerServiceCenterEventHandler implements
        IHandler<Null, CustomerServiceEvent> {


    public static final String MESSAGE_CONTACT_CHANGED = "CUSTOMER_CHANGED";
    public static final String MESSAGE_CONTACT_DOWNLOADED = "CUSTOMER_DOWNLOADED";
//    @Autowired
//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();
    @Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();
    private AppBroadcastManager appBroadcastManager = AppBroadcastManager
            .getInstance();

    @Override
    public Null handle(final CustomerServiceEvent busEvent) {
//        CommandData commandData = new CommandData();
//        GetCustomerService.Builder builder = new GetCustomerService.Builder();
//        commandData.setCommandHeader(getCommandHeader(busEvent.getCorpId()));
//        commandData.setData(builder.build().toByteArray());


//        imNetClient.sendMessage(commandData, new IMessageStatusNotify() {
//            @Override
//            public void notify(MessageRst rst) {
//
//                if (rst.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    CommandData data = rst.getCommandRespData();
//                    if (data != null) {
//
//                        GetCustomerServiceResp resp = CommandData2GetAddressBookResp(data);
//                        if (resp != null) {
//                            // 获取联系人列表
//                            AddressBookList addressBookList = resp.customerServiceList;
//                            List<AddressBookList.AddressBook> list = addressBookList.addressBookList;
//
//                            // 保存联系人
//                            CustomerServiceDao sync = new CustomerServiceDao();
//                            sync.syncAddressBooks(Config.getInstance().getUserId(), list);
//
//                            if (list.size() > 0) {
////                                AddressBookConfig
////                                        .setContactLastTime(addressBookList.lastModifiedTime);
//
//                                // 通知通讯录有新变化
//                                notifyIM(MESSAGE_CONTACT_CHANGED, busEvent.getCorpId());
//                            }
//
//                            // 判断是否下载结束
//                            GetCustomerService sendData = CommandData2GetAddressBook(rst
//                                    .getSendCommandData());
//                            if (sendData != null) {
//                                if (isEnd(list.size(), 10)) {
//                                    // 下载结束，发出结束通知
//                                    notifyRefreshEnd(0, busEvent.getCorpId());
//                                } else {
//                                    // 下载未结束，获取下一页数据
//                                    getNextPageData(sendData, busEvent.getCorpId());
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    LogUtil.e(String.format("从服务端获取联系人失败：%d",
//                            rst.getErrorCode()));
////                    notifyRefreshEnd(rst.getErrorCode());
//                }
//            }
//        });

        return new Null();
    }


//    private CommandHeader getCommandHeader(String corpId) {
//        CommandHeader.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandType.GET_CUSTOMER_SERVICE);
//        headBuilder.sendTime(System.currentTimeMillis());
//        headBuilder.compressType(CompressType.GZIP_COMPRESS);
//        headBuilder.encryptType(EncryptType.UNENCRPT);
//        headBuilder.messageId(CommonUtils.getUUID());
//        headBuilder.userId(Config.getInstance().getUserId());
//        headBuilder.corpId(corpId);
//        // headBuilder.setContentType(ContentType.TEXT);
//        CommandHeader header = headBuilder.build();
//
//        return header;
//    }
//
//    private GetCustomerServiceResp CommandData2GetAddressBookResp(CommandData data) {
//        try {
//            Wire wire = new Wire();
//            return wire.parseFrom(data.getData(), GetCustomerServiceResp.class);
//
//            // return GetAddressBookResp.parseFrom(data.getData());
//        } catch (InvalidProtocolBufferException e) {
//            LogUtil.e(String.format(
//                    "从服务端获取联系人失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        } catch (IOException e) {
//            LogUtil.e(String.format(
//                    "从服务端获取联系人失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        }
//    }
//
//    private GetCustomerService CommandData2GetAddressBook(CommandData data) {
//        try {
//            Wire wire = new Wire();
//            return wire.parseFrom(data.getData(), GetCustomerService.class);
//            // return GetAddressBook.parseFrom(data.getData());
//        } catch (InvalidProtocolBufferException e) {
//            LogUtil.e(String.format(
//                    "从服务端获取联系人时，分析发送数据失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        } catch (IOException e) {
//            LogUtil.e(String.format(
//                    "从服务端获取联系人时，分析发送数据失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        }
//    }

    private boolean isEnd(int downloadRecordsCount, int pageSize) {
        return downloadRecordsCount < pageSize;
    }

    private void notifyRefreshEnd(int status, String corpId) {
//        AddressBookConfig.setContactSyn(true);
        ConfigPersonal.getInstance().setContactSyn(true);
        CustomerServiceRespEvent event = new CustomerServiceRespEvent();
        event.setErrorCode(status);
        eventListener.notifyListener(event);

        if (status == ErrorCode.SUCCESS.getValue()) {
            // 通知IM联系人已经下载完成。
            notifyIM(MESSAGE_CONTACT_DOWNLOADED, corpId);

            notifyImModuleSyncUserGroupList(corpId);

            notifyTaskModuleChange(corpId);

//            notifyMainModule();

            notifyRemindModuleChange();

            notifyDailyModuleChange(corpId);
        }

    }

//    private void notifyMainModule() {
//        EventListenerSubjectLoader.getInstance().notifyListener(
//                new ContactChangeEvent());
//    }

    private void notifyTaskModuleChange(String corpId) {
        Intent taskChangeIntent = new Intent("taskNotifyReceiver");
        Bundle taskChangeIntentBundle = new Bundle();
        taskChangeIntentBundle.putString("action", "updateTask");
        taskChangeIntentBundle.putString("corpId", corpId);
        taskChangeIntent.putExtras(taskChangeIntentBundle);
        appBroadcastManager.sendBroadcast(taskChangeIntent);
    }

    private void notifyRemindModuleChange() {
        Intent remindChangeIntent = new Intent("remindNotifyReceiver");
        Bundle remindChangeIntentBundle = new Bundle();
        remindChangeIntentBundle.putString("action", "loadRemind");
        remindChangeIntent.putExtras(remindChangeIntentBundle);
        appBroadcastManager.sendBroadcast(remindChangeIntent);
    }

    private void notifyImModuleSyncUserGroupList(String corpId) {
        Intent syncUserGroupListIntent = new Intent("imNotifyReceiver");
        Bundle syncUserGroupListIntentBundle = new Bundle();
        syncUserGroupListIntentBundle.putString("action", "syncUserGroupList");
        syncUserGroupListIntentBundle.putString("corpId", corpId);
        syncUserGroupListIntent.putExtras(syncUserGroupListIntentBundle);
        appBroadcastManager.sendBroadcast(syncUserGroupListIntent);
    }

    private void notifyDailyModuleChange(String corpId) {
        Intent intent = new Intent("dailyNotifyReceiver");
        Bundle bundle = new Bundle();
        bundle.putString("action", "upDailyDatabase");
        bundle.putString("corpId", corpId);
        intent.putExtras(bundle);

        appBroadcastManager.sendBroadcast(intent);
    }

    private void notifyIM(String message, String corpId) {
        Intent intent = new Intent("imNotifyReceiver");
        Bundle bundle = new Bundle();
        bundle.putString("action", message);
        bundle.putString("corpId", corpId);
        intent.putExtras(bundle);

        appBroadcastManager.sendBroadcast(intent);
    }

//    private void getNextPageData(GetCustomerService sendData, String corpId) {
//        CustomerServiceEvent event = new CustomerServiceEvent();
//        event.setCorpId(corpId);
//        handle(event);
//    }
}
