package com.example.lxysss.ocrbi.Entity;

/**
 * Created by Lxysss on 2019/4/12.
 */

public class WordResult {
   Integer code;
   String  msg;
   String msg2;
   Integer  wordnum;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg1) {
        this.msg = msg1;
    }

    public Integer getWordnum() {
        return wordnum;
    }

    public void setWordnum(Integer wordnum) {
        this.wordnum = wordnum;
    }

    public String getMsg2() {
        return msg2;
    }

    public void setMsg2(String msg2) {
        this.msg2 = msg2;
    }
}
