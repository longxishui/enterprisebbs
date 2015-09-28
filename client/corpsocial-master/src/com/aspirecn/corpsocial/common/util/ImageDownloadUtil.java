package com.aspirecn.corpsocial.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.config.SysInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.trinea.android.common.entity.CacheObject;
import cn.trinea.android.common.service.impl.SimpleCache;
import cn.trinea.android.common.util.FileUtils;
import cn.trinea.android.common.util.HttpUtils;
import cn.trinea.android.common.util.StringUtils;
import cn.trinea.android.common.util.SystemUtils;

/**
 * 图片下载和显示工具。用于处理ImageView的异步下载和图片显示。<p/>
 * <strong>调用方法：</strong>ImageDownloadUtil.INSTANCE.showImage, downloadImage<br/>
 * showImage会在下载完成后自动显示图片，而downloadImage则只下载<p/>
 * <strong>监听方法：</strong>在调用showImage, downloadImage时，传入listenerId。<br/>
 * 实现DownloadStatusListener接口，传入的listernerId与DownloadStatusListener.getListenerId()返回值相同。<br/>
 * 订阅事件：addListener<br/>
 * 一定记得要取消订阅（例如在Activity结束时）：removeListener <p/>
 *
 * @author meixuesong
 */
public enum ImageDownloadUtil {
    INSTANCE;

    private static final int GET_IMAGE_SUCCESS = 1;
    private static final int GET_IMAGE_FAILED = 2;
    private static final String TMP_FOLDER = "tmppic";
    private String rootPath;
    private int timeout = 60; // 60秒
    private ConcurrentHashMap<String, DownloadTask> downloadTasks;
    private HashSet<DownloadStatusListener> listeners;
    private ExecutorService threadPool;
    private Handler handler;
    private Map<View, String> imageViews;
    private ImageCache imageCache;
    ImageDownloadUtil() {
        rootPath = Constant.PICTURE_PATH;
        downloadTasks = new ConcurrentHashMap<String, DownloadTask>();
        listeners = new HashSet<DownloadStatusListener>();
        threadPool = Executors//.newSingleThreadExecutor();
                .newFixedThreadPool(SystemUtils.getDefaultThreadPoolSize());
        handler = new MyHandler();
        imageViews = Collections.synchronizedMap(new WeakHashMap<View, String>());
        imageCache = new ImageCache(30);
    }

    /**
     * 自动下载并展示图片，以及下载成功、失败通知
     *
     * @param url           图片的url
     * @param subPath       图片保存到本地路径的子文件夹(rootPath的下级文件夹）
     * @param listenerId    记录该图片的订阅ID, 当下载结束后，会通知listenerId相同的订阅者
     * @param view          展示图片的视图
     * @param defaultBitmap 如果下载失败时的默认图片，可以为null
     */
    public void showImage(String url, String subPath, String listenerId, View view, Bitmap defaultBitmap) {
        LogUtil.d(String.format("图片缓存，size: %d, 命中率：%f, 命中次数：%d, 未捕获次数:%d",
                imageCache.getSize(),
                imageCache.getHitRate(),
                imageCache.getHitCount(),
                imageCache.getMissCount()
        ));
        if (view != null) {
            if (StringUtils.isBlank(url)) {
                imageViews.remove(view);
            } else {
                imageViews.put(view, url);
            }
        }

        if (!StringUtils.isBlank(url)) {
            DownloadTask task = downloadTasks.get(url);
            if (task != null) {
                task.setView(view);
            } else {
                CacheObject<SoftReference<Bitmap>> cacheObject = imageCache.get(url);
                Bitmap bitmap;
                if (cacheObject != null && (bitmap = cacheObject.getData().get()) != null) {
                    //先加载缓存
                    _showImage(view, bitmap, false);
                } else {
                    task = new DownloadTask(url, subPath, listenerId, view, defaultBitmap);
                    //没有缓存就判断是否有文件
                    if (task.localFileExists()) {
                        task.loadImage(false);
                        onGetSuccess(task);
                    } else {
                        //啥都没有就下载
                        downloadTasks.putIfAbsent(url, task);
                        threadDownload(task);
                    }
                }
            }
        } else if (view != null && defaultBitmap != null) {
            _showImage(view, defaultBitmap, false);
        }
    }

    /**
     * 自动下载并展示图片，没有下载成功、失败通知
     *
     * @param url     图片的url
     * @param subPath 图片保存到本地路径的子文件夹(rootPath的下级文件夹）
     * @param view    展示图片的视图
     */
    public void showImage(String url, String subPath, View view) {
        showImage(url, subPath, "", view, null);
    }

    /**
     * 自动下载并展示图片，没有下载成功、失败通知
     *
     * @param url           图片的url
     * @param subPath       图片保存到本地路径的子文件夹(rootPath的下级文件夹）
     * @param view          展示图片的视图
     * @param defaultBitmap 如果下载失败时的默认图片，可以为null
     */
    public void showImage(String url, String subPath, View view, Bitmap defaultBitmap) {
        showImage(url, subPath, "", view, defaultBitmap);
    }

    /**
     * 自动下载并展示图片，没有下载成功、失败通知。默认下载到{rootPath}/images文件夹
     *
     * @param url  图片地址
     * @param view 待显示的View(eg. ImageView)
     */
    public void showImage(String url, View view) {
        showImage(url, "images", "", view, null);
    }

    /**
     * 自动下载并展示图片，没有下载成功、失败通知。默认下载到{rootPath}/images文件夹
     *
     * @param url           图片地址
     * @param view          待显示的View(eg. ImageView)
     * @param defaultBitmap 如果下载失败时的默认图片，可以为null
     */
    public void showImage(String url, View view, Bitmap defaultBitmap) {
        showImage(url, "images", "", view, defaultBitmap);
    }

    /**
     * 下载图片，并保存到默认路径({rootPath}/images)。没有下载成功、失败通知。
     *
     * @param url 图片地址
     */
    public void downloadImage(String url) {
        downloadImage(url, "images", "");
    }

    /**
     * 下载图片，并保存到默认路径({rootPath}/images)。提供下载成功、失败通知
     *
     * @param url        图片地址
     * @param listenerId 记录该图片的订阅ID, 当下载结束后，会通知listenerId相同的订阅者
     */
    public void downloadImage(String url, String listenerId) {
        downloadImage(url, "images", listenerId);
    }

    /**
     * 下载图片，并保存到subPath子文件夹。提供下载成功、失败通知
     *
     * @param url        图片地址
     * @param subPath    图片保存到本地路径的子文件夹
     * @param listenerId 记录该图片的订阅ID, 当下载结束后，会通知listenerId相同的订阅者
     */
    public void downloadImage(String url, String subPath, String listenerId) {
        showImage(url, subPath, listenerId, null, null);
    }

    /**
     * 准备好文件夹。
     *
     * @param subPath
     * @return
     */
    public void preparePath(String subPath) {
        String tmp = getTmpFileName("asdfasf", subPath);
        String release = getFileName("asdfasf", subPath);

        FileUtils.makeDirs(tmp);
        FileUtils.makeDirs(release);
    }

    /**
     * 判断图片是否匹配，用于解决ListView复用ImageView时，图片错位的问题。
     *
     * @param view
     * @param url
     * @return
     */
    private boolean isImageViewMatch(View view, String url) {
        String str = imageViews.get(view);

        return str != null && str.equals(url);
    }

    /**
     * 下载线程
     *
     * @param task
     * @return
     */
    private void threadDownload(DownloadTask task) {
        ImageDownloadThread thread = null;
        try {
            if (task.thread != null) {
                return;
            }

            thread = new ImageDownloadThread(task.url, task.tmpFileName, task.releaseFileName, timeout);
            task.thread = thread;
            threadPool.execute(thread);
        } catch (Exception e) {
            LogUtil.e("获取线程出错！", e);
        }
    }

    private void showTaskInfo() {
//    	System.out.println(String.format("Map数量：thread %d, ImageViews数量：%d",  downloadTasks.size(), imageViews.size()));

        for (String key : downloadTasks.keySet()) {
            try {
                DownloadTask task = downloadTasks.get(key);
                if (task != null) {
                    LogUtil.e(String.format("Thread: %s, url= %s . ",
                            task.thread != null ? "not null" : "null",
                            task.url
                    ));


                }
            } catch (Exception e) {
                LogUtil.e("线程冲突，无法打印future状态");
            }
        }
    }

    protected void onGetSuccess(final DownloadTask task) {
        try {
            final String url = task.url;
            final String fileName = task.releaseFileName;
            final String listenerId = task.listenerId;

            threadPool.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            for (DownloadStatusListener listener : listeners) {
                                try {
                                    if ((!StringUtils.isBlank(listenerId)) && listener.getListenerId().equals(listenerId))
                                        listener.downloadSuccess(url, fileName);
                                } catch (Exception e) {
                                    LogUtil.e(String.format("通知下载(%s)成功时出错。", task.url), e);
                                }
                            }
                        }
                    }
            );
        } catch (Exception e) {
            LogUtil.e("通知下载(%s)成功时出错。", e);
        }
    }

    protected void onGetFailed(DownloadTask task, final String failedReason) {
        final String url = task.url;
        final String listenerId = task.listenerId;

        threadPool.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        for (DownloadStatusListener listener : listeners) {
                            try {
                                if ((!StringUtils.isBlank(listenerId)) && listener.getListenerId().equals(listenerId))
                                    listener.downloadfailed(url, failedReason);
                            } catch (Exception e) {
                                LogUtil.e(String.format("通知下载(%s)成功时出错。", url), e);
                            }
                        }
                    }
                }
        );
    }

    /**
     * 获取Url对应的文件名的后缀，如.jpg
     *
     * @param url
     * @return
     */
    private String getUrlFileExt(String url) {
        if (url.lastIndexOf('.') >= 0) {
            return url.substring(url.lastIndexOf('.'), url.length());
        }

        return "";
    }

    /**
     * 获取url下载对应的本地保存文件名。
     *
     * @param url
     * @param subPath 保存的子路径(rootPath的下级文件夹)
     * @return
     */
    public String getFileName(String url, String subPath) {
        return String.format("%s%s%s%s%s%s",
                rootPath, File.separator,
                subPath, File.separator,
                MD5Util.getMD5String(url), getUrlFileExt(url)
        );
    }

    protected String getTmpFileName(String url, String subPath) {
        return String.format("%s%s%s%s%s%s%s%s",
                rootPath, File.separator,
                TMP_FOLDER, File.separator,
                subPath, File.separator,
                MD5Util.getMD5String(url), getUrlFileExt(url)
        );
    }

    /**
     * 文件下载到本地后，保存文件的根文件夹
     *
     * @return
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * 设置文件下载到本地后，保存文件的根文件夹
     *
     * @return
     */
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * 下载超时时间，（秒）
     *
     * @return
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * 设置下载超时时间
     *
     * @param timeout （秒）
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * 增加事件监听。
     *
     * @param listener
     */
    public void addListener(DownloadStatusListener listener) {
        listeners.add(listener);
    }

    /**
     * 移除事件监听
     *
     * @param listener
     */
    public void removeListener(DownloadStatusListener listener) {
        listeners.remove(listener);
    }

    private void _showImage(View view, Bitmap bitmap, boolean animation) {
        if (view != null) {
            BitmapDisplayer bd = new BitmapDisplayer(bitmap, view, animation);
//    		Activity a = (Activity) view.getContext();
//    		a.runOnUiThread(bd);
            bd.run();
        }
    }

    private class DownloadTask {
        final String url;
        final String tmpFileName;
        final String releaseFileName;
        final String listenerId;
        final Bitmap defaultPic;
        ImageDownloadThread thread;
        Set<View> views = new HashSet<View>();

        DownloadTask(String url, String subPath, String listenerId, View view, Bitmap defaultPic) {
            this.url = url;
            this.views.add(view);
            this.listenerId = listenerId;
            tmpFileName = getTmpFileName(url, subPath);
            releaseFileName = getFileName(url, subPath);
            this.defaultPic = defaultPic;
        }

        boolean localFileExists() {
            return FileUtils.isFileExist(releaseFileName);
        }

        void setView(View view) {
            this.views.add(view);
        }

        /**
         * 显示图片，如果下载失败则只会显示默认图片
         *
         * @param animation
         */
        void loadImage(boolean animation) {
            for (View view : views) {
                if (isImageViewMatch(view, url)) {
                    Bitmap bitmap = null;
                    if (localFileExists()) {
                        try {
                            bitmap = BitmapFactory.decodeFile(releaseFileName);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        imageCache.put(url, new SoftReference<Bitmap>(bitmap));
                    } else if (defaultPic != null) {
                        bitmap = defaultPic;
                    }

                    if (bitmap != null) {
                        _showImage(view, bitmap, animation);
                    }

                    imageViews.remove(view);
                } else {
//					System.out.format("不匹配，不显示：%s", url);
                }
            }
        }
    }

    private class ImageCache extends SimpleCache<String, SoftReference<Bitmap>> {
        private static final long serialVersionUID = 1132414324214L;

        @SuppressWarnings("unused")
        public ImageCache() {
        }

        public ImageCache(int maxSize) {
            super(maxSize);
        }
    }

    private class MessageObject {
        String imageUrl;
        String failedReason;

        public MessageObject(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public MessageObject(String imageUrl, String failedReason) {
            this.imageUrl = imageUrl;
            this.failedReason = failedReason;
        }
    }

    private class MyHandler extends Handler {
        private void removeTask(String imageUrl) {
            downloadTasks.remove(imageUrl);
        }

        public void handleMessage(Message message) {
            if (message.what == GET_IMAGE_SUCCESS) {
                MessageObject msg = (MessageObject) message.obj;

                DownloadTask task = downloadTasks.get(msg.imageUrl);
                if (task != null) {
                    task.loadImage(true);
                    onGetSuccess(task);
                }

                removeTask(msg.imageUrl);
            } else if (message.what == GET_IMAGE_FAILED) {
                MessageObject msg = (MessageObject) message.obj;

                DownloadTask task = downloadTasks.get(msg.imageUrl);
                if (task != null) {
                    task.loadImage(false);
                    onGetFailed(task, msg.failedReason);
                }

                removeTask(msg.imageUrl);
            }

            if (downloadTasks.size() == 0) {
                //如果全部下载完了，则清除过期缓存。
                imageCache.getSize();
            }

            showTaskInfo();
        }
    }

    protected class ImageDownloadThread implements Runnable {
        public CountDownLatch finishGetDataLock;
        private String imageUrl;
        private String tmpFileName;  //临时保存文件名
        private String targetFileName; //最终保存的文件名
        private int httpReadTimeOut = 60000;

        /**
         * @param imageUrl
         */
        public ImageDownloadThread(String imageUrl, String tmpFileName, String targetFileName, int timeoutSecond) {
            this.imageUrl = imageUrl;
            this.tmpFileName = tmpFileName;
            this.targetFileName = targetFileName;
            this.httpReadTimeOut = timeoutSecond * 1000;
            finishGetDataLock = new CountDownLatch(1);
        }

        public void run() {
            String result = "";
            if (!StringUtils.isBlank(imageUrl)) {
                result = doDownload();
            }
            finishGetDataLock.countDown();

            if (FileUtils.isFileExist(targetFileName)) {
                // 下载成功
                handler.sendMessage(handler.obtainMessage(
                        GET_IMAGE_SUCCESS, new MessageObject(imageUrl)));
            } else {
                // 下载失败
                String failedReason = result;
                handler.sendMessage(handler.obtainMessage(
                        GET_IMAGE_FAILED, new MessageObject(imageUrl,
                                failedReason)));
            }
        }

        public String doDownload() {
            String result = "OK";
            InputStream stream = null;
            try {
                stream = getInputStreamFromUrl();
            } catch (Exception e) {
                LogUtil.e(new StringBuilder().append("get image exception, imageUrl is:").append(imageUrl).toString(), e);
            }

            if (stream != null) {
                try {
                    File tmpFile = new File(tmpFileName);
                    writeFile(tmpFile, stream, false);

                    //rename
                    if (tmpFile.length() > 0) {
                        FileUtils.copyFile(tmpFileName, targetFileName);
                    }
                    tmpFile.delete();
                } catch (Exception e1) {
                    result = e1.getMessage();
                    LogUtil.e(String.format("下载文件失败，错误信息： %s, url: %s, 保存文件名： %s",
                            e1.getMessage(), imageUrl, targetFileName));
                }
            }

            return result;
        }

        private InputStream getInputStreamFromUrl() {
            InputStream stream = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                HttpUtils.setURLConnection(null, con);
                con.setReadTimeout(httpReadTimeOut);
                stream = con.getInputStream();
            } catch (MalformedURLException e) {
                closeInputStream(stream);
                throw new RuntimeException("MalformedURLException occurred. ", e);
            } catch (IOException e) {
                closeInputStream(stream);
                throw new RuntimeException("IOException occurred. ", e);
            }
            return stream;
        }

        private void closeInputStream(InputStream s) {
            if (s == null) {
                return;
            }

            try {
                s.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            }
        }

        public boolean writeFile(File file, InputStream stream, boolean append) {
            OutputStream o = null;
            try {
                FileUtils.makeDirs(file.getAbsolutePath());
                o = new FileOutputStream(file, append);
                byte data[] = new byte[1024];
                int length = -1;
                while ((length = stream.read(data)) != -1) {
                    o.write(data, 0, length);
                }
                o.flush();
                return true;
            } catch (FileNotFoundException e) {
                throw new RuntimeException("FileNotFoundException occurred. ", e);
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            } finally {
                if (o != null) {
                    try {
                        o.close();
                        stream.close();
                    } catch (IOException e) {
                        throw new RuntimeException("IOException occurred. ", e);
                    }
                }
            }
        }
    }

    private class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        View view;
        boolean animation;

        public BitmapDisplayer(Bitmap b, View p, boolean animation) {
            bitmap = b;
            view = p;
            this.animation = animation;
        }

        public void run() {
            if (bitmap != null) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageBitmap(bitmap);
                    if (animation)
                        view.startAnimation(getInAlphaAnimation(1000));
                } else if (view instanceof TextView) {
                    view.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }
            }
        }

        private AlphaAnimation getInAlphaAnimation(long durationMillis) {
            AlphaAnimation inAlphaAnimation = new AlphaAnimation(0, 1);
            inAlphaAnimation.setDuration(durationMillis);
            return inAlphaAnimation;
        }
    }
}
