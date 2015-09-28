//package com.aspirecn.corpsocial.common.service;
//
//import java.util.Observable;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import android.os.Looper;
//
//import com.aspirecn.corp.im.client.INetworkChecker;
//import com.aspirecn.corp.im.client.callback.IUserStatusListener;
//import com.aspirecn.corp.im.client.data.ConfigData;
//import com.aspirecn.corp.im.client.data.NetState;
//import com.aspirecn.corp.im.client.impl.IMNetClientImpl;
//import com.aspirecn.corpsocial.bundle.common.ui.Notification.SendApplicationNotification;
//
//public class UserStatusListener implements IUserStatusListener {
//	private static Logger logger = LoggerFactory
//			.getLogger(UserStatusListener.class);
//	@Override
//	public void update(Observable o, Object obj) {
//		if (obj != null) {
//			NetState netState = (NetState) obj;
//			boolean flag = false;
//			if (netState.getStatus() == 0) {
//				flag = true;
//			} else {
//				flag = false;
//			}
//
////			new SendApplicationThread(flag).start();
//		}
//
//	}
//
////	class SendApplicationThread extends Thread {
////		private boolean onLineFlag;
////
////		public SendApplicationThread(boolean onLineFlag) {
////			this.onLineFlag = onLineFlag;
////		}
////
////		public void run() {
////			ConfigData configData = IMNetClientImpl.getConfigData();
////			INetworkChecker networkChecker = null;
////			if (configData != null) {
////				networkChecker = configData.getNetworkChecker();
////			}
////			if (networkChecker != null && networkChecker.getAutoLoginFlag()) {
////				logger.debug("设置通知栏在线状态");
////				Looper.prepare();
////				SendApplicationNotification.SendAppAreOnline(onLineFlag);
////				Looper.loop();
////			}
////		}
////	}
//
//}
