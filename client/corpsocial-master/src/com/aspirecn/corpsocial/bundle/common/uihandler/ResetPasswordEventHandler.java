package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.common.event.ResetPasswordEvent;
import com.aspirecn.corpsocial.bundle.common.event.ResetPasswordRespEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

import java.util.Calendar;

/**
 * 重置密码处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = ResetPasswordEvent.class)
public class ResetPasswordEventHandler implements
        IHandler<Null, ResetPasswordEvent> {

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(ResetPasswordEvent args) {
//        CommandData commandData = buildCommandData(args);

//        imNetClient.resetPasswd(commandData, new IActiveNotify() {
//            @Override
//            public void notify(Result arg0) {
//                instance.notifyListener(new ResetPasswordRespEvent(arg0
//                        .getErrorCode(), arg0.getMessage()));
//
//            }
//        });

        return new Null();
    }

//    private CommandData buildCommandData(ResetPasswordEvent args) {
//        CommandData commandData = new CommandData();
//
//        CommandHeader.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandType.RESET_PASSWD);
//        headBuilder.compressType(CompressType.UN_COMPRESS);
//        headBuilder.encryptType(EncryptType.UNENCRPT);
//        headBuilder.messageId(CommonUtils.getUUID());
//        headBuilder.sendTime(Calendar.getInstance().getTimeInMillis());
//        CommandHeader header = headBuilder.build();
//
//        ResetPasswd.Builder bodyBuilder = new ResetPasswd.Builder();
//        bodyBuilder.phoneNum(args.getMobilePhone());
//        bodyBuilder.resetStep(ResetStep.ACTIVECODE);
//
//        commandData.setCommandHeader(header);
//        commandData.setData(bodyBuilder.build().toByteArray());
//
//        return commandData;
//    }

}
