/**
 * @(#) Handlera.java Created on 2013-11-29
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.aspirecn.corpsocial.common.eventbus.CommandData;
/**
 * The class <code>Handlera</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class EventBusAnnotation {

    /**
     * 应用范围广播接收器
     *
     * @author lizhuo_a
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AppBroadcastReceiver {
        String intentFilter();
    }

    /**
     * 文件访问类注解
     *
     * @author lizhuo_a
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface File {

    }

    /**
     * SQLLite数据库访问类注解
     *
     * @author lizhuo_a
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Dao {
        /**
         * 模块名
         */
        String name();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UIEventHandler {
        Class<? extends BusEvent> eventType();
    }

    /**
     * The class <code>ReceiveHandler</code>
     * <p/>
     * 接收网络侧数据处理类
     *
     * @author lizhuo_a
     * @version 1.0
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DataReceiveHandler {

        CommandData.CommandType commandType();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DataReceiveUnpacker {

        int commandType();
    }

    /**
     * 状态变更处理类
     *
     * @author lizhuo_a
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface StatusChangeHandler {
        /**
         * 错误码
         */
        ErrorCode errorCode();

        /**
         * 状态
         */
        int status();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EventListenerSubject {
    }

    /**
     * 文件访问类注解
     *
     * @author lizhuo_a
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Autowired {

    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Component {

    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SingleTask {
    }
}
