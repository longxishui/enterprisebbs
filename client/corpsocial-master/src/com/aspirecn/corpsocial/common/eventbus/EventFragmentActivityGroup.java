package com.aspirecn.corpsocial.common.eventbus;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

public class EventFragmentActivityGroup extends Fragment implements
        EventListener {
    /**
     * UI层调用逻辑层的统一接口模版
     */
    protected UiEventHandleFacade uiEventHandleFacade;

//	private CoreService coreService;
//
//	private boolean boundService = false;
//
//	private ServiceConnection serviceConnection;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        uiEventHandleFacade = UiEventHandleFacade.getInstance();

        // 向消息队列注册被监听对象
        EventListenerSubjectLoader instance = EventListenerSubjectLoader
                .getInstance();
        instance.registerListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
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
