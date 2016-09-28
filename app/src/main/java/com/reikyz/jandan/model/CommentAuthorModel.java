package com.reikyz.jandan.model;

import java.io.Serializable;

/**
 * Created by reikyZ on 16/9/6.
 */
public class CommentAuthorModel implements Serializable {
    String user_id;
    String name;
    String avatar_url;
    String url;
    String threads;
    String comments;
    String weibo_uid;
    String qq_uid;
    String renren_uid;
    String kaixin_uid;
    String douban_uid;
    String netease_uid;
    String sohu_uid;
    String baidu_uid;
    String msn_uid;
    String google_uid;
    String taobao_uid;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getWeibo_uid() {
        return weibo_uid;
    }

    public void setWeibo_uid(String weibo_uid) {
        this.weibo_uid = weibo_uid;
    }

    public String getQq_uid() {
        return qq_uid;
    }

    public void setQq_uid(String qq_uid) {
        this.qq_uid = qq_uid;
    }

    public String getRenren_uid() {
        return renren_uid;
    }

    public void setRenren_uid(String renren_uid) {
        this.renren_uid = renren_uid;
    }

    public String getKaixin_uid() {
        return kaixin_uid;
    }

    public void setKaixin_uid(String kaixin_uid) {
        this.kaixin_uid = kaixin_uid;
    }

    public String getDouban_uid() {
        return douban_uid;
    }

    public void setDouban_uid(String douban_uid) {
        this.douban_uid = douban_uid;
    }

    public String getNetease_uid() {
        return netease_uid;
    }

    public void setNetease_uid(String netease_uid) {
        this.netease_uid = netease_uid;
    }

    public String getSohu_uid() {
        return sohu_uid;
    }

    public void setSohu_uid(String sohu_uid) {
        this.sohu_uid = sohu_uid;
    }

    public String getBaidu_uid() {
        return baidu_uid;
    }

    public void setBaidu_uid(String baidu_uid) {
        this.baidu_uid = baidu_uid;
    }

    public String getMsn_uid() {
        return msn_uid;
    }

    public void setMsn_uid(String msn_uid) {
        this.msn_uid = msn_uid;
    }

    public String getGoogle_uid() {
        return google_uid;
    }

    public void setGoogle_uid(String google_uid) {
        this.google_uid = google_uid;
    }

    public String getTaobao_uid() {
        return taobao_uid;
    }

    public void setTaobao_uid(String taobao_uid) {
        this.taobao_uid = taobao_uid;
    }

    @Override
    public String toString() {
        return "CommentAuthorModel{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", url='" + url + '\'' +
                ", threads='" + threads + '\'' +
                ", comments='" + comments + '\'' +
                ", weibo_uid='" + weibo_uid + '\'' +
                ", qq_uid='" + qq_uid + '\'' +
                ", renren_uid='" + renren_uid + '\'' +
                ", kaixin_uid='" + kaixin_uid + '\'' +
                ", douban_uid='" + douban_uid + '\'' +
                ", netease_uid='" + netease_uid + '\'' +
                ", sohu_uid='" + sohu_uid + '\'' +
                ", baidu_uid='" + baidu_uid + '\'' +
                ", msn_uid='" + msn_uid + '\'' +
                ", google_uid='" + google_uid + '\'' +
                ", taobao_uid='" + taobao_uid + '\'' +
                '}';
    }
}
