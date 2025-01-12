package com.example.ecosynergy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnalyticDashboardActivity extends BaseActivity {

    private DatabaseReference databaseReference;
    private TextView activeUsersTextView;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Provide an implementation based on your app's needs
        return null; // Placeholder
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytic_dashboard);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("UserActivity");

        // Initialize TextView
        activeUsersTextView = findViewById(R.id.tv_active_users);
        fetchActiveUsersLast7Days();

        ImageView backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("UserActivity");

        long oneWeekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int registrationsThisWeek = 0;
                int loginsThisWeek = 0;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Long registrationTimestamp = userSnapshot.child("registration").getValue(Long.class);
                    if (registrationTimestamp != null && registrationTimestamp >= oneWeekAgo) {
                        registrationsThisWeek++;
                    }

                    DataSnapshot loginsSnapshot = userSnapshot.child("logins");
                    if (loginsSnapshot.exists()) {
                        for (DataSnapshot loginSnapshot : loginsSnapshot.getChildren()) {
                            Long loginTimestamp = loginSnapshot.getValue(Long.class);
                            if (loginTimestamp != null && loginTimestamp >= oneWeekAgo) {
                                loginsThisWeek++;
                            }
                        }
                    }
                }

                updateDashboardUI(registrationsThisWeek, loginsThisWeek);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void fetchActiveUsersLast7Days() {
        long currentTime = System.currentTimeMillis();
        long sevenDaysAgo = currentTime - (7 * 24 * 60 * 60 * 1000);

        databaseReference.orderByChild("lastLogin")
                .startAt(sevenDaysAgo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int activeUsers = 0;
                        Log.d("ActiveUserCheck", "Number of users fetched: " + snapshot.getChildrenCount());

                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            Long lastLogin = userSnapshot.child("lastLogin").getValue(Long.class);
                            long lastLoginTime = 0;
                            Log.d("ActiveUserCheck", "User ID: " + userSnapshot.getKey() +
                                    ", Last Login: " + lastLogin);

                            // Check if the user is active within the last 7 days
                            if (lastLogin != null && lastLogin >= sevenDaysAgo) {
                                activeUsers++;
                            }
                        }

                        // Display the active users count
                        activeUsersTextView.setText("Active Users (Last 7 Days): " + activeUsers);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AnalyticDashboardActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private void updateDashboardUI(int registrations, int logins) {
        TextView registrationCount = findViewById(R.id.registration_count);
        TextView loginCount = findViewById(R.id.login_count);

        registrationCount.setText("Registrations: " + registrations);
        loginCount.setText("Logins: " + logins);
    }
}
