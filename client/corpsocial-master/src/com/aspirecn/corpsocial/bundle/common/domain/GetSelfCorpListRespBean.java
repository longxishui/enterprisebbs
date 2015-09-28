package com.aspirecn.corpsocial.bundle.common.domain;

import com.aspirecn.corpsocial.bundle.addrbook.repository.DepartDao;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinghuihong on 15/5/21.
 * <p/>
 * 获取企业配置信息接口返回数据封装
 */
public class GetSelfCorpListRespBean {
    public int rst;
    public String message;
    public List<UserCorp> data;
//    public static GetSelfCorpListRespBean getInstance() {
//        return new Gson().fromJson(ConfigPersonal.getString(ConfigPersonal.Key.GETLISTCORP_KEY), GetSelfCorpListRespBean.class);
//    }

    @Override
    public String toString() {
        return "GetSelfCorpListRespBean{" +
                "rst='" + rst + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
    public static List<UserCorp> find(String belongUserId) {

        GetSelfCorpListRespBean getSelfCorpListRespBean = new Gson().fromJson(ConfigPersonal.getString(ConfigPersonal.Key.GETLISTCORP_KEY), GetSelfCorpListRespBean.class);
        if (getSelfCorpListRespBean != null) {
            return getSelfCorpListRespBean.data;
        } else {
            LogUtil.e("查询的企业列表信息为空");
            return new ArrayList<UserCorp>();
        }
    }
    public static UserCorp findByCorpId(String corpId) {
        GetSelfCorpListRespBean getSelfCorpListRespBean = new Gson().fromJson(ConfigPersonal.getString(ConfigPersonal.Key.GETLISTCORP_KEY), GetSelfCorpListRespBean.class);
        if (getSelfCorpListRespBean != null) {
            List<UserCorp> userCorpList = getSelfCorpListRespBean.data;
            if (userCorpList != null) {
                for (UserCorp item : userCorpList) {
                    if (item.getCorpId().equals(corpId)) {
                        return item;
                    }
                }
            }
        }
        return null;
    }
    public static void updateLastModifedTime(String corpId, long lastModifiedTime) {
        GetSelfCorpListRespBean getSelfCorpListRespBean = new Gson().fromJson(ConfigPersonal.getString(ConfigPersonal.Key.GETLISTCORP_KEY),GetSelfCorpListRespBean.class);
        if(getSelfCorpListRespBean!=null&&null!=getSelfCorpListRespBean.data){
            for(UserCorp item:getSelfCorpListRespBean.data){
                if(item.getCorpId().equals(corpId)){
                    item.setLastModifiedTime(lastModifiedTime);
                }
            }
            ConfigPersonal.putString(ConfigPersonal.Key.GETLISTCORP_KEY,new Gson().toJson(getSelfCorpListRespBean));
        }
    }

    public static void updateUserLastModifedTime(String corpId, long lastModifiedTime) {
        GetSelfCorpListRespBean getSelfCorpListRespBean = new Gson().fromJson(ConfigPersonal.getString(ConfigPersonal.Key.GETLISTCORP_KEY),GetSelfCorpListRespBean.class);
        if(getSelfCorpListRespBean!=null&&getSelfCorpListRespBean.data!=null){
            for(UserCorp item:getSelfCorpListRespBean.data){
                if(item.getCorpId().equals(corpId)){
                    item.setUserLastModifiedTime(lastModifiedTime);
                }
            }
            ConfigPersonal.putString(ConfigPersonal.Key.GETLISTCORP_KEY,new Gson().toJson(getSelfCorpListRespBean));
        }
    }

    public static void createOrUpdate(final List<UserCorp> corps) {
        GetSelfCorpListRespBean getSelfCorpListRespBean = new GetSelfCorpListRespBean();
        getSelfCorpListRespBean.rst = 0;
        getSelfCorpListRespBean.message = "成功";
        getSelfCorpListRespBean.data = corps;
        for(UserCorp corp : corps){
            if ( "1".equals(corp.getStatus())) {
                try {
                    DepartDao departDao = new DepartDao();
                    departDao.deleteCorpDept(corp.getCorpId());
                } catch (SQLException e) {
                    LogUtil.e("departDao删除corp失败");
                }
            }
        }
        ConfigPersonal.putString(ConfigPersonal.Key.GETLISTCORP_KEY,new Gson().toJson(getSelfCorpListRespBean));
    }

}
