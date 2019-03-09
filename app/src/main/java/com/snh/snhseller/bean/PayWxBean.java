package com.snh.snhseller.bean;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/8<p>
 * <p>changeTime：2019/3/8<p>
 * <p>version：1<p>
 */
public class PayWxBean {

    public String return_code;
    public String return_msg;
    public String result_code;
    public String trade_type;
    public boolean IsError;
    public String appid;

    public String mch_id;
    public String nonce_str;
    public String sign;
    public String prepay_id;
    public String timespan;
    public String app_sign;
}
