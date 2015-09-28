/**
 * @(#) MediaRecordAndPlayer.java Created on 2013-12-18
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.util;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.widget.ImageView;

import com.aspiren.corpsocial.R;


import java.io.File;
import java.io.IOException;

/**
 * The class <code>MediaRecordAndPlayer</code>
 *
 * @author lihaiqiang
 */
public class MediaRecordAndPlayer {

    private static MediaRecordAndPlayer instance = new MediaRecordAndPlayer();
    /**
     * 录音机
     */
    private MediaRecorder mRecorder = null;
    /**
     * 播放机
     */
    private MediaPlayer mPlayer = null;
    private boolean mIsPlay = false;
    private String mVoiceFilePath = "";
    private AnimationDrawable animationDrawable;
    private ImageView mLeftImageView;
    private ImageView mRightImageView;

    private MediaRecordAndPlayer() {
    }

    public static MediaRecordAndPlayer getInstance() {
        return instance;
    }

    /**
     * 开始录音
     *
     * @param filePath 文件的绝对路径
     */
    public boolean startRecord(String filePath) {
        mRecorder = new MediaRecorder();
        try {
            if (mIsPlay) {
                stopPlay();
            }
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 设置音源为Micphone
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置封装格式
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            // 设置存放路径
            mRecorder.setOutputFile(filePath);
            // 设置编码格式
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            if (mRecorder != null) {
                mRecorder.release();
                mRecorder = null;
            }
            return false;
        }
        return true;
    }

    /**
     * 停止录音
     */
    public synchronized void stopRecord() {
        SystemClock.sleep(300);// 延时停止
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
        } catch (Exception e) {
            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * 获取音量
     *
     * @return
     */
    public int getVolume() {
        int volume = 0;
        if (mRecorder != null) {
            volume = mRecorder.getMaxAmplitude() / 3500;
            if (volume > 8) {
                volume = 8;
            }
        }
        return volume;
    }

    /**
     * 启动播放
     *
     * @param filePath  语音路劲
     * @param imageView 语音动画View
     * @param direction 位置，0：左侧 1：右侧
     */
    public void startPlay(String filePath, ImageView imageView, int direction) {
        initVoicePicture();
        if (mVoiceFilePath.equals(filePath)) {
            stopPlay();
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        if (mIsPlay) {
            stopPlay();
        }
        mIsPlay = true;
        mPlayer = new MediaPlayer();
        try {
            // 设置要播放的文件
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {// 手动停止不会调此方法
                    mIsPlay = false;
                    initVoicePicture();
                    mVoiceFilePath = "";
                }
            });
            // 启动播放
            mPlayer.start();
            mVoiceFilePath = filePath;

            // 播放动画
            refreshVoiceAnimation(filePath, imageView, direction);

        } catch (IOException e) {
            mIsPlay = false;
        }
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        if (mPlayer != null) {
            try {
                mVoiceFilePath = "";
                mIsPlay = false;
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            } catch (IllegalStateException e) {
                mPlayer.release();
                mPlayer = null;
            }
        }
    }

    /**
     * 获取音量
     *
     * @return
     */
    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude() / 2700.0);
        else
            return 0;
    }

    /**
     * 恢复声音播放图片
     */
    public void initVoicePicture() {
        if (animationDrawable != null) {// 恢复
            animationDrawable.stop();
        }
        if (mLeftImageView != null) {
            mLeftImageView
                    .setImageResource(R.drawable.im_chat_from_voice_left_playing_1);
        }
        if (mRightImageView != null) {
            mRightImageView
                    .setImageResource(R.drawable.im_chat_from_voice_right_playing_1);
        }
    }

    /**
     * 刷新语音播放动画
     *
     * @param filePath  语音路劲
     * @param imageView 语音动画View
     * @param direction 位置，0：左侧 1：右侧
     */
    public void refreshVoiceAnimation(String filePath, ImageView imageView,
                                      int direction) {
        if (mVoiceFilePath.equals(filePath) && mIsPlay) {
            // 播放动画
            if (direction == 0) {
                voiceLeftAnimation(imageView);
            } else {
                voiceRightAnimation(imageView);
            }
        }
    }

    private void voiceLeftAnimation(ImageView imageView) {
        this.mLeftImageView = imageView;
        this.mLeftImageView
                .setImageResource(R.drawable.im_chat_msg_voice_animation_left_positive);
        animationDrawable = (AnimationDrawable) mLeftImageView.getDrawable();
        animationDrawable.start();
    }

    private void voiceRightAnimation(ImageView imageView) {
        this.mRightImageView = imageView;
        this.mRightImageView
                .setImageResource(R.drawable.im_chat_msg_voice_animation_right_positive);
        animationDrawable = (AnimationDrawable) mRightImageView.getDrawable();
        animationDrawable.start();
    }

}
