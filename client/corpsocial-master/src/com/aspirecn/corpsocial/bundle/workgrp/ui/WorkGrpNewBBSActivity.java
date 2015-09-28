package com.aspirecn.corpsocial.bundle.workgrp.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.im.domain.ChatEmoji;
import com.aspirecn.corpsocial.bundle.im.ui.ChatFaceRelativeLayout;
import com.aspirecn.corpsocial.bundle.im.ui.ChatFaceRelativeLayout.OnCorpusSelectedListener;
import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.bundle.workgrp.event.CreateOrModifyBBSEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.CreateOrModifyBBSRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.listener.CreateOrModifyBBSRespSubject.CreateOrModifyBBSRespListener;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSItemDao;
import com.aspirecn.corpsocial.bundle.workgrp.ui.widget.BBSUtil;
import com.aspirecn.corpsocial.common.config.Path;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.util.BitmapUtil;
import com.aspirecn.corpsocial.common.util.FaceConversionUtil;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.workgrp_new_bbs_activity)
public class WorkGrpNewBBSActivity extends EventFragmentActivity implements
        CreateOrModifyBBSRespListener, ActionBarFragment.LifeCycleListener {

    private static final int NEW_BBS_SUCCESS = 1;
    private static final int NEW_BBS_FAILURE = 2;
    @ViewById(R.id.actionbar_back_btn)
    TextView mBackBtn;
    @ViewById(R.id.workgrp_newbbs_title_etid)
    EditText titleEt;
    @ViewById(R.id.workgrp_new_bbs_title_lenglimit)
    TextView titleLimit;
    @ViewById(R.id.workgrp_newbbs_content_etid)
    EditText contentEt;
    @ViewById(R.id.workgrp_new_bbs_content_lenglimit)
    TextView contentLimit;
    @ViewById(R.id.workgrp_newbbs_selectbar_browid)
    ImageButton browBtn;
    @ViewById(R.id.workgrp_newbbs_moreid)
    RelativeLayout moreSelectLayout;
    @ViewById(R.id.newbbs_brow_viewid)
    ChatFaceRelativeLayout faceLayout;
    @ViewById(R.id.newbbs_picture_layoutid)
    LinearLayout pictureLayout;
    @ViewById(R.id.newbbs_picture_showlayoutid)
    RelativeLayout pictureShowlayout;
    @ViewById(R.id.newbbs_picture_resultid)
    ImageView resultImageView;
    @ViewById(R.id.newbbs_picture_addid)
    ImageView addImageView;
    @ViewById(R.id.newbbs_picture_deleteid)
    ImageView delPicture;
    @ViewById(R.id.workgrp_newbbs_selectbar_pictureid)
    ImageButton pictureBtn;
    private ProgressDialog mProgressDialog = null;

//	@Click({ R.id.actionbar_back_btn })
//	void doBackClick() {
//		WorkGrpNewBBSActivity.this.finish();
//	}

    //	@ViewById(R.id.actionbar_right_btn)
//	Button confirmBtn;
    private String groupid = null;
    private BBSItem item = null;
    private Bitmap mBitmap = null;
    private String picturePath = null;
    private String cameraPath = null;
    // private FileInfoDao fileDao = null;
    private BBSItemDao itemDao = null;
    private String itemId;
    private Handler newBBSHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            switch (msg.what) {
                case NEW_BBS_SUCCESS:
                    if (item != null) {
                        Toast.makeText(getApplicationContext(), "贴子修改成功！",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "贴子发表成功！",
                                Toast.LENGTH_SHORT).show();
                    }

                    Intent mIntent = new Intent();
                    // mIntent.putExtra("item", item);
                    mIntent.putExtra("groupid", groupid);
                    setResult(RESULT_OK, mIntent);
                    finish();
                    break;
                case NEW_BBS_FAILURE:
                    if (item != null) {
                        Toast.makeText(getApplicationContext(), "贴子更新，请检查网络并重试！",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "贴子发表失败，请检查网络并重试！",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Click({R.id.newbbs_picture_addid})
    void doAddPicture() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final String[] items = new String[]{"拍照", "相册"};
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("拍照".equals(items[which])) {
                    cameraPath = Path.getInstance().getPicturePath() + "bbs_"
                            + System.currentTimeMillis() + ".jpg";
                    BBSUtil.takePicture(WorkGrpNewBBSActivity.this, cameraPath);
                } else if ("相册".equals(items[which])) {
                    BBSUtil.pickPhoto(WorkGrpNewBBSActivity.this);
                }
            }
        });
        //	dialogBuilder.setTitle("操作");
        dialogBuilder.create().show();
    }

    @Click({R.id.newbbs_picture_resultid})
    void doGetPicture() {
    }

    @Click({R.id.newbbs_picture_deleteid})
    void doDelPicture() {
        pictureShowlayout.setVisibility(View.GONE);
        picturePath = null;
        recycleBitmap();
    }
   private void doConfirmClick() {
        String mTitle = titleEt.getText().toString().trim();
        if (mTitle.isEmpty()) {
            Toast.makeText(getApplicationContext(), "标题不能为空，请输入后重试！",
                    Toast.LENGTH_LONG).show();
        } else {
            doCreateOrModifyBBS();
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("正在发表贴子,请稍候...");
            mProgressDialog.show();
        }
    }

    @Click({R.id.workgrp_newbbs_selectbar_browid})
    void selectBrowClick() {

        if (faceLayout.isShown()) {
            faceLayout.setVisibility(View.GONE);
            BBSUtil.openInputMethod(WorkGrpNewBBSActivity.this, contentEt);
        } else {
            if (pictureLayout.isShown()) {
                pictureLayout.setVisibility(View.GONE);
            }
            if (BBSUtil.isInputMethodOpend(WorkGrpNewBBSActivity.this)) {
                BBSUtil.closeInputMethod(WorkGrpNewBBSActivity.this, contentEt);
                faceLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Click({R.id.workgrp_newbbs_selectbar_pictureid})
    void selectPictureClick() {
        if (pictureLayout.isShown()) {
            pictureLayout.setVisibility(View.GONE);
        } else {
            if (faceLayout.isShown()) {
                faceLayout.setVisibility(View.GONE);
            }
            pictureLayout.setVisibility(View.VISIBLE);
            if (BBSUtil.isInputMethodOpend(WorkGrpNewBBSActivity.this)) {
                BBSUtil.closeInputMethod(WorkGrpNewBBSActivity.this, contentEt);
            }
        }
    }

    @AfterViews
    void doAfterViews() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.workgrp_newbbs_backtitle)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(this);
        // fileDao = new FileInfoDao();
        itemDao = new BBSItemDao();
        itemId = getIntent().getStringExtra("itemid");
        groupid = getIntent().getStringExtra("groupid");
        if (!TextUtils.isEmpty(itemId)) {
            item = itemDao.findItemById(itemId);
            groupid = item.getBbsItemEntity().getGroupId();
            titleEt.setText(item.getBbsItemEntity().getTitle());
            contentEt.setText(FaceConversionUtil.getInstace()
                    .getExpressionString(this, item.getBbsItemEntity().getContent()));
            if (item.getFileInfoEntity() != null
                    && item.getFileInfoEntity().getUrl() != null) {
                String imageUrl = item.getFileInfoEntity().getUrl();
                if (imageUrl.startsWith(Environment
                        .getExternalStorageDirectory().getAbsolutePath())) {
                    resultImageView.setImageDrawable(BBSUtil
                            .getLocalDrawablePicture(imageUrl));
                    picturePath = imageUrl;
                } else {
//					ImageDownloadUtil.INSTANCE.showImage(item.getFileInfo()
//							.getUrl(), "bbs", resultImageView);
                    ImageLoader.getInstance().displayImage(item.getFileInfoEntity().getUrl(), resultImageView);
                    try {
                        picturePath = ImageLoader.getInstance().getDiskCache().get(item.getFileInfoEntity().getUrl()).getAbsolutePath();
                    } catch (Exception e) {
                        picturePath = imageUrl;
                        e.printStackTrace();
                    }
                    pictureShowlayout.setVisibility(View.VISIBLE);
//                    picturePath = ImageLoader.getInstance().getDiskCache().get(item.getFileInfo().getUrl()).getAbsolutePath();
//					String path = Constant.PICTURE_PATH + "bbs"
//							+ File.separator;
//					picturePath = path
//							+ MD5Util.getMD5String(item.getFileInfo().getUrl())
//							+ ".jpg";
                }
            }
//			confirmBtn.setText("提交");
        }
        titleLimit.setText("20");
        titleEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    moreSelectLayout.setVisibility(View.GONE);
                    // moreSelectLayout.setEnabled(false);
                    // browBtn.setClickable(false);
                    // voiceBtn.setClickable(false);
                    // pictureBtn.setClickable(false);
                    if (faceLayout.isShown()) {
                        faceLayout.setVisibility(View.GONE);
                    }
                    if (pictureLayout.isShown()) {
                        pictureLayout.setVisibility(View.GONE);
                    }
                } else {
                    moreSelectLayout.setVisibility(View.VISIBLE);
                    if (titleEt.getText().toString().isEmpty()) {
                        titleEt.setHint(R.string.workgrp_newbbs_title);
                    }
                }
            }
        });
        titleEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                titleLimit.setText((20 - titleEt.getText().toString().length()) + "");
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

        });
        contentLimit.setText("250");
        contentEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    moreSelectLayout.setEnabled(true);
                    browBtn.setClickable(true);
                    // voiceBtn.setClickable(true);
                    pictureBtn.setClickable(true);
                } else {
                    if (contentEt.getText().toString().isEmpty()) {
                        contentEt.setHint(R.string.workgrp_newbbs_content);
                    }
                }
            }
        });
        contentEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceLayout.isShown()) {
                    faceLayout.setVisibility(View.GONE);
                }
                if (pictureLayout.isShown()) {
                    pictureLayout.setVisibility(View.GONE);
                }
            }
        });
        contentEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                contentLimit.setText((250 - contentEt.getText().toString().length()) + "");
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });
        faceLayout.setOnCorpusSelectedListener(new OnCorpusSelectedListener() {
            @Override
            public void onCorpusSelected(ChatEmoji emoji) {
                if (TextUtils.isEmpty(emoji.getFaceName())
                        && emoji.getId() == R.drawable.im_chat_face_del_btn) {
                    int selection = contentEt.getSelectionStart();
                    String text = contentEt.getText().toString();
                    if (selection > 0) {
                        String text2 = text.substring(selection - 1);
                        if ("]".equals(text2)) {
                            int start = text.lastIndexOf("[");
                            int end = selection;
                            contentEt.getText().delete(start, end);
                            return;
                        }
                        contentEt.getText().delete(selection - 1, selection);
                    }
                } else {
                    if (emoji.getCharacter() == null) {
                        return;
                    }
                    SpannableString spannableString = FaceConversionUtil
                            .getInstace().addFace(getApplicationContext(),
                                    emoji.getPath(), emoji.getCharacter());
                    contentEt.append(spannableString);
                }
            }

            @Override
            public void onCorpusDeleted() {

            }
        });
    }

    @Background
    void doCreateOrModifyBBS() {
        CreateOrModifyBBSEvent createOrModifyBBSEvent = new CreateOrModifyBBSEvent();
        String content = contentEt.getText().toString().trim();
        if (null == content || content.isEmpty()) {
            content = "如题！";
        }
        createOrModifyBBSEvent.setItemId(itemId);
        createOrModifyBBSEvent.setContent(content);
        createOrModifyBBSEvent.setTitle(titleEt.getText().toString().trim());
        createOrModifyBBSEvent.setGroupId(groupid);
        if (picturePath != null) {
            createOrModifyBBSEvent.setHasPic(true);
            createOrModifyBBSEvent.setPath(picturePath);
        }
        uiEventHandleFacade.handle(createOrModifyBBSEvent);
        // item = new BBSItem();
        // item.setBbsReplyInfoList(new ArrayList<BBSReplyInfo>());
        // item.setContent(content);
        // item.setCreateTime(System.currentTimeMillis());
        // item.setCreatorId(Config.getInstance().getUserId());
        // item.setCreatorName(Config.getInstance().getNickName());
        // item.setGroupId(groupid);
        // item.setPraiseTimes("0");
        // item.setReplyTimes("0");
        // item.setPraiseUseridList(new ArrayList<String>());
        // item.setTitle(titleEt.getText().toString());
    }

    @Override
    public void onHandleCreateOrModifyBBSRespEvent(
            CreateOrModifyBBSRespEvent createOrModifyBBSRespEvent) {
        Message msg = newBBSHandler.obtainMessage();
        if (ErrorCode.SUCCESS.getValue() == createOrModifyBBSRespEvent
                .getErrorCode()) {
            msg.what = NEW_BBS_SUCCESS;
            msg.obj = createOrModifyBBSRespEvent.getItemId();
        } else {
            msg.what = NEW_BBS_FAILURE;
        }
        msg.sendToTarget();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BBSUtil.BBS_REQUEST_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                picturePath = cursor.getString(column_index);
                recycleBitmap();

                BitmapUtil.correction(picturePath);
                mBitmap = BBSUtil.getLocalBitmapPicture(picturePath);

                // mBitmap = BitmapFactory.decodeFile(picturePath);
                resultImageView.setImageBitmap(mBitmap);
                pictureShowlayout.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == BBSUtil.BBS_REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                picturePath = cameraPath;
                recycleBitmap();

                BitmapUtil.correction(picturePath);

                mBitmap = BBSUtil.getLocalBitmapPicture(picturePath);
                resultImageView.setImageBitmap(mBitmap);

                // BitmapFactory.Options opts = new BitmapFactory.Options();
                // opts.inJustDecodeBounds = true;
                // BitmapFactory.decodeFile(picturePath, opts);
                // opts.inSampleSize = BBSUtil.computeSampleSize(opts, -1,
                // 128*128);
                // opts.inJustDecodeBounds = false;
                // mBitmap = BitmapFactory.decodeFile(picturePath,opts);
                // resultImageView.setImageBitmap(mBitmap);

                // mBitmap = BitmapFactory.decodeFile(picturePath);
                // resultImageView.setImageBitmap(mBitmap);
                pictureShowlayout.setVisibility(View.VISIBLE);
            } else {

            }
            cameraPath = null;
        }
    }

    private void recycleBitmap() {
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        if (!TextUtils.isEmpty(itemId)) {
            fab.build().setFirstButtonText(R.string.ticket_commit).apply();
        } else {
            fab.build().setFirstButtonText(R.string.workgrp_newbbs_publish).apply();
        }
        fab.build().setFirstButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doConfirmClick();
            }
        }).apply();
    }

}
