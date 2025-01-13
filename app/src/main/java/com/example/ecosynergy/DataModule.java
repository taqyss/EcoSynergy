package com.example.ecosynergy;

import android.util.Log;

import androidx.recyclerview.widget.AsyncListUtil;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataModule {
    private String level; // "Basic", "Intermediate", "Advanced"
    private String category; // "Solar", "Wind", "Ocean", etc.
    private Map<String, QuestionSet> questionSets; // A map to store question sets by level
    private List<Subcategory> subcategories; // A list of subcategories

    private static List<DataModule> dataModules = new ArrayList<>();
    private static DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    // Constructor
    public DataModule(String level, String category, List<Subcategory> subcategories, Map<String, QuestionSet> questionSets) {
        this.level = level;
        this.category = category;
        this.subcategories = subcategories;
        this.questionSets = questionSets;
    }
    // No-argument constructor (required for Firebase)
    public DataModule() {
    }

    public DataModule(String level, String category) {
        this.level = level;
        this.category = category;
        this.subcategories = new ArrayList<>();
        this.questionSets = new HashMap<>();
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

    public Map<String, QuestionSet> getQuestionSets() {
        return questionSets;
    }

    public void addSubcategory(Subcategory subcategory) {
        this.subcategories.add(subcategory);
    }

    public void addQuestionSet(String key, QuestionSet questionSet) {
        this.questionSets.put(key, questionSet);
    }

    public void setQuestionSets(Map<String, QuestionSet> questionSets) {
        this.questionSets = questionSets;
    }

    // Static method to add a DataModule
    public static void addDataModule(DataModule dataModule) {
        dataModules.add(dataModule);
        Log.d("DataModule", "Added data module: " + dataModule.getCategory());
    }

    // Static method to get all modules
    public static List<DataModule> getAllModules() {
        Log.d("DataModule", "Current modules count: " + dataModules.size());
        return dataModules;
    }
    // Method to load all DataModules from Firebase
    public static void addCommentToSubcategory(String category, String subcategory, Comment comment) {
        String commentId = databaseRef.push().getKey();
        if (commentId != null) {
            databaseRef.child("comments")
                    .child(category)
                    .child(subcategory)
                    .child(commentId)
                    .setValue(comment)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseHelper", "Comment added to subcategory.");
                        } else {
                            Log.e("FirebaseHelper", "Failed to add comment.");
                        }
                    });
        }
    }

    // Method to load comments for a specific subcategory from Firebase
    public static void loadCommentsForSubcategory(String category, String subcategory, ValueEventListener listener) {
        databaseRef.child("comments")
                .child(category)
                .child(subcategory)
                .addListenerForSingleValueEvent(listener);
    }
    public static void updateSubcategory(int subcategoryId, String category, Subcategory newSubcategoryData) {
        databaseRef.child("dataModules")
                .orderByChild("category")
                .equalTo(category)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot moduleSnapshot : dataSnapshot.getChildren()) {
                            DataModule module = moduleSnapshot.getValue(DataModule.class);
                            if (module != null) {
                                for (DataModule.Subcategory subcategory : module.getSubcategories()) {
                                    if (subcategory.getId() == subcategoryId) {
                                        // Update the subcategory with new data
                                        subcategory.setTitle(newSubcategoryData.getTitle());
                                        subcategory.setDescription(newSubcategoryData.getDescription());
                                        subcategory.setContentUrl(newSubcategoryData.getContentUrl());
                                        subcategory.setVideoTitle(newSubcategoryData.getVideoTitle());
                                        subcategory.setVideoDescription(newSubcategoryData.getVideoDescription());

                                        // Save the updated module to Firebase
                                        moduleSnapshot.getRef().setValue(module);
                                        return;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseHelper", "Error updating subcategory: " + databaseError.getMessage());
                    }
                });
    }

    // Method to fetch questions from Firebase
    public void fetchQuestions() {
        databaseRef.child("questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Questions> questions = new ArrayList<>();
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    String category = questionSnapshot.child("category").getValue(String.class);
                    String questionText = questionSnapshot.child("question").getValue(String.class);
                    if (category != null && questionText != null) {
                        questions.add(new Questions(category, questionText));
                    }
                }
                // Handle the list of questions here
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseHelper", "Error fetching questions: " + databaseError.getMessage());
            }
        });
    }

    // Method to fetch comments from Firebase
    public void fetchComments() {
        databaseRef.child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Comment> comments = new ArrayList<>();
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    String avatar = commentSnapshot.child("avatar").getValue(String.class);
                    String username = commentSnapshot.child("username").getValue(String.class);
                    String commentText = commentSnapshot.child("commentText").getValue(String.class);
                    int upvotes = commentSnapshot.child("upvotes").getValue(Integer.class);
                    String timestamp = commentSnapshot.child("timestamp").getValue(String.class);

                    List<Comment> replies = new ArrayList<>();
                    for (DataSnapshot replySnapshot : commentSnapshot.child("replies").getChildren()) {
                        String replyAvatar = replySnapshot.child("avatar").getValue(String.class);
                        String replyUsername = replySnapshot.child("username").getValue(String.class);
                        String replyText = replySnapshot.child("commentText").getValue(String.class);
                        int replyUpvotes = replySnapshot.child("upvotes").getValue(Integer.class);
                        String replyTimestamp = replySnapshot.child("timestamp").getValue(String.class);
                        replies.add(new Comment(replyAvatar, replyUsername, replyText, replyUpvotes, replyTimestamp, new ArrayList<>()));
                    }

                    if (avatar != null && username != null && commentText != null && timestamp != null) {
                        comments.add(new Comment(avatar, username, commentText, upvotes, timestamp, replies));
                    }
                }
                // Handle the list of comments here
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseHelper", "Error fetching comments: " + databaseError.getMessage());
            }
        });
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

        // No-argument constructor (required for Firebase)
        public Subcategory() {
        }

        public Subcategory(String title) {
            this.title = title;
            this.comments = new ArrayList<>();
        }

        // Constructor
        public Subcategory(String title, String description, String contentUrl,
                           String videoTitle, String videoDescription) {
            this.title = title;
            this.description = description;
            this.contentUrl = contentUrl;
            this.videoTitle = videoTitle;
            this.videoDescription = videoDescription;
            this.comments = new ArrayList<>();
        }

        public Subcategory(int id,String level, String title, String description, String contentUrl,
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
        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
        public void setId(int id) {
            this.id = id;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getVideoDescription() {
            return videoDescription;
        }

        public void setVideoDescription(String videoDescription) {
            this.videoDescription = videoDescription;
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void addComment(Comment comment) {
            this.comments.add(comment);
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
        List<DataModule> modules = getAllModules();
        for (DataModule module : modules) {
            if (module.getCategory().equals(category)) {
                for (Subcategory subcategory : module.getSubcategories()) {
                    if (subcategory.getId() == subcategoryId) {
                        return subcategory;
                    }
                }
            }
        }
        return null;
    }

    public List<Subcategory> getUpNextSubcategories(int currentSubcategoryId) {
        List<Subcategory> upNext = new ArrayList<>();
        int currentIndex = -1;

        for (int i = 0; i < subcategories.size(); i++) {
            if (subcategories.get(i).getId() == currentSubcategoryId) {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex != -1) {
            for (int i = currentIndex + 1; i <= currentIndex + 2 && i < subcategories.size(); i++) {
                upNext.add(subcategories.get(i));
            }
        }
        return upNext;
    }

    private static Map<Integer, Subcategory> subcategoryMap = new HashMap<>();

    public static String getURLBasedOnID(int currentSubcategoryId) {
        Subcategory subcategory = subcategoryMap.get(currentSubcategoryId);
        return (subcategory != null) ? subcategory.getContentUrl() : "URL is not available";
    }
}
