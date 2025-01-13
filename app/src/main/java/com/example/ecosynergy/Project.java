package com.example.ecosynergy;

import java.util.Map;

public class Project {
    private String projectTitle;
    private String category;
    private int collaboratorAmount;
    private String description;
    private String link;
    private String status;
    private String userId;    // User ID of the project creator
    private String userName;  // Username of the project creator
    private int currentMembers; // Current number of members in the project
    private Map<String, Boolean> joinedUsers; // Map to track joined users
    private String fileUrl;   // URL of the associated file (if any)
    private String imageUrl;  // URL of the associated image (if any)
    private String fileName;  // Name of the file
    private String imageName; // Name of the image


    // Default constructor required for Firebase
    public Project() {}

    // Parameterized constructor
    public Project(String projectTitle, String category, int collaboratorAmount, String description, String link, String status) {
        this.projectTitle = projectTitle;
        this.category = category;
        this.collaboratorAmount = collaboratorAmount + 1; // Add 1 for the creator
        this.description = description;
        this.link = link;
        this.status = status;
    }

    // Getters and Setters
    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCollaboratorAmount() {
        return collaboratorAmount;
    }

    public void setCollaboratorAmount(int collaboratorAmount) {
        this.collaboratorAmount = collaboratorAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCurrentMembers() {
        return currentMembers;
    }

    public void setCurrentMembers(int currentMembers) {
        this.currentMembers = currentMembers;
    }

    public Map<String, Boolean> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(Map<String, Boolean> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


}

