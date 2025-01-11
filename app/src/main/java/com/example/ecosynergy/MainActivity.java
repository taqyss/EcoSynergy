package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBottomNavigation(); // Setup the bottom navigation bar

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        TextView homeMessage = findViewById(R.id.home_message);
        TextView streakText = findViewById(R.id.streakText);

        // News Section Navigation
        LinearLayout newsSection = findViewById(R.id.newsSection);
        newsSection.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(intent);
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch data from Firebase and update the UI
            fetchUserData(userId, homeMessage, streakText);
        }
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private void fetchUserData(String userId, TextView homeMessage, TextView streakText) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    homeMessage.setText("Welcome back, " + username + "!");

                    // Handle streak logic
                    String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    if (snapshot.child("lastLogin").exists()) {
                        String lastLogin = snapshot.child("lastLogin").getValue(String.class);

                        if (!today.equals(lastLogin)) {
                            // If today is different from lastLogin, calculate streak
                            updateStreak(userId, snapshot, today, streakText);
                        } else {
                            // If today is the same, just update the UI
                            int streak = snapshot.child("streak").getValue(Integer.class);
                            streakText.setText("You Have a " + streak + " Day Streak!");
                            updateLeafIcons(streak);
                        }
                    } else {
                        // No lastLogin exists, initialize streak
                        initializeStreak(userId, today, streakText);
                    }
                } else {
                    homeMessage.setText("Welcome back!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Error: " + error.getMessage());
            }
        });
    }

    private void updateStreak(String userId, DataSnapshot snapshot, String today, TextView streakText) {
        int streak = 1; // Default streak value
        try {
            String lastLogin = snapshot.child("lastLogin").getValue(String.class);
            Date lastLoginDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(lastLogin);
            Date todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(today);

            Calendar calLast = Calendar.getInstance();
            Calendar calToday = Calendar.getInstance();

            calLast.setTime(lastLoginDate);
            calToday.setTime(todayDate);

            calLast.add(Calendar.DAY_OF_YEAR, 1); // Check for consecutive login
            if (calLast.get(Calendar.DAY_OF_YEAR) == calToday.get(Calendar.DAY_OF_YEAR) &&
                    calLast.get(Calendar.YEAR) == calToday.get(Calendar.YEAR)) {
                streak = snapshot.child("streak").exists() ? snapshot.child("streak").getValue(Integer.class) + 1 : 1;
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error parsing dates: " + e.getMessage());
        }

        // Update Firebase and UI
        databaseReference.child(userId).child("lastLogin").setValue(today);
        databaseReference.child(userId).child("streak").setValue(streak);
        streakText.setText("You Have a " + streak + " Day Streak!");
        updateLeafIcons(streak);
    }

    private void initializeStreak(String userId, String today, TextView streakText) {
        databaseReference.child(userId).child("lastLogin").setValue(today);
        databaseReference.child(userId).child("streak").setValue(1);
        streakText.setText("You Have a 1 Day Streak!");
        updateLeafIcons(1);
    }

    private void updateLeafIcons(int streak) {
        // Find all leaf ImageView references
        ImageView[] leaves = new ImageView[]{
                findViewById(R.id.leaf1),
                findViewById(R.id.leaf2),
                findViewById(R.id.leaf3),
                findViewById(R.id.leaf4),
                findViewById(R.id.leaf5),
                findViewById(R.id.leaf6),
                findViewById(R.id.leaf7)
        };

        // Show the correct number of leaves according to the streak
        for (int i = 0; i < leaves.length; i++) {
            if (i < streak) {
                leaves[i].setVisibility(View.VISIBLE);
            } else {
                leaves[i].setVisibility(View.GONE);
            }
        }
    }
}
