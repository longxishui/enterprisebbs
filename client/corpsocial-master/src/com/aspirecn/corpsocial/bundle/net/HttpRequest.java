package com.aspirecn.corpsocial.bundle.net;

import com.aspirecn.corpsocial.common.config.PropertyInfo;

import java.io.File;
import java.util.List;

/**
 * Created by duyz on 15/7/9.
 */
public class HttpRequest {

    public static void request(String businessId, String data, HttpCallBack callBack) {
        request(businessId, data, null, callBack);
    }

    public static void request(String businessId, String data,boolean allowRetry, HttpCallBack callBack) {
        request(businessId, data, null, callBack);
    }

    public static void request(String businessId,String data,List<File> files,final HttpCallBack callBack) {
        String url = PropertyInfo.getInstance().getString("http_url");
        ReqParameter reqParameter = new ReqParameter();
        reqParameter.setReqType(businessId);
        reqParameter.setJsonData(data);
        reqParameter.setUrl(url);
        reqParameter.setFiles(files);
        HttpReq.getInstance().req(reqParameter,callBack);
//        IMNetClientImpl.getIMNetClient().reqHttpPort(url, businessId, data, fileList, allowFailRetry, callBack);
    }

}
