package com.example.newsapp.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class CustomNews implements Serializable {

    private String title;
    private String link;
    private String keywords;
    private String creator;
    private String videoUrl;
    private String description;
    private String content;
    private String pubDate;
    private String imageUrl;
    private String sourceId;
    private Bitmap image;
    private static Integer nextPage;
    private boolean saved;

    public CustomNews() {
        this.title = "";
        this.link = "";
        this.keywords = "";
        this.creator = "";
        this.videoUrl = "";
        this.description = "";
        this.content = "";
        this.pubDate = "";
        this.imageUrl = "https://cdn.pixabay.com/photo/2016/02/01/00/56/news-1172463_960_720.jpg";
        this.sourceId = "";
        Bitmap image = null;
        saved = false;
        nextPage = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }
}
