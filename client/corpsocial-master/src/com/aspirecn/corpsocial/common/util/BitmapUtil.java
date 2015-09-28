package com.aspirecn.corpsocial.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.aspirecn.corpsocial.common.config.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by hongyinghui on 15/4/21.
 */
public class BitmapUtil {

    /**
     * 矫正图片的真实旋转度
     *
     * @param imagePath
     */
    public static void correction(String imagePath) {
        System.gc();
        int degree = readPictureDegree(imagePath);
        // file 到 bitmap
        Bitmap b = getLocalBitmapPicture(imagePath);
        // bitmap 改变角度
        b = rotaingImageView(degree, b);
        // 保存到file
        saveBitmap2file(b, imagePath);
    }

    /**
     * FILE -> BITMAP
     *
     * @param imagePath
     * @return
     */
    private static Bitmap getLocalBitmapPicture(String imagePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, 480 * 800);
        opts.inJustDecodeBounds = false;
        Bitmap mBitmap = BitmapFactory.decodeFile(imagePath, opts);
        return mBitmap;
    }

    private static int computeSampleSize(BitmapFactory.Options options,
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

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转度
     * @param bitmap 原图
     * @return Bitmap
     */
    private static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    private static void saveBitmap2file(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        try {
            int quality = 100;
            OutputStream stream = new FileOutputStream(filename);
            bmp.compress(format, quality, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            bmp.recycle();
        }
    }

    public static File uploadFile(String filePath) {
        String tmpFilePath = Constant.IM_PATH + "picture" + File.separator + "picture_"
                + System.currentTimeMillis() + ".jpg";
        int width = 360;
        int height = 480;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        opts.inJustDecodeBounds = false;
        int compressSize = ImageUtil
                .computeSampleSize(opts, -1, width * height);

        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = compressSize;
        Bitmap picture = BitmapFactory.decodeFile(filePath, option);

        OutputStream outputStream = null;
        // create file
        File file = new File(tmpFilePath);
        try {
            outputStream = new FileOutputStream(file);
            picture.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            Log.d("", "ERROR");
        } finally {
            picture.recycle();
        }
        return file;
    }
}