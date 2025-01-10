package com.example.ecosynergy;

public class Questions {
    private String title;
    private String description;  // Description of the quiz question

    public Questions(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
