package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import android.content.Intent;
import android.os.Bundle;

import com.aspirecn.corpsocial.bundle.addrbook.event.RefreshAddrbookEvent;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.AppBroadcastManager;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户主动刷新通讯录事件处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = RefreshAddrbookEvent.class)
public class RefreshAddrbookEventHandler implements
        IHandler<Null, RefreshAddrbookEvent> {

    public static final String MESSAGE_CONTACT_CHANGED = "CONTACT_CHANGED";
    public static final String MESSAGE_CONTACT_DOWNLOADED = "CONTACT_DOWNLOADED";
    private static boolean flag = false;
//    @Autowired
//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();
//    @Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();
    private AppBroadcastManager appBroadcastManager = AppBroadcastManager
            .getInstance();
    private Object obj = new Object();
//    private List<AddressBookList> taddrs = new ArrayList();
    private Map<String, RefreshAddrbookEvent> events = new HashMap();

    @Override
    public Null handle(final RefreshAddrbookEvent busEvent) {
//        CommandData commandData = new CommandData();
//
//        GetAddressBook.Builder builder = new GetAddressBook.Builder();
//
//        if (busEvent.isRefreshUpdateTime()) {
////            long lastTime = AddressBookConfig.getContactLastTime();
//
//            long lastTime = ConfigPersonal.getInstance().getContactLastTime();
//
//            builder.fromTime(lastTime);
//            //builder.startPos(0);
//            builder.startPos(busEvent.getOffset());
//            builder.limit(busEvent.getPageSize());
//            LogUtil.d("请求联系人数据，lastTime: " + lastTime);
//            synchronized (obj) {
//
//                if (!flag) {
//                    flag = true;
//                    taddrs.clear();
//                }
//            }
//        } else {
//            builder.userId(busEvent.getUserId());
//
//            synchronized (obj) {
//                if (flag) return new Null();
//                if (events.containsKey(busEvent.getUserId())) {
//                    return new Null();
//                } else {
//                    events.put(busEvent.getUserId(), busEvent);
//                }
//            }
//        }
//        commandData.setCommandHeader(getCommandHeader());
//        commandData.setData(builder.build().toByteArray());


        //LogUtil.d("aspire-----------start:" + new java.util.Date());
//        imNetClient.sendMessage(commandData, new IMessageStatusNotify() {
//            @Override
//            public void notify(MessageRst rst) {
//                //LogUtil.d("aspire-----------receive:" + new java.util.Date());
//                if (rst.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    CommandData data = rst.getCommandRespData();
//                    if (data != null) {
//                        GetAddressBookResp resp = CommandData2GetAddressBookResp(data);
//                        if (resp != null) {
//                            // 获取联系人列表
//                            AddressBookList addressBookList = resp.addressBookList;
//                            List<AddressBook> list = addressBookList.addressBookList;
//                            taddrs.add(addressBookList);
//                            // 保存联系人
////                            AddressBookSave2LocalDB sync = new AddressBookSave2LocalDB();
////                            sync.syncAddressBooks(list);
//                            //LogUtil.d("aspire-----------end:" + new java.util.Date());
//                            if (busEvent.isRefreshUpdateTime()) {
//                                if (list.size() > 0) {
////                                    AddressBookConfig.setContactLastTime(addressBookList.lastModifiedTime);
////                                    ConfigPersonal.getInstance().setContactLastTime(addressBookList.lastModifiedTime);
////                                    // 通知通讯录有新变化
////                                    notifyIM(MESSAGE_CONTACT_CHANGED);
//                                }
//
//                                // 判断是否下载结束
//                                GetAddressBook sendData = CommandData2GetAddressBook(rst
//                                        .getSendCommandData());
//
//                                if (sendData != null) {
//                                    if (isEnd(list.size(), sendData.limit)) {
//                                        //LogUtil.d("aspire-----------start save:" + new java.util.Date());
//                                        AddressBookSave2LocalDB sync = new AddressBookSave2LocalDB();
//                                        long lastModifiedTime = 0;
//                                        List<AddressBook> alist = new ArrayList<AddressBook>();
//                                        for (AddressBookList taddressBookList : taddrs) {
//                                            List<AddressBook> tlist = taddressBookList.addressBookList;
//
//                                            //sync.syncAddressBooks(tlist);
//                                            alist.addAll(tlist);
//                                            if (tlist.size() > 0) {
//                                                if (taddressBookList.lastModifiedTime != null) {
//                                                    if (taddressBookList.lastModifiedTime > lastModifiedTime) {
//                                                        lastModifiedTime = taddressBookList.lastModifiedTime;
//                                                    }
//                                                }
//                                            }
//                                        }
//                                        sync.syncAddressBooks(alist);
//                                        if (lastModifiedTime > ConfigPersonal.getInstance().getContactLastTime()) {
//                                            ConfigPersonal.getInstance().setContactLastTime(lastModifiedTime);
//                                        }
//
//                                        notifyIM(MESSAGE_CONTACT_CHANGED);
//                                        //LogUtil.d("aspire-----------end save:" + new java.util.Date());
//                                        flag = false;
//                                        // 下载结束，发出结束通知
//                                        notifyRefreshEnd(rst);
//                                    } else {
//                                        // 下载未结束，获取下一页数据
//                                        busEvent.setOffset(taddrs.size() * busEvent.getPageSize());
//                                        getNextPageData(sendData, busEvent);
//                                    }
//                                }
//                            } else {
//                                events.remove(busEvent.getUserId());
//                            }
//                        }
//                    }
//                } else {
//                    LogUtil.e(String.format("从服务端获取联系人失败：%d",
//                            rst.getErrorCode()));
//                    if (busEvent.isRefreshUpdateTime()) {
//                        flag = false;
//                    } else {
//                        events.remove(busEvent.getUserId());
//                    }
//                    notifyRefreshEnd(rst);
//                }
//            }
//        });

        return new Null();
    }

//    private CommandHeader getCommandHeader() {
//        CommandHeader.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandType.GET_ADDRESS_BOOK);
//        headBuilder.sendTime(System.currentTimeMillis());
//        headBuilder.compressType(CompressType.GZIP_COMPRESS);
//        headBuilder.encryptType(EncryptType.UNENCRPT);
//        headBuilder.messageId(CommonUtils.getUUID());
//        headBuilder.userId(Config.getInstance().getUserId());
//        headBuilder.corpId(Config.getInstance().getCorpId());
//        // headBuilder.setContentType(ContentType.TEXT);
//        CommandHeader header = headBuilder.build();
//
//        return header;
//    }
//
//    private GetAddressBookResp CommandData2GetAddressBookResp(CommandData data) {
//        try {
//            Wire wire = new Wire();
//            return wire.parseFrom(data.getData(), GetAddressBookResp.class);
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
//    private GetAddressBook CommandData2GetAddressBook(CommandData data) {
//        try {
//            Wire wire = new Wire();
//            return wire.parseFrom(data.getData(), GetAddressBook.class);
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
        if (downloadRecordsCount == 0 || downloadRecordsCount == 1) return true;
        return downloadRecordsCount < pageSize;
    }

//    private void notifyMainModule() {
//        EventListenerSubjectLoader.getInstance().notifyListener(
//                new ContactChangeEvent());
//    }

//    private void notifySimpleProcessModuleChange() {
//        Intent intent = new Intent("simpleProcessNotifyReceiver");
//        Bundle bundle = new Bundle();
//        bundle.putString("action", "updateSimpleProcess");
//        intent.putExtras(bundle);
//        appBroadcastManager.sendBroadcast(intent);
//    }

//    private void notifyTaskModuleChange() {
//        Intent taskChangeIntent = new Intent("taskNotifyReceiver");
//        Bundle taskChangeIntentBundle = new Bundle();
//        taskChangeIntentBundle.putString("action", "updateTask");
//        taskChangeIntent.putExtras(taskChangeIntentBundle);
//        appBroadcastManager.sendBroadcast(taskChangeIntent);
//    }

//    private void notifyRemindModuleChange() {
//        Intent remindChangeIntent = new Intent("remindNotifyReceiver");
//        Bundle remindChangeIntentBundle = new Bundle();
//        remindChangeIntentBundle.putString("action", "loadRemind");
//        remindChangeIntent.putExtras(remindChangeIntentBundle);
//        appBroadcastManager.sendBroadcast(remindChangeIntent);
//    }


    private void notifyIM(String message) {
        Intent intent = new Intent("imNotifyReceiver");
        Bundle bundle = new Bundle();
        bundle.putString("action", message);
        intent.putExtras(bundle);

        appBroadcastManager.sendBroadcast(intent);
    }

    private void notifySimpleProcessUpdate() {
        Intent i = new Intent("simpleProcessNotifyReceiver");
        i.putExtra("action", "updateSimpleProcess");
        appBroadcastManager.sendBroadcast(i);
    }

    private void notifyImModuleSyncUserGroupList() {
        Intent i = new Intent("imNotifyReceiver");
        i.putExtra("action", "syncUserGroupList");
        appBroadcastManager.sendBroadcast(i);
    }

    private void notifyTaskUpdate() {
        Intent i = new Intent("taskNotifyReceiver");
        i.putExtra("action", "updateTask");
        appBroadcastManager.sendBroadcast(i);
    }

    private void notifyDailyUpdate() {
        Intent i = new Intent("dailyNotifyReceiver");
        i.putExtra("action", "updateDaily");
        appBroadcastManager.sendBroadcast(i);
    }

    private void notifyAppCenterUpdate() {
        Intent i = new Intent("appDefNotifyReceiver");
        i.putExtra("action", "updateAppDef");
        appBroadcastManager.sendBroadcast(i);
    }
}
