package com.example.ecosynergy;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DataResource {
    private String branch; // "Article", "Reports", "Toolkits"
    private String category; // "Solar", "Wind", "Ocean", etc.
    private List<Subcategory> subcategories; // Subcategories for 'upNext'
    private static List<DataResource> dataResources = new ArrayList<>();
    private static DatabaseReference db = FirebaseDatabase.getInstance().getReference(); // DB reference initialization

    // Constructor
    public DataResource(String branch, String category, List<Subcategory> subcategories) {
        this.branch = branch;
        this.category = category;
        this.subcategories = subcategories;
    }

    public DataResource(String branch, String category) {
        this.branch = branch;
        this.category = category;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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

    // Static method to add a new DataResource
    public static void addDataModule(DataResource dataResource) {
        dataResources.add(dataResource);
        Log.d("DataModule", "Added data module: " + dataResource.getCategory());
    }

    // Static method to get all DataResources
    public static List<DataResource> getAllModules() {
        Log.d("DataModule", "Current modules count: " + dataResources.size());
        return dataResources;
    }

    // Nested class for Subcategory
    public static class Subcategory {
        private int id;
        private String articleTitle;

        private List<Comment> comments;
        private String articleContent;

        // Constructor

        public Subcategory() {
        }

        public Subcategory(int id, String articleTitle, String articleContent) {
            this.id = id;
            this.articleTitle = articleTitle;
            this.articleContent = articleContent;
            this.comments = new ArrayList<>();
        }

        public int getId() {
            return id;
        }

        public void addComment(Comment comment) {
            this.comments.add(comment);
        }

        public String getArticleTitle() {
            return articleTitle;
        }

        public void setArticleTitle(String articleTitle) {
            this.articleTitle = articleTitle;
        }

        public String getArticleContent() {
            return articleContent;
        }

        public void setArticleContent(String articleContent) {
            this.articleContent = articleContent;
        }
    }

    // Method to fetch all resources
    public static void getAllResources(final ValueEventListener callback) {
        db.child("resources").addListenerForSingleValueEvent(callback);
    }

    // Method to fetch a specific resource by its ID
    public static void getResourceById(String resourceId, final ValueEventListener callback) {
        db.child("resources").child(resourceId).addListenerForSingleValueEvent(callback);
    }

    // Method to add a new resource to the "resources" node
    public static void addResource(DataResource dataResource) {
        String resourceId = db.child("resources").push().getKey(); // Create a unique key for the new resource
        if (resourceId != null) {
            db.child("resources").child(resourceId).setValue(dataResource)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("DataResource", "Resource added successfully!");
                        } else {
                            Log.e("DataResource", "Failed to add resource", task.getException());
                        }
                    });
        } else {
            Log.e("DataResource", "Failed to generate resource ID");
        }
    }

    // Method to update an existing resource by its ID
    public static void updateResource(String resourceId, DataResource dataResource) {
        db.child("resources").child(resourceId).setValue(dataResource)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("DataResource", "Resource updated successfully!");
                    } else {
                        Log.e("DataResource", "Failed to update resource", task.getException());
                    }
                });
    }

    // Method to delete a resource by its ID
    public static void deleteResource(String resourceId) {
        db.child("resources").child(resourceId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("DataResource", "Resource deleted successfully!");
                    } else {
                        Log.e("DataResource", "Failed to delete resource", task.getException());
                    }
                });
    }

    public static List<DataResource.Subcategory> getSubcategoriesForCategory(String category) {
        List<DataResource.Subcategory> subcategoriesForCategory = new ArrayList<>();

        // Iterate through all data modules and add subcategories of the matching category
        for (DataResource module : getAllModules()) {
            if (module.getCategory().equals(category)) {
                subcategoriesForCategory.addAll(module.getSubcategories());
            }
        }
        return subcategoriesForCategory;
    }

    public static DataResource.Subcategory getSubcategoryById(int subcategoryId, String category) {
        // Assuming you have a method to fetch the module by category
        List<DataResource> resources = getAllModules();  // Fetch all modules (you can replace this with your actual method)

        // Log to check the modules fetched
        Log.d("DataResource", "Fetched " + resources.size() + " modules");

        // Iterate through the modules to find the subcategory
        for (DataResource dataResource : resources) {
            // Log the category of the current module
            // Check if the module's category matches
            if (dataResource.getCategory().equals(category)) {
                // Log if the category matches
                Log.d("DataResource", "Found matching category: " + category);

                // Iterate through the subcategories of this module
                for (DataResource.Subcategory subcategory : dataResource.getSubcategories()) {
                    // Log the subcategory ID being checked
                    Log.d("DataResource", "Checking subcategory with ID: " + subcategory.getId());

                    // Check if the subcategory ID matches
                    if (subcategory.getId() == subcategoryId) {
                        Log.d("DataResource", "Found subcategory with ID: " + subcategoryId);
                        return subcategory;  // Return the subcategory if found
                    }
                }
            }
        }

        // Log if no subcategory is found
        Log.d("DataResource", "No subcategory found with ID: " + subcategoryId);
        return null;  // Return null if no subcategory with the given ID is found
    }

    public List<DataResource.Subcategory> getUpNextSubcategories(int currentSubcategoryId) {
        List<DataResource.Subcategory> upNext = new ArrayList<>();
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
}
