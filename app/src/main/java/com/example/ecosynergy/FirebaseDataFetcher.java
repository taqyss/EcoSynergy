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

    public List<DataModule.Subcategory> subcategories = new ArrayList<>();

    public FirebaseDataFetcher() {
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("dataModules");
        database = FirebaseDatabase.getInstance();
        subcategoryRef = database.getReference("subcategories");
        commentsRef = database.getReference("comments");
    }

    public void fetchDataModules(final FirebaseCallback callback) {
        List<DataModule> dataModules = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot levelSnapshot : dataSnapshot.getChildren()) {
                    String level = levelSnapshot.getKey();

                    for (DataSnapshot categorySnapshot : levelSnapshot.getChildren()) {
                        String category = categorySnapshot.getKey();

                        DataModule dataModule = new DataModule(level, category);

                        // Process all subcategories for this category
                        for (DataSnapshot subcategorySnapshot : categorySnapshot.child("subcategories").getChildren()) {
                            DataModule.Subcategory subcategory = processSubcategory(subcategorySnapshot);
                            subcategories.add(subcategory);

                            // Add questions and answers to each subcategory
                            List<QuestionSet> questionSets = new ArrayList<>();

                            // Iterate through each question and answer
                            for (DataSnapshot questionSnapshot : subcategorySnapshot.child("questions").getChildren()) {
                                String questionNumber = questionSnapshot.getKey();
                                List<String> questionText  = new ArrayList<>();

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
                                // Use the title of the subcategory as the key (you can customize this if needed)
                                dataModule.addQuestionSet(subcategory.getTitle(), questionSet);
                            }
                        }

                        // Add subcategories to dataModule
                        dataModule.setSubcategories(subcategories);

                        // Add the dataModule to the list
                        dataModules.add(dataModule);
                    }
                }

                // Call callback with the fetched data
                callback.onDataFetched(dataModules);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
        subcategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDataFetcher", "Fetching subcategory with ID: " + currentSubcategory);

                DataModule.Subcategory matchedSubcategory = null;
                Log.d("FirebaseDataFetcher", "Datamodule.subcategroy:" );

                // Check if the subcategories list is populated
                if (subcategories != null && !subcategories.isEmpty()) {
                    for (DataModule.Subcategory subcategory : subcategories) {
                        if (subcategory.getTitle().equals(currentSubcategory)) {
                            matchedSubcategory = subcategory;
                            break;
                        }
                    }
                } else {
                    callback.onError("Subcategories not available.");
                    return;
                }

                if (matchedSubcategory != null) {
                    callback.onSubcategoryFetched(matchedSubcategory);
                } else {
                    callback.onError("Subcategory not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError("Error fetching data: " + databaseError.getMessage());
            }
        });
    }

    // Fetch comments for a specific subcategory
    public void fetchCommentsForSubcategory(String subcategoryTitle, final CommentsCallback callback) {
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
    public interface CommentsCallback {
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
        void onDataFetched(List<DataModule> dataModules);

        void onError(String errorMessage);
    }
}
