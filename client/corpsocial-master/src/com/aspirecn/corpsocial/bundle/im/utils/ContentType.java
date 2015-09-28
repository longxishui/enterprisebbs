package com.aspirecn.corpsocial.bundle.im.utils;

/**
 * Created by duyinzhou on 2015/9/14.
 */
public enum ContentType {
    TEXT(0),
    SYSTEM(1),
    VOICE(2),
    PICTURE(3),
    POSITION(4),
    VIDIO(5),
    FILE(6);
    private int value;
    private ContentType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
