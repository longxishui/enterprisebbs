package com.aspirecn.corpsocial.bundle.addrbook.repository.entity;

/**
 * Created by chenziqiang on 15-5-7.
 */
public class CompanyEntity {

    private String id;//ID
    private String name;//名称
    private CompanyType type;//部门还是联系人
    private boolean expanded;//是否展开 展开为true，否则为false
    private String headImgUrl;//头像
    private boolean selector;//是否已经选中 勾选的时候为true，否则为false
    private boolean unSelect;//是否不可以选择 true为不可选择状态，false为可以选择状态
    private String signature;//备注
    private int level;//级别
    private String parentId;//所属部门ID
    private int allContactCount;
    private boolean friend = false;
    private String corpId;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompanyType getType() {
        return type;
    }

    public void setType(CompanyType type) {
        this.type = type;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public boolean isSelector() {
        return selector;
    }

    public void setSelector(boolean selector) {
        this.selector = selector;
    }

    public boolean isUnSelect() {
        return unSelect;
    }

    public void setUnSelect(boolean unSelect) {
        this.unSelect = unSelect;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getAllContactCount() {
        return allContactCount;
    }

    public void setAllContactCount(int allContactCount) {
        this.allContactCount = allContactCount;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public enum CompanyType {
        GROUP(1), CONTACT(0);

        private int value;

        CompanyType(int i) {
            value = i;
        }

        public int getValue() {
            return value;
        }
    }
}
