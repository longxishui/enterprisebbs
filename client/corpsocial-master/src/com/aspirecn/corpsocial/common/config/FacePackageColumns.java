package com.aspirecn.corpsocial.common.config;

import android.os.Environment;

import java.io.File;

/**
 * 表情包下载及解压字段
 *
 * @author Administrator
 */
public class FacePackageColumns {
    /**
     * sd卡文件系统的主目录
     */
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + File.separator
            + "aspirencorpsocial"
            + File.separator;

    /**
     * 下载表情包（ZIP）所在目录
     */
    public static final String FACE_PACKAGE_PATH = ROOT_PATH + "newFace" + File.separator;

    /**
     * 表情ZIP包本地地址（本机自带）
     */
    public static final String FACE_PATH = ROOT_PATH + "fuliao.zip";

    /**
     * 表情文件夹目录路径
     */
    public static final String FACE_FOLDER_PATH = ROOT_PATH + "newFace" + File.separator;

    /**
     * 本机自带（fuliao）表情解压后，表情图片的放置目录
     */
    public static final String FACE_PACKAGE_UN_FACE_PATH = ROOT_PATH + "newFace" + File.separator + "fuliao";

    /**
     * 表情包的MD5校验值（本机自带-fuliao）
     */
    public static final String FULIAO_ZIP_MD5 = "9427b16426657b2126a71927095286e5";


}
