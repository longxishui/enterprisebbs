package com.aspirecn.corpsocial.bundle.im.repository.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 群通知
 *
 * @author lizhuo_a
 */
@DatabaseTable(tableName = "im_group_notice")
public class GroupNoticeEntity {

    @DatabaseField(generatedId = true)
    private int seqNo;

    @DatabaseField
    private String groupId;

    @DatabaseField
    private String content;

    /**
     * 通知发送者ID
     */
    @DatabaseField
    private String informSenderId;

    @DatabaseField
    private String belongUserId;

    // 创建时间
    @DatabaseField(dataType = DataType.DATE)
    private Date createTime;

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInformSenderId() {
        return informSenderId;
    }

    public void setInformSenderId(String informSenderId) {
        this.informSenderId = informSenderId;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
