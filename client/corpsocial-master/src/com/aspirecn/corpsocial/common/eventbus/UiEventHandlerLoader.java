/**
 * @(#) EventListenerCache.java Created on 2013-11-13
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.PropertyInfo;
import com.aspirecn.corpsocial.common.eventbus.ClassScanUtil.ClassFilter;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.util.BeanContainer;
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
public class UiEventHandlerLoader {

    private static UiEventHandlerLoader container = null;
    /**
     * 消息处理类集合
     */
    private Map<Class<? extends BusEvent>, IHandler<?, ?>> uiEventHandlerMap = new HashMap<Class<? extends BusEvent>, IHandler<?, ?>>();

    @SuppressWarnings("unchecked")
    private UiEventHandlerLoader(List<Class> scanPackage) {
        ClassFilter filter = new UiEventHandlerClassFilter();

        for (Class<IHandler<?, ?>> clazz : scanPackage) {
            if (filter.accept(clazz)) {
                Annotation annotation = clazz
                        .getAnnotation(UIEventHandler.class);

                Class<? extends BusEvent> eventType = ((UIEventHandler) annotation)
                        .eventType();

                IHandler<?, ?> newInstance = null;
                try {
                    newInstance = clazz.newInstance();
                    BeanContainer.getInstance().fetchBean(newInstance);
                } catch (InstantiationException e) {
                    LogUtil.e("实例化UI处理类异常", e);
                } catch (IllegalAccessException e) {
                    LogUtil.e("实例化UI处理类异常", e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                uiEventHandlerMap.put(eventType, newInstance);
            }
        }
    }

    private UiEventHandlerLoader(){
        ClassFilter filter = new UiEventHandlerClassFilter();

        List<Class> scanPackage = ClassScanUtil.scanPackage(AspirecnCorpSocial
                        .getContext(),
                PropertyInfo.getInstance().getString("eventBusScanPath"),
                filter);

        for (Class<IHandler<?, ?>> clazz : scanPackage) {
            if (filter.accept(clazz)) {
                Annotation annotation = clazz
                        .getAnnotation(UIEventHandler.class);

                Class<? extends BusEvent> eventType = ((UIEventHandler) annotation)
                        .eventType();

                IHandler<?, ?> newInstance = null;
                try {
                    newInstance = clazz.newInstance();
                    BeanContainer.getInstance().fetchBean(newInstance);
                } catch (InstantiationException e) {
                    LogUtil.e("实例化UI处理类异常", e);
                } catch (IllegalAccessException e) {
                    LogUtil.e("实例化UI处理类异常", e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                uiEventHandlerMap.put(eventType, newInstance);
            }
        }
    }

    public static UiEventHandlerLoader getInstance() {
        if (container == null) {
            container = new UiEventHandlerLoader();
        }
        return container;
    }

    public static UiEventHandlerLoader getInstance(List<Class> scanPackage) {
        if (container == null) {
            container = new UiEventHandlerLoader(scanPackage);
        }
        return container;
    }

    public IHandler<?, ?> getEventHandler(Class<? extends BusEvent> clazz) {
        return uiEventHandlerMap.get(clazz);
    }

}
