package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.common.event.ConnectEvent;
import com.aspirecn.corpsocial.bundle.common.event.ConnectRespEvent;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

/**
 * Created by yinghuihong on 15/3/31.
 */
public class ConnectEventHandler implements IHandler<Null, ConnectEvent> {
    @Override
    public Null handle(ConnectEvent args) {

//        CommandData commandData = buildReqData(args.getVeritifyCode());
//        IMNetClientImpl.getIMNetClient().sendMessage(commandData, new IMessageStatusNotify() {
//            @Override
//            public void notify(MessageRst arg0) {
//
//                int errorCode = arg0.getErrorCode();
//
//                ConnectRespEvent respEvent = new ConnectRespEvent();
//                respEvent.setErrorCode(errorCode);
//                EventListenerSubjectLoader.getInstance().notifyListener(respEvent);
//            }
//        });

        return new Null();
    }


//    private CommandData buildReqData(String veritifyCode) {
//        CommandData commandData = new CommandData();
//
//        CommandHeader.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandType.CONNECT);
//        headBuilder.sendTime(System.currentTimeMillis());
//        headBuilder.compressType(CompressType.UN_COMPRESS);
//        headBuilder.encryptType(EncryptType.UNENCRPT);
//        headBuilder.messageId(CommonUtils.getUUID());
//        headBuilder.userId(Config.getInstance().getUserId());
//        headBuilder.corpId(Config.getInstance().getCorpId());
//        CommandHeader header = headBuilder.build();
//        commandData.setCommandHeader(header);
//
//        Connect.Builder builder = new Connect.Builder();
//        builder.veritifyCode(veritifyCode);
//        commandData.setData(builder.build().toByteArray());
//
//        return commandData;
//    }


}
