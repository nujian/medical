package com.care.exception.base;

/**
 * Created by nujian on 16/2/18.
 */
public class CareException extends Exception {

    protected int code;

    private String errorBody = "";

    private String memo;

    public CareException() {}

    public String getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CareException(int code) {
        this.code = code;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public CareException(String message, int code, String errorBody) {
        super(message);
        this.code = code;
        this.errorBody = errorBody;
    }

    public CareException(String message, int code) {
        super(message);
        this.code = code;
    }

    public CareException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public CareException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public CareException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }



}
