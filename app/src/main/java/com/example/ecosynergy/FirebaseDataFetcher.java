package com.example.ecosynergy;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseDataFetcher {
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private DatabaseReference subcategoryRef;
    private DatabaseReference commentsRef;

    public DataModule dataModule;

    public List<DataModule.Subcategory> subcategories = new ArrayList<>();
    List<DataModule> dataModules = new ArrayList<>();

    public FirebaseDataFetcher() {
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("dataModules");
        database = FirebaseDatabase.getInstance();
        subcategoryRef = database.getReference("subcategories");
        commentsRef = database.getReference("comments");
    }

    public void fetchDataModules(final FirebaseCallback callback) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataModules.clear();

                for (DataSnapshot levelSnapshot : dataSnapshot.getChildren()) {
                    String level = levelSnapshot.getKey();
                    Log.d("onDataChange", "level: " + level);

                    for (DataSnapshot categorySnapshot : levelSnapshot.getChildren()) {
                        String category = categorySnapshot.getKey();
                        Log.d("onDataChange", "category: " + category);

                        // Create a new DataModule for each category
                        DataModule dataModule = new DataModule(level, category);

                        // Local list for subcategories
                        List<DataModule.Subcategory> subcategories = new ArrayList<>();

                        // Process all subcategories for this category
                        for (DataSnapshot subcategorySnapshot : categorySnapshot.child("subcategories").getChildren()) {
                            DataModule.Subcategory subcategory = processSubcategory(subcategorySnapshot);

                            // Fetch existing comments for this subcategory
                            List<Comment> comments = new ArrayList<>();
                            if (subcategorySnapshot.hasChild("comments")) {
                                for (DataSnapshot commentSnapshot : subcategorySnapshot.child("comments").getChildren()) {
                                    Comment comment = processComment(commentSnapshot);
                                    comments.add(comment);
                                    Log.d("onDataChange", "Fetched comment: " + comment.getCommentText());
                                }
                            }

                            // Set the fetched comments to the subcategory
                            subcategory.setComments(comments);

                            subcategories.add(subcategory);

                            Log.d("onDataChange", "subcategory: " + subcategory.getTitle());

                            // Add questions and answers to each subcategory
                            List<QuestionSet> questionSets = new ArrayList<>();
                            Log.d("onDataChange", "Processing subcategory: " + subcategorySnapshot.getKey());

                            // Check if the 'questions' node exists
                            if (subcategorySnapshot.hasChild("questions")) {
                                Log.d("onDataChange", "Found questions for subcategory: " + subcategorySnapshot.getKey());

                                // Iterate through each question and answer
                                for (DataSnapshot questionSnapshot : subcategorySnapshot.child("questions").getChildren()) {
                                    Log.d("onDataChange", "Processing question: " + questionSnapshot.getKey());

                                    // Get the question text
                                    String questionText = questionSnapshot.child("questionText").getValue(String.class);
                                    Log.d("onDataChange", "Question text: " + questionText);

                                    // Get the list of answer options for the question
                                    List<String> answers = new ArrayList<>();
                                    if (questionSnapshot.hasChild("options")) {
                                        for (DataSnapshot answerSnapshot : questionSnapshot.child("options").getChildren()) {
                                            String answer = answerSnapshot.getValue(String.class);
                                            answers.add(answer);
                                            Log.d("onDataChange", "Answer option: " + answer);
                                        }
                                    } else {
                                        Log.d("onDataChange", "No options found for question: " + questionSnapshot.getKey());
                                    }

                                    // Get the correct answer for the question
                                    String correctAnswer = questionSnapshot.child("correctAnswer").getValue(String.class);
                                    Log.d("onDataChange", "Correct answer: " + correctAnswer);

                                    // Validate fetched data
                                    if (questionText == null || answers.isEmpty() || correctAnswer == null) {
                                        Log.d("onDataChange", "Incomplete data for question: " + questionSnapshot.getKey());
                                        continue; // Skip incomplete questions
                                    }

                                    // Create a new QuestionSet object
                                    QuestionSet questionSet = new QuestionSet(
                                            Collections.singletonList(questionText), // Wrap questionText in a list
                                            answers,
                                            correctAnswer
                                    );
                                    Log.d("onDataChange", "Created QuestionSet for question: " + questionText);

                                    // Add the QuestionSet to the list
                                    questionSets.add(questionSet);
                                }
                            } else {
                                Log.d("onDataChange", "No questions found for subcategory: " + subcategorySnapshot.getKey());
                            }

                            Log.d("onDataChange", "Total question sets created for subcategory: " + questionSets.size());

                            // Add the list of QuestionSets to the DataModule's question set
                            for (QuestionSet questionSet : questionSets) {
                                dataModule.addQuestionSet(subcategory.getTitle(), questionSet);
                                Log.d("onDataChange", "Added QuestionSet for subcategory: " + subcategory.getTitle());
                            }
                        }

                        // Add subcategories to the DataModule
                        dataModule.setSubcategories(subcategories);

                        // Add the DataModule to the list
                        dataModules.add(dataModule);
                    }
                }

                Log.d("onDataChange firebase", "data modules size:" + dataModules.size());
                // Call the callback with the fetched data
                callback.onDataFetchedModules(dataModules);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onDataChange", "Error fetching data", databaseError.toException());
            }
        });
    }



    // Process a subcategory
    private DataModule.Subcategory processSubcategory(DataSnapshot subcategorySnapshot) {
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

        String subcategoryLevel = subcategorySnapshot.child("level").getValue(String.class);
        String subcategoryTitle = subcategorySnapshot.child("title").getValue(String.class);
        String subcategoryDescription = subcategorySnapshot.child("description").getValue(String.class);
        String contentUrl = subcategorySnapshot.child("contentUrl").getValue(String.class);
        String videoTitle = subcategorySnapshot.child("videoTitle").getValue(String.class);
        String videoDescription = subcategorySnapshot.child("videoDescription").getValue(String.class);

        DataModule.Subcategory subcategory = new DataModule.Subcategory(
                subcategoryId, subcategoryLevel, subcategoryTitle,
                subcategoryDescription, contentUrl, videoTitle, videoDescription
        );

        for (DataSnapshot commentSnapshot : subcategorySnapshot.child("comments").getChildren()) {
            Comment comment = processComment(commentSnapshot);
            subcategory.addComment(comment);
        }

        return subcategory;
    }

    private Comment processComment(DataSnapshot commentSnapshot) {
        String commentText = commentSnapshot.child("commentText").getValue(String.class);
        String username = commentSnapshot.child("username").getValue(String.class);
        String userAvatar = commentSnapshot.child("userAvatar").getValue(String.class);
        String timestamp = commentSnapshot.child("timestamp").getValue(String.class);
        int upvote = commentSnapshot.child("upvote").getValue(Integer.class);

        Comment comment = new Comment(commentText, username, userAvatar, upvote, timestamp);

        // Process replies if any
        if (commentSnapshot.hasChild("replies")) {
            for (DataSnapshot replySnapshot : commentSnapshot.child("replies").getChildren()) {
                Comment reply = processComment(replySnapshot); // Recursive call for replies
                comment.addReply(reply);
            }
        }

        return comment;
    }

    // Fetch Subcategory by ID
    public void fetchSubcategoryById(String currentSubcategory, final SubcategoryCallback callback) {
        if (currentSubcategory == null || currentSubcategory.isEmpty()) {
            Log.e("FirebaseDataFetcher", "Invalid subcategory title provided.");
            callback.onError("Invalid subcategory title.");
            return;
        }

        Log.d("FirebaseDataFetcher", "Fetching subcategory with title: " + currentSubcategory);

        boolean found = false;

        // Iterate through the dataModules to find the matching subcategory
        for (DataModule dataModule : dataModules) {
            for (DataModule.Subcategory subcategory : dataModule.getSubcategories()) {
                if (subcategory.getTitle().equalsIgnoreCase(currentSubcategory)) {
                    Log.d("FirebaseDataFetcher", "Subcategory found: " + subcategory.getTitle());
                    callback.onSubcategoryFetched(subcategory);
                    found = true;
                    break; // Exit the loop once the subcategory is found
                }
            }
            if (found) break; // Exit outer loop if subcategory is found
        }

        // If no subcategory is found after processing all dataModules
        if (!found) {
            Log.w("FirebaseDataFetcher", "Subcategory not found for title: " + currentSubcategory);
            callback.onError("Subcategory not found.");
        }
    }

    // Fetch comments for a specific subcategory
    // Fetch comments for a specific subcategory
    public void fetchCommentsForSubcategory(String subcategoryTitle, final CommentsModuleCallback callback) {
        // Find the subcategory with the matching title
        DataModule.Subcategory matchedSubcategory = null;
        Log.d("FirebaseDataFetcher", "Fetching comments for subcategory: " + subcategoryTitle);
        Log.d("FirebaseDataFetcher", "Subcategories in comment: " + dataModules.size());

        // Iterate through each DataModule and its subcategories
        for (DataModule dataModule : dataModules) {
            // Iterate through subcategories in the current DataModule
            for (DataModule.Subcategory subcategory : dataModule.getSubcategories()) {
                if (subcategory.getTitle().equals(subcategoryTitle)) {
                    Log.d("FirebaseDataFetcher", "Subcategory title exists");
                    matchedSubcategory = subcategory;
                    break; // Break out of the loop once the subcategory is found
                } else {
                    Log.d("FirebaseDataFetcher", "Subcategory title doesn't exist");
                }
            }
            if (matchedSubcategory != null) {
                break; // If matched subcategory is found, break out of the outer loop as well
            }
        }

        // Return the comments if the subcategory is found
        if (matchedSubcategory != null) {
            callback.onCommentsFetched(matchedSubcategory.getComments());
        } else {
            callback.onError("Subcategory not found");
        }
    }

    // Store a new comment for a subcategory
//    public void storeNewComment(String subcategoryTitle, Comment newComment, final StoreCommentCallback callback) {
//        DatabaseReference subcategoryCommentsRef = commentsRef.child(subcategoryTitle);
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
//            // Store replies if there are any (assuming the Comment object has a `getReplies()` method returning a List)
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
        // Assuming you have a reference to Firebase's Realtime Database or Firestore for modules
        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("modules").child(subcategory).child("comments");

        // Push the new comment to Firebase
        commentRef.push().setValue(comment)
                .addOnSuccessListener(aVoid -> {
                    // Successfully added the comment to Firebase
                    Log.d("FirebaseDataFetcher", "Comment added successfully for module: " + subcategory);
                    callback.onCommentStored(); // Notify success
                })
                .addOnFailureListener(e -> {
                    // Failed to add the comment to Firebase
                    Log.e("FirebaseDataFetcher", "Error adding comment: " + e.getMessage());
                    callback.onError("Error adding comment for module: " + e.getMessage()); // Notify error
                });
    }




    public void addReplyToComment(Comment comment, Comment reply) {
        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("comments")
                .child(comment.getUsername()) // Use the comment's username to identify the comment
                .child("replies") // Reference the replies node
                .child(reply.getUsername()); // Use the reply's username as the key for the reply

        // Set the reply
        commentRef.setValue(reply);
    }

    public void updateVoteCount(Comment comment) {
        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("comments")
                .child(comment.getUsername()) // Use the comment's username as the unique identifier
                .child("upvote");

        // Update the upvote count
        commentRef.setValue(comment.getVoteCount());
    }

    // Callback interface for subcategory fetching
    public interface SubcategoryCallback {
        void onSubcategoryFetched(DataModule.Subcategory subcategory);

        void onError(String errorMessage);
    }

    // Callback interface for comments
    public interface CommentsModuleCallback {
        void onCommentsFetched(List<Comment> comments);

        void onError(String errorMessage);
    }
    // Callback interface for data fetching
    public interface FirebaseCallback {

        void onDataFetchedModules(List<DataModule> dataModules);

        void onError(String errorMessage);
    }
}