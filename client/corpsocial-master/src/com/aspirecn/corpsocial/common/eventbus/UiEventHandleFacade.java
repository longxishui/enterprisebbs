package com.aspirecn.corpsocial.common.eventbus;

import com.aspirecn.corpsocial.bundle.im.facade.SystemMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author lizhuo_a
 */
public class UiEventHandleFacade {

    private static UiEventHandleFacade handler = null;

    public static UiEventHandleFacade getInstance() {
        if (handler == null) {
            handler = new UiEventHandleFacade();
        }
        return handler;
    }

    @SuppressWarnings("unchecked")
    public Object handle(BusEvent busEvent) {

        Class<? extends BusEvent> clazz = busEvent.getClass();

        EventBusAnnotation.SingleTask singleTask=clazz.getAnnotation(EventBusAnnotation.SingleTask.class);
        if(singleTask!=null){

        }

        @SuppressWarnings("rawtypes")
        IHandler eventHandler = UiEventHandlerLoader.getInstance().getEventHandler(clazz);
        return eventHandler.handle(busEvent);

    }

    public void addFailedTask(BusEvent busEvent){
        busEvent.failedtime= System.currentTimeMillis();
        fevents.add(busEvent);
    }

    private static Map<Class<?>,?> tasks=new HashMap();

    private BlockingQueue<BusEvent> fevents=new LinkedBlockingQueue<BusEvent>();

    class RetryThread extends Thread{

        public void run(){
            while(true){

                BusEvent event=null;

                try {
                    event = fevents.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(event!=null){
                    IHandler eventHandler = UiEventHandlerLoader.getInstance().getEventHandler(event.getClass());
                    eventHandler.handle(event);
                }
            }
        }
    }
}
