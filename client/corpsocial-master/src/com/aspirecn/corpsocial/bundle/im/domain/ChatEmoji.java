package com.aspirecn.corpsocial.bundle.im.domain;

public class ChatEmoji {

    /**
     * 表情资源图片对应的路径
     */
    private String path;

    /**
     * 表情资源图片对应的ID
     */
    private int id;

    /**
     * 表情资源对应的文字描述
     */
    private String character;

    /**
     * 表情资源的文件名
     */
    private String faceName;

    /**
     * 表情资源图片对应的ID
     */
    public int getId() {
        return id;
    }

    /**
     * 表情资源图片对应的ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 表情资源对应的文字描述
     */
    public String getCharacter() {
        return character;
    }

    /**
     * 表情资源对应的文字描述
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     * 表情资源的文件名
     */
    public String getFaceName() {
        return faceName;
    }

    /**
     * 表情资源的文件名
     */
    public void setFaceName(String faceName) {
        this.faceName = faceName;
    }

    /**
     * 表情资源图片对应的路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 表情资源图片对应的路径
     */
    public void setPath(String path) {
        this.path = path;
    }

}
