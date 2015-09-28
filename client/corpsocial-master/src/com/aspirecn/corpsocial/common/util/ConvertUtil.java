/**
 * @(#) ConvertUtil.java Created on 2013-11-27
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The class <code>ConvertUtil</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class ConvertUtil {

    /**
     * @param srcInstance 被转化的类的实例
     * @param klass
     * @return
     */
    public static <T, V> V convert(T srcInstance, Class<V> klass) {

        V newInstance = null;
        try {
            newInstance = klass.newInstance();

            Field[] declaredFields = klass.getDeclaredFields();
            for (Field targetField : declaredFields) {
                Annotation[] annotations = targetField.getDeclaredAnnotations();

                for (Annotation annotation : annotations) {
                    if (annotation instanceof ConvertAttr) {
                        // 源属性名称
                        String srcAttrName = ((ConvertAttr) annotation)
                                .srcAttrName();

                        Field srcField = srcInstance.getClass()
                                .getDeclaredField(srcAttrName);
                        String srcFieldName = srcField.getName();
                        String string1 = srcFieldName.substring(0, 1)
                                .toUpperCase() + srcFieldName.substring(1);
                        String getMethodName = "get" + string1;
                        Method getMethod = srcInstance.getClass()
                                .getDeclaredMethod(getMethodName);
                        Object value = getMethod.invoke(srcInstance);

                        String targetFieldName = targetField.getName();
                        String string2 = targetFieldName.substring(0, 1)
                                .toUpperCase() + targetFieldName.substring(1);
                        String setMethodName = "set" + string2;
                        Class<?> targetType = targetField.getType();
                        Class<?> srcType = srcField.getType();
                        // 源字段和目标字段类型一致，直接转换
                        if (targetType.equals(srcType)) {
                            // Object cast = targetType.cast(value);
                            Method setMethod = newInstance.getClass()
                                    .getDeclaredMethod(setMethodName,
                                            targetType);
                            setMethod.invoke(newInstance, value);
                        } else {
                            // 源字段和目标字段类型不一致，先判断是否可以转换
                            if (targetType.equals(String.class)) {// 目标字段是String类型
                                String valueOf = String.valueOf(value);
                                Method setMethod = newInstance.getClass()
                                        .getDeclaredMethod(setMethodName,
                                                String.class);
                                setMethod.invoke(newInstance, valueOf);
                            } else if (targetType.equals(Long.class)) {// 目标类型是Long类型
                                if (srcType.equals(String.class)) {
                                    Long valueOf = Long.valueOf(String
                                            .valueOf(value));
                                    Method setMethod = newInstance.getClass()
                                            .getDeclaredMethod(setMethodName,
                                                    Long.class);
                                    setMethod.invoke(newInstance, valueOf);
                                } else if (srcType.equals(long.class)) {
                                    Long valueOf = Long.valueOf(String
                                            .valueOf(value));
                                    Method setMethod = newInstance.getClass()
                                            .getDeclaredMethod(setMethodName,
                                                    Long.class);
                                    setMethod.invoke(newInstance, valueOf);
                                } else if (srcType.equals(int.class)) {
                                    Long valueOf = Long.valueOf(String
                                            .valueOf(value));
                                    Method setMethod = newInstance.getClass()
                                            .getDeclaredMethod(setMethodName,
                                                    Long.class);
                                    setMethod.invoke(newInstance, valueOf);
                                }

                            } else if (targetType.equals(Integer.class)) {// 目标类型是Integer类型
                                if (srcType.equals(String.class)) {
                                    Integer valueOf = Integer.valueOf(String
                                            .valueOf(value));
                                    Method setMethod = newInstance.getClass()
                                            .getDeclaredMethod(setMethodName,
                                                    Integer.class);
                                    setMethod.invoke(newInstance, valueOf);
                                } else if (srcType.equals(int.class)) {
                                    Integer valueOf = Integer.valueOf(String
                                            .valueOf(value));
                                    Method setMethod = newInstance.getClass()
                                            .getDeclaredMethod(setMethodName,
                                                    Integer.class);
                                    setMethod.invoke(newInstance, valueOf);
                                }
                            }
                        }
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return newInstance;
    }

    /**
     * The class <code>ConvertAttr</code>
     * <p/>
     * 属性转换标签
     *
     * @author lizhuo_a
     * @version 1.0
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConvertAttr {

        /**
         * 源属性名称
         */
        String srcAttrName();
    }

}
