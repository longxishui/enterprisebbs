/**
 * @(#) UIHandlerClassFilter.java Created on 2013-11-29
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

import com.aspirecn.corpsocial.common.eventbus.ClassScanUtil.ClassFilter;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.StatusChangeHandler;

import java.lang.annotation.Annotation;

/**
 * The class <code>UIHandlerClassFilter</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class StatusChangeHandlerClassFilter implements ClassFilter {

    @SuppressWarnings("rawtypes")
    @Override
    public boolean accept(Class clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof StatusChangeHandler) {
                return true;
            }
        }
        return false;
    }

}
