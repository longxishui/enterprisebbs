package com.aspirecn.corpsocial.bundle.common.domain;

/**
 * Created by yinghuihong on 15/5/26.
 * <p/>
 * 视图参数定义
 */
public class ViewDef {

    public String backgroundColor;
    public String fontColor;
    public String fontName;
    public String fontSize;

    @Override
    public String toString() {
        return "TopViewDef{" +
                "backgroundColor='" + backgroundColor + '\'' +
                ", fontColor='" + fontColor + '\'' +
                ", fontName='" + fontName + '\'' +
                ", fontSize='" + fontSize + '\'' +
                '}';
    }
}
