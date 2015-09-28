package com.aspirecn.corpsocial.common.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lizhuo_a on 2015/1/12.
 */
public class AssetsUtils {


    private static AssetsUtils sInstance = new AssetsUtils();

    public static AssetsUtils getInstance() {
        return sInstance;
    }

    /**
     * 从assets目录复制文件夹到指定目录
     *
     * @param sourceDir
     * @param targetDir
     * @throws java.io.IOException
     */
    public void copyDirectiory(Context context, String sourceDir,
                               String targetDir) {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        AssetManager assetManager = context.getResources().getAssets();
        try {
            String[] files = assetManager.list(sourceDir);
            for (int i = 0; i < files.length; i++) {
                String[] subFiles = assetManager.list(sourceDir + "/" + files[i]);
                if (subFiles.length == 0) {
                    // 目标文件
                    File targetFile = new File(targetDir + File.separator + files[i]);
                    File rootPath = new File(targetDir);
                    if (!rootPath.exists()) {// 判断文件夹目录是否存在
                        rootPath.mkdir();// 如果不存在则创建
                    }
                    InputStream open = assetManager.open(sourceDir + "/" + files[i]);
                    copyFile(open, targetFile);
                }
                if (subFiles.length > 0) {
                    // 准备复制的源文件夹
                    String dir1 = sourceDir + File.separator + files[i];
                    // 准备复制的目标文件夹
                    String dir2 = targetDir + File.separator + files[i];
                    copyDirectiory(context, dir1, dir2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从assets目录复制文件到指定目录
     *
     * @param sourceInputStream
     * @param targetFile
     */
    public void copyFile(InputStream sourceInputStream, File targetFile) {
        // 新建文件输入流并对它进行缓冲
        try {
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int count = 0;
            while (true) {
                count++;
                int len = sourceInputStream.read(buffer);
                if (len == -1) {
                    break;
                }
                fos.write(buffer, 0, len);
            }
            sourceInputStream.close();
            fos.close();
        } catch (IOException e) {
            LogUtil.e("拷贝文件失败", e);
        } finally {

        }
    }

    /**
     * read the file from assets
     *
     * @param context
     * @param fileName
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T read(Context context, String fileName, Class<T> cls) {
        T t = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            String json = inputStream2String(is);
            t = new Gson().fromJson(json, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    private String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * 从assets中读取图片
     */
    public Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
