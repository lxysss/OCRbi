package com.example.lxysss.ocrbi.Entity;

import android.content.Intent;

/**
 * Created by Lxysss on 2019/4/21.
 */

public class Rank {
  private Integer code;
    private String msg;
    private Integer rank;

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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
