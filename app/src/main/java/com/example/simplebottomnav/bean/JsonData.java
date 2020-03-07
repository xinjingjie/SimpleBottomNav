package com.example.simplebottomnav.bean;

import java.io.Serializable;

public class JsonData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    //状态码,0表示成功，-1表示失败
    private int code;

    //结果
    private T data;

    //错误描述
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public JsonData(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public JsonData(int code, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonData() {
    }

    @Override
    public String toString() {
        return "JsonData{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
