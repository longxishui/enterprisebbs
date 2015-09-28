/**
 * @(#) AddressReceiveHandler.java Created on 2013-11-25
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.common.datareceiver;

import android.content.Intent;

import com.aspirecn.corpsocial.bundle.common.repository.NotifyDao;
import com.aspirecn.corpsocial.bundle.common.repository.entity.NotifyEntity;
import com.aspirecn.corpsocial.bundle.common.service.NotifyService;
import com.aspirecn.corpsocial.bundle.common.uitils.NotifyConfigUtil;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.CommandData;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.DataReceiveHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author lizhuo_a
 */
@DataReceiveHandler(commandType = CommandData.CommandType.NOTIFY)
public class NotifyReceiveHandler implements IHandler<Null, CommandData> {


    @Override
    public Null handle(CommandData data) {
        LogUtil.d("收到推送通知：有变更。");

//        try {

//            Wire wire = new Wire();
//            Notify notify = wire.parseFrom(data.getData(), Notify.class);
//            ModelType modelType = notify.modelId;
//            LogUtil.i("model:"+modelType.getValue()+" "+NotifyConfigUtil.isEnable(modelType.getValue()));
//            if(NotifyConfigUtil.isEnable(modelType.getValue())) {
//                // 存储到DB，应该先做过滤处理（去重）
//                NotifyEntity entity = new NotifyEntity();
//                entity.belongUserId = Config.getInstance().getUserId();
//                entity.belongCorpId = data.getCommandHeader().corpId;
//                entity.modelId = modelType.getValue();
//                entity.model = modelType.toString();
//                entity.receiveDate = new Date();
//                new NotifyDao().insert(entity);

                // 启动NotifyService
                AspirecnCorpSocial.getContext().startService(new Intent(AspirecnCorpSocial.getContext(), NotifyService.class));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return new Null();
    }



}
