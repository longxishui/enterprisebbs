/**
 * 
 */
package com.aspirecn.corpsocial.bundle.net;

import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * @author duyinzhou
 *
 */
public class HttpReq {
	private static HttpReq httpReq;
	public static HttpReq getInstance(){
		if(httpReq==null){
			httpReq = new HttpReq();
		}
			return httpReq;
	}
    /**
     *            要上传的文件路径
     * @param url
     *            服务端接收URL
     * @throws Exception
     */
    public static void uploadFile(ReqParameter reqParameter,String url, final HttpCallBack httpCallBack) throws Exception {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if (reqParameter.getFile()!=null&&reqParameter.getFile().length()>0) {
            params.put("attach", reqParameter.getFile());
        }
        JSONObject jsonObject = new JSONObject(reqParameter.getJsonData());
            String userId = "";
            String corpId = "";
            String veritify = "";
            if (jsonObject.has("userId")) {
                userId = jsonObject.getString("userId");
            }
            if (jsonObject.has("corpId")) {
                corpId = jsonObject.getString("corpId");
            }
            if (jsonObject.has("veritify")) {
                veritify = jsonObject.getString("veritify");
            }
            params.put("userId", userId);
            params.put("corpId", corpId);
            params.put("veritify", veritify);
            params.put("data", reqParameter.getJsonData());
            params.put("bussinessId", reqParameter.getReqType());
            // 上传文件
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    // 上传成功后要做的工作
//                    progress.setProgress(0);
                    String strResult = "";
                    boolean notify = false;
                   for(Header header:headers){
                       //取出回应字串
                       if (header.getName().equals("Content-Encoding") && "gzip".equals(header.getValue())) {
                           strResult = GzipZiper.getInstance().unZipBytes(responseBody, "UTF-8");
                           httpCallBack.notifyResult(ErrorCode.SUCCESS.getValue(), strResult);
                           notify = true;
                           break;
                       }
                   }
                    if(!notify){
                        httpCallBack.notifyResult(ErrorCode.SUCCESS.getValue(), new String(responseBody));
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    // 上传失败后要做到工作
//                    Toast.makeText(mContext, "上传失败", Toast.LENGTH_LONG).show();
                    httpCallBack.notifyResult(ErrorCode.TIMEOUT.getValue(),"发送失败");
                }

                @Override
                public void onProgress(int bytesWritten, int totalSize) {
                    // TODO Auto-generated method stub
                    super.onProgress(bytesWritten, totalSize);
                    int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                    // 上传进度显示
//                    progress.setProgress(count);
//                    Log.e("上传 Progress>>>>>", bytesWritten + " / " + totalSize);
                }
                @Override
                public void onRetry() {
                    // TODO Auto-generated method stub
                    super.onRetry();
                }
            });

    }

    public void req(final ReqParameter reqParameter, final HttpCallBack httpCallBack) {
        try {
            uploadFile(reqParameter,reqParameter.getUrl(),httpCallBack);
        } catch (Exception e) {
            e.printStackTrace();
            httpCallBack.notifyResult(ErrorCode.NETWORK_FAILED.getValue(),"上传异常");
        }
    }
}
