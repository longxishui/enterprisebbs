package com.aspirecn.corpsocial.bundle.settings.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.settings.event.ModifyHeadImgEvent;
import com.aspirecn.corpsocial.bundle.settings.event.ModifyHeadImgRespEvent;
import com.aspirecn.corpsocial.bundle.settings.listener.ModifyHeadImgRespSubject.ModifyHeadPortraitRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventActivity;
import com.aspirecn.corpsocial.common.ui.widget.CustomDialog;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;

/**
 * 上传头像
 *
 * @author
 */

@EActivity(R.layout.setting_upload_head_portrait_activity)
public class UploadHeadPortraitActivity extends EventActivity implements
        ModifyHeadPortraitRespEventListener {

    // 必须硬编码才有效，Constant.TEMP_PATH.concat("avatar.jpg")
    private final Uri avatarUri = getUri("file:///sdcard/aspirencorpsocial/avatar/".concat("avatar_" + System.currentTimeMillis() + ".jpg"));

    @ViewById(R.id.setting_upload_head_portrait_menu_ll)
    View mMenuLL;

    @ViewById(R.id.setting_upload_head_portrait_loading)
    ProgressBar progressbar;

    @ViewById(R.id.setting_upload_head_portrait_rl)
    RelativeLayout headPortrait;

    @ViewById(R.id.setting_upload_head_portrait_location_bt)
    Button locationBtn;

    @ViewById(R.id.setting_upload_head_portrait_take_photo_bt)
    Button photoBtn;

    @ViewById(R.id.setting_upload_head_portrait_cancel_bt)
    Button cancelBtn;

    /**
     * 确保文件所在目录存在，没有则创建
     *
     * @param path
     * @return
     */
    public static Uri getUri(String path) {
        if (path != null) {
            Uri uri = Uri.parse(path);
            File file = new File(uri.getPath());
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            return uri;
        }
        return null;
    }

    @AfterViews
    void doAfterViews() {
        AppConfig appConfig = AppConfig.getInstance();
        String actionBarColor = appConfig.topViewDef.backgroundColor;
//        locationBtn.setTextColor(Color.parseColor(actionBarColor));
//        photoBtn.setTextColor(Color.parseColor(actionBarColor));
        cancelBtn.setTextColor(Color.parseColor(actionBarColor));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setShowAnimation(mMenuLL);
    }

    /**
     * 本地图片按钮
     */
    @Click(R.id.setting_upload_head_portrait_location_bt)
    void onClickLocationPictureBtn() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, ActionKey.LOCATION_PICTURE);
    }

    /**
     * 拍照按钮
     */
    @Click(R.id.setting_upload_head_portrait_take_photo_bt)
    void onClickTakePhotoBtn() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, avatarUri);
        startActivityForResult(intent, ActionKey.TAKE_PHOTO);
    }

    /**
     * 取消按钮
     */
    @Click(R.id.setting_upload_head_portrait_cancel_bt)
    void onClickCancelBtn() {
        finish();
    }

    /**
     * 剪切图片
     */
    private void startForCropPictrue(Uri sourceUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(sourceUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("scale", true);
        intent.putExtra("output", avatarUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, ActionKey.CROP_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != -1) {
            return;
        }
        switch (requestCode) {
            case ActionKey.LOCATION_PICTURE:// 本地图片
                startForCropPictrue(data.getData());
                headPortrait.setVisibility(View.GONE);
                break;
            case ActionKey.TAKE_PHOTO:// 拍照
                startForCropPictrue(avatarUri);
                headPortrait.setVisibility(View.GONE);
                break;
            case ActionKey.CROP_PICTURE:// 图片剪切
                handleCropPictureResult(avatarUri.getPath());
                break;
            default:
                break;
        }
    }

    /**
     * 处理剪切图片返回结果
     *
     * @param picturePath 剪切后图片存放地址
     */
    private void handleCropPictureResult(String picturePath) {
        // 转化成圆角图片上传
//        ImageUtil.saveThePicture(ImageUtil.getRoundedCornerBitmap(ImageUtil.getBitmapByPath(picturePath), 60.0f), picturePath);

        doModifyHeadPortrait(picturePath);
    }

    /**
     * 上传头像
     */
    private void doModifyHeadPortrait(String picturePath) {
        CustomDialog.showProgeress(this);
        ModifyHeadImgEvent mModifyHeadImgEvent = new ModifyHeadImgEvent();
        mModifyHeadImgEvent.setHeadImageUrl(picturePath);
        this.uiEventHandleFacade.handle(mModifyHeadImgEvent);
    }

    /**
     * 上移动画
     *
     * @param view
     */
    private void setShowAnimation(View view) {
        Animation animation = new TranslateAnimation(0, 0, dp2px(200), 0);
        animation.setDuration(300);
        // animation.setRepeatCount(1);
        view.startAnimation(animation);
        animation.start();
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    private int dp2px(int dp) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        return (int) (dp * metric.density);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    /**
     * 处理修改头像响应事件
     */
    @UiThread
    @Override
    public void onHandleModifyHeadPortraitRespEvent(ModifyHeadImgRespEvent event) {
        CustomDialog.closeProgress(this);
        int errorCode = event.getErrorCode();
        if (ErrorCode.SUCCESS.getValue() == errorCode) {
            show("头像修改成功!");
            this.finish();
        } else if (ErrorCode.NO_NETWORK.getValue() == errorCode) {
            show("网络连接不可用！");
            this.finish();
        } else {
            show("头像修改失败!");
            this.finish();
        }
    }

    @UiThread
    void show(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public interface ActionKey {
        int LOCATION_PICTURE = 0;
        int TAKE_PHOTO = 1;
        int CROP_PICTURE = 2;
    }

}
