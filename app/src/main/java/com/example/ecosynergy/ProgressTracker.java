package com.example.ecosynergy;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProgressTracker {
    private static final String PROGRESS_REF = "module_progress";
    private final DatabaseReference userProgressRef;
    private final String userId;

    public ProgressTracker(String userId) {
        this.userId = userId;
        this.userProgressRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .child(PROGRESS_REF);
    }

    public void trackModuleAccess(String categoryId, String categoryName,
                                  int totalSubcategories, ValueEventListener progressListener) {
        userProgressRef.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DashboardModuleProgress progress;
                if (snapshot.exists()) {
                    progress = snapshot.getValue(DashboardModuleProgress.class);
                    if (progress != null) {
                        progress.setCompletedItems(progress.getCompletedItems() + 20);
                    }
                } else {
                    progress = new DashboardModuleProgress(categoryId, categoryName, totalSubcategories, 1);
                }

                if (progress != null) {
                    progress.setLastUpdated(System.currentTimeMillis());
                    userProgressRef.child(categoryId).setValue(progress)
                            .addOnSuccessListener(aVoid -> {
                                if (progressListener != null) {
                                    progressListener.onDataChange(snapshot);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProgressTracker", "Error tracking progress: " + error.getMessage());
            }
        });
    }

    public void getModuleProgress(String categoryId, ValueEventListener listener) {
        userProgressRef.child(categoryId).addValueEventListener(listener);
    }

}
