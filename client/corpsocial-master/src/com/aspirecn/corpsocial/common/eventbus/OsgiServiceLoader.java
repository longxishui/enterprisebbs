/**
 * @(#) EventListenerCache.java Created on 2013-11-13
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.PropertyInfo;
import com.aspirecn.corpsocial.common.eventbus.ClassScanUtil.ClassFilter;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation.OsgiService;
import com.aspirecn.corpsocial.common.util.BeanContainer;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizhuo_a
 */
public class OsgiServiceLoader {

    private static OsgiServiceLoader container = null;
    /**
     * 消息处理类集合
     */
    @SuppressWarnings("rawtypes")
    private Map<Class<? extends IOsgiService>, IOsgiService> osgiServiceMap = new HashMap<Class<? extends IOsgiService>, IOsgiService>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    private OsgiServiceLoader() {
        ClassFilter filter = new OsgiServiceClassFilter();

        List<Class> scanPackage = ClassScanUtil.scanPackage(AspirecnCorpSocial
                        .getContext(),
                PropertyInfo.getInstance().getString("eventBusScanPath"),
                filter);
        for (Class<? extends IOsgiService> clazz : scanPackage) {
            if (filter.accept(clazz)) {
                Annotation annotation = clazz.getAnnotation(OsgiService.class);

                Class<? extends IOsgiService> serviceType = ((OsgiService) annotation)
                        .serviceType();
//                LogUtil.i("aspire-----"+clazz.getSimpleName());
//                Class[] clss= clazz.getInterfaces();
//                for(Class c:clss){
//                    LogUtil.i("aspire-----"+c.getSimpleName());
//                }
                IOsgiService newInstance = null;
                try {
                    newInstance = clazz.newInstance();
                    BeanContainer.getInstance().fetchBean(newInstance);
                } catch (InstantiationException e) {
                    LogUtil.e("实例化Osgi Service类异常", e);
                } catch (IllegalAccessException e) {
                    LogUtil.e("实例化Osgi Service类异常", e);
                }
                osgiServiceMap.put(serviceType, newInstance);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private OsgiServiceLoader(List<Class> scanPackage) {
        ClassFilter filter = new OsgiServiceClassFilter();

        for (Class<? extends IOsgiService> clazz : scanPackage) {
            if (filter.accept(clazz)) {
                Annotation annotation = clazz.getAnnotation(OsgiService.class);

                Class<? extends IOsgiService> serviceType = ((OsgiService) annotation)
                        .serviceType();

                IOsgiService newInstance = null;
                try {
                    newInstance = clazz.newInstance();
                } catch (InstantiationException e) {
                    LogUtil.e("实例化Osgi Service类异常", e);
                } catch (IllegalAccessException e) {
                    LogUtil.e("实例化Osgi Service类异常", e);
                }
                osgiServiceMap.put(serviceType, newInstance);
            }
        }
    }

    public static OsgiServiceLoader getInstance() {
        if (container == null) {
            container = new OsgiServiceLoader();
        }
        return container;
    }

    public static OsgiServiceLoader getInstance(
            @SuppressWarnings("rawtypes") List<Class> scanPackage) {
        if (container == null) {
            container = new OsgiServiceLoader(scanPackage);
        }
        return container;
    }

    public IOsgiService getService(Class<? extends IOsgiService> serviceType) {
        return osgiServiceMap.get(serviceType);
    }

}
