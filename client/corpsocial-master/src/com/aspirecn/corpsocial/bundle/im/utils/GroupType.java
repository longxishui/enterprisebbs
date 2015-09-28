package com.aspirecn.corpsocial.bundle.im.utils;

/**
 * Created by duyinzhou on 2015/9/14.
 */
public enum  GroupType {
    GROUP(0),
    DEPRT(1),
    DEPT(2);
    int value;
    private GroupType(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
