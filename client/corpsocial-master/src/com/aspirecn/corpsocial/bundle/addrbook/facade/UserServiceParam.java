package com.aspirecn.corpsocial.bundle.addrbook.facade;

import java.util.Map;

/**
 * Created by Amos on 15-6-18.
 */
public class UserServiceParam {

    private String servie;

    private Map params;

    public UserServiceParam() {

    }

    public UserServiceParam(String service, Map params) {
        this.servie = service;
        this.params = params;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public String getServie() {
        return servie;
    }

    public void setServie(String servie) {
        this.servie = servie;
    }
}
