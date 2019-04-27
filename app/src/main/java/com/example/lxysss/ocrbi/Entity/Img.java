package com.example.lxysss.ocrbi.Entity;

/**
 * Created by Lxysss on 2019/4/25.
 */

public class Img {
    Integer code;
    String msg;
    String print;
    String len;

    public Img(Integer code, String msg, String print, String len) {
        this.code = code;
        this.msg = msg;
        this.print = print;
        this.len = len;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public String getLen() {
        return len;
    }

    public void setLen(String len) {
        this.len = len;
    }
}
