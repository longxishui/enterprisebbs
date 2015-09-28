package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * Created by Amos on 15-6-17.
 */
public class GetSelfCorpListRespEvent extends BusEvent {

    private String message;

    private String rst;

    private int errorCode;

    private List<UserCorp> corpList;

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

    public List<UserCorp> getCorpList() {
        return corpList;
    }

    public void setCorpList(List<UserCorp> corpList) {
        this.corpList = corpList;
    }
}
