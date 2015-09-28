package com.aspirecn.corpsocial.bundle.addrbook.event.vo;

/**
 * Created by Amos on 15-6-16.
 */
public class CorpModified {

    private String corpId;

    private long lastModifiedTime;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
