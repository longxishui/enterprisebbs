package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.common.event.GetConfigListEvent;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.config.NotifyConfig;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * 获取用户配置列表
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = GetConfigListEvent.class)
public class GetConfigListEventHandler implements
        IHandler<Null, GetConfigListEvent> {

    @Override
    public Null handle(final GetConfigListEvent args) {
//        CommandData commandData = buildReqData();
//        IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();
//        imNetClient.sendMessage(commandData, new IMessageStatusNotify() {
//            @Override
//            public void notify(MessageRst rst) {
//                if (rst.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    CommandData commandRespData = rst.getCommandRespData();
//
//                    handleSuccessResult(commandRespData);
//                }
//            }
//        });

        return new Null();
    }

//    private CommandData buildReqData() {
//        CommandData commandData = new CommandData();
//
//        CommandHeader.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandType.GET_CONFIG_LIST);
//        headBuilder.compressType(CompressType.UN_COMPRESS);
//        headBuilder.encryptType(EncryptType.UNENCRPT);
//        headBuilder.messageId(CommonUtils.getUUID());
//        headBuilder.userId(Config.getInstance().getUserId());
//        headBuilder.corpId(Config.getInstance().getCorpId());
//        headBuilder.sendTime(System.currentTimeMillis());
//        CommandHeader header = headBuilder.build();
//
//        GetConfigList.Builder bodyBuilder = new GetConfigList.Builder();
//
//        commandData.setCommandHeader(header);
//        commandData.setData(bodyBuilder.build().toByteArray());
//
//        return commandData;
//    }
//
//    private void handleSuccessResult(CommandData commandRespData) {
//        Wire wire = new Wire();
//        try {
//            GetConfigListResp getConfigListResp = wire.parseFrom(
//                    commandRespData.getData(), GetConfigListResp.class);
//
//            JSONArray appNotifyArray = new JSONArray();
//            try {
//                for (ConfigList configList : getConfigListResp.corpConfigList) {
//                    JSONObject moduleNotifyObject = new JSONObject();
//                    String moduleKey = configList.key;
//                    List<ConfigInfo> corpConfigList1 = configList.corpConfigList;
//
//                    JSONArray moduleNotifyArray = new JSONArray();
//                    for (ConfigInfo configInfo : corpConfigList1) {
//                        String areaKey = configInfo.key;
//                        String areaValue = configInfo.value;
//
//                        JSONObject areaNotifyObject = new JSONObject();
//
//                        areaNotifyObject.put("areaKey", areaKey);
//                        areaNotifyObject.put("areaValue", areaValue);
//
//                        moduleNotifyArray.put(areaNotifyObject);
//                    }
//                    moduleNotifyObject.put("moduleKey", moduleKey);
//                    moduleNotifyObject.put("moduleValue", moduleNotifyArray);
//                    appNotifyArray.put(moduleNotifyObject);
//                }
//            } catch (JSONException e) {
//                LogUtil.e("组装通知配置失败", e);
//            }
//            String notifyConfigValue = appNotifyArray.toString();
//
//            // 存储配置信息
//            ConfigPersonal instance = ConfigPersonal.getInstance();
//            instance.setNotifyConfig(notifyConfigValue);
//            // 初始化提醒配置类
//            NotifyConfig.getInstance().init(notifyConfigValue);
//
//        } catch (IOException e) {
//            LogUtil.e("解析配置信息失败", e);
//        }
//    }

}
