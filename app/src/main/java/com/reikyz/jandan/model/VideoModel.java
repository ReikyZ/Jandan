package com.reikyz.jandan.model;

import java.io.Serializable;

/**
 * Created by reikyZ on 16/8/25.
 */
public class VideoModel implements Serializable {
    
    private String itemCode;
    private String vcode;
    private String title;
    private String tags;
    private String description;
    private String picUrl;
    private Integer totalTime;
    private String pubDate;
    private Integer ownerId;
    private String ownerName;
    private String ownerNickname;
    private String ownerPic;
    private String ownerURL;
    private Integer channelId;
    private String outerPlayerUrl;
    private String playUrl;
    private String mediaType;
    private boolean secret;
    private String hdType;
    private Integer playTimes;
    private Integer commentCount;
    private String bigPicUrl;
    private String alias;
    private boolean downEnable;
    private String location;
    private Integer favorCount;
    private String outerGPlayerUrl;
    private Integer digCount;
    String player;
    String link;
    private Integer buryCount;
    private String video_source;
    String thumbnail;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerNickname() {
        return ownerNickname;
    }

    public void setOwnerNickname(String ownerNickname) {
        this.ownerNickname = ownerNickname;
    }

    public String getOwnerPic() {
        return ownerPic;
    }

    public void setOwnerPic(String ownerPic) {
        this.ownerPic = ownerPic;
    }

    public String getOwnerURL() {
        return ownerURL;
    }

    public void setOwnerURL(String ownerURL) {
        this.ownerURL = ownerURL;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getOuterPlayerUrl() {
        return outerPlayerUrl;
    }

    public void setOuterPlayerUrl(String outerPlayerUrl) {
        this.outerPlayerUrl = outerPlayerUrl;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isSecret() {
        return secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public String getHdType() {
        return hdType;
    }

    public void setHdType(String hdType) {
        this.hdType = hdType;
    }

    public Integer getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(Integer playTimes) {
        this.playTimes = playTimes;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getBigPicUrl() {
        return bigPicUrl;
    }

    public void setBigPicUrl(String bigPicUrl) {
        this.bigPicUrl = bigPicUrl;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isDownEnable() {
        return downEnable;
    }

    public void setDownEnable(boolean downEnable) {
        this.downEnable = downEnable;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getFavorCount() {
        return favorCount;
    }

    public void setFavorCount(Integer favorCount) {
        this.favorCount = favorCount;
    }

    public String getOuterGPlayerUrl() {
        return outerGPlayerUrl;
    }

    public void setOuterGPlayerUrl(String outerGPlayerUrl) {
        this.outerGPlayerUrl = outerGPlayerUrl;
    }

    public Integer getDigCount() {
        return digCount;
    }

    public void setDigCount(Integer digCount) {
        this.digCount = digCount;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getBuryCount() {
        return buryCount;
    }

    public void setBuryCount(Integer buryCount) {
        this.buryCount = buryCount;
    }

    public String getVideo_source() {
        return video_source;
    }

    public void setVideo_source(String video_source) {
        this.video_source = video_source;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "VideoModel{" +
                "itemCode='" + itemCode + '\'' +
                ", vcode='" + vcode + '\'' +
                ", title='" + title + '\'' +
                ", tags='" + tags + '\'' +
                ", description='" + description + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", totalTime=" + totalTime +
                ", pubDate='" + pubDate + '\'' +
                ", ownerId=" + ownerId +
                ", ownerName='" + ownerName + '\'' +
                ", ownerNickname='" + ownerNickname + '\'' +
                ", ownerPic='" + ownerPic + '\'' +
                ", ownerURL='" + ownerURL + '\'' +
                ", channelId=" + channelId +
                ", outerPlayerUrl='" + outerPlayerUrl + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", secret=" + secret +
                ", hdType='" + hdType + '\'' +
                ", playTimes=" + playTimes +
                ", commentCount=" + commentCount +
                ", bigPicUrl='" + bigPicUrl + '\'' +
                ", alias='" + alias + '\'' +
                ", downEnable=" + downEnable +
                ", location='" + location + '\'' +
                ", favorCount=" + favorCount +
                ", outerGPlayerUrl='" + outerGPlayerUrl + '\'' +
                ", digCount=" + digCount +
                ", player='" + player + '\'' +
                ", link='" + link + '\'' +
                ", buryCount=" + buryCount +
                ", video_source='" + video_source + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
