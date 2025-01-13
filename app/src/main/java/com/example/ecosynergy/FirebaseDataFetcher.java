package com.example.ecosynergy;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

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
                for (DataSnapshot levelSnapshot : dataSnapshot.getChildren()) {
                    String level = levelSnapshot.getKey();
                    Log.d("onDataChange", "level: " + level);

                    for (DataSnapshot categorySnapshot : levelSnapshot.getChildren()) {
                        String category = categorySnapshot.getKey();
                        Log.d("onDataChange", "level: " + level);
                        Log.d("onDataChange", "category: " + category);

                        // Create a new DataModule for each category
                        DataModule dataModule = new DataModule(level, category);

                        // Local list for subcategories
                        List<DataModule.Subcategory> subcategories = new ArrayList<>();

                        // Process all subcategories for this category
                        for (DataSnapshot subcategorySnapshot : categorySnapshot.child("subcategories").getChildren()) {
                            DataModule.Subcategory subcategory = processSubcategory(subcategorySnapshot);
                            subcategories.add(subcategory);

                            Log.d("onDataChange", "subcategory: " + subcategory.getTitle());

                            // Add questions and answers to each subcategory
                            List<QuestionSet> questionSets = new ArrayList<>();

                            // Iterate through each question and answer
                            for (DataSnapshot questionSnapshot : subcategorySnapshot.child("questions").getChildren()) {
                                String questionNumber = questionSnapshot.getKey();
                                List<String> questionText = new ArrayList<>(); // Populate this list if needed

                                // Get the list of answer options for the question
                                List<String> answers = new ArrayList<>();
                                for (DataSnapshot answerSnapshot : subcategorySnapshot.child("answers").child(questionNumber).getChildren()) {
                                    answers.add(answerSnapshot.getValue(String.class));
                                }

                                // Get the correct answer for the question
                                String correctAnswer = subcategorySnapshot.child("correctAnswers").child(questionNumber).getValue(String.class);

                                // Create a new QuestionSet object with the correct answer
                                QuestionSet questionSet = new QuestionSet(questionText, answers, correctAnswer);

                                // Add the QuestionSet to the list
                                questionSets.add(questionSet);
                            }

                            // Add the list of QuestionSets to the DataModule's question set
                            for (QuestionSet questionSet : questionSets) {
                                dataModule.addQuestionSet(subcategory.getTitle(), questionSet);
                                Log.d("onDataChange", "questionSet: " + subcategory.getTitle());
                            }
                        }

                        // Add subcategories to the DataModule
                        dataModule.setSubcategories(subcategories);

                        // Add the DataModule to the list
                        dataModules.add(dataModule);
                    }
                }

                // Debugging: Log all Basic-level DataModules
                for (DataModule dm : dataModules) {
                    if ("Basic".equals(dm.getLevel())) {
                        Log.d("onDataChange", "DM: " + dm.getCategory());
                    }
                }

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
        String userAvatar = commentSnapshot.child("userAvatar").getValue(String.class);
        String username = commentSnapshot.child("username").getValue(String.class);
        String commentText = commentSnapshot.child("commentText").getValue(String.class);
        int upvote = commentSnapshot.child("upvote").getValue(Integer.class);
        String timestamp = commentSnapshot.child("timestamp").getValue(String.class);

        // Creating Comment object
        Comment comment = new Comment(userAvatar, username, commentText, upvote, timestamp);

        // Handling replies if any
        for (DataSnapshot replySnapshot : commentSnapshot.child("replies").getChildren()) {
            Comment reply = processComment(replySnapshot);
            comment.addReply(reply);
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
    public void fetchCommentsForSubcategory(String subcategoryTitle, final CommentsModuleCallback callback) {
        // Find the subcategory with matching title
        DataModule.Subcategory matchedSubcategory = null;
        Log.d("FirebaseDataFetcher", "Fetching comments for subcategory:" + subcategoryTitle);
        for (DataModule.Subcategory subcategory : subcategories) {
            if (subcategory.getTitle().equals(subcategoryTitle)) {
                Log.d("FirebaseDataFetcher", "Subcategory title exist");
                matchedSubcategory = subcategory;
                break;
            } else Log.d("FirebaseDataFetcher", "Subcategory title doesnt exist");
        }

        if (matchedSubcategory != null) {
            callback.onCommentsFetched(matchedSubcategory.getComments());
        } else {
            callback.onError("Subcategory not found");
        }
    }

    // Store a new comment for a subcategory
    public void storeNewComment(String subcategoryTitle, Comment newComment, final StoreCommentCallback callback) {
        DatabaseReference subcategoryCommentsRef = commentsRef.child(subcategoryTitle);

        String commentId = subcategoryCommentsRef.push().getKey();
        if (commentId != null) {
            subcategoryCommentsRef.child(commentId).setValue(newComment)
                    .addOnSuccessListener(aVoid -> callback.onCommentStored())
                    .addOnFailureListener(e -> callback.onError("Failed to store comment"));
        } else {
            callback.onError("Failed to generate comment ID");
        }
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

    // Callback interface for storing comment
    public interface StoreCommentCallback {
        void onCommentStored();

        void onError(String errorMessage);
    }

    // Callback interface for data fetching
    public interface FirebaseCallback {

        void onDataFetchedModules(List<DataModule> dataModules);

        void onError(String errorMessage);
    }
}
