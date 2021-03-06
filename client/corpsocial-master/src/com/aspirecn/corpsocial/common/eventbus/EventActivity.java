package com.aspirecn.corpsocial.common.eventbus;

import android.app.Activity;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

/**
 * @author lizhuo_a
 */
public class EventActivity extends Activity implements EventListener {

    /**
     * UI层调用逻辑层的统一接口模版
     */
    protected UiEventHandleFacade uiEventHandleFacade;

//    private CoreService coreService;
//
//    private boolean boundService = false;
//
//    private ServiceConnection serviceConnection;

//    public final static String PARAM = "param";

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        // 从service中获取访问访问对象
        // Intent intent = new Intent(this, CoreService.class);
        // bindService(intent, new ServiceConnection() {
        // @Override
        // public void onServiceConnected(ComponentName arg0, IBinder arg1) {
        // CoreBinder binder = (CoreBinder) arg1;
        // coreService = binder.getService();
        // boundService = true;
        //
        // // 从service中获取网络访问对象
        // IIMNetClient imNetClient = coreService.getImNetClient();
        // }
        //
        // @Override
        // public void onServiceDisconnected(ComponentName arg0) {
        // boundService = false;
        // }
        // }, Context.BIND_AUTO_CREATE);

        uiEventHandleFacade = UiEventHandleFacade.getInstance();

        // 向消息队列注册被监听对象
        EventListenerSubjectLoader instance = EventListenerSubjectLoader
                .getInstance();
        instance.registerListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        // super.onDestroy();

        // if (boundService) {
        // unbindService(serviceConnection);
        // boundService = false;
        // }

        // 向消息队列注销被监听对象
        EventListenerSubjectLoader instance = EventListenerSubjectLoader
                .getInstance();
        instance.unregisterListener(this);
        super.onDestroy();
    }

//    protected void forward(String nextClass) {
//        forward(nextClass, null);
//    }
//
//    /**
//     * Forward to nextClass.
//     *
//     * @param nextClass
//     * @param param
//     */
//    protected void forward(String nextClass, Serializable param) {
//        this.startActivity(newForwardIntent(nextClass, param));
//    }
//
//    protected void forwardForResult(String nextClass) {
//        this.forwardForResult(nextClass, null);
//    }
//
//    protected void forwardForResult(String nextClass, Serializable param) {
//        this.startActivityForResult(newForwardIntent(nextClass, param), 0);
//    }
//
//    private Intent newForwardIntent(String nextClass, Serializable param) {
//        Intent intent = new Intent();
//
//        Class<?> forName = null;
//        try {
//            forName = Class.forName(nextClass);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        intent.setClass(this, forName);
//        if (param != null) {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(PARAM, param);
//            intent.putExtras(bundle);
//        }
//        return intent;
//    }

}
