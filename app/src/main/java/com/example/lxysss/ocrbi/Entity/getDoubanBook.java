package com.example.lxysss.ocrbi.Entity;

/**
 * Created by Lxysss on 2019/4/13.
 */

public class getDoubanBook {
    private  String title;
    private  String author;
    private Double rating;
    private Integer votes;
    private String  publisher ;
    private String isbn;
    private String summary;
    private String simage ;
    private String mimage ;
    private String limage ;

    public getDoubanBook(String title, String author, Double rating, Integer votes, String publisher
            , String isbn, String summary, String simage, String mimage, String limage) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.votes = votes;
        this.publisher = publisher;
        this.isbn = isbn;
        this.summary = summary;
        this.simage = simage;
        this.mimage = mimage;
        this.limage = limage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSimage() {
        return simage;
    }

    public void setSimage(String simage) {
        this.simage = simage;
    }

    public String getMimage() {
        return mimage;
    }

    public void setMimage(String mimage) {
        this.mimage = mimage;
    }

    public String getLimage() {
        return limage;
    }

    public void setLimage(String limage) {
        this.limage = limage;
    }
}
