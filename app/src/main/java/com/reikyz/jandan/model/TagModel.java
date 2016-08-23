package com.reikyz.jandan.model;

import java.io.Serializable;

/**
 * Created by reikyZ on 16/8/23.
 */
public class TagModel implements Serializable {

    Integer id;
    String slug;
    String title;
    String description;
    Integer post_count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPost_count() {
        return post_count;
    }

    public void setPost_count(Integer post_count) {
        this.post_count = post_count;
    }

    @Override
    public String toString() {
        return "TagModel{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", post_count=" + post_count +
                '}';
    }
}
