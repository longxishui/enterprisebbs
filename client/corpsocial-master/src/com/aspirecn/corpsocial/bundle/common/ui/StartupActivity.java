/**
 * @(#) StartupActivity.java Created on 2013-12-11
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.common.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.uitils.NotifyConfigUtil;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.config.FacePackageColumns;
import com.aspirecn.corpsocial.common.eventbus.EventBusLoader;
import com.aspirecn.corpsocial.common.util.SDUtils;
import com.aspirecn.corpsocial.common.util.ZipUtils_2;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.shamanland.fonticon.FontIconView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

/**
 * The class <code>StartupActivity</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
@EActivity(R.layout.common_startup_activity)
public class StartupActivity extends Activity {

    @ViewById(R.id.startup_image)
    FontIconView startupImg;
    @ViewById(R.id.startup_slogan)
    TextView startupSlogan;
    @ViewById(R.id.startup_name)
    TextView startupName;
    @ViewById(R.id.login_logo)
    ImageView loginView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }


    void init() {

        initView();
        start();

    }

    @UiThread
    void initView() {
        AppConfig appConfig = AppConfig.getInstance();
        if (appConfig != null) {
//            startupImg.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                    .showImageForEmptyUri(R.drawable.app_icon)
                    .showImageOnFail(R.drawable.app_icon)
                    .build();
            ImageLoader.getInstance().displayImage(appConfig.preIcon, loginView, options);

            startupSlogan.setText(appConfig.slogan);
            startupName.setText(appConfig.appName);
//            if(!StringUtils.isEmpty(appConfig.bottomViewDef.fontColor)) {
//                startupName.setTextColor(Color.parseColor(appConfig.bottomViewDef.fontColor));
//            }
        }
    }

    @Background
    void start() {

        // 初始化事件总线
        EventBusLoader.getInstance(getApplicationContext());

        NotifyConfigUtil.init(getApplicationContext());

        interActivity();

        initFace();
    }

    @UiThread
    void interActivity() {

        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (preferences.getBoolean("firststart", true)) {
                    editor = preferences.edit();

                    editor.putBoolean("firststart", false);
                    editor.commit();
                    Intent intent = new Intent();
                    intent.setClass(StartupActivity.this, ViewPagerActivity_.class);
                    StartupActivity.this.startActivity(intent);
                    StartupActivity.this.finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(StartupActivity.this, com.aspirecn.corpsocial.bundle.workgrp.ui.WorkGrpMainActivity_.class);
                    StartupActivity.this.startActivity(intent);
                    StartupActivity.this.finish();

                }

            }
        }, 300);
    }


    /**
     * 初始化表情模块
     */
    private void initFace() {
        if (!(new File(FacePackageColumns.FACE_PATH)
                .exists())) {
            // 拷贝表情包
            copyFaceZipToSD();
            // 解压表情包
            try {
                upZipFaceFile();
            } catch (ZipException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        else {
//            String newMd5 = null;
//            try {
//                newMd5 = MD5Util.getFileMD5String(new File(
//                        FacePackageColumns.FACE_PATH));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (!FacePackageColumns.FULIAO_ZIP_MD5.equals(newMd5)) {
//                //System.out.println("newMd5: "+newMd5);
//                System.out.println("MD5值不一致，覆盖zip并重新解压");
//                // 判断一下MD5值是否一致，如果不一致，覆盖原表情包，并解压
//                // 拷贝表情包
//                copyFaceZipToSD();
//                // 解压表情包
//                try {
//                    upZipFaceFile();
//                } catch (ZipException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    /**
     * 拷贝表情包到sd卡上
     */
    private void copyFaceZipToSD() {
        if (!SDUtils.ExistSDCard()) {

        }
        InputStream inputStream;
        try {
            inputStream = this.getResources().getAssets().open("fuliao.zip");
            File file = new File(FacePackageColumns.FACE_PACKAGE_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(
                    FacePackageColumns.FACE_PATH);
            byte[] buffer = new byte[512];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解压表情包
     *
     * @throws java.io.IOException
     * @throws java.util.zip.ZipException
     */
    private void upZipFaceFile() throws IOException {
        String faceStr = FacePackageColumns.FACE_PATH;
        File file = new File(faceStr);
        ZipUtils_2.unZipFiles(file, FacePackageColumns.FACE_FOLDER_PATH);
    }

}
