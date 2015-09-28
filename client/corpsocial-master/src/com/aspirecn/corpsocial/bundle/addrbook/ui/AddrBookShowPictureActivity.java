package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.aspirecn.corpsocial.bundle.im.ui.ChatPictureLoadProgressWheel;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.eventbus.EventActivity;
import com.aspirecn.corpsocial.common.util.ImageUtil;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.trinea.android.common.entity.CacheObject;
import cn.trinea.android.common.entity.FailedReason;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.service.impl.ImageCache.CompressListener;
import cn.trinea.android.common.service.impl.ImageMemoryCache.OnImageCallbackListener;
import cn.trinea.android.common.service.impl.PreloadDataCache.OnGetDataListener;
import cn.trinea.android.common.util.CacheManager;
import cn.trinea.android.common.util.FileUtils;
import cn.trinea.android.common.util.StringUtils;

@EActivity(R.layout.addrboor_show_picture_activity)
public class AddrBookShowPictureActivity extends EventActivity {
    private final ImageCache imageCache = CacheManager.getImageCache();
    @ViewById(R.id.addr_book_show_picture_iv)
    ImageView mImageView;

    @ViewById(R.id.addr_book_show_picture_pb)
    ChatPictureLoadProgressWheel mProgressBar;
    // 头像地址
    private String headImgUrl;

    @AfterInject
    void doAfterInject() {
        headImgUrl = getIntent().getStringExtra("headImgUrl");
        initImageCache();
    }

    @AfterViews
    void doAfterViews() {
//		ImageDownloadUtil.INSTANCE.showImage(headImgUrl,
//				AddressBookConfig.CONTACT_IMAGE_PATH, mImageView);
        ImageLoader.getInstance().displayImage(headImgUrl, mImageView);

        mImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 退出的时候销毁缓存中的对象
        if (!StringUtils.isBlank(headImgUrl)) {
            CacheObject<Bitmap> cacheObject = imageCache.get(headImgUrl);
            if (cacheObject != null) {
                Bitmap data = cacheObject.getData();
                data.recycle();
                imageCache.remove(headImgUrl);
            }
        }
    }

    private void initImageCache() {
        imageCache.setOnImageCallbackListener(new OnImageCallbackListener() {

            // callback function before get image, run on ui thread
            @Override
            public void onPreGet(String imageUrl, View view) {
                Log.d("pre get image", "pre get image");
                imageCache.remove(imageUrl);
            }

            // callback function after get image successfully, run on ui thread
            @Override
            public void onGetSuccess(String imageUrl, Bitmap loadedImage,
                                     View view, boolean isInCache) {
                // can be another view child, like textView and so on
                if (view != null && loadedImage != null
                        && view instanceof ImageView) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageBitmap(loadedImage);
                }
            }

            // callback function after get image failed, run on ui thread
            @Override
            public void onGetFailed(String imageUrl, Bitmap loadedImage,
                                    View view, FailedReason failedReason) {
                LogUtil.i("failedReason:", failedReason.getCause());
            }

            @Override
            public void onGetNotInCache(String imageUrl, View view) {

            }
        });

        imageCache
                .setOnGetDataListener(new OnGetDataListener<String, Bitmap>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public CacheObject<Bitmap> onGetData(String imagePath) {
                        if (!FileUtils.isFileExist(imagePath)) {
                            return null;
                        }

                        CompressListener compressListener = imageCache
                                .getCompressListener();
                        int compressSize = 0;
                        if (compressListener != null) {
                            compressSize = compressListener
                                    .getCompressSize(imagePath);
                        }
                        Bitmap bm;
                        if (compressSize > 1) {
                            BitmapFactory.Options option = new BitmapFactory.Options();
                            option.inSampleSize = compressSize;
                            bm = BitmapFactory.decodeFile(imagePath, option);
                        } else {
                            bm = BitmapFactory.decodeFile(imagePath);
                        }
                        return (bm == null ? null : new CacheObject<Bitmap>(bm));
                    }
                });

        imageCache.setCompressListener(new CompressListener() {

            @Override
            public int getCompressSize(String imagePath) {
                if (FileUtils.isFileExist(imagePath)) {
                    final float scale = AspirecnCorpSocial.getContext()
                            .getResources().getDisplayMetrics().density;
                    int width = (int) (720 * scale + 0.5f);
                    int height = (int) (1280 * scale + 0.5f);

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imagePath, opts);
                    opts.inJustDecodeBounds = false;
                    return ImageUtil
                            .computeSampleSize(opts, -1, width * height);
                }
                return 1;
            }
        });
    }
}
