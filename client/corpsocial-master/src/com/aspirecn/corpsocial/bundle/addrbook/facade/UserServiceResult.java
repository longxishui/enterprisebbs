package com.aspirecn.corpsocial.bundle.addrbook.facade;

/**
 * Created by Amos on 15-6-18.
 */
public class UserServiceResult<T> {

    private int errorCode;

    private T data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
