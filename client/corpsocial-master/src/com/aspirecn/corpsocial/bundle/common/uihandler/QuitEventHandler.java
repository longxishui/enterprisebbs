package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.common.event.QuitEvent;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

/**
 * 退出应用事件处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = QuitEvent.class)
public class QuitEventHandler implements IHandler<Null, QuitEvent> {

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    @Override
    public Null handle(QuitEvent args) {
        Config instance = Config.getInstance();
        // 修改登陆状态
        instance.setLoginStatus(false);
        // 如果是游客模式清空用户名密码
//        if (instance.isVisitorMode()) {
//            instance.setUserName("");
//            instance.setPassword("");
//            instance.setVisitorMode(false);
//            // 注销账号
//            instance.setAccountStatus(false);
//        }
        // 关闭所有界面
        EventListenerSubjectLoader.getInstance().clearListener();

//        imNetClient.quit(new IQuitNotify() {
//            @Override
//            public void notify(Result arg0) {
//            }
//        });

        return new Null();
    }

}
