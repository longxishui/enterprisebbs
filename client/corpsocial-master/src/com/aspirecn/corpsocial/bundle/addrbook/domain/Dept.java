/**
 * @(#) Depart.java Created on 2013-11-25
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.domain;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * The class <code>Depart</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class Dept implements Serializable {

//	private static final long serialVersionUID = -528546919868560675L;
//
//	/** 标识 */
//	private Long ids;
//	/** 所属企业 */
//	private Long corpIds;
//	/** 部门编码(每级4位) */
//	private String deptCode;
//	/** 上级部门标识 */
//	private Long pIds;
//	/** 部门名称 */
//	private String name;
//	/** 部门全称（以/分隔） */
//	private String fullName;
//	/** 高分辨率显示（以/分隔） */
//	private String highResolutionShow;
//	/** 中等分辨率显示（以/分隔） */
//	private String middleResolutionShow;
//	/** 低分辨率显示（以/分隔） */
//	private String lowResolutionShow;
//	/** 删除标记 */
//	private Integer deleted;
//	/** 操作员 */
//	private Long userIds;
//	/** 入库日期 */
//	private Date indate;
//
//	/**
//	 * @return the 标识
//	 */
//	public Long getIds() {
//		return ids;
//	}
//
//	/**
//	 * @param 标识
//	 *            the ids to set
//	 */
//	public void setIds(Long ids) {
//		this.ids = ids;
//	}
//
//	/**
//	 * @return the 所属企业
//	 */
//	public Long getCorpIds() {
//		return corpIds;
//	}
//
//	/**
//	 * @param 所属企业
//	 *            the corpIds to set
//	 */
//	public void setCorpIds(Long corpIds) {
//		this.corpIds = corpIds;
//	}
//
//	/**
//	 * @return the 部门编码(每级4位)
//	 */
//	public String getDeptCode() {
//		return deptCode;
//	}
//
//	/**
//	 * @param 部门编码
//	 *            (每级4位) the deptCode to set
//	 */
//	public void setDeptCode(String deptCode) {
//		this.deptCode = deptCode;
//	}
//
//	/**
//	 * @return the 上级部门标识
//	 */
//	public Long getpIds() {
//		return pIds;
//	}
//
//	/**
//	 * @param 上级部门标识
//	 *            the pIds to set
//	 */
//	public void setpIds(Long pIds) {
//		this.pIds = pIds;
//	}
//
//	/**
//	 * @return the 部门名称
//	 */
//	public String getName() {
//		return name;
//	}
//
//	/**
//	 * @param 部门名称
//	 *            the name to set
//	 */
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	/**
//	 * @return the 部门全称（以分隔）
//	 */
//	public String getFullName() {
//		return fullName;
//	}
//
//	/**
//	 * @param 部门全称
//	 *            （以分隔） the fullName to set
//	 */
//	public void setFullName(String fullName) {
//		this.fullName = fullName;
//	}
//
//	/**
//	 * @return the 高分辨率显示（以分隔）
//	 */
//	public String getHighResolutionShow() {
//		return highResolutionShow;
//	}
//
//	/**
//	 * @param 高分辨率显示
//	 *            （以分隔） the highResolutionShow to set
//	 */
//	public void setHighResolutionShow(String highResolutionShow) {
//		this.highResolutionShow = highResolutionShow;
//	}
//
//	/**
//	 * @return the 中等分辨率显示（以分隔）
//	 */
//	public String getMiddleResolutionShow() {
//		return middleResolutionShow;
//	}
//
//	/**
//	 * @param 中等分辨率显示
//	 *            （以分隔） the middleResolutionShow to set
//	 */
//	public void setMiddleResolutionShow(String middleResolutionShow) {
//		this.middleResolutionShow = middleResolutionShow;
//	}
//
//	/**
//	 * @return the 低分辨率显示（以分隔）
//	 */
//	public String getLowResolutionShow() {
//		return lowResolutionShow;
//	}
//
//	/**
//	 * @param 低分辨率显示
//	 *            （以分隔） the lowResolutionShow to set
//	 */
//	public void setLowResolutionShow(String lowResolutionShow) {
//		this.lowResolutionShow = lowResolutionShow;
//	}
//
//	/**
//	 * @return the 删除标记
//	 */
//	public Integer getDeleted() {
//		return deleted;
//	}
//
//	/**
//	 * @param 删除标记
//	 *            the deleted to set
//	 */
//	public void setDeleted(Integer deleted) {
//		this.deleted = deleted;
//	}
//
//	/**
//	 * @return the 操作员
//	 */
//	public Long getUserIds() {
//		return userIds;
//	}
//
//	/**
//	 * @param 操作员
//	 *            the userIds to set
//	 */
//	public void setUserIds(Long userIds) {
//		this.userIds = userIds;
//	}
//
//	/**
//	 * @return the 入库日期
//	 */
//	public Date getIndate() {
//		return indate;
//	}
//
//	/**
//	 * @param 入库日期
//	 *            the indate to set
//	 */
//	public void setIndate(Date indate) {
//		this.indate = indate;
//	}

    /**
     * 标识
     */
    private String deptId;
    /**
     * 所属企业
     */
    private String corpId;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 全称
     */
    private String fullName;
    /**
     * 上级编码 最上级为0
     */
    private String pcode;
    /**
     * 状态  0正常 1删除
     */
    private String status;
    /**
     * 0分末端节点，1末端节点
     */
    private String leaf;
    /**
     * 0为行业协会  1普通企业
     */
    private String corpType;
    /**
     * 排序
     */
    private String sortNo;
    /**
     * 最后更新时间
     */
    private long lastModifiedTime;

    private int isMainDept;

    private int level;

    public int getLevel() {
        int count = 0;
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == '.') {
                count++;
            }
        }
        return count;
    }


    public int getIsMainDept() {
        return isMainDept;
    }

    public void setIsMainDept(int isMainDept) {
        this.isMainDept = isMainDept;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaf() {
        return leaf;
    }

    public void setLeaf(String leaf) {
        this.leaf = leaf;
    }

    public String getCorpType() {
        return corpType;
    }

    public void setCorpType(String corpType) {
        this.corpType = corpType;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
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
