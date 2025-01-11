package com.example.ecosynergy;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnalyticDashboardActivity extends BaseActivity {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Provide an implementation based on your app's needs
        return null; // Placeholder
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytic_dashboard);

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
