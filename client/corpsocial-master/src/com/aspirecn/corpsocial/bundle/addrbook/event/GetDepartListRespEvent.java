package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.bundle.addrbook.domain.Dept;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * Created by Amos on 15-6-16.
 */
public class GetDepartListRespEvent extends BusEvent {

    private String message;

    private String rst;

    private int errorCode;

    private List<Dept> depts;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRst() {
        return rst;
    }

    public void setRst(String rst) {
        this.rst = rst;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<Dept> getDepts() {
        return depts;
    }

    public void setDepts(List<Dept> depts) {
        this.depts = depts;
    }
}
