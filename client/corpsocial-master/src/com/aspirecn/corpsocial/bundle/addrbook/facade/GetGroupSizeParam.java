package com.aspirecn.corpsocial.bundle.addrbook.facade;

/**
 * 查询指定部门联系人的参数。
 *
 * @author meixuesong
 */
public class GetGroupSizeParam {
    private String deptId;
    private int start;
    private int limit;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * 对应sqlite数据库分页查询的start
     *
     * @return
     */
    public int getStart() {
        return start;
    }

    /**
     * 对应sqlite数据库分页查询的start
     *
     * @return
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * 对应sqlite数据库分页查询的limit
     *
     * @return
     */
    public int getLimit() {
        return limit;
    }

    /**
     * 对应sqlite数据库分页查询的limit
     *
     * @return
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }
}
