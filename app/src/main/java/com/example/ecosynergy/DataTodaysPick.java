package com.example.ecosynergy;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataTodaysPick {

    private static final String TAG = "FirebaseHelper";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    public DataTodaysPick() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("todaysPick");  // Updated to "todaysPick"
    }

    private <T> void fetchData(String childNode, DataParser<T> parser, DataCallback<List<T>> callback) {
        mRef.child(childNode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<T> items = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    T item = parser.parse(snapshot);
                    if (item != null) {
                        items.add(item);
                    }
                }
                // Pass the fetched items to the callback
                callback.onDataFetched(items);
                Log.d(TAG, "Fetched " + items.size() + " items from " + childNode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error fetching " + childNode + ": " + databaseError.getMessage());
            }
        });
    }

    public void fetchArticles(final DataCallback<List<Article>> callback) {
        fetchData("DidYouKnow/articles", snapshot -> {  // Path adjusted
            String title = snapshot.child("title").getValue(String.class);
            String snippet = snapshot.child("snippet").getValue(String.class);  // Adjusted to match the field name
            if (title != null && snippet != null) {
                return new Article(title, snippet);
            }
            return null;
        }, callback);
    }

    public void fetchVideos(final DataCallback<List<Video>> callback) {
        fetchData("CheckThisOut/videos", snapshot -> {  // Path adjusted
            String title = snapshot.child("videoTitle").getValue(String.class);  // Adjusted to match the field name
            String url = snapshot.child("videoURL").getValue(String.class);  // Adjusted to match the field name
            if (title != null && url != null) {
                return new Video(title, url, "");  // Assuming no thumbnail in this example
            }
            return null;
        }, callback);
    }

    public void fetchQuestions(final DataCallback<List<Questions>> callback) {
        fetchData("TestYourKnowledge/questions", snapshot -> {  // Path adjusted
            String questionText = snapshot.child("question").getValue(String.class);
            if (questionText != null) {
                return new Questions(questionText, null);  // Answers could be processed further
            }
            return null;
        }, callback);
    }

    // DataParser interface for parsing snapshot into objects
    public interface DataParser<T> {
        T parse(DataSnapshot snapshot);
    }

    // DataCallback interface to handle the fetched data
    public interface DataCallback<T> {
        void onDataFetched(T data);
    }
}
