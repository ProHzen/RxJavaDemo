package com.bbk.yang.rxjavademo;

/**
 * Created by yang on 2017/3/23.
 */

public class HttpResult<T> {
    private int resultCode;
    private String resultMessage;

    private T data;

    public int getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public T getData() {
        return data;
    }

}
