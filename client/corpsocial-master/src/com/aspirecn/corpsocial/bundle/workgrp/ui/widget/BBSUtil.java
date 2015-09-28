package com.aspirecn.corpsocial.bundle.workgrp.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.bundle.workgrp.domain.KeyValue;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

public class BBSUtil {

    public static final int BBS_REQUEST_PHOTO = 1;
    public static final int BBS_REQUEST_CAMERA = 2;

    /**
     * 获取指定大小的缩略图
     *
     * @param imagePath
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public static void closeInputMethod(Context mContext, EditText et) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void openInputMethod(Context mContext, EditText et) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void toggleInputMethod(Context mContext) {
        InputMethodManager m = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean isInputMethodOpend(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        return isOpen;
    }

    /**
     * 选择图片
     *
     * @param mActivity
     */
    public static void pickPhoto(Activity mActivity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, BBS_REQUEST_PHOTO);
    }

    /**
     * 拍照
     *
     * @param mActivity
     * @param imagePath
     */
    public static void takePicture(Activity mActivity, String imagePath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (null != imagePath && !imagePath.isEmpty()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
        }
        mActivity.startActivityForResult(intent, BBS_REQUEST_CAMERA);
    }

    public static void showPicture(Context mContext, String imagePath) {
        File file = new File(imagePath);
        if (file != null && file.isFile() == true) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "image/*");
            mContext.startActivity(intent);
        }
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static SpannableString getPraisedUserTip(ArrayList<KeyValue> userNames) {
        String names = null;
        String info = null;
        if (userNames == null || userNames.size() == 0) {
            SpannableString result = new SpannableString("赞过");
            result.setSpan(new ForegroundColorSpan(R.color.workgrp_praised_name), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return result;
        }
        int size = userNames.size();
        if (size == 1) {
            names = userNames.get(0).getValue();
            info = " 赞过";
        } else {
            names = userNames.get(0).getValue() + "," + userNames.get(1).getValue();
            if (size == 2) {
                info = " 赞过";
            } else {
                info = " 等" + size + "人赞过";
            }
        }
        SpannableString result = new SpannableString(names + info);
        result.setSpan(new ForegroundColorSpan(R.color.workgrp_praised_name), 0, names.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return result;
    }

    public static String getBetweenTime(long time) {
//		long distance = (System.currentTimeMillis()-time)/1000;
        long distance = (Calendar.getInstance().getTimeInMillis() - time) / 1000;
        String result = null;
        if (distance / (60 * 60 * 24) > 0) { //日期
//			result = new SimpleDateFormat("yyyy年M月d日 HH:mm").format(new Date(time));
            result = new SimpleDateFormat("yyyy年M月d日").format(new Date(time));
        } else if (distance / (60 * 60) > 0) { //小时
            result = distance / (60 * 60) + "小时前";
        } else if (distance / 60 > 0) { //分钟
            result = distance / 60 + "分钟前";
        } else { //秒
            result = distance + "秒前";
        }
        return result;
    }

    public static void goBBSPraiseListActivity(Context mContext, BBSItem bbsItem) {
        Intent mIntent = new Intent(mContext, com.aspirecn.corpsocial.bundle.workgrp.ui.WorkgrpBBSPraiseListActivity_.class);
        mIntent.putExtra("bbsId", bbsItem.getBbsItemEntity().getId());
        mContext.startActivity(mIntent);
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Drawable getLocalDrawablePicture(String imagePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, opts);
        opts.inSampleSize = BBSUtil.computeSampleSize(opts, -1, 256 * 256);
        opts.inJustDecodeBounds = false;
        Bitmap mBitmap = BitmapFactory.decodeFile(imagePath, opts);
        Drawable mDrawable = new BitmapDrawable(mBitmap);
        return mDrawable;
    }

    public static Bitmap getLocalBitmapPicture(String imagePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, opts);
        opts.inSampleSize = BBSUtil.computeSampleSize(opts, -1, 256 * 256);
        opts.inJustDecodeBounds = false;
        Bitmap mBitmap = BitmapFactory.decodeFile(imagePath, opts);
        return mBitmap;
    }

//	public static File getUploadFile(String filePath) {
//		String tmpFilePath = Constant.PICTURE_PATH + "bbs_"
//				+ System.currentTimeMillis() + ".jpg";
//		// long fileSize = FileUtils.getFileSize(filePath) / 1000;
//		// final float scale = AspirecnCorpSocial.getContext().getResources()
//		// .getDisplayMetrics().density;
//		// int width = (int) (500 * scale + 0.5f);
//		// int height = (int) (500 * scale + 0.5f);
//		int width = 500;
//		int height = 500;
//
//		BitmapFactory.Options opts = new BitmapFactory.Options();
//		opts.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(filePath, opts);
//		opts.inJustDecodeBounds = false;
//		int compressSize = ImageUtil
//				.computeSampleSize(opts, -1, width * height);
//
//		BitmapFactory.Options option = new BitmapFactory.Options();
//		option.inSampleSize = compressSize;
//		Bitmap picture = BitmapFactory.decodeFile(filePath, option);
//
//		OutputStream outputStream = null;
//		// create file
//		File file = new File(tmpFilePath);
//		try {
//			outputStream = new FileOutputStream(file);
//			picture.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
//			outputStream.flush();
//			outputStream.close();
//		} catch (IOException e) {
//			Log.d("", "ERROR");
//		}
//
//		return file;
//	}

    public static void setUserHeadImg(String userId, ImageView img) {
        User contact = getUser(userId);

        //解决由于ImageView复用，造成头像可能错位的问题
//		img.setImageResource(R.drawable.component_initial_headimg);
        String headImgUrl = null;
        if (contact != null) {
            headImgUrl = contact.getUrl();
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                .showImageForEmptyUri(R.drawable.component_initial_headimg)
                .build();
        ImageLoader.getInstance().displayImage(headImgUrl, img, options);
    }

    /**
     * 根据userId获取用户
     *
     * @param userId
     * @return
     */
    private static User getUser(String userId) {
//        UserService service = (UserService) OsgiServiceLoader.getInstance()
//                .getService(UserService.class);
//        UserServiceParam param = new UserServiceParam();
//        param.setServie("FindContactService");
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("userid", userId);
//        param.setParams(map);
//        UserServiceResult<List<User>> result = service.invoke(param);
//        List<User> users = result.getData();
//        if (users != null && users.size() > 0) {
//            return users.get(0);
//        }
//        return null;
        return (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(userId);
    }

}
