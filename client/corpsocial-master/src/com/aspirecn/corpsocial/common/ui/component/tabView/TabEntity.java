package com.aspirecn.corpsocial.common.ui.component.tabView;

/**
 * Created by duyinzhou on 2015/5/26.
 */
public class TabEntity {
    private String name;
    private int notifyNum;

    public TabEntity() {

    }

    public TabEntity(String name, int notifyNum) {
        this.name = name;
        this.notifyNum = notifyNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNotifyNum() {
        return notifyNum;
    }

    public void setNotifyNum(int notifyNum) {
        this.notifyNum = notifyNum;
    }

}
