//package com.aspirecn.corpsocial.bundle.common.listener;
//
//import com.aspirecn.corpsocial.bundle.common.event.PubAccountUnReadCountNotifyEvent;
//import com.aspirecn.corpsocial.bundle.common.listener.PubAccountUnReadCountNotifySubject.PubAccountUnReadCountNotifyEventListener;
//import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
//import com.aspirecn.corpsocial.common.eventbus.EventListener;
//import com.aspirecn.corpsocial.common.eventbus.EventSubject;
//
//@EventListenerSubject
//public class PubAccountUnReadCountNotifySubject extends
//        EventSubject<PubAccountUnReadCountNotifyEventListener, PubAccountUnReadCountNotifyEvent> {
//
//    @Override
//    public void notifyListener(PubAccountUnReadCountNotifyEvent event) {
//        for (PubAccountUnReadCountNotifyEventListener eventListener : eventListenerQueue) {
//            eventListener.onHandlePubAccountUnReadCountNotifyEvent(event);
//        }
//    }
//
//    public interface PubAccountUnReadCountNotifyEventListener extends EventListener {
//        void onHandlePubAccountUnReadCountNotifyEvent(PubAccountUnReadCountNotifyEvent event);
//    }
//}
