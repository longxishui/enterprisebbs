package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.common.event.ResetPasswordActiveEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

import java.util.Calendar;

/**
 * 重置密码-设置新密码处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = ResetPasswordActiveEvent.class)
public class ResetPasswordActiveEventHandler implements
        IHandler<Null, ResetPasswordActiveEvent> {

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(ResetPasswordActiveEvent args) {
//        CommandData commandData = buildCommandData(args);

//        imNetClient.active(commandData, new IActiveNotify() {
//            @Override
//            public void notify(Result arg0) {
//                instance.notifyListener(new ResetPasswordActiveRespEvent(arg0
//                        .getErrorCode(), arg0.getMessage()));
//
//            }
//        });

        return new Null();
    }

//    private CommandData buildCommandData(ResetPasswordActiveEvent args) {
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
//        bodyBuilder.activeCode(args.getActiveCode());
//        bodyBuilder.passwd(args.getNewPassword());
//        bodyBuilder.resetStep(ResetStep.RESET_PWD);
//
//        commandData.setCommandHeader(header);
//        commandData.setData(bodyBuilder.build().toByteArray());
//
//        return commandData;
//    }

}
