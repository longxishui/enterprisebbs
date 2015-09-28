package com.aspirecn.corpsocial.bundle.im.facade;


public class Group {

    /**
     * 群组ID
     */
    private String groupId;

    /**
     * 群名称
     */
    private String name;

    private String description;

    /**
     * 群组类型
     */
    private int groupType;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
