/**
 * @(#) EventBusLoader.java Created on 2013-11-28
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

import android.content.Context;

import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.config.PropertyInfo;
import com.aspirecn.corpsocial.common.service.ConnectionChangeReceiver;
import com.aspirecn.corpsocial.common.util.AssetsUtils;
import com.aspirecn.corpsocial.common.util.BeanContainer;
import com.aspirecn.corpsocial.common.util.EventChainUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The class <code>EventBusLoader</code>
 * <p/>
 * 事件总线加载器
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class EventBusLoader {

    private static EventBusLoader loader;

    private EventBusLoader(Context context) {

        String eventBusScanPath = PropertyInfo.getInstance().getString("eventBusScanPath");
        @SuppressWarnings("rawtypes")
        List<Class> scanPackage = ClassScanUtil.scanPackage(context, eventBusScanPath);
        // 1. 初始化数据库
        String databaseName = PropertyInfo.getInstance().getString("databaseName");
        int databaseVer = PropertyInfo.getInstance().getInt("databaseVer");
        DatabaseTableLoader.getInstance(context, databaseName, databaseVer, scanPackage);

        EventChainUtil.init(context);
        BeanContainer.getInstance().initClass(scanPackage);
        // 1.1. 加载应用广播管理器
        AppBroadcastManager.getInstance(context, scanPackage);

        // 2. 加载事件监听器处理类
        EventListenerSubjectLoader.getInstance(scanPackage);
        // 3. 加载数据接收处理类
        DataReceiveHandlerLoader.getInstance(scanPackage);
        // 4. 加载UI事件处理类
        UiEventHandlerLoader.getInstance(scanPackage);
        // 5. 加载用户状态变更处理类
        StatusChangeHandlerLoader.getInstance(scanPackage);
        // 6. 加载OSGI service类
        OsgiServiceLoader.getInstance(scanPackage);

        // 8. 初始化文件存储储存路径
        initFileDirectory(context);

        // 启动一个单独的服务，执行初始化工作
        // Intent newIntent = new Intent(context, CoreService.class);
        // context.startService(newIntent);
    }

    public static EventBusLoader getInstance(Context context) {
        if (loader == null) {
            loader = new EventBusLoader(context);
        }
        return loader;
    }

    private void initFileDirectory(Context context) {
        File rootPath = new File(Constant.ROOT_PATH);
        if (!rootPath.exists()) {// 判断文件夹目录是否存在
            rootPath.mkdir();// 如果不存在则创建
        }

        // 创建屏蔽系统扫描媒体文件的文件
        File no_media = new File(Constant.ROOT_PATH, ".nomedia");
        if (!no_media.exists()) {
            try {
                no_media.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File picturePath = new File(Constant.PICTURE_PATH);
        if (!picturePath.exists()) {// 判断文件夹目录是否存在
            picturePath.mkdir();// 如果不存在则创建
        }

        File settingsPath = new File(Constant.SETTING_PATH);
        if (!settingsPath.exists()) {// 判断文件夹目录是否存在
            settingsPath.mkdir();// 如果不存在则创建
        }

        File imPath = new File(Constant.IM_PATH);
        if (!imPath.exists()) {// 判断文件夹目录是否存在
            imPath.mkdir();// 如果不存在则创建
        }

        File imPicPath = new File(Constant.IM_PATH + "picture" + File.separator);
        if (!imPicPath.exists()) {// 判断文件夹目录是否存在
            imPicPath.mkdir();// 如果不存在则创建
        }

        File imVoicePath = new File(Constant.IM_PATH + "voice" + File.separator);
        if (!imVoicePath.exists()) {// 判断文件夹目录是否存在
            imVoicePath.mkdir();// 如果不存在则创建
        }

        File template_commons_path = new File(Constant.PROCESS_PATH + "template/commons/");
        if (!template_commons_path.exists()) {// 判断文件夹目录是否存在
            AssetsUtils.getInstance().copyDirectiory(context, "app/commons",
                    Constant.PROCESS_PATH + "template/commons/");
        }

        File template_lib_path = new File(Constant.PROCESS_PATH + "template/lib/");
        if (!template_lib_path.exists()) {// 判断文件夹目录是否存在
            AssetsUtils.getInstance().copyDirectiory(context, "app/lib",
                    Constant.PROCESS_PATH + "template/lib/");
        }

        File processAttachPath = new File(Constant.PROCESS_PATH + "attachment" + File.separator);
        if (!processAttachPath.exists()) {// 判断文件夹目录是否存在
            processAttachPath.mkdir();// 如果不存在则创建
        }

        File pubAccountVoicePath = new File(Constant.PUBACCOUNT_PATH + "voice" + File.separator);
        if (!pubAccountVoicePath.exists()) {
            pubAccountVoicePath.mkdirs();
        }

        File pubAccountPicturePath = new File(Constant.PUBACCOUNT_PATH + "picture" + File.separator);
        if (!pubAccountPicturePath.exists()) {
            pubAccountPicturePath.mkdirs();
        }

        File pubAccountFilePath = new File(Constant.PUBACCOUNT_PATH + "file" + File.separator);
        if (!pubAccountFilePath.exists()) {
            pubAccountFilePath.mkdirs();
        }

    }

}
