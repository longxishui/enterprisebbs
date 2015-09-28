/**
 * @(#) AspWeiXinApplication.java Created on 2013-10-30
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */

package com.aspirecn.corpsocial.common;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.config.PropertyInfo;
import com.aspirecn.corpsocial.common.config.SysInfo;
import com.aspirecn.corpsocial.common.service.NetworkService;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shamanland.fonticon.FontIconTypefaceHolder;
import com.umeng.analytics.MobclickAgent;



/**
 * @author lizhuo_a
 */
public class AspirecnCorpSocial extends MultiDexApplication {

    private static AspirecnCorpSocial context;

    public AspirecnCorpSocial() {
        context = this;
    }

    public static AspirecnCorpSocial getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 友盟奔溃日志上报
        initUMeng();
        // 本地日志系统
        initLogger();
        // 内存泄露检测
//        LeakCanary.install(this);
        // 应用信息配置
        Config.getInstance().init(this);
        // 个人信息配置；防止应用非法“登陆”，因没初始化导致空指针异常；首次登陆UserName将默认为""
        ConfigPersonal.getInstance().init(Config.getInstance().getUserName());
        // 系统信息
        SysInfo.getInstance().init(this);
        // 字体图标
        FontIconTypefaceHolder.init(getAssets(), "icons.ttf");
        // 图片加载配置
        initImageLoader(this);
        // 讯飞语言
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=54a241e1");
        // 启动网络服务
        getApplicationContext().startService(new Intent(this, NetworkService.class));
        Config.getInstance().setUserId("492205");
        Config.getInstance().setCorpId("10");
        //        CrashHandler.getInstance().init(getApplicationContext());
    }

    /**
     * 初始化log的配置信息
     */
    private void initLogger() {
//        PropertyInfo instance = PropertyInfo.getInstance();
//        int logMaxFileSize = instance.getInt("logMaxFileSize");
//        int logMaxBackupSize = instance.getInt("logMaxBackupSize");
//        LogConfigurator logConfigurator = new LogConfigurator();
//        logConfigurator.setFileName(Constant.LOG_PATH + "log.txt");
//        logConfigurator.setRootLevel(Log.Level.DEBUG);
//        logConfigurator.setLevel("org.apache", Log.Level.ERROR);
//        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
//        logConfigurator.setMaxFileSize(1024 * 1024 * logMaxFileSize);
//        logConfigurator.setMaxBackupSize(logMaxBackupSize);
//        logConfigurator.setImmediateFlush(false);
//        logConfigurator.configure();
    }

    /**
     * 友盟奔溃日志上报
     */
    private void initUMeng() {
        // 本地打印日志，开启的话将导致在线配置updateOnlineConfig失效
        // MobclickAgent.setDebugMode(true);
        // http://dev.umeng.com/analytics/android/quick-start#2.3.2
        // 禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
        // http://dev.umeng.com/analytics/android/quick-start#2.4
        MobclickAgent.updateOnlineConfig(getApplicationContext());
        // MobclickAgent.flush(this);
    }

    /**
     * 图片加载配置
     *
     * @param context
     */
    private void initImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .build();
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.defaultDisplayImageOptions(options);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

}
