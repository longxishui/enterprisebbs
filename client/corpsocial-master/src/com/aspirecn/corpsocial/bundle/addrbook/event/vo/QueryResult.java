package com.aspirecn.corpsocial.bundle.addrbook.event.vo;

/**
 * Created by Amos on 15-6-17.
 */
public class QueryResult<T> {

    private T result;

    private int errorCode;

    private String message;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
