package com.example.ecosynergy;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseResourceFetcher {
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private DatabaseReference subcategoryRef;
    private DatabaseReference commentsRef;

    private List<DataResource> dataResources = new ArrayList<>();
    public List<DataResource.Subcategory> subcategories = new ArrayList<>();

    public FirebaseResourceFetcher() {
        mDatabase = FirebaseDatabase.getInstance().getReference("dataResources");
        database = FirebaseDatabase.getInstance();
        subcategoryRef = database.getReference("subcategories");
        commentsRef = database.getReference("comments");
    }

    public void fetchDataResource(final SubcategoryCallback callback) {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Iterate through all branches (e.g., Article, Report, Toolkit)
                for (DataSnapshot branchSnapshot : dataSnapshot.getChildren()) {
                    String branch = branchSnapshot.getKey();

                    // Log the branch name for debugging
                    Log.d("onDataChange", "Branch: " + branch);

                    // Iterate through categories within the branch (e.g., Solar Energy, Wind Energy)
                    for (DataSnapshot categorySnapshot : branchSnapshot.getChildren()) {
                        String category = categorySnapshot.getKey();

                        // Log the category name for debugging
                        Log.d("onDataChange", "Category: " + category);

                        DataResource dataResource = new DataResource(branch, category);

                        // Iterate through subcategories (e.g., individual topics like "Introduction to Solar Energy")
                        for (DataSnapshot subcategorySnapshot : categorySnapshot.child("subcategories").getChildren()) {
                            DataResource.Subcategory subcategory = processSubcategory(subcategorySnapshot);
                            subcategories.add(subcategory);

                            // Log the subcategory information for debugging
                            Log.d("onDataChange", "Subcategory: " + subcategory.getArticleTitle());
                            Log.d("onDataChange", "Subcategory: " + subcategory.getArticleContent());

                        }

                        // Set the subcategories in the DataResource
                        dataResource.setSubcategories(subcategories);
                        dataResources.add(dataResource);
                    }
                }

                // Log the final data resources
                Log.d("onDataChange", "Fetched Data Resources: " + dataResources.toString());

                // Call callback with the fetched data
                callback.onDataFetched(dataResources);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if necessary
            }
        });
    }

    // Process a subcategory
    private DataResource.Subcategory processSubcategory(DataSnapshot subcategorySnapshot) {
        int subcategoryId = -1;
        try {
            String key = subcategorySnapshot.getKey();
            if (key != null) {
                subcategoryId = Integer.parseInt(key);
            } else {
                Log.e("FirebaseDataFetcher", "Subcategory ID key is null");
            }
        } catch (NumberFormatException e) {
            Log.e("FirebaseDataFetcher", "Error parsing subcategory ID", e);
        }

        String articleContent = subcategorySnapshot.child("articleContent").getValue(String.class);
        String articleTitle = subcategorySnapshot.child("articleTitle").getValue(String.class);

        DataResource.Subcategory subcategory = new DataResource.Subcategory(
                subcategoryId, articleTitle, articleContent
        );

        // Process all comments associated with the subcategory
        for (DataSnapshot commentSnapshot : subcategorySnapshot.child("comments").getChildren()) {
            Comment comment = processComment(commentSnapshot);
            subcategory.addComment(comment);
        }

        return subcategory;
    }

    // Process a comment
    private Comment processComment(DataSnapshot commentSnapshot) {
        String userAvatar = commentSnapshot.child("userAvatar").getValue(String.class);
        String username = commentSnapshot.child("username").getValue(String.class);
        String commentText = commentSnapshot.child("commentText").getValue(String.class);
        int upvote = commentSnapshot.child("upvote").getValue(Integer.class);
        String timestamp = commentSnapshot.child("timestamp").getValue(String.class);

        Comment comment = new Comment(userAvatar, username, commentText, upvote, timestamp);

        // Process any replies to the comment (if any)
        for (DataSnapshot replySnapshot : commentSnapshot.child("replies").getChildren()) {
            Comment reply = processComment(replySnapshot);
            comment.addReply(reply);
        }

        return comment;
    }

    // Fetch subcategory content based on category and subcategory title
    // Fetch subcategory content based on category and subcategory title
    public void fetchSubcategoryContent(String category, String subcategoryTitle, String branch, final SubcategoryCallback callback) {
        this.fetchDataResource(new FirebaseResourceFetcher.SubcategoryCallback() {

            @Override
            public void onDataFetched(List<DataResource> dataResources) {

                Log.d("SubCategoryResourceActivity", "Overriding");

                if (subcategories.isEmpty()) {
                    callback.onError(new Exception("Subcategories are not loaded. Please fetch data resources first."));
                    return;
                }
                for (DataResource dataResource : dataResources) {
                    if (dataResource.getCategory().equals(category) && dataResource.getBranch().equals(branch)) {
                        for (DataResource.Subcategory subcategory : dataResource.getSubcategories()) {
                            if (subcategory.getArticleTitle().equals(subcategoryTitle)) {
                                // Match found, call the callback with the subcategory
                                callback.onSubcategoryFetched(subcategory);
                                return;
                            }
                        }
                    }
                }

                // If no match is found, return an error
                callback.onError(new Exception("Subcategory with the title \"" + subcategoryTitle + "\" not found in category \"" + category + "\"."));
            }

            @Override
            public void onSubcategoryFetched(DataResource.Subcategory subcategory) {

            }

            @Override
            public void onDataFetchedResource(List<DataResource> dataResources) {

            }

            @Override
            public void onSuccess(Object result) {
                // Handle success if necessary
            }
            @Override
            public void onError(Exception error) {
                Log.e("SubCategoryResourceActivity", "Error fetching data", error);
            }
        });
        // Ensure subcategories are already fetched

    }


    // Fetch resources by branch (Article, Report, Toolkit) and category (e.g., Solar Energy)
    public void getResourcesByBranchAndCategory(String branch, String category, SubcategoryCallback callback) {
        mDatabase.child(branch).child(category).child("subcategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, DataResource> resources = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataResource resource = snapshot.getValue(DataResource.class);
                    if (resource != null) {
                        resources.put(snapshot.getKey(), resource);
                    }
                }
                callback.onSuccess(resources);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    // Fetch a specific subcategory by branch, category, and subcategory ID
    public void getResourceBySubcategoryId(String branch, String category, String subcategoryId, SubcategoryCallback callback) {
        mDatabase.child(branch).child(category).child("subcategories").child(subcategoryId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataResource resource = dataSnapshot.getValue(DataResource.class);
                        if (resource != null) {
                            callback.onSuccess(resource);
                        } else {
                            callback.onError(new Exception("Resource not found"));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.toException());
                    }
                });
    }

    // Fetch comments for a specific subcategory
    public void fetchCommentsForSubcategory(String subcategoryTitle, final CommentsCallback callback) {
        // Ensure that dataResources are already fetched
        if (dataResources.isEmpty()) {
            callback.onError("Data resources not loaded.");
            return;
        }

        DataResource.Subcategory matchedSubcategory = null;
        Log.d("FirebaseResourceFetcher", "Fetching comments for subcategory:" + subcategoryTitle);

        // Iterate over the dataResources to find the subcategory
        for (DataResource dataResource : dataResources) {
            for (DataResource.Subcategory subcategory : dataResource.getSubcategories()) {
                if (subcategory.getArticleTitle().equals(subcategoryTitle)) {
                    Log.d("FirebaseResourceFetcher", "Subcategory title exists in resource");
                    matchedSubcategory = subcategory;
                    break;
                }
            }
            if (matchedSubcategory != null) {
                break;
            }
        }

        // If the subcategory is found, invoke the callback with its comments
        if (matchedSubcategory != null) {
            callback.onCommentsFetched(matchedSubcategory.getComments());
        } else {
            callback.onError("Subcategory not found");
        }
    }


    // Fetch all comments for a given subcategory
    public void getComments(String branch, String category, String subcategoryId, SubcategoryCallback callback) {
        mDatabase.child(branch).child(category).child("subcategories").child(subcategoryId).child("comments")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Comment> comments = new HashMap<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Comment comment = snapshot.getValue(Comment.class);
                            if (comment != null) {
                                comments.put(snapshot.getKey(), comment);
                            }
                        }
                        callback.onSuccess(comments);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.toException());
                    }
                });
    }
    public void addComment(String branch, String category, String subcategoryId, Comment comment, SubcategoryCallback callback) {
        String commentId = mDatabase.child(branch).child(category).child("subcategories").child(subcategoryId).child("comments").push().getKey();
        if (commentId != null) {
            mDatabase.child(branch).child(category).child("subcategories").child(subcategoryId).child("comments").child(commentId).setValue(comment)
                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                    .addOnFailureListener(callback::onError);
        } else {
            callback.onError(new Exception("Failed to generate comment ID"));
        }
    }

    // Update an existing comment
    public void updateComment(String branch, String category, String subcategoryId, String commentId, Comment comment, SubcategoryCallback callback) {
        mDatabase.child(branch).child(category).child("subcategories").child(subcategoryId).child("comments").child(commentId).setValue(comment)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    public interface CommentsCallback {
        void onCommentsFetched(List<Comment> comments);
        void onError(String errorMessage);
    }
    // Callback interface
    public interface FirebaseCallback {
        void onDataFetchedResource(List<DataResource> dataResources); // Called when all data is fetched
        void onSuccess(Object result); // Called when operation is successful
        void onError(Exception error); // Called on error
    }
    public interface SubcategoryCallback {
        void onDataFetched(List<DataResource> dataResources);
        void onSubcategoryFetched(DataResource.Subcategory subcategory); // Called when a subcategory is fetched

        void onDataFetchedResource(List<DataResource> dataResources);

        void onSuccess(Object result); // Called when operation is successful
        void onError(Exception error); // Called on error
    }

}
