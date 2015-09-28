package com.aspirecn.corpsocial.common.util;

import android.content.Context;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.AssetsUtils;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenbin on 2015/8/24.
 */
public class EventChainUtil {

    private class EventChains{
        private List<EventChain> data;

        public List<EventChain> getData() {
            return data;
        }

        public void setData(List<EventChain> data) {
            this.data = data;
        }
    }

    private class EventChain{
        private String event;

        private List<NextEvent> nextevents;

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public List<NextEvent> getNextevents() {
            return nextevents;
        }

        public void setNextevents(List<NextEvent> nextevents) {
            this.nextevents = nextevents;
        }
    }

    private class NextEvent{
        private String event;

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }
    }

    private static Map<String,List<Class>> map=new HashMap();

    public static List<BusEvent> getNextEvents(String key){
        //return map.get(key);
        List<Class> classes=map.get(key);
        List<BusEvent> events=new ArrayList<>();
        for(Class cls:classes){
            try {
                BusEvent bs = (BusEvent) cls.newInstance();
                events.add(bs);
            }catch(Exception e){
                LogUtil.e("",e);
            }
        }
        return events;
    }

    public static void init(Context context){
        EventChains eventChains = AssetsUtils.getInstance().read(context, "eventchain.json", EventChains.class);
        for(EventChain eventChain:eventChains.getData()){
            String event=eventChain.getEvent();
            List<NextEvent> nextEvents=eventChain.getNextevents();
            List<Class> list=new ArrayList();
            for(NextEvent nextEvent:nextEvents){
                try {
                    Class cls = Class.forName(nextEvent.getEvent());
                    //BusEvent bs=(BusEvent)cls.newInstance();
                    list.add(cls);
                }catch(Exception e){
                    LogUtil.e("",e);
                }
            }
            map.put(event,list);
        }
    }
}
