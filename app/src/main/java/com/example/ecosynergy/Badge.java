package com.example.ecosynergy;

public class Badge {

    private String id;
    private String description;

    public Badge() {} // Required for Firebase

    public Badge(String id, String description) {
        this.id = id;
        this.description = description;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

}
