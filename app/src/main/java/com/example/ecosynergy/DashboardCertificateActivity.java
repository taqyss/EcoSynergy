package com.example.ecosynergy;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import android.widget.HorizontalScrollView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;

public class DashboardCertificateActivity extends BaseActivity{
    /*private Context context;
    private LinearLayout badgeContainer, certificateContainer;  //THIS COMMENT IS NOT DONE YET
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_certificate);
        context = this;

        // Set up the toolbar with back button enabled
        setupToolbar(true);
        getSupportActionBar().setTitle("Badge & Certificate");
        setupBottomNavigation();

        // Initialize Views
        /*badgeContainer = findViewById(R.id.badgeContainer);
        certificateContainer = findViewById(R.id.certificateContainer);

        // Initialize Firebase (uncomment when Firebase is set up)
        initializeFirebase();
        loadBadgesAndCertificates();

        // For testing, add some dummy data
        addDummyData();
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(mAuth.getCurrentUser().getUid());
    }

    private void loadBadgesAndCertificates() {
        // Load Badges
        userRef.child("badges").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                badgeContainer.removeAllViews(); // Clear existing badges
                for (DataSnapshot badgeSnapshot : snapshot.getChildren()) {
                    Badge badge = badgeSnapshot.getValue(Badge.class);
                    if (badge != null) {
                        addBadge(badge);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to load badges", Toast.LENGTH_SHORT).show();
            }
        });

        // Load Certificates
        userRef.child("certificates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                certificateContainer.removeAllViews(); // Clear existing certificates
                for (DataSnapshot certSnapshot : snapshot.getChildren()) {
                    Certificate cert = certSnapshot.getValue(Certificate.class);
                    if (cert != null) {
                        addCertificate(cert);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to load certificates", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBadge(Badge badge) {
        ImageView badgeView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dpToPx(100), // width: 100dp
                dpToPx(100)  // height: 100dp
        );
        params.setMargins(0, 0, dpToPx(8), 0); // 8dp right margin
        badgeView.setLayoutParams(params);
        badgeView.setImageResource(R.drawable.badge); // Your badge drawable
        badgeView.setContentDescription(badge.getDescription());

        // Add badge view to container
        badgeContainer.addView(badgeView);

        // Add animation
        badgeView.setAlpha(0f);
        badgeView.animate()
                .alpha(1f)
                .setDuration(500)
                .start();
    }

    private void addCertificate(Certificate certificate) {
        View certView = getLayoutInflater().inflate(R.layout.certificate_layout, certificateContainer, false);

        ImageView certImage = certView.findViewById(R.id.certificateImage);
        TextView certTitle = certView.findViewById(R.id.certificateTitle);

        certTitle.setText(certificate.getTitle());
        certImage.setImageResource(R.drawable.certificate);

        // Add click listener for download
        certView.setOnClickListener(v -> downloadCertificate(certificate));

        certificateContainer.addView(certView);
    }

    private void downloadCertificate(Certificate certificate) {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1
            );
            return;
        }

        // Create certificate image/PDF and save
        try {
            String fileName = "certificate_" + certificate.getId() + ".jpg";
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, fileName);

            // Save certificate
            FileOutputStream out = new FileOutputStream(file);
            // Add certificate generation logic here
            out.flush();
            out.close();

            Toast.makeText(this, "Certificate downloaded to Downloads folder",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to download certificate",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Helper method to convert dp to pixels
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // For testing purposes
    private void addDummyData() {
        // Add some test badges
        for (int i = 0; i < 5; i++) {
            Badge badge = new Badge("badge" + i, "Login Streak " + (i+1) * 5 + " Days");
            addBadge(badge);
        }

        // Add some test certificates
        for (int i = 0; i < 3; i++) {
            Certificate cert = new Certificate(
                    "cert" + i,
                    "Module " + (i+1) + " Completion Certificate"
            );
            addCertificate(cert);
        }
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_certificate);

        // Set up the toolbar with back button enabled
        setupToolbar(true);

        //Set a custom title, or leave it blank if there's no need for a title
        getSupportActionBar().setTitle("Badge & Certificate");

        // Set up bottom navigation
        setupBottomNavigation();
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

