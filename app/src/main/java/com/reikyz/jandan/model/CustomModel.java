package com.reikyz.jandan.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by reikyZ on 16/8/23.
 */
public class CustomModel implements Serializable {

    List<String> thumb_c;

    public List<String> getThumb_c() {
        return thumb_c;
    }

    public void setThumb_c(List<String> thumb_c) {
        this.thumb_c = thumb_c;
    }

    @Override
    public String toString() {
        return "CustomModel{" +
                "thumb_c=" + thumb_c +
                '}';
    }
}
