package com.example.lxysss.ocrbi.Entity;

import android.content.Intent;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lxysss on 2019/4/10.
 */

public class Bean  {
    private Integer code;
    private String msg;
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
