package com.aspirecn.corpsocial.common.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息主题
 * <p/>
 * The class <code>MsgSubject</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public abstract class EventSubject<T extends EventListener, V extends BusEvent> {

    /**
     * 事件监听者队列
     */
    protected List<T> eventListenerQueue = new ArrayList<T>();

    abstract public void notifyListener(V event);

    public void attach(T t) {
        eventListenerQueue.add(t);
    }

    public void detach(T t) {
        eventListenerQueue.remove(t);
    }

    public boolean contains(T t) {
        return eventListenerQueue.contains(t);
    }

    public List<T> getEventListenerQueue() {
        return eventListenerQueue;
    }

}
