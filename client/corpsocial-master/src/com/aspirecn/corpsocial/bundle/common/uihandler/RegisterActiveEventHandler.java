package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.common.event.RegisterActiveEvent;
import com.aspirecn.corpsocial.bundle.common.event.RegisterActiveRespEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

/**
 * 注册-激活逻辑处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = RegisterActiveEvent.class)
public class RegisterActiveEventHandler implements
        IHandler<Null, RegisterActiveEvent> {

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(RegisterActiveEvent args) {
//        CommandData commandData = buildCommandData(args);

//        imNetClient.active(commandData, new IActiveNotify() {
//            @Override
//            public void notify(Result arg0) {
//                instance.notifyListener(new RegisterActiveRespEvent(arg0
//                        .getErrorCode(), arg0.getMessage()));
//            }
//        });

        return new Null();
    }

//    private CommandData buildCommandData(RegisterActiveEvent args) {
//        CommandData commandData = new CommandData();
//
//        CommandHeader.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandType.REGISTER);
//        headBuilder.compressType(CompressType.UN_COMPRESS);
//        headBuilder.encryptType(EncryptType.UNENCRPT);
//        headBuilder.messageId(CommonUtils.getUUID());
//        headBuilder.sendTime(System.currentTimeMillis());
//        CommandHeader header = headBuilder.build();
//
//        Register.Builder bodyBuilder = new Register.Builder();
//        bodyBuilder.activeCode(args.getActiveCode());
//        bodyBuilder.passwd(args.getPassword());
//        bodyBuilder.mobile(args.getMobilePhone());
//        bodyBuilder.registerStep(Register.RegisterStep.ACTIVE);
//
//        commandData.setCommandHeader(header);
//        commandData.setData(bodyBuilder.build().toByteArray());
//
//        return commandData;
//    }

}
