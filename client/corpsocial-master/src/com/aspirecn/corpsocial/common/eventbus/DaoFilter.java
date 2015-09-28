package com.aspirecn.corpsocial.common.eventbus;

import java.lang.annotation.Annotation;

/**
 * Created by chenbin on 2015/8/25.
 */
public class DaoFilter implements ClassScanUtil.ClassFilter {

    @SuppressWarnings("rawtypes")
    @Override
    public boolean accept(Class clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof EventBusAnnotation.Dao) {
                return true;
            }
        }
        return false;
    }

}
