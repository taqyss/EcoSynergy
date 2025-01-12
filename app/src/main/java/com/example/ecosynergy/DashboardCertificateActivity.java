package com.example.ecosynergy;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardCertificateActivity extends BaseActivity {
    private ImageView badge1, badge2, badge3;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_certificate);

        // Set up the toolbar with back button enabled
        setupToolbar(true);
        getSupportActionBar().setTitle("Badge & Certificate");
        setupBottomNavigation();

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Initialize badge views
        badge1 = findViewById(R.id.badge1);
        badge2 = findViewById(R.id.badge2);
        badge3 = findViewById(R.id.badge3);

        // Initially hide all badges
        badge1.setVisibility(View.GONE);
        badge2.setVisibility(View.GONE);
        badge3.setVisibility(View.GONE);

        // Load badges based on streak
        loadBadges();
    }

    private void loadBadges() {
        userRef.child("streak").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer streak = snapshot.getValue(Integer.class);
                    if (streak != null) {
                        // Show badges based on streak
                        if (streak >= 3) {
                            badge1.setVisibility(View.VISIBLE);
                        }
                        if (streak >= 5) {
                            badge2.setVisibility(View.VISIBLE);
                        }
                        if (streak >= 7) {
                            badge3.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_dashboard; // This is the ID of the "Dashboard" menu item
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
}
