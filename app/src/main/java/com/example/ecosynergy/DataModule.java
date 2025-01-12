package com.example.ecosynergy;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataModule {
    private String level; // "Basic", "Intermediate", "Advanced"
    private String category; // "Solar", "Wind", "Ocean", etc.
    private List<Subcategory> subcategories; // Subcategories for 'upNext'

    private static List<DataModule> dataModules = new ArrayList<>();
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

    public static void addDataModule(DataModule dataModule) {
        dataModules.add(dataModule);
        Log.d("DataModule", "Added data module: " + dataModule.getCategory());
    }

    public static List<DataModule> getAllModules() {
        Log.d("DataModule", "Current modules count: " + dataModules.size());
        return dataModules;
    }


    // Nested class for subcategory
    public static class Subcategory {

        private int id;
        private List<Comment> comments;
        private String title;
        private String level;
        private String description;
        private String videoTitle;
        private String videoDescription;
        private String contentUrl; // URL or resource ID to fetch video/content

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
            this.comments = new ArrayList<>();
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void addComment(Comment comment) {
            this.comments.add(comment);
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
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

    private static Map<String, Map<String, List<Comment>>> commentsData = new HashMap<>();
    // Fetch comments for a specific category and subcategory
    public static List<Comment> getCommentsForSubcategory(String category, String subcategory) {
        if (commentsData.containsKey(category)) {
            Map<String, List<Comment>> subcategories = commentsData.get(category);
            if (subcategories.containsKey(subcategory)) {
                return subcategories.get(subcategory);
            }
        }
        return new ArrayList<>();  // Return empty list if no comments found
    }

    // Add a comment to a subcategory
    public static void addCommentToSubcategory(String category, String subcategory, Comment comment) {
        commentsData.putIfAbsent(category, new HashMap<>());
        Map<String, List<Comment>> subcategories = commentsData.get(category);
        subcategories.putIfAbsent(subcategory, new ArrayList<>());
        subcategories.get(subcategory).add(comment);
    }

    // Add multiple comments to a subcategory
    public static void addCommentsForSubcategory(String category, String subcategory, List<Comment> comments) {
        commentsData.putIfAbsent(category, new HashMap<>());
        Map<String, List<Comment>> subcategories = commentsData.get(category);
        subcategories.putIfAbsent(subcategory, new ArrayList<>());
        subcategories.get(subcategory).addAll(comments);
    }

    public static List<Subcategory> getSubcategoriesForCategory(String category) {
        List<Subcategory> subcategoriesForCategory = new ArrayList<>();

        // Iterate through all data modules and add subcategories of the matching category
        for (DataModule module : getAllModules()) {
            if (module.getCategory().equals(category)) {
                subcategoriesForCategory.addAll(module.getSubcategories());
            }
        }
        return subcategoriesForCategory;
    }

    public static Subcategory getSubcategoryById(int subcategoryId, String category) {
        // Assuming you have a method to fetch the module by category
        List<DataModule> modules = getAllModules();  // Fetch all modules (you can replace this with your actual method)

        // Log to check the modules fetched
        Log.d("DataModule", "Fetched " + modules.size() + " modules");

        // Iterate through the modules to find the subcategory
        for (DataModule module : modules) {
            // Log the category of the current module
            Log.d("DataModule", "Checking module - category: " + module.getCategory());

            // Check if the module's category matches
            if (module.getCategory().equals(category)) {
                // Log if the category matches
                Log.d("DataModule", "Found matching category: " + category);

                // Iterate through the subcategories of this module
                for (Subcategory subcategory : module.getSubcategories()) {
                    // Log the subcategory ID being checked
                    Log.d("DataModule", "Checking subcategory with ID: " + subcategory.getId());

                    // Check if the subcategory ID matches
                    if (subcategory.getId() == subcategoryId) {
                        Log.d("DataModule", "Found subcategory with ID: " + subcategoryId);
                        return subcategory;  // Return the subcategory if found
                    }
                }
            }
        }

        // Log if no subcategory is found
        Log.d("DataModule", "No subcategory found with ID: " + subcategoryId);
        return null;  // Return null if no subcategory with the given ID is found
    }

    public List<Subcategory> getUpNextSubcategories(int currentSubcategoryId) {
        List<Subcategory> upNext = new ArrayList<>();
        int currentIndex = -1;

        // Find the current subcategory index
        for (int i = 0; i < subcategories.size(); i++) {
            if (subcategories.get(i).getId() == currentSubcategoryId) {
                currentIndex = i;
                break;
            }
        }

        // Add the current subcategory and the next two (if available)
        if (currentIndex != -1) {
            for (int i = currentIndex + 1; i <= currentIndex + 2 && i < subcategories.size(); i++) {
                upNext.add(subcategories.get(i));
            }
        }
        return upNext;
    }
    
    public static List<DataModule> getDataModulesForCategory(String categoryName) {

        // Solar Energy
        if ("Solar Energy".equals(categoryName)) {
            List<Subcategory> basicSubcategories = new ArrayList<>();
            basicSubcategories.add(new Subcategory(1, "Basic", "What is Solar Energy?", "Intro to solar energy", "url1", "Intro to Solar Energy", "Solar energy is a renewable resource. In this video, we will learn the basics of solar energy."));

            List<Subcategory> intermediateSubcategories = new ArrayList<>();
            intermediateSubcategories.add(new Subcategory(2, "Intermediate", "Solar Energy Conversion", "How solar panels work", "url2", "Solar Energy Conversion", "This video explains the process of converting solar energy into usable electricity."));

            List<Subcategory> advancedSubcategories = new ArrayList<>();
            advancedSubcategories.add(new Subcategory(3, "Advanced", "Advanced Solar Technologies", "Exploring cutting-edge solar technology", "url3", "Advanced Solar", "In this video, we explore advanced solar technologies and their applications."));

            dataModules.add(new DataModule("Basic", "Solar Energy", basicSubcategories));
            dataModules.add(new DataModule("Intermediate", "Solar Energy", intermediateSubcategories));
            dataModules.add(new DataModule("Advanced", "Solar Energy", advancedSubcategories));
        }
        // Wind Energy
        else if ("Wind Energy".equals(categoryName)) {
            List<Subcategory> basicSubcategories = new ArrayList<>();
            basicSubcategories.add(new Subcategory(1, "Basic", "What is Wind Energy?", "Intro to wind energy", "url4", "Intro to Wind Energy", "Wind energy is harnessed using turbines. Learn the basics of wind energy in this video."));

            List<Subcategory> intermediateSubcategories = new ArrayList<>();
            intermediateSubcategories.add(new Subcategory(2, "Intermediate", "Wind Turbine Mechanics", "How wind turbines work", "url5", "Wind Turbine Mechanics", "In this video, we dive into the mechanics of wind turbines and their working principle."));

            List<Subcategory> advancedSubcategories = new ArrayList<>();
            advancedSubcategories.add(new Subcategory(3, "Advanced", "Wind Energy in Large-Scale Applications", "Harnessing wind for entire cities", "url6", "Large-Scale Wind Energy", "We explore how large-scale wind energy is used to power cities and industries."));

            dataModules.add(new DataModule("Basic", "Wind Energy", basicSubcategories));
            dataModules.add(new DataModule("Intermediate", "Wind Energy", intermediateSubcategories));
            dataModules.add(new DataModule("Advanced", "Wind Energy", advancedSubcategories));
        }
        // Hydro Energy
        else if ("Hydro Energy".equals(categoryName)) {
            List<Subcategory> basicSubcategories = new ArrayList<>();
            basicSubcategories.add(new Subcategory(1, "Basic", "Introduction to Hydro Energy", "How hydroelectricity works", "url7", "Intro to Hydro Energy", "Learn how hydroelectric power plants convert water flow into electricity."));

            List<Subcategory> intermediateSubcategories = new ArrayList<>();
            intermediateSubcategories.add(new Subcategory(2, "Intermediate", "Hydroelectric Power Plants", "Exploring hydroelectric power plants", "url8", "Hydroelectric Power Plants", "In this video, we will take a closer look at the components of hydroelectric power plants."));

            List<Subcategory> advancedSubcategories = new ArrayList<>();
            advancedSubcategories.add(new Subcategory(3, "Advanced", "Pumped Storage Hydroelectricity", "Understanding pumped storage", "url9", "Pumped Storage Hydroelectricity", "This video explores the advanced technique of pumped storage hydroelectricity, used to manage energy demand."));

            dataModules.add(new DataModule("Basic", "Hydro Energy", basicSubcategories));
            dataModules.add(new DataModule("Intermediate", "Hydro Energy", intermediateSubcategories));
            dataModules.add(new DataModule("Advanced", "Hydro Energy", advancedSubcategories));
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

