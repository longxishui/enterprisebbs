package com.aspirecn.corpsocial.bundle.addrbook.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/***
 * 部门的表
 *
 * @author lc.liweican
 */
@DatabaseTable(tableName = "addrbook_dept")
public class DeptEntity {
    /**
     * 序列号
     */
    @DatabaseField(generatedId = true, unique = true)
    private int seqNo;

    @DatabaseField
    private String id;

    /**
     * 用于多账号识别
     */
    @DatabaseField
    private String belongUserId;

    /**
     * 部门名称
     */
    @DatabaseField
    private String name;

    /**
     * 上级部门ID
     */
    @DatabaseField(index = true, indexName = "IDX_PID")
    private String pid;

    /**
     * 是否叶节点
     */
    @DatabaseField(columnName = "leaf")
    private String leafValue;

    /**
     * 级别，根的级别为0
     */
    @DatabaseField(columnName = "deptLevel")
    private int level = 0;

    /**
     * 部门路径名
     */
    @DatabaseField
    private String deptNamePath;

    @DatabaseField
    private String sortNo;

    /**
     * 服务端的部门ID
     */
    @DatabaseField(index = true, indexName = "IDX_SERVER_ID")
    private String serverDeptId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isLeaf() {
        return leafValue != null && leafValue.equals("1");
    }

    /**
     * 用字符串1表示 true, 0表示false
     *
     * @return
     */
    public String getLeafValue() {
        return leafValue;
    }

    /**
     * 用字符串1表示 true, 0表示false
     *
     * @param leafValue
     */
    public void setLeafValue(String leafValue) {
        this.leafValue = leafValue;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDeptNamePath() {
        return deptNamePath;
    }

    public void setDeptNamePath(String deptNamePath) {
        this.deptNamePath = deptNamePath;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getServerDeptId() {
        return serverDeptId;
    }

    public void setServerDeptId(String serverDeptId) {
        this.serverDeptId = serverDeptId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DeptEntity other = (DeptEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }
}
