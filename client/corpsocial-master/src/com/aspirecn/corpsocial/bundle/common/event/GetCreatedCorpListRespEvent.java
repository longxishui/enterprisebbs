package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.bundle.common.domain.Corp;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * Created by Amos on 15-6-16.
 */
public class GetCreatedCorpListRespEvent extends BusEvent {

    private String message;

    private String rst;

    private int errorCode;

    private List<Corp> corpList;

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

    public List<Corp> getCorpList() {
        return corpList;
    }

    public void setCorpList(List<Corp> corpList) {
        this.corpList = corpList;
    }
}
