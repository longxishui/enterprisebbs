package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.common.event.RegisterEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

/**
 * 注册step1逻辑处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = RegisterEvent.class)
public class RegisterEventHandler implements IHandler<Null, RegisterEvent> {

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(RegisterEvent args) {
//        CommandData commandData = buildCommandData(args);

//        imNetClient.active(commandData, new IActiveNotify() {
//
//            @Override
//            public void notify(Result arg0) {
//                instance.notifyListener(new RegisterRespEvent(arg0
//                        .getErrorCode(), arg0.getMessage()));
//            }
//
//        });

        return new Null();
    }

//    private CommandData buildCommandData(RegisterEvent args) {
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
//        bodyBuilder.mobile(args.getMobilePhone());
//        bodyBuilder.registerStep(Register.RegisterStep.ACTIVECODE);
//
//        commandData.setCommandHeader(header);
//        commandData.setData(bodyBuilder.build().toByteArray());
//
//        return commandData;
//    }
}
