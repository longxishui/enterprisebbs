package com.aspirecn.corpsocial.bundle.im.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "im_group_member")
public class GroupMemberEntity {

    /**
     * 序列号
     */
    @DatabaseField(generatedId = true)
    private int seqId;

    /**
     * 群组ID
     */
    @DatabaseField
    private String groupId;

    /**
     * 群成员ID
     */
    @DatabaseField
    private String memberId;

    /**
     * 群成员名称
     */
    @DatabaseField
    private String memberName;

    @DatabaseField
    private String headImgUrl;

    @DatabaseField
    private String belongUserId;

    /**
     * 微信号状态：0-已开通，1-未开通，2-已激活，3-已关闭
     */
    @DatabaseField
    private String status;

    @DatabaseField
    private Date createTime;

    /**
     * 拼音首字母
     */
    @DatabaseField(index = true, indexName = "IDX_INITIALCODE")
    private String initialCode;

    private boolean canCommunicate;

    @DatabaseField
    private String cellphone;

    @DatabaseField
    private String spellKey;

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getSpellKey() {
        return spellKey;
    }

    public void setSpellKey(String spellKey) {
        this.spellKey = spellKey;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getInitialCode() {
        return initialCode;
    }

    public void setInitialCode(String initialCode) {
        this.initialCode = initialCode;
    }

    public boolean isCanCommunicate() {
        return canCommunicate;
    }

    public void setCanCommunicate(boolean canCommunicate) {
        this.canCommunicate = canCommunicate;
    }
}
