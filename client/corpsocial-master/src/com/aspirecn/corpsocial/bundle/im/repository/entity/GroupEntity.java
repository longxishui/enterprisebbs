package com.aspirecn.corpsocial.bundle.im.repository.entity;

import com.aspirecn.corpsocial.common.util.LogUtil;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.trinea.android.common.util.JSONUtils;

@DatabaseTable(tableName = "im_group")
public class GroupEntity implements Serializable {

    private static final long serialVersionUID = 6257983045542182551L;

    /**
     * 序列号
     */
    @DatabaseField(generatedId = true)
    private int seqId;

    /**
     * 群组ID
     */
    @DatabaseField
    private String groupId;

    /**
     * 群名称
     */
    @DatabaseField
    private String name;

    /**
     * 群描述
     */
    @DatabaseField
    private String description;

    /**
     * 创建者
     */
    @DatabaseField
    private String creator;

    /**
     * 群组类型
     */
    @DatabaseField
    private int groupType;

    /**
     * 群成员列表
     */
    @DatabaseField
    private String memberList;

    /**
     * 管理员列表
     */
    @DatabaseField
    private String adminList;

    /**
     * 允许发言的用户列表
     */
    @DatabaseField
    private String speekList;

    /**
     * 发言限制类型：0-不限制，1-只限制部分人发言
     */
    @DatabaseField
    private String limitType;
    /**
     * 会话所属用户Id，适应多用户场景
     */
    @DatabaseField
    private String belongUserId;
    @DatabaseField(dataType = DataType.DATE)
    private Date createTime;
    @DatabaseField(dataType = DataType.DATE)
    private Date lastUpdateTime;

    public GroupEntity() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public List<String> getMemberList() {
        List<String> asList = new ArrayList<String>();

        JSONArray array = JSONUtils.getJSONArray(memberList, "memberList", null);
        try {
            int length = array.length();
            for (int i = 0; i < length; i++) {
                String string = array.getString(i);
                asList.add(string);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return asList;
    }

    public void setMemberList(List<String> memberList) {
        JSONArray array = new JSONArray();

        for (String member : memberList) {
            array.put(member);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberList", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String string = jsonObject.toString();
        this.memberList = string;
    }

    public List<String> getAdminList() {
        List<String> asList = new ArrayList<String>();

        JSONArray array = JSONUtils.getJSONArray(adminList, "adminList", null);

        try {
            if (array != null) {
                int length = array.length();
                for (int i = 0; i < length; i++) {
                    String string = array.getString(i);
                    asList.add(string);
                }
            }
        } catch (JSONException e) {
            LogUtil.e("解析管理员列表失败", e);
        }

        return asList;
    }

    public void setAdminList(List<String> adminList) {
        JSONArray array = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        for (String member : adminList) {
            array.put(member);
        }
        try {
            jsonObject.put("adminList", array);
        } catch (JSONException e) {
            LogUtil.e("保存管理员列表失败", e);
        }
        String string = jsonObject.toString();
        this.adminList = string;

    }

    public List<String> getSpeekList() {

        List<String> asList = new ArrayList<String>();

        JSONArray array = JSONUtils.getJSONArray(speekList, "speekList", null);
        try {
            if (array != null) {
                int length = array.length();
                for (int i = 0; i < length; i++) {
                    String string = array.getString(i);
                    asList.add(string);
                }
            }

        } catch (JSONException e) {
            LogUtil.e("解析发言者列表失败", e);
        }

        return asList;
    }

    public void setSpeekList(List<String> speekList) {
        JSONArray array = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        for (String member : speekList) {
            array.put(member);
        }
        try {
            jsonObject.put("speekList", array);
        } catch (JSONException e) {
            LogUtil.e("保存发言者列表失败", e);
        }
        String string = jsonObject.toString();
        this.speekList = string;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public interface LimitType {
        String Limit = "1";
        String Unlimit = "0";
    }

}
