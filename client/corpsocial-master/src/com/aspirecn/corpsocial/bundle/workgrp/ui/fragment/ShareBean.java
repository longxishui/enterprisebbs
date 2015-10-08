package com.aspirecn.corpsocial.bundle.workgrp.ui.fragment;

/**
 * Created by duyinzhou on 2015/9/1.
 */
public class ShareBean {
    public static final int WEIXIN_PY = 0;
    public static final int WEIXIN_PYQ = 1;
    public static final int QQ = 2;
    public static final int QQ_ZONE = 3;

    public ShareBean(){

    }

    public ShareBean(String name, int shareApp) {
        this.name = name;
        this.shareApp = shareApp;
    }

    private String name;
    private int shareApp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShareApp() {
        return shareApp;
    }

    public void setShareApp(int shareApp) {
        this.shareApp = shareApp;
    }
}
