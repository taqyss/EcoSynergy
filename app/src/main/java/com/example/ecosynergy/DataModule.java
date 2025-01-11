package com.example.ecosynergy;

import java.util.ArrayList;
import java.util.List;

public class DataModule {
    private String level; // "Basic", "Intermediate", "Advanced"
    private String category; // "Solar", "Wind", "Ocean", etc.
    private List<Subcategory> subcategories; // Subcategories for 'upNext'
    // Constructor
    public DataModule(String level, String category, List<Subcategory> subcategories) {
        this.level = level;
        this.category = category;
        this.subcategories = subcategories;
    }

    // Getters and Setters
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }


    // Nested class for subcategory
    public static class Subcategory {

        private int id;
        private String title;
        private String level;
        private String description;
        private String videoTitle;
        private String videoDescription;
        private String contentUrl; // URL or resource ID to fetch video/content

        private List<Subcategory> basicSub = new ArrayList<>();
        private List<Subcategory> intermediateSub = new ArrayList<>();
        private List<Subcategory> advancedSub = new ArrayList<>();
        // Constructor
        public Subcategory(int id, String level, String title, String description, String contentUrl,
                           String videoTitle, String videoDescription) {
            this.id = id;
            this.level = level;
            this.title = title;
            this.description = description;
            this.contentUrl = contentUrl;
            this.videoTitle = videoTitle;
            this.videoDescription = videoDescription;
        }

        public int getId() {
            return id;
        }
        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public String getVideoDescription() {
            return videoDescription;
        }

        public void setVideoDescription(String videoDescription) {
            this.videoDescription = videoDescription;
        }
    }

    public static List<DataModule> getDataModulesForCategory(String categoryName) {
        List<DataModule> dataModules = new ArrayList<>();

        // Solar Energy
        if ("Solar Energy".equals(categoryName)) {
            List<Subcategory> basicSubcategories = new ArrayList<>();
            basicSubcategories.add(new Subcategory(0, "Basic", "What is Solar Energy?", "Intro to solar energy", "url1", "Intro to Solar Energy", "Solar energy is a renewable resource. In this video, we will learn the basics of solar energy."));

            List<Subcategory> intermediateSubcategories = new ArrayList<>();
            intermediateSubcategories.add(new Subcategory(1, "Intermediate", "Solar Energy Conversion", "How solar panels work", "url2", "Solar Energy Conversion", "This video explains the process of converting solar energy into usable electricity."));

            List<Subcategory> advancedSubcategories = new ArrayList<>();
            advancedSubcategories.add(new Subcategory(2, "Advanced", "Advanced Solar Technologies", "Exploring cutting-edge solar technology", "url3", "Advanced Solar", "In this video, we explore advanced solar technologies and their applications."));

            dataModules.add(new DataModule("Basic", "Solar", basicSubcategories));
            dataModules.add(new DataModule("Intermediate", "Solar", intermediateSubcategories));
            dataModules.add(new DataModule("Advanced", "Solar", advancedSubcategories));
        }
        // Wind Energy
        else if ("Wind Energy".equals(categoryName)) {
            List<Subcategory> basicSubcategories = new ArrayList<>();
            basicSubcategories.add(new Subcategory(0, "Basic", "What is Wind Energy?", "Intro to wind energy", "url4", "Intro to Wind Energy", "Wind energy is harnessed using turbines. Learn the basics of wind energy in this video."));

            List<Subcategory> intermediateSubcategories = new ArrayList<>();
            intermediateSubcategories.add(new Subcategory(1, "Intermediate", "Wind Turbine Mechanics", "How wind turbines work", "url5", "Wind Turbine Mechanics", "In this video, we dive into the mechanics of wind turbines and their working principle."));

            List<Subcategory> advancedSubcategories = new ArrayList<>();
            advancedSubcategories.add(new Subcategory(2, "Advanced", "Wind Energy in Large-Scale Applications", "Harnessing wind for entire cities", "url6", "Large-Scale Wind Energy", "We explore how large-scale wind energy is used to power cities and industries."));

            dataModules.add(new DataModule("Basic", "Wind", basicSubcategories));
            dataModules.add(new DataModule("Intermediate", "Wind", intermediateSubcategories));
            dataModules.add(new DataModule("Advanced", "Wind", advancedSubcategories));
        }
        // Hydro Energy
        else if ("Hydro Energy".equals(categoryName)) {
            List<Subcategory> basicSubcategories = new ArrayList<>();
            basicSubcategories.add(new Subcategory(0, "Basic", "Introduction to Hydro Energy", "How hydroelectricity works", "url7", "Intro to Hydro Energy", "Learn how hydroelectric power plants convert water flow into electricity."));

            List<Subcategory> intermediateSubcategories = new ArrayList<>();
            intermediateSubcategories.add(new Subcategory(1, "Intermediate", "Hydroelectric Power Plants", "Exploring hydroelectric power plants", "url8", "Hydroelectric Power Plants", "In this video, we will take a closer look at the components of hydroelectric power plants."));

            List<Subcategory> advancedSubcategories = new ArrayList<>();
            advancedSubcategories.add(new Subcategory(2, "Advanced", "Pumped Storage Hydroelectricity", "Understanding pumped storage", "url9", "Pumped Storage Hydroelectricity", "This video explores the advanced technique of pumped storage hydroelectricity, used to manage energy demand."));

            dataModules.add(new DataModule("Basic", "Hydro", basicSubcategories));
            dataModules.add(new DataModule("Intermediate", "Hydro", intermediateSubcategories));
            dataModules.add(new DataModule("Advanced", "Hydro", advancedSubcategories));
        }
        // Geothermal Energy
        else if ("Geothermal Energy".equals(categoryName)) {
            // Add subcategories for Geothermal Energy...
        }
        // Biomass Energy
        else if ("Biomass Energy".equals(categoryName)) {
            // Add subcategories for Biomass Energy...
        }
        // Ocean Energy
        else if ("Ocean Energy".equals(categoryName)) {
            // Add subcategories for Ocean Energy...
        }

        return dataModules;
    }


}

