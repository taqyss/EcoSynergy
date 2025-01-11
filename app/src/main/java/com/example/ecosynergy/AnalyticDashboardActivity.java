package com.example.ecosynergy;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnalyticDashboardActivity extends BaseActivity {

    private static final String TAG = "AnalyticDashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytic_dashboard);

        // Get references to TextView elements
        TextView registrationCount = findViewById(R.id.registration_count);
        TextView loginCount = findViewById(R.id.login_count);

        // Reference to the Firebase database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("UserActivity");

        // Calculate the timestamp for one week ago
        long oneWeekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);

        // Retrieve data from Firebase
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int registrationsThisWeek = 0;
                int loginsThisWeek = 0;

                // Loop through each user in the database
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    try {
                        // Check registration timestamp
                        Long registrationTimestamp = userSnapshot.child("registration").getValue(Long.class);
                        if (registrationTimestamp != null && registrationTimestamp >= oneWeekAgo) {
                            registrationsThisWeek++;
                        }

                        // Check login timestamps
                        DataSnapshot loginsSnapshot = userSnapshot.child("logins");
                        if (loginsSnapshot.exists()) {
                            for (DataSnapshot loginSnapshot : loginsSnapshot.getChildren()) {
                                Long loginTimestamp = loginSnapshot.getValue(Long.class);
                                if (loginTimestamp != null && loginTimestamp >= oneWeekAgo) {
                                    loginsThisWeek++;
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing user data: " + e.getMessage());
                    }
                }

                // Update the UI with the retrieved counts
                registrationCount.setText("Registrations: " + registrationsThisWeek);
                loginCount.setText("Logins: " + loginsThisWeek);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }
}
