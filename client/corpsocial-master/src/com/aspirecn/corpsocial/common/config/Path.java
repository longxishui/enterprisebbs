package com.aspirecn.corpsocial.common.config;

import android.os.Environment;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.util.AssetsUtils;

import java.io.File;
import java.io.IOException;

/**
 * 文件目录路径
 * <p/>
 * Created by yinghuihong on 15/9/6.
 */
public class Path {

    private static Path instance;

    public static Path getInstance() {
        if (instance == null) {
            instance = new Path();
        }
        return instance;
    }

    /**
     * sd卡文件系统的主目录
     */
    private String ROOT_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + File.separator
            + "aspirencorpsocial"
            + File.separator;

    /**
     * 日志的目录
     */
    private String LOG_PATH = getRootPath() + "log" + File.separator;

    /**
     * 图片的目录
     */
    private String PICTURE_PATH = getRootPath() + "picture" + File.separator;
    /**
     * 设置模块图片的目录
     */
    private String SETTING_PATH = getRootPath() + "setting" + File.separator;
    /**
     * 临时目录
     */
    private String TEMP_PATH = getRootPath() + "tmpdir" + File.separator;
    /**
     * HTML应用目录
     */
    private String HTML_PATH = getRootPath() + "html" + File.separator;
    /**
     * NATIVE应用目录
     */
    private String NATIVE_PATH = getRootPath() + "native" + File.separator;
    /**
     * IM目录
     */
    private String IM_PATH = getRootPath() + "im" + File.separator;
    /**
     * oa目录
     */
    private String PROCESS_PATH = getRootPath() + "process" + File.separator;
    /**
     * 公众号目录
     */
    private String PUBACCOUNT_PATH = getRootPath() + "pubaccount" + File.separator;
    /**
     * 用户行为文件目录
     */
    private String APP_FEEDBACK_PATH = getRootPath() + "appFeedBack" + File.separator;

    private String DATABASE_PATH = getRootPath() + "database" + File.separator;

    private String ZIP_PATH = getRootPath() + "zip" + File.separator;


    public String getRootPath() {
        File rootPath = new File(ROOT_PATH);
        if (!rootPath.exists()) {// 判断文件夹目录是否存在
            rootPath.mkdir();// 如果不存在则创建
        }
        // 创建屏蔽系统扫描媒体文件的文件
        File no_media = new File(ROOT_PATH, ".nomedia");
        if (!no_media.exists()) {
            try {
                no_media.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ROOT_PATH;
    }

    public String getLogPath() {
        File logPath = new File(LOG_PATH);
        if (!logPath.exists()) {// 判断文件夹目录是否存在
            logPath.mkdir();// 如果不存在则创建
        }
        return LOG_PATH;
    }

    public String getPicturePath() {
        File picturePath = new File(PICTURE_PATH);
        if (!picturePath.exists()) {// 判断文件夹目录是否存在
            picturePath.mkdir();// 如果不存在则创建
        }
        return PICTURE_PATH;
    }

    public String getSettingPath() {
        File settingsPath = new File(SETTING_PATH);
        if (!settingsPath.exists()) {// 判断文件夹目录是否存在
            settingsPath.mkdir();// 如果不存在则创建
        }
        return SETTING_PATH;
    }

    public String getZipPath() {
        File zipFilePath = new File(ZIP_PATH);
        if (!zipFilePath.exists()) {
            zipFilePath.mkdirs();
        }
        return ZIP_PATH;
    }

    public String getImPath() {
        File imPath = new File(IM_PATH);
        if (!imPath.exists()) {// 判断文件夹目录是否存在
            imPath.mkdir();// 如果不存在则创建
        }

        File imPicPath = new File(IM_PATH + "picture" + File.separator);
        if (!imPicPath.exists()) {// 判断文件夹目录是否存在
            imPicPath.mkdir();// 如果不存在则创建
        }

        File imVoicePath = new File(IM_PATH + "voice" + File.separator);
        if (!imVoicePath.exists()) {// 判断文件夹目录是否存在
            imVoicePath.mkdir();// 如果不存在则创建
        }
        return IM_PATH;
    }

    public String getProcessPath() {
        File path = new File(PROCESS_PATH);
        if (!path.exists()) {// 判断文件夹目录是否存在
            path.mkdir();// 如果不存在则创建
        }

        File template_commons_path = new File(PROCESS_PATH + "template/commons/");
        if (!template_commons_path.exists()) {// 判断文件夹目录是否存在
            AssetsUtils.getInstance().copyDirectiory(AspirecnCorpSocial.getContext(), "app/commons",
                    PROCESS_PATH + "template/commons/");
        }

        File template_lib_path = new File(PROCESS_PATH + "template/lib/");
        if (!template_lib_path.exists()) {// 判断文件夹目录是否存在
            AssetsUtils.getInstance().copyDirectiory(AspirecnCorpSocial.getContext(), "app/lib", PROCESS_PATH + "template/lib/");
        }

        File processAttachPath = new File(PROCESS_PATH + "attachment" + File.separator);
        if (!processAttachPath.exists()) {// 判断文件夹目录是否存在
            processAttachPath.mkdir();// 如果不存在则创建
        }

        return PROCESS_PATH;
    }

    public String getPubAccountPath() {
        File path = new File(PUBACCOUNT_PATH);
        if (!path.exists()) {// 判断文件夹目录是否存在
            path.mkdir();// 如果不存在则创建
        }

        File pubAccountVoicePath = new File(PUBACCOUNT_PATH + "voice" + File.separator);
        if (!pubAccountVoicePath.exists()) {
            pubAccountVoicePath.mkdirs();
        }

        File pubAccountPicturePath = new File(PUBACCOUNT_PATH + "picture" + File.separator);
        if (!pubAccountPicturePath.exists()) {
            pubAccountPicturePath.mkdirs();
        }

        File pubAccountFilePath = new File(PUBACCOUNT_PATH + "file" + File.separator);
        if (!pubAccountFilePath.exists()) {
            pubAccountFilePath.mkdirs();
        }

        return PUBACCOUNT_PATH;
    }

    public String getDatabasePath() {
        File databasePath = new File(DATABASE_PATH);
        if (!databasePath.exists()) {
            databasePath.mkdirs();
        }
        return DATABASE_PATH;
    }

    public String getAppFeedbackPath() {
        File path = new File(APP_FEEDBACK_PATH);
        if (!path.exists()) {// 判断文件夹目录是否存在
            path.mkdir();// 如果不存在则创建
        }
        return APP_FEEDBACK_PATH;
    }

    public String getTempPath() {
        File path = new File(TEMP_PATH);
        if (!path.exists()) {// 判断文件夹目录是否存在
            path.mkdir();// 如果不存在则创建
        }
        return TEMP_PATH;
    }

    public String getHtmlPath() {
        File path = new File(HTML_PATH);
        if (!path.exists()) {// 判断文件夹目录是否存在
            path.mkdir();// 如果不存在则创建
        }
        return HTML_PATH;
    }

    public String getNativePath() {
        File path = new File(NATIVE_PATH);
        if (!path.exists()) {// 判断文件夹目录是否存在
            path.mkdir();// 如果不存在则创建
        }
        return NATIVE_PATH;
    }

}
