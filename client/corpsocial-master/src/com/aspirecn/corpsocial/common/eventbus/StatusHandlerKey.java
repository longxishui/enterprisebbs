package com.aspirecn.corpsocial.common.eventbus;

public class StatusHandlerKey {
    public ErrorCode errorCode;

    public int status;

    public StatusHandlerKey(ErrorCode errorCode, int status) {
        super();
        this.errorCode = errorCode;
        this.status = status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((errorCode == null) ? 0 : errorCode.hashCode());
        result = prime * result + status;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StatusHandlerKey other = (StatusHandlerKey) obj;
        if (errorCode != other.errorCode)
            return false;
        return status == other.status;
    }

}
