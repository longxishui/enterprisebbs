package com.aspirecn.corpsocial.bundle.im.notify;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactDetailEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactVO;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.NotifyConfig;
import com.aspirecn.corpsocial.common.config.NotifyConfig.NofityArea;
import com.aspirecn.corpsocial.common.config.NotifyConfig.NofityModule;
import com.aspirecn.corpsocial.common.config.NotifyConfig.NotifySwitch;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.DateUtils;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.aspiren.corpsocial.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 群组聊天消息提醒
 *
 * @author liangjian
 */
public class MsgNotificationControllers {

    private static MsgNotificationControllers instance;
    /**
     * Notification管理
     */
    public NotificationManager mNotificationManager;
    /**
     * Notification的ID
     */
    int notifyId = Config.getInstance().getChatNotifyId();
    /**
     * Notification构造器
     */
    NotificationCompat.Builder mBuilder;
    private ChatDao chatDao = new ChatDao();
    private Context mContext;
    private ActivityManager mActivityManager;
    private ComponentName mComponentName;
    private String packageName;

    private int newsCount = 1;
    private int currentTime = 2;
    private Notification mNotification;
    private Thread mNotifyThread;

    private MsgNotificationControllers() {
        this.mContext = AspirecnCorpSocial.getContext();
        mNotificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static MsgNotificationControllers getInstance() {
        if (null == instance) {
            instance = new MsgNotificationControllers();
        }
        return instance;
    }

    /**
     * 显示自定义通知栏
     */
    public void showNotify(boolean groupMsg, MsgEntity msgEntity) {
        NotifyConfig instance2 = NotifyConfig.getInstance();
        if (groupMsg) {
            Map<String, String> groupMsgConfig = instance2
                    .getNotifyConfig(NofityModule.NOTIFY_MSG_GROUP);
            if (groupMsgConfig == null) {
                return;
            }
            String notifySwitch = groupMsgConfig
                    .get(NofityArea.APP_AREA_NOTIFY.value);
            if (TextUtils.isEmpty(notifySwitch)) {
                return;
            }
            if (NotifySwitch.OFF.value.equals(notifySwitch)) {
                return;
            }
        } else {
            Map<String, String> p2pMsgDialog = instance2
                    .getNotifyConfig(NofityModule.NOTIFY_MSG_DIALOG);
            if (p2pMsgDialog == null) {
                return;
            }
            String notifySwitch = p2pMsgDialog
                    .get(NofityArea.APP_AREA_NOTIFY.value);
            if (TextUtils.isEmpty(notifySwitch)) {
                return;
            }
            if (NotifySwitch.OFF.value.equals(notifySwitch)) {
                return;
            }
        }
        // 判断是否开启消息通知
        if (!isNewMsgNotify(msgEntity)) {
            if (Config.getInstance().getAtMsg()) {
                Config.getInstance().setAtMsg(false);
            } else {
                return;
            }
        }

        // 判断软件是否处于可见状态
        mActivityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        mComponentName = mActivityManager.getRunningTasks(1).get(0).topActivity;
        packageName = mComponentName.getPackageName();
        if (packageName != null && packageName.startsWith("com.aspiren.corpsocial")) {
            return;
        }

        // 设定RemoteViews
        RemoteViews view_custom = new RemoteViews(mContext.getPackageName(),
                R.layout.im_chat_view_notify_item);
        // 设置对应IMAGEVIEW的ID的资源图片
        setImageView(msgEntity, view_custom);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setContentIntent(
                getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
                .setWhen(System.currentTimeMillis())
                        // 通知产生的时间，会在通知信息里显示
                .setOngoing(true)
                        // ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setSmallIcon(R.drawable.chat_icon)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                        // 设置闪烁提示
                        // .setDefaults(Notification.DEFAULT_ALL)
                .setContent(view_custom)
                .setAutoCancel(true)
                        // 点击后让通知将消失
                .setContentIntent(
                        getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("有新消息").setOngoing(false)// 不是正在进行的 true为正在进行
                .setSmallIcon(R.drawable.app_icon);

        // 提醒方式：震动、声音。。。
        if (Config.getInstance().isNotifyVibrate()
                && Config.getInstance().isNotifySound()) {
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
        } else if (Config.getInstance().isNotifyVibrate()) {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        } else if (Config.getInstance().isNotifySound()) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }

        Notification notify = mBuilder.build();
        notify.contentView = view_custom;

        Intent resultIntent = getIntent(msgEntity);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                notifyId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        //mNotificationManager.notify(notifyId, mBuilder.build());

        mNotification = mBuilder.build();
        currentTime = 2;
        doNotify();
    }

    private void doNotify() {
        if (mNotifyThread == null) {
            mNotifyThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (currentTime > 0 && newsCount > 2) {
                        SystemClock.sleep(1000);
                        currentTime -= 1;
                    }
                    mNotificationManager.notify(notifyId, mNotification);
                    mNotifyThread = null;
                }
            });

            mNotifyThread.start();
        }
    }

    private Intent getIntent(MsgEntity msgEntity) {
        Intent resultIntent;
        if (newsCount == 2) {
            resultIntent = new Intent(mContext, com.aspirecn.corpsocial.bundle.im.ui.ChatActivity_.class);
            ChatEntity chatEntity = getChatEntity(msgEntity);
            resultIntent.putExtra("chatId", chatEntity.getChatId());
            resultIntent.putExtra("chatName", chatEntity.getChatName());
            resultIntent.putExtra("chatType", chatEntity.getChatType());

        } else {
            resultIntent = new Intent(mContext, com.aspirecn.corpsocial.bundle.common.ui.MainTabActivity_.class);
            resultIntent.putExtra("tab", 1);
        }
        return resultIntent;
    }

    /**
     * 设置对应IMAGEVIEW的ID的资源图片
     *
     * @param msgEntity
     * @param view_custom
     */
    private void setImageView(MsgEntity msgEntity, RemoteViews view_custom) {
        view_custom
                .setImageViewResource(R.id.custom_icon, R.drawable.chat_icon);
        if (newsCount == 1) {// 如果是只有一条未读
            view_custom.setTextViewText(R.id.tv_custom_title,
                    setUserName(msgEntity));
            view_custom.setTextViewText(R.id.tv_custom_content, "新消息："
                    + setContent(msgEntity));
            newsCount++;
        } else {
            view_custom.setTextViewText(R.id.tv_custom_title, "您有新消息");
            view_custom.setTextViewText(R.id.tv_custom_yet, "［" + newsCount++
                    + "］");
            view_custom.setTextViewText(R.id.tv_custom_content,
                    setUserName(msgEntity) + ":" + setContent(msgEntity));
        }
        view_custom.setTextViewText(R.id.tv_custom_time,
                DateUtils.getWeiXinTime(System.currentTimeMillis()));
    }

    /**
     * 判断是否开启消息通知
     *
     * @param msgEntity
     */
    private boolean isNewMsgNotify(MsgEntity msgEntity) {
        // 判断总消息开关是否开启,再判断具体某个点对点或群聊天消息提醒是否开启
        boolean newMsgNotify = false;
        if (Config.getInstance().isNotify()) {
            ChatEntity chatEntity = getChatEntity(msgEntity);
            if (chatEntity != null) {
                String setting = chatEntity.getSetting();
                try {
                    if (setting != null) {
                        newMsgNotify = new JSONObject(setting).optBoolean(
                                "newMsgNotify", false);
                    }
                } catch (JSONException e) {
                    LogUtil.e("读取个人聊天设置失败", e);
                }
            }
        }
        return newMsgNotify;
    }

    /**
     * 取得ChatEntity
     *
     * @param msgEntity
     * @return
     */
    private ChatEntity getChatEntity(MsgEntity msgEntity) {
        Map<String, Object> chatWhere = new HashMap<String, Object>();
        chatWhere.put("chatId", msgEntity.getChatId());
        chatWhere.put("belongUserId", Config.getInstance().getUserId());
        return chatDao.findByWhere(chatWhere);
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT 点击去除：
     * Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1,
                new Intent(), flags);
        return pendingIntent;
    }

    /**
     * 清除所有通知栏
     */
    public void clearAllNotify() {
        if (null != mNotificationManager) {
            // mNotificationManager.cancelAll();// 删除你发的所有通知
            mNotificationManager.cancel(notifyId);
            newsCount = 1;
        }
    }

    /**
     * 设置名称
     *
     * @param msgEntity
     */
    private String setUserName(MsgEntity msgEntity) {
        String userNameVal = msgEntity.getOwnedUserId();
        if (!TextUtils.isEmpty(msgEntity.getOwnedUserName())) {
            userNameVal = msgEntity.getOwnedUserName();
        } else {
            GetContactDetailEvent getContactDetailEvent = new GetContactDetailEvent();
            getContactDetailEvent.setContactId(msgEntity.getOwnedUserId());
            ContactVO contactVO = (ContactVO) UiEventHandleFacade.getInstance()
                    .handle(getContactDetailEvent);
            if (!TextUtils.isEmpty(contactVO.getName())) {
                userNameVal = contactVO.getName();
            }

        }
        return userNameVal;
    }

    /**
     * 设置内容
     *
     * @param msgEntity
     * @return
     */
    private String setContent(MsgEntity msgEntity) {
        if (msgEntity.getContentType() == 0) {
            return msgEntity.getContent();
        } else if (msgEntity.getContentType() == 1) {
            return "来自" + setUserName(msgEntity) + "的新语音信息";
        } else if (msgEntity.getContentType() == 2) {
            return "来自" + setUserName(msgEntity) + "的新图片信息";
        } else if (msgEntity.getContentType() == 3) {
            return "来自" + setUserName(msgEntity) + "的新视频信息";
        } else if (msgEntity.getContentType() == 4) {
            return "来自" + setUserName(msgEntity) + "的新位置信息";
        }
        return "来自" + setUserName(msgEntity) + "的新文件信息";
    }
}
