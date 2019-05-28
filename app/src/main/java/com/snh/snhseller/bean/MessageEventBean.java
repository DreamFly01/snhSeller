package com.snh.snhseller.bean;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/28<p>
 * <p>changeTime：2019/5/28<p>
 * <p>version：1<p>
 */
public class MessageEventBean {
    private String message;

    public MessageEventBean(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
