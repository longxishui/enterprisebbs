package com.aspirecn.corpsocial.bundle.im.domain;

public class SearchItem {

    private String id;
    private SearchType type;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SearchType getType() {
        return type;
    }

    public void setType(SearchType type) {
        this.type = type;
    }

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

    public enum SearchType {
        P2PChat, CustomGroupChat, CorpGroupChat
    }

}
