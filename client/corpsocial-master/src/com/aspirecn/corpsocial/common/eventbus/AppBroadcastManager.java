package com.aspirecn.corpsocial.common.eventbus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.PropertyInfo;
import com.aspirecn.corpsocial.common.eventbus.ClassScanUtil.ClassFilter;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.AppBroadcastReceiver;
import com.aspirecn.corpsocial.common.util.BeanContainer;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 应用广播管理器
 *
 * @author lizhuo_a
 */
public class AppBroadcastManager {

    private static AppBroadcastManager container = null;
    private LocalBroadcastManager localBroadcastManager;

    @SuppressWarnings("unchecked")
    private AppBroadcastManager(Context context,
                                @SuppressWarnings("rawtypes") List<Class> scanPackage) {
        init(context, scanPackage);
    }

    public AppBroadcastManager() {
        List<Class> scanPackage = ClassScanUtil.scanPackage(AspirecnCorpSocial.getContext(),
                PropertyInfo.getInstance().getString("eventBusScanPath"));
        init(AspirecnCorpSocial.getContext(), scanPackage);
    }

    private void init(Context context,
                      @SuppressWarnings("rawtypes") List<Class> scanPackage) {
        localBroadcastManager = LocalBroadcastManager.getInstance(context);

        ClassFilter filter = new AppBroadcastReceiverClassFilter();

        for (Class<BroadcastReceiver> clazz : scanPackage) {
            if (filter.accept(clazz)) {
                Annotation annotation = clazz
                        .getAnnotation(AppBroadcastReceiver.class);

                String intentFilter = ((AppBroadcastReceiver) annotation)
                        .intentFilter();

                BroadcastReceiver newInstance = null;
                try {
                    newInstance = clazz.newInstance();
                    BeanContainer.getInstance().fetchBean(newInstance);
                } catch (InstantiationException e) {
                    LogUtil.e("实例化广播接受类异常", e);
                } catch (IllegalAccessException e) {
                    LogUtil.e("实例化广播接受类异常", e);
                }

                localBroadcastManager.registerReceiver(newInstance,
                        new IntentFilter(intentFilter));
            }
        }
    }

    public static AppBroadcastManager getInstance() {
        if (container == null) {
            container = new AppBroadcastManager();
        }
        return container;
    }

    public static AppBroadcastManager getInstance(Context context,
                                                  @SuppressWarnings("rawtypes") List<Class> scanPackage) {
        if (container == null) {
            container = new AppBroadcastManager(context, scanPackage);
        }
        return container;
    }

    public boolean sendBroadcast(Intent intent) {
        return localBroadcastManager.sendBroadcast(intent);
    }

}
