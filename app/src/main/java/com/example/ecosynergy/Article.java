package com.example.ecosynergy;

public class Article {
    private String title;
    private String snippet;  // Snippet of the article content

    public Article(String title, String snippet) {
        this.title = title;
        this.snippet = snippet;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }
}
