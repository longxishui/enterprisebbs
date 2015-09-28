/**
 * @(#) AddressReceiveHandler.java Created on 2013-11-25
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.common.datareceiver;

import com.aspirecn.corpsocial.bundle.common.event.QuitEvent;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.CommandData;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.DataReceiveHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.LogUtil;

/**
 * @author lizhuo_a
 */
@DataReceiveHandler(commandType = CommandData.CommandType.QUIT)
public class QuitReceiveHandler implements IHandler<Null, CommandData> {

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(CommandData data) {
        LogUtil.d("收到推送通知：账号已在其他客户端登陆。");

        // 修改账号状态
        Config.getInstance().setAccountStatus(false);
        // 断开连接
        UiEventHandleFacade instance = UiEventHandleFacade.getInstance();
        instance.handle(new QuitEvent());

//        try {
//            Wire wire = new Wire();
//            Quit quit = wire.parseFrom(data.getData(), Quit.class);
//            // Quit quit = Quit.parseFrom(data.getData());
//            String quitInfo = quit.quitInfo;
//            QuitEvent event = new QuitEvent();
//            event.setQuitInfo(quitInfo);
//            eventListener.notifyListener(event);
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return new Null();
    }

}
