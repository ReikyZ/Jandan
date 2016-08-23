package com.reikyz.jandan.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by reikyZ on 16/8/23.
 */
public class NewsModel implements Serializable {

    Integer id;
    String url;
    String title;
    String date;
    List<TagModel> tags;
    AuthorModel author;
    Integer comment_count;
    CustomModel custom_fields;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public AuthorModel getAuthor() {
        return author;
    }

    public void setAuthor(AuthorModel author) {
        this.author = author;
    }

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    public CustomModel getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(CustomModel custom_fields) {
        this.custom_fields = custom_fields;
    }

    @Override
    public String toString() {
        return "NewsModel{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", tags=" + tags +
                ", author=" + author +
                ", comment_count=" + comment_count +
                ", custom_fields=" + custom_fields +
                '}';
    }
}
