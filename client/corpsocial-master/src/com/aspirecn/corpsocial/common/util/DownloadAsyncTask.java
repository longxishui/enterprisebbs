package com.aspirecn.corpsocial.common.util;

import android.os.AsyncTask;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载工具类，用于http下载
 *
 * @author meixuesong
 */
public class DownloadAsyncTask extends AsyncTask<String, Integer, String> {
    private String saveFilePath = ""; // 文件保存路径
    private int timeout = 30; // 超时时间（秒）
    private DownloadStatus downloadStatus = DownloadStatus.NA;

    public String getSaveFilePath() {
        return saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath) {
        if (!saveFilePath.endsWith(File.separator)) {
            this.saveFilePath = saveFilePath + File.separator;
        } else {
            this.saveFilePath = saveFilePath;
        }
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public DownloadStatus getDownloadStatus() {
        return downloadStatus;
    }

    public String getFileName(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

    protected String getFileNameWithoutExt(String url) {
        String fileName = getFileName(url);
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    /**
     * 判断是否断点续传，如果是，则返回上次的位置。否则返回0
     */
    private long getBreakPoint(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return file.length();
        }

        return 0;
    }

    private int getContentLength(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            return connection.getContentLength();
        } catch (Exception e) {
            LogUtil.e("无法获取下载文件的大小。", e);
        }
        return 0;
    }

    @Override
    protected String doInBackground(String... args) {
        RandomAccessFile file = null;
        InputStream inStream = null;
        try {
            int fileSize = getContentLength(args[0]);
            URL url = new URL(args[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeout * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Connection", "Keep-Alive");

            downloadStatus = DownloadStatus.Downloading;
            String finalFileName = saveFilePath + getFileName(args[0]);
            String saveFileName = saveFilePath + getFileName(args[0]) + "." + String.valueOf(fileSize);

            //下载，并考虑断点续传,
            Long offset = getBreakPoint(saveFileName);
            conn.setRequestProperty("Range", "bytes=" + offset.toString() + "-");

            file = new RandomAccessFile(saveFileName, "rw");
            file.seek(offset);
            long length = offset;
            inStream = conn.getInputStream();

            byte[] buffer = new byte[1024];
            int hasRead = 0;
            while ((hasRead = inStream.read(buffer)) > 0) {
                file.write(buffer, 0, hasRead);
                length += hasRead;

                int downloadPercent = (int) Math.round(length * 100.0
                        / fileSize);
                if (downloadPercent >= 100) {
                    downloadPercent = 100;
                }

                publishProgress(downloadPercent);
            }
            file.close();
            inStream.close();
            downloadStatus = DownloadStatus.Downloaded;

            //将临时文件改名
            File fromFile = new File(saveFileName);
            File toFile = new File(finalFileName);
            if (toFile.exists()) {
                toFile.delete();
            }
            fromFile.renameTo(toFile);

            return finalFileName;
        } catch (Exception e) {
            downloadStatus = DownloadStatus.Failed;
            LogUtil.e(
                    String.format("下载文件失败：%s。错误信息：%s", args[0], e.getMessage()));

            return "";
        } finally {
            try {
                if (file != null) file.close();
                if (inStream != null) inStream.close();
            } catch (Exception e) {
                LogUtil.e(
                        String.format("下载文件失败时，关闭文件失败。%s。错误信息：%s", args[0], e.getMessage()));

            }
        }
    }

    public enum DownloadStatus {
        NA,
        Downloading,
        Downloaded,
        Failed
    }
}
