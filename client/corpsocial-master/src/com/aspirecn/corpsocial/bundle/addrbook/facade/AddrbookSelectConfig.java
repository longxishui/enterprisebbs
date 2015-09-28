package com.aspirecn.corpsocial.bundle.addrbook.facade;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenziqiang on 15-4-7.
 */
public class AddrbookSelectConfig implements Serializable {

    private AddrbookSelectType selectType;//入口类型,单选还是多选
    private boolean selectForMe;//是否选择自己
    private List<String> contactIds;//缓存的联系人
    private List<String> groupIds;//允许选择的部门
    private List<String> existingContactIds;//已经存在的联系人
    private int maxNumber;//最大人数
    private int minNumber;//最小人数
    private boolean expansion;//底部图标是否展开
    private boolean showFriend = true;//是否隐藏好友

    public AddrbookSelectType getSelectType() {
        return selectType;
    }

    public void setSelectType(AddrbookSelectType selectType) {
        this.selectType = selectType;
    }

    public boolean isSelectForMe() {
        return selectForMe;
    }

    public void setSelectForMe(boolean selectForMe) {
        this.selectForMe = selectForMe;
    }

    public List<String> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<String> contactIds) {
        this.contactIds = contactIds;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public List<String> getExistingContactIds() {
        return existingContactIds;
    }

    public void setExistingContactIds(List<String> existingContactIds) {
        this.existingContactIds = existingContactIds;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public int getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(int minNumber) {
        this.minNumber = minNumber;
    }

    public boolean isExpansion() {
        return expansion;
    }

    public void setExpansion(boolean expansion) {
        this.expansion = expansion;
    }

    public boolean isShowFriend() {
        return showFriend;
    }

    public void setShowFriend(boolean showFriend) {
        this.showFriend = showFriend;
    }

    public enum AddrbookSelectType {

        WATCH(0), ONLYSELECT(1), SELECT(2);

        private int value;

        AddrbookSelectType(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }
}
