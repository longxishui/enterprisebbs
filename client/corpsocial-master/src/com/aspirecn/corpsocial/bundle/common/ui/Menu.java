package com.aspirecn.corpsocial.bundle.common.ui;

/**
 * 底部导航栏应用
 * <p/>
 * Created by yinghuihong on 15/8/19.
 */
public class Menu implements Comparable<Menu> {
    int sortNo;
    String startPath;
    String fontResId;
    String appName;
    String cls;

    @Override
    public int compareTo(Menu menu) {
        return Integer.valueOf(sortNo).compareTo(menu.sortNo);
    }
}
