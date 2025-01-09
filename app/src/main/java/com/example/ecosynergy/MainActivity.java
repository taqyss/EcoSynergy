package com.example.ecosynergy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        TextView homeMessage = findViewById(R.id.home_message);
        TextView streakText = findViewById(R.id.streakText);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);
                        homeMessage.setText("Welcome back, " + username + "!");

                        // Handle streak logic
                        updateStreak(userId, streakText);
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
    }

    private void updateStreak(String userId, TextView streakText) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                int streak = 1;

                if (snapshot.exists()) {
                    String lastLogin = snapshot.child("lastLogin").getValue(String.class);

                    try {
                        Date lastLoginDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(lastLogin);
                        Date todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(today);

                        Calendar calLast = Calendar.getInstance();
                        Calendar calToday = Calendar.getInstance();

                        calLast.setTime(lastLoginDate);
                        calToday.setTime(todayDate);

                        calLast.add(Calendar.DAY_OF_YEAR, 1);
                        if (calLast.get(Calendar.DAY_OF_YEAR) == calToday.get(Calendar.DAY_OF_YEAR)) {
                            streak = snapshot.child("streak").exists() ? snapshot.child("streak").getValue(Integer.class) + 1 : 1;
                        }
                    } catch (Exception e) {
                        Log.e("MainActivity", "Error parsing dates");
                    }
                }

                databaseReference.child(userId).child("lastLogin").setValue(today);
                databaseReference.child(userId).child("streak").setValue(streak);
                streakText.setText("You Have a " + streak + " Day Streak!");
                updateLeafIcons(streak);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to update streak", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Error: " + error.getMessage());
            }
        });
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

