package com.aspirecn.corpsocial.bundle.common.uihandler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.aspirecn.corpsocial.bundle.common.event.LoginEvent;
import com.aspirecn.corpsocial.bundle.common.event.LoginRespEvent;
import com.aspirecn.corpsocial.bundle.common.service.GotyeService;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.AppBroadcastManager;
import com.aspirecn.corpsocial.common.eventbus.CommandData;
import com.aspirecn.corpsocial.common.eventbus.CommandHead;
import com.aspirecn.corpsocial.common.eventbus.DataReceiveHandlerLoader;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeDelegate;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeStatusCode;
import com.gotye.api.GotyeUser;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;


/**
 * 登录逻辑处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = LoginEvent.class)
public class LoginEventHandler implements IHandler<Null, LoginEvent> {

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    private AppBroadcastManager appBroadcastManager = AppBroadcastManager
            .getInstance();
    private String mTag = "LoginEventHandler";
    @Override
    public Null handle(final LoginEvent loginEvent) {
        // 切换为当前登陆用户的配置文件
        ConfigPersonal.getInstance().init(loginEvent.getUsername());
        GotyeAPI apiist = GotyeAPI.getInstance();
        apiist.addListener(delegate);
        Intent login = new Intent(AspirecnCorpSocial.getContext(), GotyeService.class);
        login.setAction(GotyeService.ACTION_LOGIN);
        login.putExtra("name", loginEvent.getUsername());
        AspirecnCorpSocial.getContext().startService(login);
//        LoginData loginData = new LoginData();
//        loginData.setUsername(loginEvent.getUsername());
//        loginData.setPasswd(loginEvent.getPassword());
//
//        if (!imNetClient.checkLogined()) {
//            // 缓存1秒，等待系统进入主界面再抛出事件
//
//            SystemClock.sleep(1000);
//
//            notifyImModuleLoginStart();
//
//            imNetClient.login(loginData, new ILoginNotify() {
//                @Override
//                public void notify(LoginResultData arg0) {
//
//                    LoginRespEvent loginRespEvent = new LoginRespEvent();
//                    int errorCode = arg0.getErrorCode();
//
//                    if (ErrorCode.SUCCESS.getValue() == errorCode) {
//                        handleLoginSuccess(arg0.getLoginResp(), loginEvent);
//                    }
//
//                    loginRespEvent.setRespCode(errorCode);
//                    loginRespEvent.setMessage(arg0.getMessage());
//                    eventListener.notifyListener(loginRespEvent);
//                    notifyImModuleLoginResp(errorCode, arg0.getMessage());
//
//                }
//            });
//        } else {
//            // 每次重新登陆时执行
////			String userId = Config.getInstance().getUserId();
////			ConfigPersonal.getInstance().init(userId);
////			getNotificationConfig();
//            // 清理"协同办公"、"新闻中心"的过期数据
//
////            UserServiceParam userServiceParam = new UserServiceParam();
////            userServiceParam.setServie("FindCorpService");
////            UserServiceResult result = (UserServiceResult) OsgiServiceLoader.getInstance().getService(UserService.class).invoke(userServiceParam);
////            List<UserCorp> userCorps = (List<UserCorp>) result.getData();
//            List<UserCorp> userCorps = (List<UserCorp>) OsgiServiceLoader.getInstance().getService(FindCorpService.class).invoke(new Null());
//
//            for (UserCorp uc : userCorps) {
//                notifyProcessModuleClearOutDatedData(uc.getCorpId());
//                notifyNewsCenterModuleClearOutDatedData(uc.getCorpId());
//            }
//
//            //通知公众号模块登录完成
//            notifyPubAccountLoginFinish();
//        }

//        //通知公众号模块登录完成
//        notifyPubAccountLoginFinish();

//        notifyAppDefModuleChange();

        return new Null();
    }
    // 申明一个监听器，在这个里面，重写那些您感兴趣的回调函数
    private GotyeDelegate delegate = new GotyeDelegate() {

        public void onLogin(int code, GotyeUser user) {
            if(code== GotyeStatusCode.CodeOK||code==GotyeStatusCode.CodeReloginOK){
                LoginRespEvent loginRespEvent = new LoginRespEvent();
                loginRespEvent.setRespCode(ErrorCode.SUCCESS.getValue());
                eventListener.notifyListener(loginRespEvent);
                Config.getInstance().setLoginStatus(true);
                Config.getInstance().setUserId("492205");
                Config.getInstance().setCorpId("10");
            }else{
                LoginRespEvent loginRespEvent = new LoginRespEvent();
                loginRespEvent.setRespCode(ErrorCode.NETWORK_FAILED.getValue());
                loginRespEvent.setMessage(String.valueOf(code));
                eventListener.notifyListener(loginRespEvent);
                Config.getInstance().setLoginStatus(false);
            }
            Log.i(mTag, "login callback, code: " + code);
            // 测试文本发送功能：
//	        GotyeUser receiver = new GotyeUser("duyz1"); ///< 创建一个名叫Finn的消息接收者（要确保接收者账号已经注册过）
//	        GotyeMessage message = GotyeMessage.createTextMessage(receiver, "hello"); ///< 创建一条发送给Finn简单文本消息
//	        apiist.sendMessage(message); ///< 发送文本消息

            // 测试语音发送功能：
//	        apiist.startTalk(receiver, WhineMode.DEFAULT, false, 10000); ///< 开始录制一段长度为10秒钟的语音消息发送给Finn
        };

        // 监听录音停止回调
        public void onStopTalk(int code, GotyeMessage message,
                               boolean isVoiceReal) {
            Log.i(mTag, "recording finished. calling sendMessage to send it...");

            if(message.getText()!=null && message.getText().length() > 0){
                message.putExtraData(message.getText().getBytes());///< 如果启动了语音识别，将识别出来的文字作为消息附加字段发送出去
            }
//            apiist.sendMessage(message); ///< 发送录制好的语音消息
        };

        // 监听接收消息回调
        public void onReceiveMessage(GotyeMessage message) {
        };

        // 监听消息下载回调
        public void onDownloadMediaInMessage(int code, GotyeMessage message) {
//            apiist.playMessage(message); ///< 下载完成，自动播放语音消息
        };
    };
    private void notifyImModuleLoginStart() {
        Intent loginStartIntent = new Intent("imNotifyReceiver");
        Bundle loginStartIntentBundle = new Bundle();
        loginStartIntentBundle.putString("action", "loginStartNotify");
        loginStartIntent.putExtras(loginStartIntentBundle);

        appBroadcastManager.sendBroadcast(loginStartIntent);
    }

    //
//    /**
//     * 通知同步公司群组列表
//     */
//    private void notifyImModuleSyncCorpGroup() {
//        Intent loginStartIntent = new Intent("imNotifyReceiver");
//        Bundle loginStartIntentBundle = new Bundle();
//        loginStartIntentBundle.putString("action", "syncCorpGroup");
//        loginStartIntent.putExtras(loginStartIntentBundle);
//
//        appBroadcastManager.sendBroadcast(loginStartIntent);
//    }
//
    private void notifyImModuleLoginResp(int errorCode, String message) {
        Intent loginRespIntent = new Intent("imNotifyReceiver");
        Bundle loginRespBundle = new Bundle();
        loginRespBundle.putString("action", "loginRespNotify");
        Bundle loginRespData = new Bundle();
        loginRespData.putInt("errorCode", errorCode);
        loginRespData.putString("message", message);
        loginRespBundle.putBundle("data", loginRespData);
        loginRespIntent.putExtras(loginRespBundle);
        appBroadcastManager.sendBroadcast(loginRespIntent);
    }
    //end comment

//	/**
//	 * 通知通讯录切换公司
//	 */
//	private void notifyAddrbookModuleCorpChange() {
//		Intent loginStartIntent = new Intent("addrbookNotifyReceiver");
//		Bundle loginStartIntentBundle = new Bundle();
//		loginStartIntentBundle.putString("action", "corpChange");
//		loginStartIntent.putExtras(loginStartIntentBundle);
//
//		appBroadcastManager.sendBroadcast(loginStartIntent);
//	}

//	/**
//	 * 通知通讯录切换登陆用户
//	 */
//    private void notifyAddrbookModuleUserChange() {
//        Intent loginStartIntent = new Intent("addrbookNotifyReceiver");
//        Bundle loginStartIntentBundle = new Bundle();
//        loginStartIntentBundle.putString("action", "userChange");
//        loginStartIntent.putExtras(loginStartIntentBundle);
//
//        appBroadcastManager.sendBroadcast(loginStartIntent);
//    }

//	private void notifyNewsModuleCorpChange() {
//		Intent loginStartIntent = new Intent("newsNotifyReceiver");
//		Bundle loginStartIntentBundle = new Bundle();
//		loginStartIntentBundle.putString("action", "corpChange");
//		loginStartIntent.putExtras(loginStartIntentBundle);
//
//		appBroadcastManager.sendBroadcast(loginStartIntent);
//	}

//	private void notifyDocumentsModuleCorpChange() {
//		Intent loginStartIntent = new Intent("documentsNotifyReceiver");
//		Bundle loginStartIntentBundle = new Bundle();
//		loginStartIntentBundle.putString("action", "corpChange");
//		loginStartIntent.putExtras(loginStartIntentBundle);
//
//		appBroadcastManager.sendBroadcast(loginStartIntent);
//	}

//	/**
//	 * 通知同步用户群组列表
//	 */
//	private void notifyImModuleSyncCustomGroup() {
//		Intent loginStartIntent = new Intent("imNotifyReceiver");
//		Bundle loginStartIntentBundle = new Bundle();
//		loginStartIntentBundle.putString("action", "syncUserGroupList");
//		loginStartIntent.putExtras(loginStartIntentBundle);
//
//		appBroadcastManager.sendBroadcast(loginStartIntent);
//	}

//    private void notifyCheckinModuleSyncCheckinConfig() {
//        Intent loginStartIntent = new Intent("checkinNotifyReceiver");
//        Bundle loginStartIntentBundle = new Bundle();
//        loginStartIntentBundle.putString("action", "syncCheckinConfig");
//        loginStartIntent.putExtras(loginStartIntentBundle);
//
//        appBroadcastManager.sendBroadcast(loginStartIntent);
//    }


//    private void notifyAppDefModuleChange() {
//        Intent i = new Intent("appDefNotifyReceiver");
//        Bundle b = new Bundle();
//        b.putString("action", "updateAppDef");
//        i.putExtras(b);
//
//        AppBroadcastManager instance = AppBroadcastManager.getInstance();
//        instance.sendBroadcast(i);
//    }

//    private void handleLoginSuccess(LoginResp loginResp, LoginEvent loginEvent) {
//        if (loginResp == null)
//            return;//FIXME 临时处理
//        UserInfo userInfo = loginResp.userInfo;
////		ConfigPersonal.getInstance().init(userInfo.userId);
//        Config config = Config.getInstance();
//        String corpId = config.getCorpId();
//        String userId = config.getUserId();
//        boolean loginStatus = config.getLoginStatus();
//        // 首次登陆，设置默认企业
//        if (TextUtils.isEmpty(ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED))) {
//            ConfigPersonal.putString(ConfigPersonal.Key.CORP_ID_SELECTED, userInfo.corpId);
//        }
//        if (!loginStatus) {
//            // 首次登录， 将用户登录信息加入缓存
//            config.setLoginStatus(true);
//            config.setAccountStatus(true);
//            config.setUserId(userInfo.userId);
//        }
//        config.setUserName(loginEvent.getUsername());
//        config.setCorpId(userInfo.corpId);
//
//        config.setHeadImageUrl(userInfo.headImgUrl);
//        config.setNickName(userInfo.nickName);
//        config.setSex(userInfo.sex.getValue());
//        config.setUserJobNum(userInfo.jobNum);
//
//        // 每次登陆，更新提醒配置参数
//        //getNotificationConfig();
//
//        String loginName = userInfo.loginName;
//        config.setLoginName(loginName);
//        String departId = userInfo.departId;
//        config.setDepartId(departId);
//
//        List<LoginResp.CorpInfo> corpInfos = loginResp.corpList;
//        // TODO save the personal corp info
//
//        // 企业ID变更，通知公司组织结构更新
////		if (!userInfo.corpId.equals(corpId)) {
////			notifyAddrbookModuleCorpChange();
////			notifyNewsModuleCorpChange();
////			notifyDocumentsModuleCorpChange();
////			notifyAppCenterModuleCorpChange();
////			config.setValue("ADDRBOOK_SYNC_DONE", false);
////		}
//
//        // 用户ID变更，通知联系人列表更新
////        if(!userInfo.userId.equals(userId)){
////            notifyAddrbookModuleUserChange();
//        //config.setValue("ADDRBOOK_SYNC_DONE", false);
////        }
//
//        // 同步公司群组
//        //notifyImModuleSyncCorpGroup();
//
////		Boolean addrbookInited = Config.getInstance().getBooleanValue(
////				"ADDRBOOK_SYNC_DONE", false);
////		if (addrbookInited) {
////			//通讯录同步完成后，再同步创建的群组
////			notifyImModuleSyncCustomGroup();
////		}
//
////        notifyCheckinModuleSyncCheckinConfig();
//
//        //notifyGetCorpViewDef();
//
//        //通知公众号模块登录完成
//        notifyPubAccountLoginFinish();
//
//        AddrbookConfig.reset();
//        List<BusEvent> nextevents = EventChainUtil.getNextEvents(LoginEvent.class.getSimpleName());
//        if (nextevents != null && nextevents.size() > 0) {
//            for (BusEvent nevent : nextevents) {
//                UiEventHandleFacade.getInstance().handle(nevent);
//            }
//        }
//    }

//    private void notifyGetCorpViewDef() {
//        UiEventHandleFacade.getInstance().handle(new GetCorpViewDefEvent());
//    }

//    private void getNotificationConfig() {
//        UiEventHandleFacade.getInstance().handle(new GetConfigListEvent());
//    }
}
