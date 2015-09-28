package com.aspirecn.corpsocial.bundle.im.utils;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;

import java.io.File;

/**
 * im文件相关处理方法
 * Created by lihaiqiang on 2015/7/31.
 */
public class ImFileUtil {

    public static String createVoiceFilePath(String chatId) {
        return createFilePath(chatId, "voice", "amr");
    }

    public static String createVideoFilePath(String chatId) {
        return createFilePath(chatId, "video", "mp4");
    }

    public static String createPicFilePath(String chatId) {
        return createFilePath(chatId, "picture", "jpg");
    }

    public static String createLocationFilePath(String chatId) {
        return createFilePath(chatId, "location", "jpg");
    }

    private static String createFilePath(String id, String type, String fileType) {
        StringBuilder filePathSb = new StringBuilder();
        //文件夹
        filePathSb.append(Constant.IM_PATH).append(type).append(File.separator);
        File file = new File(filePathSb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        //文件名
        filePathSb.append(id).append("_").append(Config.getInstance().getUserId()).append("_");
        filePathSb.append(System.currentTimeMillis()).append(".").append(fileType);

        return filePathSb.toString();
    }

}
