package com.aspirecn.corpsocial.bundle.common.domain;

/**
 * Created by duyz on 15/8/25.
 * <p/>
 * 第三方账户信息
 */
public class OtherAccount {
    public String id;
    public String account;
    public String type;
    public String passwd;
    public String corpId;
    public String status;
    public OtherData otherData;

    @Override
    public String toString() {
        return "OtherAccount{" +
                "id='" + id + '\'' +
                ", account='" + account + '\'' +
                ", type='" + type + '\'' +
                ", passwd='" + passwd + '\'' +
                ", corpId='" + corpId + '\'' +
                ", status='" + status + '\'' +
                ", otherData=" + otherData +
                '}';
    }
}
