package com.aspirecn.corpsocial.bundle.common.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.CommandData;
import com.aspirecn.corpsocial.common.eventbus.CommandHead;
import com.aspirecn.corpsocial.common.eventbus.DataReceiveHandlerLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeDelegate;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeMessageType;
import com.gotye.api.GotyeNotify;
import com.gotye.api.GotyeUser;

public class GotyeService extends Service {
	public static final String ACTION_INIT = "gotyeim.init";
	public static final String ACTION_LOGIN = "gotyeim.login";
	private GotyeAPI api;
    private String mTag = "GotyeService";
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		api = GotyeAPI.getInstance();
//	    api.setNetConfig("", -1);
		//初始化
		int code = api.init(getBaseContext(), Constant.QINJIA_APPKEY);
        //添加推送消息监听
        api.addListener(mDelegate);
		//语音识别初始化
		api.initIflySpeechRecognition();
		//api.enableLog(false, true, false);
		//开始接收离线消息
		api.beginReceiveOfflineMessage();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			if (ACTION_LOGIN.equals(intent.getAction())) {
				System.out.println("调用服务的登录接口");
				String name = intent.getStringExtra("name");
				String pwd = intent.getStringExtra("pwd");
				int code = api.login(name, pwd);
			} else if (ACTION_INIT.equals(intent.getAction())) {
					api.setNetConfig("", -1);
				int code = api.init(getBaseContext(), Constant.QINJIA_APPKEY);
			}
		} else {
			String[] user = getUser(this);
			Log.d("ServiceCommand", "user = "+user[0]);
			if (!TextUtils.isEmpty(user[0])) {
				//登陆,注意code返回状态，-1表示正在登陆，1表示已经登陆或者正在登陆
				//导致杀掉进程后自动登录的Bug
//				int code = api.login(user[0], user[1]);
//				Log.d("ServiceCommand", "33333333333333333 = "+code);
			}
		}
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.d("gotye_service", "onDestroy");
		GotyeAPI.getInstance().removeListener(mDelegate);
		Intent localIntent = new Intent();
		localIntent.setClass(this, GotyeService.class); // 銷毀時重新啟動Service
		this.startService(localIntent);
		super.onDestroy();
	}

	public static String[] getUser(Context context) {
		String name =  Config.getInstance().getUserName();;
		String password =  Config.getInstance().getPassword();
		String[] user = new String[2];
		user[0] = name;
		user[1] = password;
		return user;
	}

	@SuppressWarnings("deprecation")
	private void notify(String msg) {
//		String currentPackageName = AppUtil.getTopAppPackage(getBaseContext());
//		if (currentPackageName.equals(getPackageName())) {
//			return;
//		}
//		NotificationManager notificationManager = (NotificationManager) this
//				.getSystemService(NOTIFICATION_SERVICE);
//		notificationManager.cancel(0);
//		Notification notification = new Notification(R.drawable.ic_launcher,
//				msg, System.currentTimeMillis());
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.putExtra("notify", 1);
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//				intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		notification.setLatestEventInfo(this, getString(R.string.app_name),
//				msg, pendingIntent);
//		notificationManager.notify(0, notification);
	}

	
	private GotyeDelegate mDelegate = new GotyeDelegate(){
		public void onLogin(int code, GotyeUser user) {
			System.out.println("调用服务的login回调"+code);
		};
		public void onLogout(int code) {
			System.out.println("调用服务的logout回调"+code);
		};
		@Override
		public void onReceiveMessage(GotyeMessage message) {
			String msg = null;

			if (message.getType() == GotyeMessageType.GotyeMessageTypeText) {
				msg = message.getSender().getName() + ":"+message.getText();
			} else if (message.getType() == GotyeMessageType.GotyeMessageTypeImage) {
				msg = message.getSender().getName() + "发来了一条图片消息";
			} else if (message.getType() == GotyeMessageType.GotyeMessageTypeAudio) {
				msg = message.getSender().getName() + "发来了一条语音消息";
			} else if (message.getType() == GotyeMessageType.GotyeMessageTypeUserData) {
				msg = message.getSender().getName() + "发来了一条自定义消息";
			} else {
				msg = message.getSender().getName() + "发来了一条群邀请信息";
			}
			if (message.getReceiver() instanceof GotyeGroup) {
//				if (!(MyApplication.isGroupDontdisturb(message.getReceiver().getId()))) {
					GotyeService.this.notify(msg);
//				}
				return;
			}else{
				GotyeService.this.notify(msg);
			}
            Log.i(mTag, "received a message from "
                    + message.getSender().getName() + ", message type is "
                    + message.getType());
//            switch (message.getType()) {
//                case GotyeMessageTypeText:///< 文本消息
//                    IHandler handler = DataReceiveHandlerLoader.getInstance().getHandler(CommandData.CommandType.DIALOG_MESSAGE);
//                    if(handler!=null){
//                        CommandData commandData = new CommandData();
//                        CommandHead commandHead = new CommandHead();
//                        commandHead.commandType = CommandData.CommandType.DIALOG_MESSAGE;
//                        commandHead.groupMsg = false;
//                        commandHead.messageId = String.valueOf(message.getId());
//                        commandHead.userId = String.valueOf(message.getSender().getId());
//                        commandHead.targetId = String.valueOf(message.getSender().getId());
//                        commandHead.sendTime = message.getDate();
//                        commandData.setContent(message.getText());
//                        commandData.setCommandHead(commandHead);
//                        handler.handle(commandData);
//                    }
//                    Log.i(mTag, "text: " + message.getText()); ///< 显示接收到的文本消息内容
//                    break;
//
//                case GotyeMessageTypeAudio: ///< 语音消息
//                    Log.i(mTag, "audio duration: "
//                            + message.getMedia().getDuration() / 1000
//                            + "s, downloading audio automatically...");
////                    apiist.downloadMediaInMessage(message); ///< 开始下载语音消息内容
//                    break;
//            }
		}

        @Override
        public void onGetMessageList(int code, int totalCount) {
            super.onGetMessageList(code, totalCount);
        }

        @Override
		public void onReceiveNotify(GotyeNotify notify) {
			String msg = notify.getSender().getName() + "邀请您加入群[";
			if (!TextUtils.isEmpty(notify.getFrom().getName())) {
				msg += notify.getFrom().getName() + "]";
			} else {
				msg += notify.getFrom().getId() + "]";
			}
			GotyeService.this.notify(msg);
		}

		 
	};
}
