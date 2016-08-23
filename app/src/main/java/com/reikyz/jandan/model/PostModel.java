package com.reikyz.jandan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reikyZ on 16/8/23.
 */
public class PostModel implements Serializable {
    Integer id;
    List<CommentModel> comments = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "PostModel{" +
                "id=" + id +
                ", comments=" + comments +
                '}';
    }
}
