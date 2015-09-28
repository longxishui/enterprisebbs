package com.aspirecn.corpsocial.common.ui.component.SpeechInput;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aspiren.corpsocial.R;

/**
 * Created by chenping on 15/1/19.
 */
public class SpeechInputDialog extends Dialog {

    /**
     * 音量显示
     */
    private ImageView mVolumeIv;

    /**
     * 加载框布局
     */
    private RelativeLayout mProgressLayout;

    private ImageView mLoadingIv;

    private RotateAnimation animation = null;

    /**
     * 音量图标资源id
     */
    private int[] mVolumeImgResIds = {R.drawable.speech_voice_icon_0,
            R.drawable.speech_voice_icon_1,
            R.drawable.speech_voice_icon_2,
            R.drawable.speech_voice_icon_3,
            R.drawable.speech_voice_icon_4,
            R.drawable.speech_voice_icon_5,
            R.drawable.speech_voice_icon_6,
            R.drawable.speech_voice_icon_7,
            R.drawable.speech_voice_icon_8};

    public SpeechInputDialog(Context context) {
        super(context, R.style.MyDialog1);
        initView();
    }

    private void initView() {
        setContentView(R.layout.component_speech_input_dialog);
        mVolumeIv = (ImageView) findViewById(R.id.im_record_voice_volume_iv);
        mProgressLayout = (RelativeLayout) findViewById(R.id.speech_input_dialog_progressId);
        mLoadingIv = (ImageView) findViewById(R.id.speech_recognizing_iv);
        initAnimation();
    }

    private void initAnimation() {
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setFillAfter(true);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
    }

    /**
     * 设置音量
     *
     * @param volume 音量等级1-4个等级
     */
    public void setVolume(int volume) {
        if (volume >= 0 && volume < mVolumeImgResIds.length) {
            mVolumeIv.setImageResource(mVolumeImgResIds[volume]);
        }
    }

    public void startRecognizer() {
        mProgressLayout.setVisibility(View.VISIBLE);
        mVolumeIv.setVisibility(View.GONE);
        if (null != animation) {
            mLoadingIv.startAnimation(animation);
        }
    }

    public void endRecognizer() {
        mProgressLayout.setVisibility(View.GONE);
        mVolumeIv.setVisibility(View.VISIBLE);
        if (null != animation) {
            mLoadingIv.clearAnimation();
        }
    }

    /**
     * 设置提示信息
     *
     * @param msg
     */
    public void setPromptMsg(String msg) {

    }

}
