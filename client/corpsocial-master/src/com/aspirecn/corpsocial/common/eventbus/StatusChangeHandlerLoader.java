/**
 * @(#) EventListenerCache.java Created on 2013-11-13
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

import android.annotation.SuppressLint;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.PropertyInfo;
import com.aspirecn.corpsocial.common.eventbus.ClassScanUtil.ClassFilter;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.StatusChangeHandler;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class <code>StatusChangeHandlerLoader</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
@SuppressLint("UseSparseArrays")
public class StatusChangeHandlerLoader {

    private static StatusChangeHandlerLoader container = null;
    /**
     * 状态改变处理类集合
     */
    private Map<StatusHandlerKey, IHandler<?, Integer>> statusChangeHandlerMap = new HashMap<StatusHandlerKey, IHandler<?, Integer>>();

    @SuppressWarnings("unchecked")
    private StatusChangeHandlerLoader() {
        ClassFilter filter = new StatusChangeHandlerClassFilter();

        @SuppressWarnings("rawtypes")
        List<Class> scanPackage = ClassScanUtil.scanPackage(AspirecnCorpSocial
                        .getContext(),
                PropertyInfo.getInstance().getString("eventBusScanPath"),
                filter);
        for (Class<IHandler<?, Integer>> clazz : scanPackage) {
            Annotation annotation = clazz
                    .getAnnotation(StatusChangeHandler.class);

            Integer status = ((StatusChangeHandler) annotation).status();
            ErrorCode errorCode = ((StatusChangeHandler) annotation)
                    .errorCode();
            IHandler<?, Integer> newInstance = null;
            try {
                newInstance = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            statusChangeHandlerMap.put(new StatusHandlerKey(errorCode, status),
                    newInstance);
        }
    }

    @SuppressWarnings("unchecked")
    private StatusChangeHandlerLoader(
            @SuppressWarnings("rawtypes") List<Class> scanPackage) {
        ClassFilter filter = new StatusChangeHandlerClassFilter();

        for (Class<IHandler<?, Integer>> clazz : scanPackage) {
            if (filter.accept(clazz)) {
                Annotation annotation = clazz
                        .getAnnotation(StatusChangeHandler.class);
                ErrorCode errorCode = ((StatusChangeHandler) annotation)
                        .errorCode();
                int status = ((StatusChangeHandler) annotation).status();
                IHandler<?, Integer> newInstance = null;
                try {
                    newInstance = clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                statusChangeHandlerMap.put(new StatusHandlerKey(errorCode,
                        status), newInstance);
            }
        }
    }

    public static StatusChangeHandlerLoader getInstance() {
        if (container == null) {
            container = new StatusChangeHandlerLoader();
        }
        return container;
    }

    public static StatusChangeHandlerLoader getInstance(
            @SuppressWarnings("rawtypes") List<Class> scanPackage) {
        if (container == null) {
            container = new StatusChangeHandlerLoader(scanPackage);
        }
        return container;
    }

    public IHandler<?, Integer> getHandler(StatusHandlerKey statusHandlerKey) {
        IHandler<?, Integer> handler = statusChangeHandlerMap
                .get(statusHandlerKey);
        return handler;
    }

}
