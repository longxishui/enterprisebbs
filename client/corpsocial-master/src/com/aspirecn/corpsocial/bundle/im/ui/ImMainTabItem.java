package com.aspirecn.corpsocial.bundle.im.ui;

import java.util.Date;

/**
 * @author lizhuo_a
 */
public class ImMainTabItem {

    private ItemType itemType;
    /**
     * 标题内容
     */
    private String title;
    /**
     * 简介
     */
    private String intro;
    /**
     * 头像地址
     */
    private String headImg;
    /**
     * 未读条目数
     */
    private int unReadCount = 0;
    /**
     * 显示时间
     */
    private Date time;
    private String itemId;
    /**
     * 对象，
     */
    private Object itemObj;
    private boolean isSetMoveToTop;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public Object getItemObj() {
        return itemObj;
    }

    public void setItemObj(Object itemObj) {
        this.itemObj = itemObj;
    }

    public boolean isSetMoveToTop() {
        return isSetMoveToTop;
    }

    public void setMoveToTop(boolean isSetMoveToTop) {
        this.isSetMoveToTop = isSetMoveToTop;
    }

    enum ItemType {
        P2PChat,
        CustomGroupChat,
        CorpGroupChat,
        News,
        Teleconference,
        PubAccountCommonChat,//普通公众号回话
        PubAccountServiceChat,//服务公众号回话
        Other
    }
}
