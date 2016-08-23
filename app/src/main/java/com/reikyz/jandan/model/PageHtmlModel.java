package com.reikyz.jandan.model;

import java.io.Serializable;

/**
 * Created by reikyZ on 16/8/24.
 */
public class PageHtmlModel implements Serializable {
    Integer id;
    String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PageHtmlModel{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
