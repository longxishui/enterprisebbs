package com.aspirecn.corpsocial.bundle.workgrp.domain;

/**
 * @author chenping
 */
public class BBSPraiseInfo {

    private String userId;
    private long time;

    public BBSPraiseInfo() {
        super();
    }

    public BBSPraiseInfo(String userId, long time) {
        super();
        this.userId = userId;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BBSPraiseInfo [userId=" + userId + ", time=" + time + "]";
    }

}
