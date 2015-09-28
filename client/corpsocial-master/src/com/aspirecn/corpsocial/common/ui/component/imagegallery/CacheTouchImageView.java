package com.aspirecn.corpsocial.common.ui.component.imagegallery;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.aspirecn.corpsocial.bundle.im.ui.ChatPictureLoadProgressWheel;
import com.aspirecn.corpsocial.common.util.DensityUtil;
import com.aspirecn.corpsocial.common.util.DownloadAsyncTask;
import com.aspirecn.corpsocial.common.util.ImageDownloadUtil;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.io.File;

import ru.truba.touchgallery.TouchView.TouchImageView;

/**
 * 用于图片墙等多个图片滑动浏览的控件
 *
 * @author meixuesong
 */
public class CacheTouchImageView extends RelativeLayout {
    protected ChatPictureLoadProgressWheel mProgressBar;
    protected TouchImageView mImageView;

    protected Context mContext;
    private String imageSubPath;

    public CacheTouchImageView(Context ctx, String imageSubPath) {
        super(ctx);
        mContext = ctx;
        this.imageSubPath = imageSubPath;
        init();
    }

    public CacheTouchImageView(Context ctx, AttributeSet attrs,
                               String imageSubPath) {
        super(ctx, attrs);
        mContext = ctx;
        this.imageSubPath = imageSubPath;
        init();
    }

    public TouchImageView getImageView() {
        return mImageView;
    }

    protected void init() {
        mImageView = new TouchImageView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        mImageView.setLayoutParams(params);
        this.addView(mImageView);
        mImageView.setVisibility(GONE);

        mProgressBar = new ChatPictureLoadProgressWheel(mContext, null);
        /*
         * <com.aspirecn.corpsocial.bundle.im.ui.ChatPictureLoadProgressWheel
		 * xmlns:ProgressWheel="http://schemas.android.com/apk/res-auto"
		 * android:id="@+id/im_chat_show_picture_pb" android:layout_width="80dp"
		 * android:layout_height="80dp" ProgressWheel:barColor="#339BB9"
		 * ProgressWheel:barWidth="4dp" ProgressWheel:rimColor="#44000000"
		 * ProgressWheel:rimWidth="4dp" ProgressWheel:spinSpeed="3dp"
		 * ProgressWheel:textColor="#222222" ProgressWheel:textSize="14sp"
		 * android:visibility="visible"/>
		 */
        LayoutParams params2 = new LayoutParams(
                DensityUtil.dip2px(mContext, 80), DensityUtil.dip2px(mContext,
                80));
        params2.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setLayoutParams(params2);
        mProgressBar.setBarColor(Color.parseColor("#339BB9"));
        mProgressBar.setBarWidth(DensityUtil.dip2px(mContext, 4));
        mProgressBar.setRimColor(Color.parseColor("#44000000"));
        mProgressBar.setRimWidth(DensityUtil.dip2px(mContext, 4));
        mProgressBar.setSpinSpeed(DensityUtil.dip2px(mContext, 3));
        mProgressBar.setTextColor(Color.parseColor("#222222"));
        mProgressBar.setTextSize(14);
        mProgressBar.setVisibility(GONE);

        this.addView(mProgressBar);
    }

    public void setUrl(String imageUrl) {
        if (imageUrl == null) {
            LogUtil.e("图片展示时，图片的地址为空。");
            return;
        }
        String fileName = getLocalFileName(imageUrl);
        File file = new File(fileName);
        if (file.exists()) {
            // 文件存在，直接显示
            showImage(fileName);
        } else {
            // 异步下载
            DownloadTask task = new DownloadTask();
            System.out.println("pic path: " + task.getSaveFilePath());
            task.execute(imageUrl);
        }
    }

    private String getLocalFileName(String url) {
        if (url != null && url.startsWith("file:")) {
            String urlStr = url.substring("file:".length());
            return urlStr;
        }
        return ImageDownloadUtil.INSTANCE.getFileName(url, imageSubPath);
    }

    private String extractFileName(String fullPathFileName) {
        int i = fullPathFileName.lastIndexOf('/');
        if (i >= 0) {
            return fullPathFileName.substring(i + 1, fullPathFileName.length());
        } else {
            return fullPathFileName;
        }
    }

    private String extractPathName(String fullPathFileName) {
        int i = fullPathFileName.lastIndexOf('/');
        if (i >= 0) {
            return fullPathFileName.substring(0, i);
        } else {
            return fullPathFileName;
        }
    }

    private void showImage(String fileName) {
        mProgressBar.setVisibility(GONE);
        mImageView.setScaleType(ScaleType.MATRIX);
        try {
            mImageView.setImageBitmap(BitmapFactory.decodeFile(fileName));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        mImageView.setVisibility(VISIBLE);
    }

    public void setScaleType(ScaleType scaleType) {
        mImageView.setScaleType(scaleType);
    }

    private void updateProgress(int percent) {
        mProgressBar.setVisibility(VISIBLE);
        mProgressBar.setProgress((int) (percent * 3.6));
        mProgressBar.setText(percent + "%");
    }

    // 异步下载类
    private class DownloadTask extends DownloadAsyncTask {
        DownloadTask() {
            super();
            String path = extractPathName(getLocalFileName("http://asdkfjsldfa/abc.jpg"));
            setSaveFilePath(path);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.length() > 0) {
                showImage(result);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            updateProgress(values[0]);
        }

        @Override
        public String getFileName(String url) {
            return extractFileName(getLocalFileName(url));
        }
    }
}
