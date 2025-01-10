package com.example.ecosynergy;

public class Project {
    private String projectTitle;
    private String category;
    private int collaboratorAmount;
    private String description;
    private String link;
    private String status;

    // Constructor (required for Firebase)
    public Project() {}

    // Parameterized Constructor
    public Project(String projectTitle, String category, int collaboratorAmount, String description, String link, String status) {
        this.projectTitle = projectTitle;
        this.category = category;
        this.collaboratorAmount = collaboratorAmount;
        this.description = description;
        this.link = link;
        this.status = status;
    }

    // Getters and Setters (optional but useful)
    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getCollaboratorAmount() { return collaboratorAmount; }
    public void setCollaboratorAmount(int collaboratorAmount) { this.collaboratorAmount = collaboratorAmount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
