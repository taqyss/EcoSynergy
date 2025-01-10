package com.example.ecosynergy.models;

public class Module {
    private String name;
    private int progress;

    // Constructor, getters, and setters
    public Module(String name, int progress) {
        this.name = name;
        this.progress = progress;
}

    public String getName() {
        return name;
    }

    public int getProgress() {
    return progress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
