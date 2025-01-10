package com.example.ecosynergy;

public class Certificate {

    private String id;
    private String title;
    private long dateEarned;

    public Certificate() {} // Required for Firebase

    public Certificate(String id, String title) {
        this.id = id;
        this.title = title;
        this.dateEarned = System.currentTimeMillis();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public long getDateEarned() { return dateEarned; }
    public void setDateEarned(long dateEarned) { this.dateEarned = dateEarned; }

}
