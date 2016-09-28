package com.reikyz.jandan.model;

import java.io.Serializable;

/**
 * Created by reikyZ on 16/8/26.
 */
public class CommentThreadModel implements Serializable {

    private String thread_id;

    private String channel_key;

    private String thread_key;

    private Integer reposts;

    private Integer views;

    private Integer likes;

    private Integer dislikes;

    private Integer comments;

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getChannel_key() {
        return channel_key;
    }

    public void setChannel_key(String channel_key) {
        this.channel_key = channel_key;
    }

    public String getThread_key() {
        return thread_key;
    }

    public void setThread_key(String thread_key) {
        this.thread_key = thread_key;
    }

    public Integer getReposts() {
        return reposts;
    }

    public void setReposts(Integer reposts) {
        this.reposts = reposts;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "CommentThreadModel{" +
                "thread_id='" + thread_id + '\'' +
                ", channel_key='" + channel_key + '\'' +
                ", thread_key='" + thread_key + '\'' +
                ", reposts=" + reposts +
                ", views=" + views +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", comments=" + comments +
                '}';
    }
}
