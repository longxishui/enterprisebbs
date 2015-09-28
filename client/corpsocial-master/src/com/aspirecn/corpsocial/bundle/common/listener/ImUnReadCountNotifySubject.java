//package com.aspirecn.corpsocial.bundle.common.listener;
//
//import com.aspirecn.corpsocial.bundle.common.event.ImUnReadCountNotifyEvent;
//import com.aspirecn.corpsocial.bundle.common.listener.ImUnReadCountNotifySubject.UnReadCountNotifyEventListener;
//import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
//import com.aspirecn.corpsocial.common.eventbus.EventListener;
//import com.aspirecn.corpsocial.common.eventbus.EventSubject;
//
//@EventListenerSubject
//public class ImUnReadCountNotifySubject extends
//        EventSubject<UnReadCountNotifyEventListener, ImUnReadCountNotifyEvent> {
//
//    @Override
//    public void notifyListener(ImUnReadCountNotifyEvent event) {
//        for (UnReadCountNotifyEventListener eventListener : eventListenerQueue) {
//            eventListener.onHandleUnReadCountNotifyEvent(event);
//        }
//    }
//
//    public interface UnReadCountNotifyEventListener extends EventListener {
//        void onHandleUnReadCountNotifyEvent(ImUnReadCountNotifyEvent event);
//    }
//}
