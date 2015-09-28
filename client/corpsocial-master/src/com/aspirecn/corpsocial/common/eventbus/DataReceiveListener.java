/**
 * @(#) EventBusNetAdapter.java Created on 2013-11-12
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

import com.aspirecn.corpsocial.common.util.LogUtil;

/**
 * The class <code>MessageDispatcher</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class DataReceiveListener {

    public void onReceiver(CommandData data) {
        CommandHead commandHeader = data.getCommandHeader();

        CommandData.CommandType commandType = commandHeader.commandType;

        LogUtil.i(String.format("接收消息内容 %s :", data.toString()));

        IHandler handler = DataReceiveHandlerLoader.getInstance().getHandler(commandType);
        if (handler != null) {
            handler.handle(data);
        }
    }
}
