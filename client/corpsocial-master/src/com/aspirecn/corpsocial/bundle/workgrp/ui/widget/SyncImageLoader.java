package com.aspirecn.corpsocial.bundle.workgrp.ui.widget;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;

import com.aspirecn.corpsocial.common.config.Path;
import com.aspirecn.corpsocial.common.util.MD5Util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

public class SyncImageLoader {

    private static SyncImageLoader instance;
    final Handler handler = new Handler();
    private Object lock = new Object();

    //	private int mStartLoadLimit = 0;
//
//	private int mStopLoadLimit = 0;
    private boolean mAllowLoad = true;
    private boolean firstLoad = true;
    private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();

    public static SyncImageLoader getInstance() {
        if (instance == null) {
            instance = new SyncImageLoader();
        }
        return instance;
    }

    public static Drawable loadImageFromUrl(String url) throws IOException {
        if (url.startsWith(Environment.getExternalStorageDirectory().toString())) {
            File localFile = new File(url);
            if (localFile.exists()) {
                return BBSUtil.getLocalDrawablePicture(url);
            }
        }
//		if(url.startsWith(Environment.getExternalStorageDirectory().toString())){
////			return Drawable.createFromPath(url);
//			return BBSUtil.getLocalDrawablePicture(url);
//		}
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File f = new File(Path.getInstance().getPicturePath() + "/bbs/" + MD5Util.getMD5String(url));
            try {
                if (f.exists()) {
                    FileInputStream fis = new FileInputStream(f);
                    Drawable d = Drawable.createFromStream(fis, "src");
                    return d;
                } else {
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                }
                URL m = new URL(url);
                InputStream i = (InputStream) m.getContent();
                DataInputStream in = new DataInputStream(i);
                FileOutputStream out = new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int byteread = 0;
                while ((byteread = in.read(buffer)) != -1) {
                    out.write(buffer, 0, byteread);
                }
                in.close();
                out.close();
                return loadImageFromUrl(url);
            } catch (Exception e) {
                if (f.exists()) {
                    f.delete();
                }
                throw new IOException();
            }
        } else {
            URL m = new URL(url);
            InputStream i = (InputStream) m.getContent();
            Drawable d = Drawable.createFromStream(i, "src");
            return d;
        }

    }

    public void setLoadLimit(int startLoadLimit, int stopLoadLimit) {
        if (startLoadLimit > stopLoadLimit) {
            return;
        }
//		mStartLoadLimit = startLoadLimit;
//		mStopLoadLimit = stopLoadLimit;
    }

    public void restore() {
        mAllowLoad = true;
        firstLoad = true;
    }

    public void lock() {
        mAllowLoad = false;
        firstLoad = false;
    }

    public void unlock() {
        mAllowLoad = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void loadImage(Integer t, String imageUrl,
                          OnImageLoadListener listener) {
        final OnImageLoadListener mListener = listener;
        final String mImageUrl = imageUrl;
        final Integer mt = t;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!mAllowLoad) {
                    synchronized (lock) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (mAllowLoad && firstLoad) {
                    loadImage(mImageUrl, mt, mListener);
                }
//				if (mAllowLoad && mt <= mStopLoadLimit && mt >= mStartLoadLimit) {
//					loadImage(mImageUrl, mt, mListener);
//				}
            }

        }).start();
    }

    private void loadImage(final String mImageUrl, final Integer mt,
                           final OnImageLoadListener mListener) {

        if (imageCache.containsKey(mImageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(mImageUrl);
            final Drawable d = softReference.get();
            if (d != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mAllowLoad) {
                            mListener.onImageLoad(mt, d);
                        }
                    }
                });
                return;
            }
        }
        try {
            final Drawable d = loadImageFromUrl(mImageUrl);
            if (d != null) {
                imageCache.put(mImageUrl, new SoftReference<Drawable>(d));
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (mAllowLoad) {
                        mListener.onImageLoad(mt, d);
                    }
                }
            });
        } catch (IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onError(mt);
                }
            });
            e.printStackTrace();
        }
    }

    public interface OnImageLoadListener {
        void onImageLoad(Integer t, Drawable drawable);

        void onError(Integer t);
    }
}
