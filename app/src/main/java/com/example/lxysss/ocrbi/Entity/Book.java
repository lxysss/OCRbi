package com.example.lxysss.ocrbi.Entity;

/**
 * Created by Lxysss on 2019/4/6.
 */

public class Book {
    private Integer bookid;
    private String bookpic;
    private String bookname;

    public Book(Integer bookid, String bookpic, String bookname) {
        this.bookid = bookid;
        this.bookpic = bookpic;
        this.bookname = bookname;
    }

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public String getBookpic() {
        return bookpic;
    }

    public void setBookpic(String bookpic) {
        this.bookpic = bookpic;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
}
