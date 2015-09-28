package com.aspirecn.corpsocial.common.ui.component.SpeechInput;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import com.aspirecn.corpsocial.common.config.Constant;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import cn.trinea.android.common.util.ToastUtils;

/**
 * Created by chenping on 14/12/29.
 */
public class SpeechInputManager {

    private Context mContext;
    private SpeechRecognizer mSpeechRecognizer;

    private String resultText;
    private EditText resultEditText;

    private SpeechInputDialog mVoiceDialog;
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                ToastUtils.show(mContext, "初始化失败");
            }
        }
    };
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int i) {
            if (null != mVoiceDialog) {
                mVoiceDialog.setVolume(i == 0 ? 0 : (i / 4 + 1));
            }
        }

        @Override
        public void onBeginOfSpeech() {
            if (null != mVoiceDialog) {
                mVoiceDialog.show();
            }
        }

        @Override
        public void onEndOfSpeech() {
            if (null != mVoiceDialog) {
                mVoiceDialog.startRecognizer();
            }
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            if (null != mVoiceDialog) {
                mVoiceDialog.dismiss();
            }
            String text = SpeechJsonParser.parseIatResult(results.getResultString());
            resultText = resultText + text;
            if (isLast) {
                resultEditText.setText(resultText);
                resultEditText.setSelection(resultText.length());
                resultEditText.setCursorVisible(true);
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            if (null != mVoiceDialog) {
                mVoiceDialog.dismiss();
            }
            ToastUtils.show(mContext, speechError.getErrorDescription());
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            System.out.println("event");
        }
    };

    private SpeechInputManager() {

    }

    public static SpeechInputManager getInstance() {
        return SingletonHolder.instance;
    }

    public void init(Context mContext) {
        this.mContext = mContext;
        mSpeechRecognizer = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        setSpeechParams();
        mVoiceDialog = new SpeechInputDialog(mContext);
        mVoiceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mVoiceDialog.endRecognizer();
            }
        });
    }

    public void startSpeech(EditText mEditText) {
        resultText = mEditText.getText().toString();
        this.resultEditText = mEditText;
        int resCode = mSpeechRecognizer.startListening(mRecognizerListener);
        if (ErrorCode.SUCCESS != resCode) {
            ToastUtils.show(mContext, "语音输入启动失败");
        }
    }

    private void setSpeechParams() {
        // 清空参数
        mSpeechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置语言
        mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
//        mSpeechRecognizer.setParameter(SpeechConstant.ACCENT,lag);
        // 设置语音前端点
//        mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
        // 设置语音后端点
//        mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
        // 设置标点符号
//        mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
        // 设置音频保存路径
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Constant.ROOT_PATH + "/speechinput/wavaudio.pcm");
    }

    public void destory() {
        mSpeechRecognizer.cancel();
        mSpeechRecognizer.destroy();
        mVoiceDialog = null;
    }

    private static class SingletonHolder {
        private static SpeechInputManager instance = new SpeechInputManager();
    }
}
