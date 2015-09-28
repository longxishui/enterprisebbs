package com.aspirecn.corpsocial.common.util;

import android.util.Log;

import com.aspirecn.corpsocial.common.eventbus.DaoFilter;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by chenbin on 2015/8/25.
 */
public class BeanContainer {

    public static BeanContainer container=new BeanContainer();

    private BeanContainer(){

    }

    public static BeanContainer getInstance(){
        return container;
    }

    private Map<Class<?>,Object> beans=new HashMap();

    private DaoFilter daoFilter=new DaoFilter();

    public void addBean(Class<?> cls){
        try {
            if(daoFilter.accept(cls)) {
                beans.put(cls, cls.newInstance());
            }
        }catch(Exception e){
            LogUtil.e("",e);
        }
    }

    public void initClass(List<Class> classes){
        for(Class clazz:classes) {
            if (getBean(clazz) == null) {
                addBean(clazz);
            }
        }
        //BeanContainer.getInstance().fetchBean();
    }

    public Object getBean(Class<?> cls){
        return beans.get(cls);
    }

    public void fetchBean(Object object){

            Class<?> cls=object.getClass();
            Field[] fields= cls.getDeclaredFields();
            for(Field field:fields){
                EventBusAnnotation.Component c= field.getAnnotation(EventBusAnnotation.Component.class);
                if(c!=null){
                    try {
                        field.setAccessible(true);
                        if(field.get(object)==null) {//目标对象的属性值为null
                            if(beans.get(field.getType())==null){//容器里没有Component对象
                                Class cc=field.getType();
                                addBean(cc);
                            }
                            //注入目标对象属性值

                            field.set(object, beans.get(field.getType()));
                        }
                    }catch(Exception e){
                        LogUtil.e("",e);
                    }
                }
            }

    }

    public void fetchBean(){
        Iterator<Class<?>> it= beans.keySet().iterator();
        while(it.hasNext()){
            Class<?> cls=it.next();
            Field[] fields= cls.getDeclaredFields();
            for(Field field:fields){
                EventBusAnnotation.Component c= field.getAnnotation(EventBusAnnotation.Component.class);
                if(c!=null){
                    try {

                        if(field.get(beans.get(cls))==null) {
                            if(beans.get(field.getType())==null){
                                Class cc=field.getType();
                                addBean(cc);
                            }
                            field.set(beans.get(cls), beans.get(field.getType()));
                        }
                    }catch(Exception e){
                        LogUtil.e("",e);
                    }
                }
            }
        }
    }
}
