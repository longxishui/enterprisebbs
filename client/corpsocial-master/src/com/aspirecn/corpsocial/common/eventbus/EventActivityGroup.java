package com.aspirecn.corpsocial.common.eventbus;

import android.app.ActivityGroup;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

@SuppressWarnings("deprecation")
public class EventActivityGroup extends ActivityGroup implements EventListener {

    /**
     * UI层调用逻辑层的统一接口模版
     */
    protected UiEventHandleFacade uiEventHandleFacade;

//    private CoreService coreService;
//
//    private boolean boundService = false;
//
//    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        // 从service中获取访问访问对象
        // Intent intent = new Intent(this, CoreService.class);
        // serviceConnection = new ServiceConnection() {
        // @Override
        // public void onServiceConnected(ComponentName arg0, IBinder arg1) {
        // CoreBinder binder = (CoreBinder) arg1;
        // coreService = binder.getService();
        // boundService = true;
        // }
        //
        // @Override
        // public void onServiceDisconnected(ComponentName arg0) {
        // boundService = false;
        // }
        // };
        // bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        // IIMNetClient imNetClient = coreService.getImNetClient();

        uiEventHandleFacade = UiEventHandleFacade.getInstance();

        // 向消息队列注册被监听对象
        EventListenerSubjectLoader instance = EventListenerSubjectLoader
                .getInstance();
        instance.registerListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 不应重复统计
        // MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // if (boundService) {
        // unbindService(serviceConnection);
        // boundService = false;
        // }
        // 向消息队列注销被监听对象
        EventListenerSubjectLoader instance = EventListenerSubjectLoader
                .getInstance();
        instance.unregisterListener(this);
    }

}
