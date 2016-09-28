package com.reikyz.jandan.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by reikyZ on 16/9/6.
 */
public class DuoshuoCommentModel implements Serializable {
    String post_id;
    String thread_id;
    String status;
    String source;
    String type;
    String message;
    String created_at;
    String parent_id;
    String root_id;
    Integer reposts;
    Integer comments;
    String author_id;
    Integer author_key;
    String agent;
    Integer likes;
    Integer dislikes;
    Integer reports;
    List<String> parents;
    CommentAuthorModel author;
    String ip;
    String iplocation;
    Integer is_top;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getRoot_id() {
        return root_id;
    }

    public void setRoot_id(String root_id) {
        this.root_id = root_id;
    }

    public Integer getReposts() {
        return reposts;
    }

    public void setReposts(Integer reposts) {
        this.reposts = reposts;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public Integer getAuthor_key() {
        return author_key;
    }

    public void setAuthor_key(Integer author_key) {
        this.author_key = author_key;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
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

    public Integer getReports() {
        return reports;
    }

    public void setReports(Integer reports) {
        this.reports = reports;
    }

    public List<String> getParents() {
        return parents;
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    public CommentAuthorModel getAuthor() {
        return author;
    }

    public void setAuthor(CommentAuthorModel author) {
        this.author = author;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIplocation() {
        return iplocation;
    }

    public void setIplocation(String iplocation) {
        this.iplocation = iplocation;
    }

    public Integer getIs_top() {
        return is_top;
    }

    public void setIs_top(Integer is_top) {
        this.is_top = is_top;
    }

    @Override
    public String toString() {
        return "DuoshuoCommentModel{" +
                "post_id='" + post_id + '\'' +
                ", thread_id='" + thread_id + '\'' +
                ", status='" + status + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", created_at='" + created_at + '\'' +
                ", parent_id=" + parent_id +
                ", root_id=" + root_id +
                ", reposts=" + reposts +
                ", comments=" + comments +
                ", author_id='" + author_id + '\'' +
                ", author_key=" + author_key +
                ", agent='" + agent + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", reports=" + reports +
                ", parents=" + parents +
                ", author=" + author +
                ", ip='" + ip + '\'' +
                ", iplocation='" + iplocation + '\'' +
                ", is_top=" + is_top +
                '}';
    }
}
