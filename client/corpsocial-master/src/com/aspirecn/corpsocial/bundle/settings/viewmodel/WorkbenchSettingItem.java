package com.aspirecn.corpsocial.bundle.settings.viewmodel;

/**
 * Created by chenziqiang on 15-8-27.
 */
public class WorkbenchSettingItem implements Comparable<WorkbenchSettingItem>{
    /**
     * 代号
     */
    public int code;
    /**
     *顺序
     */
    public int number;
    /**
     * 名称
     */
    public String name;
    /**
     * 图片地址
     */
    public String imgSrc;
    /**
     * 跳转类
     */
    public String cls;
    /**
     * 标注
     */
    public String label;
    /**
     * 标注图标
     */
    public String labelImage;
    /**
     * 跳转图标
     */
    public boolean jumpImg;

    /**
     * 是否隐藏
     */
    public boolean hide;

    /**
     *
     */
    public String iconPath;

    @Override
    public int compareTo(WorkbenchSettingItem another) {
        int flag = Integer.valueOf(number).compareTo(another.number);
        if (flag == 0) {
            return Integer.valueOf(number).compareTo(another.number);
        }
        return flag;
    }
}
