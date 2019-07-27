package com.snh.module_netapi.requestApi;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public class BaseResultBean<T> {
    public String code;
    public String msg;
    public T data;
    public String filepath;
    public String orderNo;
    public BaseResultBean() {
    }

    public BaseResultBean(String error, String code, T data) {
        this.msg = error;
        this.code = code;
        this.data = data;
    }
}
