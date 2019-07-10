package com.aorise.weeklyreport.network;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/26.
 */
public class Result<T> {
    private String msg;
    private boolean ret;
    private int code;
    private String message;
    private T data;

    @Override
    public String toString() {
        return "Result{" +
                "msg='" + msg + '\'' +
                ", ret=" + ret +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
