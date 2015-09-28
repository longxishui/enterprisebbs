package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.domain.ChatEmoji;
import com.aspirecn.corpsocial.bundle.im.notify.MsgNotificationControllers;
import com.aspirecn.corpsocial.bundle.im.presenter.ChatPresenter;
import com.aspirecn.corpsocial.bundle.im.presenter.impl.ChatPresenterImpl;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.bundle.im.ui.ChatFaceRelativeLayout.OnCorpusSelectedListener;
import com.aspirecn.corpsocial.bundle.im.view.ChatView;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.ui.chatitemview.MsgItem;
import com.aspirecn.corpsocial.common.ui.component.SpeechInput.SpeechInputManager;
import com.aspirecn.corpsocial.common.ui.widget.CustomDialog;
import com.aspirecn.corpsocial.common.util.FaceConversionUtil;
import com.aspirecn.corpsocial.common.util.MediaRecordAndPlayer;
import com.aspirecn.corpsocial.common.util.TimeUtil;
import com.aspiren.corpsocial.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 聊天界面
 *
 * @author lihaiqiang
 */
@EActivity(R.layout.im_chat_activity)
public class ChatActivity extends EventFragmentActivity implements
        MsgItem.OnClickSendAgainListener,
        MsgItem.OnLongClickHeadImgListener,
        ActionBarFragment.LifeCycleListener, ChatView {

    /**
     * 最长录音时长
     */
    private static final int MAX_TIME = 30;
    /**
     * 下拉刷新加载的消息数
     */
    private static final int PAGE_SIZE = 5;
    @ViewById(R.id.im_chat_item)
    PullToRefreshListView mPullToRefreshListView;
    /**
     * 无消息提示
     */
    @ViewById(R.id.im_chat_no_chat_msg_tips)
    TextView mNoMsgTips;
    @ViewById(R.id.im_chat_face)
    ChatFaceRelativeLayout mChatFaceRelativeLayout;
    /**
     * 发送消息模块布局
     */
    @ViewById(R.id.im_chat_send_msg_layout)
    View mSendMsgLayout;
    /**
     * 转换语音或文本输入按钮
     */
    @ViewById(R.id.im_chat_voice_keybord_btn)
    ImageView mVoiceKeybordBtn;
    /**
     * 文本输入框外层布局
     */
    @ViewById(R.id.im_chat_input_text_rl)
    View mInputTextRL;
    /**
     * 文本输入框
     */
    @ViewById(R.id.im_chat_input_edit)
    EditText mInputEdit;
    /**
     * 表情图标按钮
     */
    @ViewById(R.id.im_chat_brow_icon)
    ImageView mBrowIcon;
    /**
     * 表情选择框布局
     */
    @ViewById(R.id.im_chat_face)
    View mFaceView;
    /**
     * 按下说话按钮
     */
    @ViewById(R.id.im_chat_send_voice_btn)
    Button mSendVoiceBtn;
    /**
     * 更多选项按钮
     */
    @ViewById(R.id.im_chat_more_btn)
    View mMoreBtn;
    /**
     * 更多选项内容布局
     */
    @ViewById(R.id.im_chat_more_select_ll)
    View mMoreSelectLL;
    /**
     * 发送消息按钮
     */
    @ViewById(R.id.im_chat_send_message_btn)
    Button mSendMessageBtn;
    /**
     * 语音输入按钮
     */
    @ViewById(R.id.im_chat_voice_input_btn)
    TextView mVoiceInputBtn;
    @ViewById(R.id.im_chat_speak_power_tips_bt)
    Button mAskForSpeakBtn;
    private int mBtnStatus;
    /**
     * 录音时长
     */
    private int time;
    private boolean isStarting = false;
    private boolean isStopping = false;
    /**
     * 表情数目 不能超过100个
     */
    private int faceCount = 0;
    private RecordVoiceDialog mRecordVoiceDialog;
    private ChatAdapter mChatItemAdapter;
    /**
     * 语音输入
     */
    private SpeechInputManager speechInputManager;
    private ChatPresenter mPresenter = new ChatPresenterImpl(this);

    @Override
    protected void onRestart() {
        super.onRestart();
        mPresenter.onRestart();
    }

    @AfterViews
    void doAfterViews() {
        EventListenerSubjectLoader.getInstance().registerListener(mPresenter);

        String corpId = getIntent().getStringExtra("corpId");
        String chatId = getIntent().getStringExtra("chatId");
        String chatName = getIntent().getStringExtra("chatName");
        int chatType = getIntent().getIntExtra("chatType", 0);

        ActionBarFragment fab = ActionBarFragment_.builder().title(chatName).build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(this);

        mPresenter.initData(corpId, chatId, chatName, chatType);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mRecordVoiceDialog = new RecordVoiceDialog(this);

        faceCallBack();

        mInputEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isKeyboardOpened = imm.isActive();
                if (v.hasFocus() && isKeyboardOpened) {
                    mBrowIcon
                            .setImageResource(R.drawable.im_chat_brow_bg_selector);
                    mFaceView.setVisibility(View.GONE);
                    mMoreSelectLL.setVisibility(View.GONE);
                }
            }
        });

        speechInputManager = SpeechInputManager.getInstance();
        speechInputManager.init(AspirecnCorpSocial.getContext());

        mSendVoiceBtn.setTextColor(mPresenter.getThemeColor());
        mSendMessageBtn.setBackgroundColor(mPresenter.getThemeColor());

        doLoadChat();
    }

    /**
     * 加载聊天记录
     */
    @Background
    void doLoadChat() {
        mPresenter.doLoadChat();
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        initView();

        fab.build().setFirstButtonIcon(R.drawable.actionbar_group_set_btn_bg_selector).apply();
        fab.build().setFirstButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onClickActionbarRightBtn();
            }
        }).apply();
    }

    public void startP2PChatSettingActivity(String mChatId, String mCorpId, String mChatName) {
        Intent intent = new Intent();
        intent.setClass(this, P2PChatSettingActivity_.class);
        intent.putExtra("corpId", mCorpId);
        intent.putExtra("chatId", mChatId);
        intent.putExtra("chatName", mChatName);
        startActivityForResult(intent, Action.CHAT_SETTING);
    }

    public void startGroupChatSettingActivity(String mChatId, String mCorpId, String mChatName) {
        Intent intent = new Intent();
        // 群聊，会话ID即为群ID
        intent.setClass(this, GroupChatSettingActivity_.class);
        intent.putExtra("corpId", mCorpId);
        intent.putExtra("chatId", mChatId);
        intent.putExtra("chatName", mChatName);
        startActivityForResult(intent, Action.CHAT_SETTING);
    }

    /**
     * 发言权限设置
     */
    @UiThread
    public void setSpeakPower(Chat chat, boolean isSpeak) {
        mVoiceKeybordBtn.setClickable(isSpeak);
        mInputEdit.setClickable(isSpeak);
        // mInputEdit.setFocusable(isSpeak);
        // mInputEdit.setFocusableInTouchMode(isSpeak);
        if (isSpeak) {
            mInputEdit.setText("");
            mInputEdit.setTextColor(0xff000000);
            // mInputEdit.requestFocus();
            mBrowIcon.setVisibility(View.VISIBLE);
            mAskForSpeakBtn.setVisibility(View.GONE);
        } else {
            if (chat.getApplySpeekAdmin() != null) {
                String adminName = chat.getApplySpeekAdmin().getMemberName();
                String adminId = chat.getApplySpeekAdmin().getMemberId();
                String tips = "您可向群管理员<font color=\"#ffed8223\">" + adminName
                        + "</font>申请发言权限";
                mInputEdit.setText(Html.fromHtml(tips));
                mAskForSpeakBtn.setTag(adminId);
            } else {
                mInputEdit.setText("您可向群管理员申请发言权限");
                mAskForSpeakBtn.setTag("");
            }
            mAskForSpeakBtn.setVisibility(View.VISIBLE);
            mInputEdit.setTextColor(0xff999999);
            mBrowIcon.setVisibility(View.GONE);
            mSendMessageBtn.setBackgroundColor(0xffe1e1e1);
        }
        mMoreBtn.setClickable(isSpeak);
        mSendMessageBtn.setClickable(isSpeak);
        mSendMessageBtn.setSelected(!isSpeak);
        mSendMsgLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 点击跳转管理员详情页（申请发言权限）
     */
    @Click(R.id.im_chat_speak_power_tips_bt)
    void onClickAskForSpeak(View view) {
        String id = (String) view.getTag();
        if (TextUtils.isEmpty(id)) {// 没有获取管理员id时返回
            return;
        }
        Intent intent = new Intent(this,
                com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookPersonalParticularsActivity_.class);
        intent.putExtra("ContactId", id);
        startActivity(intent);
    }

    /**
     * 表情控件监听回调方法
     */
    private void faceCallBack() {
        OnCorpusSelectedListener mListener = new OnCorpusSelectedListener() {

            @Override
            public void onCorpusSelected(ChatEmoji emoji) {
                if (TextUtils.isEmpty(emoji.getFaceName())
                        && emoji.getId() == R.drawable.im_chat_face_del_btn) {
                    int selection = mInputEdit.getSelectionStart();
                    String text = mInputEdit.getText().toString();
                    if (selection > 0) {
                        String text2 = text.substring(selection - 1);
                        if ("]".equals(text2)) {
                            int start = text.lastIndexOf("[");
                            mInputEdit.getText().delete(start, selection);
                            if (faceCount > 0) {
                                faceCount--;
                            } else {
                                faceCount = 0;
                            }
                            return;
                        }
                        mInputEdit.getText().delete(selection - 1, selection);
                    }
                } else {
                    if (emoji.getCharacter() == null) {
                        return;
                    }
                    if (faceCount <= 100) {
                        SpannableString spannableString = FaceConversionUtil
                                .getInstace().addFace(getApplicationContext(),
                                        emoji.getPath(), emoji.getCharacter());
                        mInputEdit.append(spannableString);
                        faceCount++;
                    }
                }
            }

            @Override
            public void onCorpusDeleted() {
            }
        };
        mChatFaceRelativeLayout.setOnCorpusSelectedListener(mListener);
    }

    /**
     * 输入框焦点改变监听方法
     */
    @FocusChange(R.id.im_chat_input_edit)
    void onFocusChange(boolean isFocus) {
        if (isFocus) {
            mFaceView.setVisibility(View.GONE);
            mMoreSelectLL.setVisibility(View.GONE);
        }
    }

    /**
     * 输入框文本内容变化监听方法
     */
    @TextChange(R.id.im_chat_input_edit)
    void onTextChanged(CharSequence text) {
        if (text.toString().length() > 0) {
            mSendMessageBtn.setVisibility(View.VISIBLE);
        } else {
            mSendMessageBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 显示软键盘
     */
    private void showInputMethod() {
        mMoreSelectLL.setVisibility(View.GONE);
        mFaceView.setVisibility(View.GONE);
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(mInputEdit, 1);
    }

    /**
     * 隐藏软键盘
     */
    private void hideInputMethod() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(mInputEdit.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        mBrowIcon.setImageResource(R.drawable.im_chat_brow_bg_selector);
    }

    /**
     * 点击语音输入或文本输入转换按钮方法
     */
    @Click(R.id.im_chat_voice_keybord_btn)
    void onClickSendMessageLeftBtn() {
        if (mInputTextRL.isShown()) {
            hideInputMethod();
            mInputTextRL.setVisibility(View.GONE);
            mSendVoiceBtn.setVisibility(View.VISIBLE);
            mVoiceKeybordBtn.setImageResource(R.drawable.im_chat_keybord_icon);
            mMoreSelectLL.setVisibility(View.GONE);
            mFaceView.setVisibility(View.GONE);
            mInputEdit.setText("");
        } else {
            mInputTextRL.setVisibility(View.VISIBLE);
            mSendVoiceBtn.setVisibility(View.GONE);
            mVoiceKeybordBtn.setImageResource(R.drawable.im_chat_voice_icon);
        }
    }

    /**
     * 点击表情按钮方法
     */
    @Click(R.id.im_chat_brow_icon)
    void onClickSendMessageBrowBtn() {
        if (mFaceView.isShown()) {
            hideMessageBrow();
        } else {
            showMessageBrow();
        }
    }

    @UiThread
    void showMessageBrow() {
        hideInputMethod();
        mBrowIcon.setImageResource(R.drawable.im_chat_keybord_icon);
        mMoreSelectLL.setVisibility(View.GONE);
        mFaceView.setVisibility(View.VISIBLE);
    }

    @UiThread
    void hideMessageBrow() {
        mBrowIcon.setImageResource(R.drawable.im_chat_brow_bg_selector);
        mFaceView.setVisibility(View.GONE);
        showInputMethod();
    }

    /**
     * 点击更多选项按钮
     */
    @Click(R.id.im_chat_more_btn)
    void onClickSendMessageRightBtn() {
        if (mMoreSelectLL.isShown()) {
            mMoreSelectLL.setVisibility(View.GONE);
        } else {
            hideInputMethod();
            mMoreSelectLL.setVisibility(View.VISIBLE);
            mInputTextRL.setVisibility(View.VISIBLE);
            mSendVoiceBtn.setVisibility(View.GONE);
            mVoiceKeybordBtn.setImageResource(R.drawable.im_chat_voice_icon);
            mFaceView.setVisibility(View.GONE);
        }
    }

    /**
     * 发送消息按钮方法
     */
    @Click(R.id.im_chat_send_message_btn)
    void onClickSendMessageBtn() {
        // 发送消息到聊天窗口
        sendTextMsg(mInputEdit.getText().toString());
        // 清空输入框
        mInputEdit.setText("");
        faceCount = 0;
    }


    /**
     * 选择图片按钮方法
     */
    @Click(R.id.im_chat_picture_select)
    void onClickPictureSelectBtn() {
        choosePicture();
    }

    /**
     * 选择拍照按钮方法
     */
    @Click(R.id.im_chat_photograph_select)
    void onClickPhotographSelectBtn() {
        mPresenter.takeAPhoto();
    }

    /**
     * 位置
     */
    @Click(R.id.im_chat_location)
    void onClickLocationSelectBtn() {
        mPresenter.onClickLocationSelectBtn();

    }

    @Override
    public void startLocationActivity(String path) {
//        Intent mIntent = new Intent(this, LocationActivity.class);
//        mIntent.putExtra("path", path);
//        startActivityForResult(mIntent, Action.MAP_LOCATION);
    }

    /**
     * 语音输入
     */
    @Click(R.id.im_chat_voice_input_btn)
    void onClickVoiceInputBtn() {
        speechInputManager.startSpeech(mInputEdit);
    }

    /**
     * 删除聊天消息
     */
    @Background
    public void doDeleteMessage(String msgId) {
        mPresenter.doDeleteMessage(msgId);
    }

    /**
     * 按住说话按钮触碰事件方法
     */
    @Touch(R.id.im_chat_send_voice_btn)
    void onTouchVoiceBtn(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 按下说话
                mBtnStatus = MotionEvent.ACTION_DOWN;
                mPresenter.startRecordVoice();
                break;
            case MotionEvent.ACTION_UP:// 放开结束
                mBtnStatus = MotionEvent.ACTION_UP;
                mPresenter.stopRecordVoice();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:// 其他情况结束
                mBtnStatus = MotionEvent.ACTION_UP;
                mPresenter.stopRecordVoice();
                break;
        }
    }

    /**
     * 启动录音
     */
    @Override
    public void startRecordVoice(final int mThemeColor, final String voiceFilePath) {
        new Thread() {
            public void run() {
                for (int i = 0; i < 100; i++) {// 防止连续点击
                    if (mBtnStatus == MotionEvent.ACTION_UP || isStarting) {
                        return;
                    }
                    SystemClock.sleep(4);
                }
                changeRecordBtnBg(true, mThemeColor);
                initVoiceAnimation();
                isStarting = true;
                // 启动录音
                if (MediaRecordAndPlayer.getInstance().startRecord(voiceFilePath)) {
                    // 开始计时
                    time();
                    // 启动录音成功，震动提示
                    vibrate();
                    // 显示音量dialog
                    showVolumeDialog();
                } else {
                    isStarting = false;
                    showToast("录音出错，请重新开始！");
                }
            }

        }.start();
    }

    /**
     * 重置播放语音动画
     */
    @UiThread
    void initVoiceAnimation() {
        MediaRecordAndPlayer.getInstance().initVoicePicture();
    }

    /**
     * 停止录音
     */
    @Override
    public void stopRecordVoice(final int mThemeColor) {
        new Thread() {
            public void run() {
                SystemClock.sleep(50);
                changeRecordBtnBg(false, mThemeColor);
                if (isStarting && !isStopping) {// 防止多次调用
                    isStopping = true;
                    MediaRecordAndPlayer.getInstance().stopRecord();
                    isStopping = false;
                    isStarting = false;

                    if (time < 1) {
                        showToast("按键时间太短！");
                        return;
                    }
                    // 发送语音信息
                    sendVoiceMsg();
                }
            }

        }.start();
    }

    /**
     * 改变语音录制按钮背景
     */
    @UiThread
    void changeRecordBtnBg(boolean isPressed, int mThemeColor) {
        if (isPressed) {
            mSendVoiceBtn.setBackgroundColor(mThemeColor);
            mSendVoiceBtn.setTextColor(getResources().getColor(R.color.level_3));
        } else {
            mSendVoiceBtn.setBackgroundResource(R.drawable.common_input_border);
            mSendVoiceBtn.setTextColor(mThemeColor);
            mSendVoiceBtn.setText("按住说话");
            closeVolumeDialog();
        }
    }

    /**
     * 震动
     */
    private void vibrate() {
        long[] pattern = {0, 150}; // 停止,开启 ...
        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(
                pattern, -1);
    }

    /**
     * 录音计时
     */
    private void time() {
        new Thread() {
            public void run() {
                for (int i = 0; i <= MAX_TIME; i++) {
                    time = i;
                    changeSendVoiceBtnTextShow(time + "''");
                    for (int j = 0; j < 100; j++) {// 循环结束为1秒
                        if (isStopping || !isStarting) {
                            changeSendVoiceBtnTextShow("按住说话");
                            return;
                        }
                        if (j % 20 == 0) {// 音量
                            changeVolume(MediaRecordAndPlayer.getInstance().getVolume());
                        }
                        SystemClock.sleep(10);
                    }
                }
                // 录音超时处理
                showToast("录音时间到达" + time + "''上限。");
                changeSendVoiceBtnTextShow("松手发送");
                MediaRecordAndPlayer.getInstance().stopRecord();
            }

        }.start();
    }

    /**
     * 显示音量dialog
     */
    @UiThread
    void showVolumeDialog() {
        mRecordVoiceDialog.show();
    }

    /**
     * 关闭音量dialog
     */
    @UiThread
    void closeVolumeDialog() {
        mRecordVoiceDialog.dismiss();
    }

    /**
     * 改变音量
     */
    @UiThread
    void changeVolume(int volume) {
        mRecordVoiceDialog.setVolume(volume);
    }

    /**
     * 界面显示计时效果
     */
    @UiThread
    void changeSendVoiceBtnTextShow(String text) {
        mSendVoiceBtn.setText(text);
    }

    /**
     * Toast提示
     */
    @UiThread
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @UiThread
    @Override
    public void closeCustomDialog() {
        CustomDialog.closeProgress(this);
    }

    /**
     * 选取本地图片
     */
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, Action.CHOOSE_PICTURE);
    }

    /**
     * 拍照
     */
    public void takeAPhoto(String pictureFilePath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(pictureFilePath)));
        startActivityForResult(intent, Action.TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Action.RECORDER_VIDEO: {
                if (resultCode != RESULT_OK) {
                    return;
                }
                String videoFilePath = data.getStringExtra("path");
                sendVideoMsg(videoFilePath);
                break;
            }
            case Action.TAKE_PHOTO: {
                if (resultCode != RESULT_OK) {
                    return;
                }
                sendPictureMsg();
                break;
            }
            case Action.CHOOSE_PICTURE: {
                if (resultCode != RESULT_OK) {
                    return;
                }
                // 获取图片文件地址
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                @SuppressWarnings("deprecation")
                Cursor cursor = this.managedQuery(uri, projection, null, null, null);
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String imgPath = cursor.getString(columnIndex);
                sendPictureMsg(imgPath);
                break;
            }
            case Action.SHOW_NATIVE_PICTURE: {
                // 刷新数据
                mPresenter.refreshMsgList(false);
                break;
            }
            case Action.CHAT_SETTING: {
                mPresenter.refreshMsgList(false);
                break;
            }
            case Action.MAP_LOCATION:
                if (resultCode != RESULT_OK) {
                    return;
                }
                String pictureFilePath = data.getStringExtra("path");
                double lat = data.getDoubleExtra("latitude", 0.0);
                double lon = data.getDoubleExtra("longitude", 0.0);
                if (lat != 0.0 && lon != 0.0) {
                    sendLocationMsg(pictureFilePath, String.valueOf(lat), String.valueOf(lon));
                } else {
                    showToast("位置信息获取失败，请重新获取！");
                }
                break;
            default:
                showToast("处理其他结果");
                break;
        }
    }

    /**
     * 初始化列表控件
     */
    @UiThread
    public void initPullToRefreshListView(final Chat chat) {
        mPullToRefreshListView.getRefreshableView().setDividerHeight(0);

        mChatItemAdapter = new ChatAdapter(this, chat);
        mChatItemAdapter.setOnClickSendAgainListener(this);
        mChatItemAdapter.setOnLongClickHeadImgListener(this);

        mPullToRefreshListView.setAdapter(mChatItemAdapter);

        int count = mChatItemAdapter.getCount();
        mPullToRefreshListView.getRefreshableView().setSelection(count);
        if (count > 0) {
            mPullToRefreshListView.setVisibility(View.VISIBLE);
        } else {
            mNoMsgTips.setVisibility(View.VISIBLE);
        }

        mPullToRefreshListView.getLoadingLayoutProxy().setPullLabel("下拉加载更多");
        mPullToRefreshListView.getLoadingLayoutProxy().setReleaseLabel(
                "释放立即加载更多");
        mPullToRefreshListView.getLoadingLayoutProxy().setRefreshingLabel(
                "正在加载...");
        mPullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(PullToRefreshBase<ListView> arg0) {
                        new AsyncTask<Void, Void, List<MsgEntity>>() {
                            @Override
                            protected List<MsgEntity> doInBackground(
                                    Void... params) {
                                SystemClock.sleep(1000);
                                // 下拉刷新会话列表
                                int index = chat.getMsgList().size();
                                return chat.doLoadMoreMsg(index, PAGE_SIZE);
                            }

                            @Override
                            protected void onPostExecute(List<MsgEntity> msgs) {
                                super.onPostExecute(msgs);

                                if (msgs.isEmpty()) {
                                    showToast("已加载完毕");
                                }
                                LinkedList<MsgEntity> msgList = chat
                                        .getMsgList();
                                for (MsgEntity msgEntity : msgs) {
                                    msgList.addFirst(msgEntity);
                                }
                                chat.setMsgList(msgList);
                                notifyDataChanged(chat, false);
                                ConfigPersonal.getInstance()
                                        .setLastUpdateChatList(
                                                System.currentTimeMillis());
                                mPullToRefreshListView.onRefreshComplete();
                            }
                        }.execute();

                    }
                });

        mPullToRefreshListView
                .setOnPullEventListener(new OnPullEventListener<ListView>() {

                    @Override
                    public void onPullEvent(PullToRefreshBase<ListView> arg0,
                                            State arg1, Mode arg2) {
                        long lastUpdateTime = ConfigPersonal.getInstance()
                                .getLastUpdateChatList();
                        String lastUpdateLabel = TimeUtil
                                .convert(lastUpdateTime);
                        arg0.getLoadingLayoutProxy().setLastUpdatedLabel(
                                lastUpdateLabel);
                    }
                });

        mPullToRefreshListView
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                    }
                });

        mPullToRefreshListView.getRefreshableView().setOnTouchListener(
                new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // 点击列表空白处收起软盘、表情等
                        hideInputMethod();
                        mMoreSelectLL.setVisibility(View.GONE);
                        mFaceView.setVisibility(View.GONE);
                        return false;
                    }
                });
    }

    @UiThread
    public void notifyDataChanged(Chat chat, boolean showLastItem) {
        if (mChatItemAdapter != null) {
            if (chat.getMsgList().size() > 0) {
                mPullToRefreshListView.setVisibility(View.VISIBLE);
                mNoMsgTips.setVisibility(View.GONE);
            } else {
                mPullToRefreshListView.setVisibility(View.GONE);
                mNoMsgTips.setVisibility(View.VISIBLE);
            }
            mChatItemAdapter.notifyDataSetChanged();
            if (showLastItem) {
                mPullToRefreshListView.getRefreshableView().setSelection(
                        mChatItemAdapter.getCount());
            }
        }
    }

    @UiThread
    void sendMessageDataChanged(Chat chat, MsgEntity msgEntity, boolean showLastItem) {
        chat.getMsgList().add(msgEntity);

        notifyDataChanged(chat, showLastItem);
    }

    /**
     * 发送文本消息
     */
    @Background
    void sendTextMsg(String textMst) {
        mPresenter.sendTextMsg(textMst);
    }

    /**
     * 发送图片信息
     */
    @Background
    void sendPictureMsg() {
        mPresenter.sendPictureMsg();
    }

    @Background
    void sendPictureMsg(String picturePath) {
        mPresenter.sendPictureMsg(picturePath);
    }

    /**
     * 发送位置信息
     */
    @Background
    void sendLocationMsg(String imagePath, String latitude, String longitude) {
        mPresenter.sendLocationMsg(imagePath, latitude, longitude);
    }

    @Background
    void sendVideoMsg(String videoFilePath) {
        mPresenter.sendVideoMsg(videoFilePath);
    }

    /**
     * 发送语音信息
     */
    @Background
    void sendVoiceMsg() {
        mPresenter.sendVoiceMsg(time);
    }

    /**
     * 重新发送消息
     */
    @Background
    @Override
    public void onClickSendAgain(int position) {
        mPresenter.onClickSendAgain(position);
    }

    /**
     * 长按头像触发方法
     */
    @Override
    public void onLongClick(MsgEntity msg) {
        showInputMethod();
        String title = mInputEdit.getText().toString() + "@" + msg.getOwnedUserName() + " ";
        mInputEdit.setText(title);
        mInputEdit.setSelection(title.length());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideInputMethod();
        mMoreSelectLL.setVisibility(View.GONE);
        mFaceView.setVisibility(View.GONE);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        EventListenerSubjectLoader.getInstance().unregisterListener(mPresenter);
        super.onDestroy();
        MediaRecordAndPlayer.getInstance().stopPlay();
    }

    @Override
    public void doFinish() {
        Intent intent = new Intent(this, com.aspirecn.corpsocial.bundle.common.ui.MainTabActivity_.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        MsgNotificationControllers.getInstance().clearAllNotify();
        super.onResume();
    }

    /**
     * 捕获回退按钮事件，进行自定义
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mFaceView.isShown()) {
            mFaceView.setVisibility(View.GONE);
            mBrowIcon.setImageResource(R.drawable.im_chat_brow_bg_selector);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 动作
     *
     * @author lizhuo_a
     */
    public final class Action {
        public static final int TAKE_PHOTO = 0;
        public static final int CHOOSE_PICTURE = 1;
        public static final int SHOW_NATIVE_PICTURE = 2;
        public static final int CHAT_SETTING = 3;
        public static final int MAP_LOCATION = 4;
        public static final int RECORDER_VIDEO = 5;
    }


}
