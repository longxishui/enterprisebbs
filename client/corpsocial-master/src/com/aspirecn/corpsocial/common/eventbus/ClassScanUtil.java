/**
 * @(#) ClassPathScanHandler.java Created on 2013-11-26
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

import android.content.Context;

import com.aspirecn.corpsocial.common.util.BeanContainer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * The class <code>ClassPathScanHandler</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ClassScanUtil {

    private static final String ERROR_MESSAGE = "packageName can't be null";
    private static final ClassFilter NULL_CLASS_FILTER = null;

    /**
     * 如果packageName为空，就抛出空指针异常。</br>
     * (本工具类有一个bug，如果扫描文件时需要一个包路径为截取字符串的条件，现在还没有修复,所以加上该条件)
     *
     * @param packageName
     */
    private static void ckeckNullPackageName(String packageName) {
        if (packageName == null || packageName.trim().length() == 0)
            throw new NullPointerException(ERROR_MESSAGE);
    }

    /**
     * 扫面包路径下满足class过滤器条件的所有class文件，</br> 如果包路径为 com.abs + A.class 但是输入 abs
     * 会产生classNotFoundException</br> 因为className 应该为 com.abs.A 现在却成为
     * abs.A,此工具类对该异常进行忽略处理,有可能是一个不完善的地方，以后需要进行修改</br>
     *
     * @param packageName 包路径 com | com. | com.abs | com.abs.
     * @param classFilter class过滤器，过滤掉不需要的class
     * @return
     */
    public static List<Class> scanPackage(Context context, String packageName,
                                          ClassFilter classFilter) {
        // 检测packageName 是否为空，如果为空就抛出NullPointException
        ckeckNullPackageName(packageName);
        List<Class> classes = new ArrayList<Class>();
        // 遍历在classpath 下面的jar包，class文件夹(现在没有包括 java jre)
        classes = getClassPathArray(context, packageName, classFilter);

        return classes;
    }

    /**
     * 扫面改包路径下所有class文件
     *
     * @param packageName 包路径 com | com. | com.abs | com.abs.
     * @return
     */
    public static List<Class> scanPackage(Context context, String packageName) {
        return scanPackage(context, packageName, NULL_CLASS_FILTER);
    }

    private static List<Class> getClassPathArray(Context context,
                                                 String packageName, ClassFilter classFilter) {
        List<Class> classes = new ArrayList<Class>();

        DexFile dexfile = null;
        try {
            // AspirenCorpSocial context = AspirenCorpSocial.getContext();
            dexfile = new DexFile(context.getPackageResourcePath());

            Enumeration entries = dexfile.entries();
            while (entries.hasMoreElements()) {
                String name = (String) entries.nextElement();
                if (name.startsWith(packageName)) {
                    Class<?> clazz = Class.forName(name);
                    if (classFilter != NULL_CLASS_FILTER
                            && classFilter.accept(clazz)) {
                        classes.add(clazz);
                    } else if (classFilter == NULL_CLASS_FILTER) {
                        classes.add(clazz);
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                dexfile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return classes;
    }


    public interface ClassFilter {

        boolean accept(Class clazz);

    }

}
