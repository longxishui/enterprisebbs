package com.aspirecn.corpsocial.bundle.im.ui;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;

import com.aspiren.corpsocial.R;

public class RecordVoiceDialog extends Dialog {

    /**
     * 音量显示
     */
    private ImageView mVolumeIv;
    /**
     * 音量图标资源id
     */
    private int[] mVolumeImgResIds = {R.drawable.im_record_voice_icon_0,
            R.drawable.im_record_voice_icon_1,
            R.drawable.im_record_voice_icon_2,
            R.drawable.im_record_voice_icon_3,
            R.drawable.im_record_voice_icon_4,
            R.drawable.im_record_voice_icon_5,
            R.drawable.im_record_voice_icon_6,
            R.drawable.im_record_voice_icon_7,
            R.drawable.im_record_voice_icon_8};

    public RecordVoiceDialog(Context context) {
        super(context, R.style.MyDialog1);
        initView();
    }

    private void initView() {
        setContentView(R.layout.im_record_voice_dialog);
        mVolumeIv = (ImageView) findViewById(R.id.im_record_voice_volume_iv);
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

    /**
     * 设置提示信息
     *
     * @param msg
     */
    public void setPromptMsg(String msg) {

    }

}
