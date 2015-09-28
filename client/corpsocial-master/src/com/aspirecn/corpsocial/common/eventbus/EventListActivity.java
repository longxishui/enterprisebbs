package com.aspirecn.corpsocial.common.eventbus;

import android.app.ListActivity;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

public class EventListActivity extends ListActivity implements EventListener {

    /**
     * UI层调用逻辑层的统一接口模版
     */
    protected UiEventHandleFacade uiEventHandleFacade;

    //private CoreService coreService;

    private boolean boundService = false;

    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        // // 从service中获取网络访问对象
        // IIMNetClient imNetClient = coreService.getImNetClient();

        uiEventHandleFacade = UiEventHandleFacade.getInstance();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (boundService) {
            unbindService(serviceConnection);
            boundService = false;
        }

        // 向消息队列注销被监听对象
        EventListenerSubjectLoader instance = EventListenerSubjectLoader
                .getInstance();
        instance.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // 向消息队列注册被监听对象
        EventListenerSubjectLoader instance = EventListenerSubjectLoader
                .getInstance();
        instance.registerListener(this);
        MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

}
