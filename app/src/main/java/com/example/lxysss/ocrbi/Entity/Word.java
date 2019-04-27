package com.example.lxysss.ocrbi.Entity;

/**
 * Created by Lxysss on 2019/4/22.
 */

public class Word {
    private Integer code;
    private String word;
    private String gif;
    private String pinyin;
    private String bihua;
    private String bushou;
    private String yisi1;
    private String yisi2;
    private String yisi3;

    public Word(Integer code, String word, String gif, String pinyin, String bihua, String bushou, String yisi1, String yisi2, String yisi3) {
        this.code = code;
        this.word = word;
        this.gif = gif;
        this.pinyin = pinyin;
        this.bihua = bihua;
        this.bushou = bushou;
        this.yisi1 = yisi1;
        this.yisi2 = yisi2;
        this.yisi3 = yisi3;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getBihua() {
        return bihua;
    }

    public void setBihua(String bihua) {
        this.bihua = bihua;
    }

    public String getBushou() {
        return bushou;
    }

    public void setBushou(String bushou) {
        this.bushou = bushou;
    }

    public String getYisi1() {
        return yisi1;
    }

    public void setYisi1(String yisi1) {
        this.yisi1 = yisi1;
    }

    public String getYisi2() {
        return yisi2;
    }

    public void setYisi2(String yisi2) {
        this.yisi2 = yisi2;
    }

    public String getYisi3() {
        return yisi3;
    }

    public void setYisi3(String yisi3) {
        this.yisi3 = yisi3;
    }
}
