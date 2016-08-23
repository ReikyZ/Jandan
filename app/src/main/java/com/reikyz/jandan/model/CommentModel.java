package com.reikyz.jandan.model;

import android.content.Intent;

import java.io.Serializable;

/**
 * Created by reikyZ on 16/8/23.
 */
public class CommentModel implements Serializable {

    Integer id;
    String name;
    String url;
    String date;
    String content;
    Integer parent;
    Integer vote_positive;
    Integer vote_negative;
    Integer index;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getVote_positive() {
        return vote_positive;
    }

    public void setVote_positive(Integer vote_positive) {
        this.vote_positive = vote_positive;
    }

    public Integer getVote_negative() {
        return vote_negative;
    }

    public void setVote_negative(Integer vote_negative) {
        this.vote_negative = vote_negative;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", parent=" + parent +
                ", vote_positive=" + vote_positive +
                ", vote_negative=" + vote_negative +
                ", index=" + index +
                '}';
    }
}
