package com.example.ecosynergy;

public class Video {
    private String title;
    private String url;  // URL to the video
    private String thumbnailUrl;  // URL to the video thumbnail

    public Video(String title, String url, String thumbnailUrl) {
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
