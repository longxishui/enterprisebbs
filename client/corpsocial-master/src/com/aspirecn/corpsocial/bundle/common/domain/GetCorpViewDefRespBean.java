package com.aspirecn.corpsocial.bundle.common.domain;

/**
 * Created by yinghuihong on 15/5/21.
 * <p/>
 * 获取企业配置信息接口返回数据封装
 */
public class GetCorpViewDefRespBean {
    public int rst;
    public String message;
    public AppConfig data;

    @Override
    public String toString() {
        return "GetCorpViewDefRespBean{" +
                "rst='" + rst + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
