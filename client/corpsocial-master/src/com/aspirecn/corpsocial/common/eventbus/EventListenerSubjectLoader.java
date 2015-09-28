/**
 * @(#) EventListenerCache.java Created on 2013-11-13
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */

package com.aspirecn.corpsocial.common.eventbus;

import android.app.Activity;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.PropertyInfo;
import com.aspirecn.corpsocial.common.eventbus.ClassScanUtil.ClassFilter;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * The class <code>EventListenerCache</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class EventListenerSubjectLoader {

    private static EventListenerSubjectLoader container = null;
    private List<EventSubject<EventListener, BusEvent>> eventSubjectList = new ArrayList<EventSubject<EventListener, BusEvent>>();

    @SuppressWarnings("unchecked")
    private EventListenerSubjectLoader() {
        ClassFilter filter = new EventListenerSubjectClassFilter();

        @SuppressWarnings("rawtypes")
        List<Class> scanPackage = ClassScanUtil.scanPackage(AspirecnCorpSocial
                        .getContext(),
                PropertyInfo.getInstance().getString("eventBusScanPath"),
                filter);
        for (Class<EventSubject<EventListener, BusEvent>> clazz : scanPackage) {
            EventSubject<EventListener, BusEvent> newInstance = null;
            try {
                newInstance = clazz.newInstance();
            } catch (InstantiationException e) {
                LogUtil.e("实例化UI事件监听器类异常", e);
            } catch (IllegalAccessException e) {
                LogUtil.e("实例化UI事件监听器类异常", e);
            }
            eventSubjectList.add(newInstance);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private EventListenerSubjectLoader(List<Class> scanPackage) {
        ClassFilter filter = new EventListenerSubjectClassFilter();

        for (Class<EventSubject<EventListener, BusEvent>> clazz : scanPackage) {
            if (filter.accept(clazz)) {
                EventSubject<EventListener, BusEvent> newInstance = null;
                try {
                    newInstance = clazz.newInstance();
                } catch (InstantiationException e) {
                    LogUtil.e("实例化UI事件监听器类异常", e);
                } catch (IllegalAccessException e) {
                    LogUtil.e("实例化UI事件监听器类异常", e);
                }
                eventSubjectList.add(newInstance);
            }
        }
    }

    public static EventListenerSubjectLoader getInstance() {
        if (container == null) {
            container = new EventListenerSubjectLoader();
        }
        return container;
    }

    public static EventListenerSubjectLoader getInstance(
            @SuppressWarnings("rawtypes") List<Class> scanPackage) {
        if (container == null) {
            container = new EventListenerSubjectLoader(scanPackage);
        }
        return container;
    }

    /**
     * 注册事件监听器
     *
     * @param listener
     */
    public void registerListener(EventListener listener) {
        for (EventSubject<EventListener, BusEvent> subject : eventSubjectList) {
            Type mySuperClass = subject.getClass().getGenericSuperclass();
            Type listenerType = ((ParameterizedType) mySuperClass)
                    .getActualTypeArguments()[0];

            Class<?>[] interfaces = listener.getClass().getInterfaces();
            attach(subject, listenerType, listener, interfaces);

            if (listener.getClass().getSuperclass() == null) {
                continue;
            }
            Class<?>[] parentInterfaces = listener.getClass().getSuperclass()
                    .getInterfaces();
            attach(subject, listenerType, listener, parentInterfaces);

            if (listener.getClass().getSuperclass().getSuperclass() == null) {
                continue;
            }
            Class<?>[] sParentInterfaces = listener.getClass().getSuperclass()
                    .getSuperclass().getInterfaces();
            attach(subject, listenerType, listener, sParentInterfaces);
        }
    }

    private void attach(EventSubject<EventListener, BusEvent> subject, Type listenerType,
                        EventListener listener, Class<?>[] interfaces) {
        for (Class<?> listenerClass : interfaces) {
            if (listenerClass.equals(listenerType)) {
                if (!subject.contains(listener)) {
                    subject.attach(listener);
                }
            }
            attach(subject, listenerType, listener, listenerClass.getInterfaces());
        }
    }

    /**
     * 注销事件监听器
     *
     * @param listener
     */
    public void unregisterListener(EventListener listener) {
        for (EventSubject<EventListener, BusEvent> subject : eventSubjectList) {
            Type mySuperClass = subject.getClass().getGenericSuperclass();
            Type listenerType = ((ParameterizedType) mySuperClass)
                    .getActualTypeArguments()[0];

            Class<?>[] interfaces = listener.getClass().getInterfaces();
            detach(subject, listenerType, listener, interfaces);

            if (listener.getClass().getSuperclass() == null) {
                continue;
            }
            Class<?>[] parentInterfaces = listener.getClass().getSuperclass()
                    .getInterfaces();
            detach(subject, listenerType, listener, parentInterfaces);

            if (listener.getClass().getSuperclass().getSuperclass() == null) {
                continue;
            }
            Class<?>[] sParentInterfaces = listener.getClass().getSuperclass()
                    .getSuperclass().getInterfaces();
            detach(subject, listenerType, listener, sParentInterfaces);

        }
    }

    private void detach(EventSubject<EventListener, BusEvent> subject, Type listenerType,
                        EventListener listener, Class<?>[] interfaces) {
        for (Class<?> listenerClass : interfaces) {
            if (listenerClass.equals(listenerType)) {
                if (subject.contains(listener)) {
                    subject.detach(listener);
                }
            }
            detach(subject, listenerType, listener, listenerClass.getInterfaces());
        }
    }

    /**
     * 通知消息监听者
     *
     * @param event
     */
    public void notifyListener(BusEvent event) {
        for (EventSubject<EventListener, BusEvent> subject : eventSubjectList) {
            Type mySuperClass = subject.getClass().getGenericSuperclass();
            Type eventType = ((ParameterizedType) mySuperClass)
                    .getActualTypeArguments()[1];

            Class<? extends BusEvent> class1 = event.getClass();

            if (eventType.equals(class1)) {
                subject.notifyListener(event);
            }
        }
    }

    /**
     * 情况所有消息监听者
     */
    public void clearListener() {

        for (EventSubject<EventListener, BusEvent> subject : eventSubjectList) {
            List<EventListener> eventListenerQueue = subject
                    .getEventListenerQueue();
            for (EventListener listener : eventListenerQueue) {
                if (listener instanceof Activity) {
                    Activity activity = (Activity) listener;
                    activity.finish();
                }
            }
        }
    }

}
