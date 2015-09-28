package com.aspirecn.corpsocial.bundle.common.domain;

import java.util.List;

/**
 * Created by yinghuihong on 15/5/21.
 * <p/>
 * 工作台APP分组定义
 */
public class WorkAreaDef {
    /**
     * 分组名称
     */
    public String groupName;
    /**
     * 排序，升序排列，越小越前(也即groupNo)
     */
    public int sortNo;
    /**
     * 应用列表
     */
    public List<AppInfo> appList;

    @Override
    public String toString() {
        return "AppGroupDef{" +
                "groupName='" + groupName + '\'' +
                ", sortNo='" + sortNo + '\'' +
                ", appList=" + appList +
                '}';
    }
}
