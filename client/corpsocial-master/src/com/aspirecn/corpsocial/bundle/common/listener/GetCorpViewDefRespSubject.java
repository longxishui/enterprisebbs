package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.GetCorpViewDefRespEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by yinghuihong on 15/5/21.
 * <p/>
 * 通知获取企业配置信息完成
 */
@EventBusAnnotation.EventListenerSubject
public class GetCorpViewDefRespSubject extends EventSubject<GetCorpViewDefRespSubject.GetCorpViewDefRespListener, GetCorpViewDefRespEvent> {

    @Override
    public void notifyListener(GetCorpViewDefRespEvent event) {
        for (GetCorpViewDefRespListener eventListener : eventListenerQueue) {
            eventListener.onGetCorpViewDefEvent(event);
        }
    }

    public interface GetCorpViewDefRespListener extends EventListener {
        void onGetCorpViewDefEvent(GetCorpViewDefRespEvent event);
    }
}
