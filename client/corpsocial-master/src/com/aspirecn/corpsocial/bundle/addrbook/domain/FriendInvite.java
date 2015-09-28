package com.aspirecn.corpsocial.bundle.addrbook.domain;

import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amos on 15-6-19.
 */
public class FriendInvite {

    /**
     * 本地主键
     */
    private int seqNo;
    /**
     * 邀请方id
     */
    private String userid;
    /**
     * 邀请方用户名
     */
    private String username;
    /**
     * 邀请方公司
     */
    private String corpName;
    /**
     * 状态， 0未接受 1 接受
     */
    private int status;
    /**
     * 创建日期
     */
    private long createTime;

    private String smallUrl;

    private int isNew = 1;

    private String signature;

    private String corpInfo;

    private List<User> corps;

    private User user;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCorpInfo() {
        return corpInfo;
    }

    public void setCorpInfo(String corpInfo) {
        this.corpInfo = corpInfo;
    }

    public List<User> getCorps() {
        JSONArray array = (new Gson()).fromJson(corpInfo, JSONArray.class);
        List<User> users = new ArrayList<User>();
        try {
            for (int i = 0; i < array.length(); i++) {
                String corpId = array.getJSONObject(i).getString("corpId");
                String corpName = array.getJSONObject(i).getString("corpName");
                String duty = array.getJSONObject(i).getString("duty");
                User user = new User();
                user.setCorpId(corpId);
                user.setCorpName(corpName);
                user.setDuty(duty);
                users.add(user);
            }
        } catch (Exception ex) {
            LogUtil.e("解析出错", ex);
        }
        return users;
    }

    public void setCorps(List<User> corps) {
        this.corps = corps;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
