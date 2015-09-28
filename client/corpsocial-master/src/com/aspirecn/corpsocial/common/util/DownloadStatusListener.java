package com.aspirecn.corpsocial.common.util;

public interface DownloadStatusListener {
    /**
     * 下载成功事件
     *
     * @param url      下载的url
     * @param fileName 保存到本地的文件名（全路径）
     * @param view     用于展示图片的view（可为null)
     */
    void downloadSuccess(String url, String fileName);

    /**
     * 下载失败事件
     *
     * @param url     下载的url
     * @param message 失败原因
     * @param view    用于展示图片的view（可为null)
     */
    void downloadfailed(String url, String message);

    /**
     * 监听Id，当下载成功或失败时，与ListenerId匹配的监听会收到事件通知
     *
     * @return
     */
    String getListenerId();
}
