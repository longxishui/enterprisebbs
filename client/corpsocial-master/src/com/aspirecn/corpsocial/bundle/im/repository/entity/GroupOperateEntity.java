package com.aspirecn.corpsocial.bundle.im.repository.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 群组消息记录表
 *
 * @author lizhuo_a
 */
@DatabaseTable(tableName = "im_group_operate")
public class GroupOperateEntity {

    @DatabaseField(generatedId = true)
    private int seqNo;

    @DatabaseField
    private String groupId;

    /**
     * 通知类型
     */
    // 创建群
    // 邀请成员加入群
    // 接收到邀请后反馈
    // 申请加入群
    // 接受到加入群组申请后反馈
    // 踢出群成员
    // 解散群
    @DatabaseField
    private int operateType;
    /**
     * 操作状态
     */
    @DatabaseField
    private int status;
    @DatabaseField
    private String content;
    /**
     * 通知发送者ID
     */
    @DatabaseField
    private String noticeSenderId;
    @DatabaseField
    private String belongUserId;
    // 创建时间
    @DatabaseField(dataType = DataType.DATE)
    private Date createTime;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNoticeSenderId() {
        return noticeSenderId;
    }

    public void setNoticeSenderId(String noticeSenderId) {
        this.noticeSenderId = noticeSenderId;
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

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public enum GroupOperateStatus {
        HANDING(0), SUCCESS(1), FAIL(2);

        public int value;

        GroupOperateStatus(int value) {
            this.value = value;
        }

    }

}
