package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.RefreshAddrbookEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RefreshDepartmentEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RefreshDepartmentRespEvent;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

/**
 * 用户主动刷新部门事件处理类
 *
 * @author meixuesong
 */
@UIEventHandler(eventType = RefreshDepartmentEvent.class)
public class RefreshDepartmentEventHandler implements
        IHandler<Null, RefreshDepartmentEvent> {

//    @Autowired
//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    @Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(RefreshDepartmentEvent busEvent) {

//        GetDeptList.Builder builder = new GetDeptList.Builder();
//        builder.fromTime(ConfigPersonal.getInstance().getDeptLastTime());
//
//        CommandData commandData = new CommandData();
//        commandData.setCommandHeader(getCommandHeader());
//        commandData.setData(builder.build().toByteArray());

//        imNetClient.sendMessage(commandData, new IMessageStatusNotify() {
//            @Override
//            public void notify(MessageRst rst) {
//                if (rst.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    CommandData data = rst.getCommandRespData();
//                    if (data != null) {
//                        try {
//                            Wire wire = new Wire();
//                            GetDeptListResp resp = wire.parseFrom(data.getData(), GetDeptListResp.class);
//                            DeptList deptList = resp.deptList;
//                            if (deptList != null) {
//                                // 保存到DB
//                                if (deptList.deptList != null && deptList.deptList.size() != 0) {
//                                    new DeptListSave2LocalDB().syncDepartments(deptList.deptList);
//                                }
//                                System.out.println("主动同步部门数据，lastTime=" + deptList.lastModifiedTime);
//                                // 保存更新时间
//                                if (deptList.lastModifiedTime != null) {
//                                    ConfigPersonal.getInstance().setDeptLastTime(deptList.lastModifiedTime);
//                                }
//                            } else {
//                                LogUtil.e("同步部门时，resp.getDeptList()返回为空.");
//                            }
//                        } catch (InvalidProtocolBufferException e) {
//                            LogUtil.e(String.format("从服务端获取部门失败：InvalidProtocolBufferException, %s", e.getMessage()));
//                        } catch (IOException e) {
//                            LogUtil.e(String.format("从服务端获取部门失败：InvalidProtocolBufferException, %s", e.getMessage()));
//                        }
//                    }
//
//                    // 下载通讯录
//                    UiEventHandleFacade.getInstance().handle(new RefreshAddrbookEvent());
//                } else {
//                    LogUtil.e(String.format("从服务端获取部门失败：%d", rst.getErrorCode()));
//                }
//
//                RefreshDepartmentRespEvent event = new RefreshDepartmentRespEvent();
//                event.setErrorCode(rst.getErrorCode());
//                event.setMessage(rst.getMessage());
//                eventListener.notifyListener(event);
//            }
//        });

        return new Null();
    }

//    private CommandHeader getCommandHeader() {
//        CommandHeader.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandType.GET_DEPT_LIST);
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

}
