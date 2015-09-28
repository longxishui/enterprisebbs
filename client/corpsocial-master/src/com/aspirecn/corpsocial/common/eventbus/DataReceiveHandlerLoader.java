/**
 * @(#) EventListenerCache.java Created on 2013-11-13
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.PropertyInfo;
import com.aspirecn.corpsocial.common.eventbus.ClassScanUtil.ClassFilter;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.DataReceiveHandler;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class <code>EventListenerCache</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class DataReceiveHandlerLoader {

    private static DataReceiveHandlerLoader container = null;
    /**
     * 消息处理类集合
     */
    private Map<CommandData.CommandType, IHandler<?, CommandData>> dataReceiveHandlerMap = new HashMap<CommandData.CommandType, IHandler<?, CommandData>>();

    @SuppressWarnings("unchecked")
    private DataReceiveHandlerLoader() {
        ClassFilter filter = new DataReceiveHandlerClassFilter();

        @SuppressWarnings("rawtypes")
        List<Class> scanPackage = ClassScanUtil.scanPackage(AspirecnCorpSocial
                        .getContext(),
                PropertyInfo.getInstance().getString("eventBusScanPath"),
                filter);
        for (Class<IHandler<?, CommandData>> clazz : scanPackage) {
            if (filter.accept(clazz)) {
                Annotation annotation = clazz
                        .getAnnotation(DataReceiveHandler.class);

                CommandData.CommandType commandType = ((DataReceiveHandler) annotation)
                        .commandType();

                IHandler<?, CommandData> newInstance = null;
                try {
                    newInstance = clazz.newInstance();
                } catch (InstantiationException e) {
                    LogUtil.e("实例化数据接收类异常", e);
                } catch (IllegalAccessException e) {
                    LogUtil.e("实例化数据接收类异常", e);
                }
                dataReceiveHandlerMap.put(commandType, newInstance);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private DataReceiveHandlerLoader(List<Class> scanPackage) {
        ClassFilter filter = new DataReceiveHandlerClassFilter();

        for (Class<?> clazz : scanPackage) {
            if (filter.accept(clazz)) {
                Annotation annotation = clazz
                        .getAnnotation(DataReceiveHandler.class);

                CommandData.CommandType commandType = ((DataReceiveHandler) annotation)
                        .commandType();

                IHandler<?, CommandData> newInstance = null;
                try {
                    newInstance = (IHandler<?, CommandData>) clazz
                            .newInstance();
                } catch (InstantiationException e) {
                    LogUtil.e("实例化数据接收类异常", e);
                } catch (IllegalAccessException e) {
                    LogUtil.e("实例化数据接收类异常", e);
                }
                dataReceiveHandlerMap.put(commandType, newInstance);
            }
        }
    }

    public static DataReceiveHandlerLoader getInstance() {
        if (container == null) {
            container = new DataReceiveHandlerLoader();
        }
        return container;
    }

    @SuppressWarnings("rawtypes")
    public static DataReceiveHandlerLoader getInstance(List<Class> scanPackage) {
        if (container == null) {
            container = new DataReceiveHandlerLoader(scanPackage);
        }
        return container;
    }

    public IHandler<?, CommandData> getHandler(CommandData.CommandType commandType) {
        IHandler<?, CommandData> handler = dataReceiveHandlerMap
                .get(commandType);
        return handler;
    }

}
