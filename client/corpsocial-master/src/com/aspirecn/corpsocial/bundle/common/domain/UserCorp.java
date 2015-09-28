package com.aspirecn.corpsocial.bundle.common.domain;

import com.aspirecn.corpsocial.bundle.addrbook.domain.UserDept;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Amos on 15-6-16.
 * 用户所属企业
 */
public class UserCorp implements Serializable {

    private static final long serialVersionUID = -674624358696975018L;

    private String userId;
    /**
     * 企业标示
     */
    private String corpId;
    /**
     * 企业名称
     */
    private String corpName;
    /**
     * 外线电话
     */
    private String telephone;
    /**
     * 手机短号
     */
    private String innerPhone;
    /**
     * 办公地点
     */
    private String location;
    /**
     *
     */
    private String email;
    /**
     * 是否标星
     */
    private int isStar;
    /**
     * 排序
     */
    private String sortNo;
    /**
     * oa登录名
     */
    private String loginName;
    /**
     * 状态 0正常 1已删除
     */
    private String status;
    /**
     * 0正式员工 1外包人员 2兼职
     */
    private String userType;
    /**
     * 是否显示手机号码 0显示 1隐藏
     */
    private int isShow;
    /**
     * 所属部门，第一个为主部门
     */
   private List<OtherAccount> otherAccountList;
    private List<UserDept> deptList;

    private UserDept mainDept;

    private long lastModifiedTime;

    private long userLastModifiedTime;

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public long getUserLastModifiedTime() {
        return userLastModifiedTime;
    }

    public void setUserLastModifiedTime(long userLastModifiedTime) {
        this.userLastModifiedTime = userLastModifiedTime;
    }

    public UserDept getMainDept() {
        return mainDept;
    }

    public void setMainDept(UserDept mainDept) {
        this.mainDept = mainDept;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getInnerPhone() {
        return innerPhone;
    }

    public void setInnerPhone(String innerPhone) {
        this.innerPhone = innerPhone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsStar() {
        return isStar;
    }

    public void setIsStar(int isStar) {
        this.isStar = isStar;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public List<UserDept> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<UserDept> deptList) {
        this.deptList = deptList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<OtherAccount> getOtherAccountList() {
        return otherAccountList;
    }

    public void setOtherAccountList(List<OtherAccount> otherAccountList) {
        this.otherAccountList = otherAccountList;
    }

    public String toString() {
        Field[] fields = this.getClass().getDeclaredFields();
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < fields.length; i++) {
                builder.append(fields[i].getName() + ":" + fields[i].get(this)).append(" ");
            }
        } catch (Exception e) {
        }

        return builder.toString();
    }

}
