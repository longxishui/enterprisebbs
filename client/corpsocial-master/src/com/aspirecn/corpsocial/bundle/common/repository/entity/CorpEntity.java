package com.aspirecn.corpsocial.bundle.common.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Amos on 15-6-17.
 */
@DatabaseTable(tableName = "corp")
public class CorpEntity {

    @DatabaseField(generatedId = true, unique = true)
    private int seqNo;

    @DatabaseField
    private String belongUserId;
    @DatabaseField
    private String corpId;
    @DatabaseField
    private String corpName;
    @DatabaseField
    private long lastModifiedTime;

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
}
