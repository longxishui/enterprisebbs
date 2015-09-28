package com.aspirecn.corpsocial.bundle.addrbook.domain;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Amos on 15-6-16.
 * 用户所属部门
 */
public class UserDept implements Serializable {

    private static final long serialVersionUID = -674624358696975016L;

    private String code;

    private String name;

    private String status;

    private String duty;

    private String deptId;

    private Dept dept;

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
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
