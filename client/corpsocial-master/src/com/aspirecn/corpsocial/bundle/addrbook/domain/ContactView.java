package com.aspirecn.corpsocial.bundle.addrbook.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amos on 15-6-23.
 */
public class ContactView {

    private String corpId;

    private String corpName;

    //0 root(company) , >0 company
    private int level;

    private String deptId;

    private String deptName;

    private String deptCode;

    private List<User> users = new ArrayList<User>();
    //下级
    private List<Dept> depts = new ArrayList<Dept>();

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Dept> getDepts() {
        return depts;
    }

    public void setDepts(List<Dept> depts) {
        this.depts = depts;
    }
}
