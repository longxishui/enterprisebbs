//package com.aspirecn.corpsocial.bundle.addrbook.listener;
//
//import com.aspirecn.corpsocial.bundle.addrbook.event.UIRefreshAddrbookViewPagerRespEvent;
//import com.aspirecn.corpsocial.bundle.addrbook.listener.UIRefreshAddrbookViewPagerRespSubject.UIRefreshContactsRespEventListener;
//import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
//import com.aspirecn.corpsocial.common.eventbus.EventListener;
//import com.aspirecn.corpsocial.common.eventbus.EventSubject;
//
//@EventListenerSubject
//public class UIRefreshAddrbookViewPagerRespSubject
//        extends
//        EventSubject<UIRefreshContactsRespEventListener, UIRefreshAddrbookViewPagerRespEvent> {
//
//    @Override
//    public void notifyListener(UIRefreshAddrbookViewPagerRespEvent event) {
//        for (UIRefreshContactsRespEventListener eventListener : eventListenerQueue) {
//            eventListener.onHandleUIRefreshContactsRespEvent(event);
//        }
//    }
//
//    public interface UIRefreshContactsRespEventListener extends EventListener {
//        void onHandleUIRefreshContactsRespEvent(
//                UIRefreshAddrbookViewPagerRespEvent event);
//    }
//}
