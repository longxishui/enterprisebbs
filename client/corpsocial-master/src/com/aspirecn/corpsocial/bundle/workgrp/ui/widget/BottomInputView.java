package com.aspirecn.corpsocial.bundle.workgrp.ui.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.im.domain.ChatEmoji;
import com.aspirecn.corpsocial.bundle.im.ui.ChatFaceRelativeLayout;
import com.aspirecn.corpsocial.bundle.im.ui.ChatFaceRelativeLayout.OnCorpusSelectedListener;
import com.aspirecn.corpsocial.common.config.Path;
import com.aspirecn.corpsocial.common.util.FaceConversionUtil;
import com.aspiren.corpsocial.R;

public class BottomInputView extends LinearLayout {

    private String picture_path = null;

    private Context mContext = null;

    private ImageView voiceBtn = null;
    private ImageButton moreselectBtn = null;
    private ChatFaceRelativeLayout faceLayout = null;
    private LinearLayout moreSelectLayout = null;
    private EditText replyEdit = null;
    private Button mPressVoiceBtn = null;

    private ImageButton browLayout = null;
    private TextView photoLayout = null;
    private TextView cameralayout = null;

    private LinearLayout pictureLayout = null;
    private RelativeLayout showPictureLayout = null;
    private ImageView showPicture = null;
    private ImageView delPicture = null;
    private ImageView addPicture = null;

    private Button sendBtn = null;

    private Bitmap mBitmap = null;
    /**
     * 字数限制
     */
    private int limitCountText = 200;
    /**
     * 气球的布局
     */
    private RelativeLayout mRL_balloonBtn;
    /**
     * 气球的发送按钮
     */
    private Button mBT_balloonBtn;
    /**
     * 气球的数字
     */
    private TextView mTV_number;
    private OnClickListener bivOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == voiceBtn) {
                if (!mPressVoiceBtn.isShown()) {
                    BBSUtil.closeInputMethod(mContext, replyEdit);
                    mPressVoiceBtn.setVisibility(View.VISIBLE);
                    voiceBtn.setBackgroundResource(R.drawable.im_chat_keybord_icon);
                    faceLayout.setVisibility(View.GONE);
                    moreSelectLayout.setVisibility(View.GONE);
                } else {
                    replyEdit.requestFocus();
                    BBSUtil.openInputMethod(mContext, replyEdit);
                    mPressVoiceBtn.setVisibility(View.GONE);
                    voiceBtn.setBackgroundResource(R.drawable.im_chat_voice_icon);
                }
                if (pictureLayout.isShown()) {
                    pictureLayout.setVisibility(View.GONE);
                }
            } else if (v == moreselectBtn) {
                if (faceLayout.isShown()) {
                    faceLayout.setVisibility(View.GONE);
                }
                if (pictureLayout.isShown()) {
                    pictureLayout.setVisibility(View.GONE);
                }
                if (moreSelectLayout.isShown()) {
                    moreSelectLayout.setVisibility(View.GONE);
                    replyEdit.requestFocus();
                    BBSUtil.openInputMethod(mContext, replyEdit);
                } else {
                    replyEdit.clearFocus();
                    BBSUtil.closeInputMethod(mContext, replyEdit);
                    moreSelectLayout.setVisibility(View.VISIBLE);
                }
            } else if (v == browLayout) {
                moreSelectLayout.setVisibility(View.GONE);
                if (pictureLayout.isShown()) {
                    pictureLayout.setVisibility(View.GONE);
                }
                if (faceLayout.isShown()) {
                    faceLayout.setVisibility(View.GONE);
                    BBSUtil.openInputMethod(mContext, replyEdit);
                } else {
                    BBSUtil.closeInputMethod(mContext, replyEdit);
                    faceLayout.setVisibility(View.VISIBLE);
                }
            } else if (v == photoLayout) {
                if (picture_path == null || picture_path.isEmpty()) {
                    BBSUtil.pickPhoto((Activity) mContext);
                } else {
                    moreSelectLayout.setVisibility(View.GONE);
                    pictureLayout.setVisibility(View.VISIBLE);
                }
            } else if (v == cameralayout) {
                if (picture_path == null || picture_path.isEmpty()) {
                    picture_path = Path.getInstance().getPicturePath() + "bbs_" + System.currentTimeMillis() + ".jpg";
                    BBSUtil.takePicture((Activity) mContext, picture_path);
                } else {
                    moreSelectLayout.setVisibility(View.GONE);
                    pictureLayout.setVisibility(View.VISIBLE);
                }
            } else if (v == delPicture) {
                showPictureLayout.setVisibility(View.GONE);
                picture_path = null;
                showPicture.setImageBitmap(null);
                if (mBitmap != null && !mBitmap.isRecycled()) {
                    mBitmap.recycle();
                    mBitmap = null;
                }
            } else if (v == addPicture) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                final String[] items = new String[]{"拍照", "相册"};
                dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("拍照".equals(items[which])) {
                            picture_path = Path.getInstance().getPicturePath() + "bbs_" + System.currentTimeMillis() + ".jpg";
                            BBSUtil.takePicture((Activity) mContext, picture_path);
                        } else if ("相册".equals(items[which])) {
                            BBSUtil.pickPhoto((Activity) mContext);
                        }
                    }
                });
                dialogBuilder.setTitle("操作");
                dialogBuilder.create().show();
            }
        }
    };

    public BottomInputView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public BottomInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public BottomInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.workgrp_replyinput_view, this, true);
        mTV_number = (TextView) findViewById(R.id.workgrp_detail_reply_number);
        mRL_balloonBtn = (RelativeLayout) findViewById(R.id.workgrp_detail_reply_send_rl);
        mBT_balloonBtn = (Button) findViewById(R.id.workgrp_detail_reply_btnid_balloon);
        voiceBtn = (ImageView) findViewById(R.id.workgrp_detail_reply_voiceid);
        moreselectBtn = (ImageButton) findViewById(R.id.biv_more_btnid);
        faceLayout = (ChatFaceRelativeLayout) findViewById(R.id.biv_brow_viewid);
        moreSelectLayout = (LinearLayout) findViewById(R.id.biv_more_select_viewid);
        replyEdit = (EditText) findViewById(R.id.workgrp_detail_reply_inputid);
        mPressVoiceBtn = (Button) findViewById(R.id.biv_pressed_voice_btnid);
        browLayout = (ImageButton) findViewById(R.id.biv_brow_select_viewid);
        photoLayout = (TextView) findViewById(R.id.biv_photo_select_viewid);
        cameralayout = (TextView) findViewById(R.id.biv_camera_select_viewid);
        sendBtn = (Button) findViewById(R.id.workgrp_detail_reply_btnid);
        sendBtn.setBackgroundColor(Color.parseColor(AppConfig.getInstance().topViewDef.backgroundColor));
        sendBtn.setTextColor(Color.WHITE);
        pictureLayout = (LinearLayout) findViewById(R.id.biv_picture_layoutid);
        showPictureLayout = (RelativeLayout) findViewById(R.id.biv_picture_showlayoutid);
        showPicture = (ImageView) findViewById(R.id.biv_picture_resultid);
        delPicture = (ImageView) findViewById(R.id.biv_picture_deleteid);
        addPicture = (ImageView) findViewById(R.id.biv_picture_addid);
        delPicture.setOnClickListener(bivOnClickListener);
        addPicture.setOnClickListener(bivOnClickListener);
        replyEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (moreSelectLayout.isShown()) moreSelectLayout.setVisibility(View.GONE);
                    if (faceLayout.isShown()) faceLayout.setVisibility(View.GONE);
                    if (pictureLayout.isShown()) pictureLayout.setVisibility(View.GONE);
                }
            }
        });
        replyEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                BBSUtil.openInputMethod(mContext, replyEdit);
                if (moreSelectLayout.isShown()) moreSelectLayout.setVisibility(View.GONE);
                if (faceLayout.isShown()) faceLayout.setVisibility(View.GONE);
                if (pictureLayout.isShown()) pictureLayout.setVisibility(View.GONE);
            }
        });
        faceLayout.setOnCorpusSelectedListener(new OnCorpusSelectedListener() {
            @Override
            public void onCorpusSelected(ChatEmoji emoji) {
                if (TextUtils.isEmpty(emoji.getFaceName()) && emoji.getId() == R.drawable.im_chat_face_del_btn) {
                    int selection = replyEdit.getSelectionStart();
                    String text = replyEdit.getText().toString();
                    if (selection > 0) {
                        if ("]".equals(text.substring(selection - 1))) {
                            int start = text.lastIndexOf("[");
                            int end = selection;
                            replyEdit.getText().delete(start, end);
                        } else {
                            replyEdit.getText().delete(selection - 1, selection);
                        }
                    }
                } else {
                    if (emoji.getCharacter() == null) {
                        return;
                    }
                    SpannableString spannableString = FaceConversionUtil.getInstace().addFace(mContext.getApplicationContext(), emoji.getPath(), emoji.getCharacter());
                    replyEdit.append(spannableString);
                }
            }

            @Override
            public void onCorpusDeleted() {

            }
        });
        voiceBtn.setOnClickListener(bivOnClickListener);
        moreselectBtn.setOnClickListener(bivOnClickListener);
        browLayout.setOnClickListener(bivOnClickListener);
        photoLayout.setOnClickListener(bivOnClickListener);
        cameralayout.setOnClickListener(bivOnClickListener);
        replyEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (input.isEmpty()) {
                    mRL_balloonBtn.setVisibility(View.GONE);
                    sendBtn.setVisibility(View.VISIBLE);
//					sendBtn.setBackgroundColor(Color.parseColor(AppInfoConfig.getInstance().topViewDef.backgroundColor));
//					sendBtn.setTextColor(Color.WHITE);
                } else {
                    System.out.println("行数：" + replyEdit.getLineCount() + "长度：" + input.length());
                    if (replyEdit.getLineCount() >= 3 && input.length() > limitCountText - 100) {
                        mRL_balloonBtn.setVisibility(View.VISIBLE);
                        sendBtn.setVisibility(View.GONE);
                        reviewSendBtn(input);
                    } else {
                        mRL_balloonBtn.setVisibility(View.GONE);
                        sendBtn.setVisibility(View.VISIBLE);
//					sendBtn.setBackgroundResource(R.drawable.addrbook_btn_ok);
//					sendBtn.setTextColor(Color.WHITE);
                    }
                }
            }

            private void reviewSendBtn(String input) {
                // TODO Auto-generated method stub
                int count = (input.length() - limitCountText + 100) / 20;
                if (replyEdit.getText().length() > limitCountText) {
                    String realString = replyEdit.getText().toString().substring(0, limitCountText);
                    replyEdit.setText(realString);
                }
                mTV_number.setText((limitCountText - replyEdit.getText().length()) + "");
                switch (count) {
                    case 0:
                        mBT_balloonBtn.setBackgroundResource(R.drawable.workgrp_send_balloon_bg_01);
                        break;
                    case 1:
                        mBT_balloonBtn.setBackgroundResource(R.drawable.workgrp_send_balloon_bg_02);
                        break;
                    case 2:
                        mBT_balloonBtn.setBackgroundResource(R.drawable.workgrp_send_balloon_bg_03);
                        break;
                    case 3:
                        mBT_balloonBtn.setBackgroundResource(R.drawable.workgrp_send_balloon_bg_04);
                        break;
                    case 4:
                        mBT_balloonBtn.setBackgroundResource(R.drawable.workgrp_send_balloon_bg_05);
                        break;
                    case 5:
                        mBT_balloonBtn.setBackgroundResource(R.drawable.workgrp_send_balloon_bg_05);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean isNeedCloseMore() {
        return moreSelectLayout.isShown() || faceLayout.isShown();
    }

    public void closeMoreSelectLayout() {
        moreSelectLayout.setVisibility(View.GONE);
        faceLayout.setVisibility(View.GONE);
        pictureLayout.setVisibility(View.GONE);
//		if(moreSelectLayout.isShown()){
//			moreSelectLayout.setVisibility(View.GONE);
//		}
//		if(faceLayout.isShown()){
//			faceLayout.setVisibility(View.GONE);
//		}
//		if(pictureLayout.isShown()){
//			pictureLayout.setVisibility(View.GONE);
//		}
    }

    public void showPicture(String imagePath) {
        picture_path = imagePath;
        moreSelectLayout.setVisibility(View.GONE);
        faceLayout.setVisibility(View.GONE);
        pictureLayout.setVisibility(View.VISIBLE);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, opts);
        opts.inSampleSize = BBSUtil.computeSampleSize(opts, -1, 128 * 128);
        opts.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(imagePath, opts);

//		mBitmap = BitmapFactory.decodeFile(imagePath);
        if (mBitmap != null) {
            showPictureLayout.setVisibility(View.VISIBLE);
            showPicture.setImageBitmap(mBitmap);
        }
    }

    public String getPicture_path() {
        return picture_path;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    public String getInputResult() {
        return replyEdit.getText().toString().trim();
    }

    public void setSendButtonClick(OnClickListener onClickListener) {
        sendBtn.setOnClickListener(onClickListener);
        mBT_balloonBtn.setOnClickListener(onClickListener);
    }

    public void clearBIVStatus() {
        replyEdit.setText("");
        picture_path = null;
        closeMoreSelectLayout();
        BBSUtil.closeInputMethod(mContext, replyEdit);
        showPicture.setImageBitmap(null);
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    public EditText getInputEditText() {
        return replyEdit;
    }
}
