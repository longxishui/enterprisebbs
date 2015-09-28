/**
 * 
 */
package com.aspirecn.corpsocial.bundle.net;

import java.io.File;

/**
 * @author duyinzhou
 *
 */
public class ReqParameter {
	private String url;
	private String jsonData;
	private String reqType;
    private File file;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
