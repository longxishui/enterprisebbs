/**
 * 
 */
package com.aspirecn.corpsocial.bundle.net;

import java.io.File;
import java.util.List;

/**
 * @author duyinzhou
 *
 */
public class ReqParameter {
	private String url;
	private String jsonData;
	private String reqType;
    private List<File> files;
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

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
}
