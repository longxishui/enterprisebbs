package com.aspirecn.corpsocial.common.eventbus;

/**
 * Created by duyinzhou on 2015/9/10.
 */
public enum ErrorCode {
    SUCCESS(0),
    NETWORK_FAILED(1),
    OTHER_ERROR(2),
    NO_NETWORK(3),
    TIMEOUT(4),
    OVER_GROUP_LIMIT(5),
    OVER_GROUP_NUMBER_LIMIT(6),
    ILLEGAL(7);
    private int code;
    private ErrorCode(int code){
        this.code = code;
    }
    public int getValue(){
        return code;
    }
}
