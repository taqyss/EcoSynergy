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
                    Log.d("onDataChange", "Branch: " + branch);

                    // Iterate through categories within the branch
                    for (DataSnapshot categorySnapshot : branchSnapshot.getChildren()) {
                        String category = categorySnapshot.getKey();
                        Log.d("onDataChange", "Category: " + category);

                        DataResource dataResource = new DataResource(branch, category);

                        // Create a new list of subcategories for this dataResource
                        List<DataResource.Subcategory> subcategories = new ArrayList<>();

                        // Populate subcategories for this category
                        for (DataSnapshot subcategorySnapshot : categorySnapshot.child("subcategories").getChildren()) {
                            DataResource.Subcategory subcategory = processSubcategory(subcategorySnapshot);
                            subcategories.add(subcategory);

                            Log.d("onDataChange", "Subcategory: " + subcategory.getArticleTitle());
                            Log.d("onDataChange", "Subcategory: " + subcategory.getArticleContent());
                        }

                        // Set the unique subcategories list to the current dataResource
                        dataResource.setSubcategories(subcategories);
                        dataResources.add(dataResource);
                    }
                }

                Log.d("onDataChange", "Fetched Data Resources: " + dataResources.toString());

                // Call callback with the fetched data
                callback.onDataFetched(dataResources);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onDataChange", "Error fetching data", databaseError.toException());
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

                // Iterate through the data resources to find the matching category and branch
                for (DataResource dataResource : dataResources) {
                    if (dataResource.getCategory().equals(category) && dataResource.getBranch().equals(branch)) {
                        // Get the subcategories for the matching category
                        List<DataResource.Subcategory> localSubcategories = dataResource.getSubcategories();

                        if (localSubcategories == null || localSubcategories.isEmpty()) {
                            callback.onError(new Exception("No subcategories found for category \"" + category + "\" in branch \"" + branch + "\"."));
                            return;
                        }

                        // Search for the matching subcategory title
                        for (DataResource.Subcategory subcategory : localSubcategories) {
                            if (subcategory.getArticleTitle().equals(subcategoryTitle)) {
                                // Match found, call the callback with the subcategory
                                callback.onSubcategoryFetched(subcategory);
                                return;
                            }
                        }
                    }
                }

                // If no match is found, return an error
                callback.onError(new Exception("Subcategory with the title \"" + subcategoryTitle + "\" not found in category \"" + category + "\" and branch \"" + branch + "\"."));
            }

            @Override
            public void onSubcategoryFetched(DataResource.Subcategory subcategory) {
                // Not used in this context
            }

            @Override
            public void onDataFetchedResource(List<DataResource> dataResources) {
                // Not used in this context
            }

            @Override
            public void onSuccess(Object result) {
                // Optional handling for success
            }

            @Override
            public void onError(Exception error) {
                Log.e("SubCategoryResourceActivity", "Error fetching data", error);
            }
        });
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

//    public void storeNewComment(String branch, String category, String subcategoryId, Comment newComment, final StoreCommentCallback callback) {
//        // Reference to the subcategory's comments
//        DatabaseReference subcategoryCommentsRef = mDatabase.child(branch).child(category).child("subcategories")
//                .child(subcategoryId).child("comments");
//
//        // Generate a unique comment ID using push()
//        String commentId = subcategoryCommentsRef.push().getKey();
//
//        if (commentId != null) {
//            // Create a map to store the comment with replies
//            Map<String, Object> commentMap = new HashMap<>();
//            commentMap.put("commentText", newComment.getCommentText());
//            commentMap.put("timestamp", newComment.getTimestamp());
//            commentMap.put("upvote", newComment.getVoteCount());
//            commentMap.put("username", newComment.getUsername());
//            commentMap.put("userAvatar", newComment.getUserAvatar());
//
//            // Store replies if there are any (assuming the Comment object has a getReplies() method returning a List)
//            if (newComment.getReplies() != null && !newComment.getReplies().isEmpty()) {
//                List<Map<String, Object>> repliesList = new ArrayList<>();
//                for (Comment reply : newComment.getReplies()) {
//                    Map<String, Object> replyMap = new HashMap<>();
//                    replyMap.put("commentText", reply.getCommentText());
//                    replyMap.put("timestamp", reply.getTimestamp());
//                    replyMap.put("upvote", reply.getVoteCount());
//                    replyMap.put("username", reply.getUsername());
//                    replyMap.put("userAvatar", reply.getUserAvatar());
//                    repliesList.add(replyMap);
//                }
//                commentMap.put("replies", repliesList);
//            }
//
//            // Store the comment with the generated comment ID
//            subcategoryCommentsRef.child(commentId).setValue(commentMap)
//                    .addOnSuccessListener(aVoid -> callback.onCommentStored())
//                    .addOnFailureListener(e -> callback.onError("Failed to store comment"));
//        } else {
//            callback.onError("Failed to generate comment ID");
//        }
//    }

    public void storeNewComment(final String subcategory, final Comment comment, final StoreCommentCallback callback) {
        // Assuming you have a reference to Firebase's Realtime Database or Firestore for resources
        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("resources").child(subcategory).child("comments");

        // Push the new comment to Firebase
        commentRef.push().setValue(comment)
                .addOnSuccessListener(aVoid -> {
                    // Successfully added the comment to Firebase
                    Log.d("FirebaseResourceFetcher", "Comment added successfully for resource: " + subcategory);
                    callback.onCommentStored(); // Notify success
                })
                .addOnFailureListener(e -> {
                    // Failed to add the comment to Firebase
                    Log.e("FirebaseResourceFetcher", "Error adding comment: " + e.getMessage());
                    callback.onError("Error adding comment for resource: " + e.getMessage()); // Notify error
                });
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
