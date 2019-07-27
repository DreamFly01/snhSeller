package com.snh.module_netapi.requestApi;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public class APIException extends RuntimeException {

    public String code;
    public String msg;
    public String url;

    public APIException(String erroMsg) {
        this.msg = erroMsg;
    }

    public APIException(String erroMsg,String code,String url) {
        this.code =code;
        this.msg = erroMsg;
        this.url = url;
    }
    public APIException(String erroMsg,String code) {
        this.code =code;
        this.msg = erroMsg;
    }
}
