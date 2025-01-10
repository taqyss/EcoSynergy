package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

// Firebase Authentication
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Firebase Realtime Database
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CollabProjectsAddActivity extends BaseActivity{
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ImageView selectedImageView = null; // To track the currently selected ImageView
    private TextView selectedTextView = null; // To track the currently selected TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_project2);

        // Set up the toolbar with back button enabled
        setupToolbar(true);
        getSupportActionBar().setTitle("Work On Projects");

        // Set up bottom navigation
        setupBottomNavigation();

        ImageView collabSearchUserIcon = findViewById(R.id.collabSearcUserIcon);
        View.OnClickListener navigateToSearchUserProjects = view -> {
            Intent intent = new Intent(CollabProjectsAddActivity.this, CollabSearchUserActivity.class);
            startActivity(intent);
        };

        collabSearchUserIcon.setOnClickListener(navigateToSearchUserProjects);


        TextView totalCollaborators = findViewById(R.id.totalCollaborators);
        ImageView collabCircle1 = findViewById(R.id.collabCircle1);
        ImageView plusIcon = findViewById(R.id.plusIcon);
        ImageView minusIcon = findViewById(R.id.minusIcon);
        ImageView collabCircle2 = findViewById(R.id.collabCircle2);

        final int MAX_COUNT = 20;
        final int MIN_COUNT = 0;

        // Decrement Click Listener
        View.OnClickListener decrementClickListener = v -> {
            int currentCount = Integer.parseInt(totalCollaborators.getText().toString());
            if (currentCount > MIN_COUNT) {
                totalCollaborators.setText(String.valueOf(currentCount - 1));
            } else {
                Toast.makeText(this, "Minimum limit reached!", Toast.LENGTH_SHORT).show();
            }
        };

// Increment Click Listener
        View.OnClickListener incrementClickListener = v -> {
            int currentCount = Integer.parseInt(totalCollaborators.getText().toString());
            if (currentCount < MAX_COUNT) {
                totalCollaborators.setText(String.valueOf(currentCount + 1));
            } else {
                Toast.makeText(this, "Maximum limit reached!", Toast.LENGTH_SHORT).show();
            }
        };
        // Attach the same listener to both views
        collabCircle1.setOnClickListener(incrementClickListener);
        plusIcon.setOnClickListener(incrementClickListener);
        collabCircle2.setOnClickListener(decrementClickListener);
        minusIcon.setOnClickListener(decrementClickListener);

        setupSelectableOptions();

        // Initialize Firebase Auth and Database Reference
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        TextView publishText = findViewById(R.id.publishText);
        ImageView publishButton = findViewById(R.id.publishButton);

        View.OnClickListener publishClickListener = v -> saveProjectToFirebase();

        publishText.setOnClickListener(publishClickListener);
        publishButton.setOnClickListener(publishClickListener);

    }
    private void saveProjectToFirebase() {
        FirebaseUser user = auth.getCurrentUser(); // Use the global 'auth' instance
        if (user != null) {
            // Retrieve user ID
            String userId = user.getUid();

            // Generate a unique ID for the project
            String projectId = databaseReference.child("Users").child(userId).child("Projects").push().getKey();

            // Get the values from the UI
            String projectTitle = ((EditText) findViewById(R.id.projectTitleEdit)).getText().toString();
            String category = selectedTextView != null ? selectedTextView.getText().toString() : null;
            int collaboratorAmount = Integer.parseInt(((TextView) findViewById(R.id.totalCollaborators)).getText().toString());
            String description = ((EditText) findViewById(R.id.editTextDescription)).getText().toString();
            String link = ((EditText) findViewById(R.id.linkEditText)).getText().toString();
            String status = ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_group_status)).getCheckedRadioButtonId())).getText().toString();

            // Create a new Project object
            Project project = new Project(projectTitle, category, collaboratorAmount, description, link, status);

            // Save the project to Firebase
            databaseReference.child("Users").child(userId).child("Projects").child(projectId).setValue(project)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Project saved successfully!", Toast.LENGTH_SHORT).show();

                            // Optional: Reset the form after successful submission
                            resetForm();
                        } else {
                            Toast.makeText(this, "Failed to save project.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetForm() {
        ((EditText) findViewById(R.id.projectTitleEdit)).setText("");
        ((TextView) findViewById(R.id.totalCollaborators)).setText("0");
        ((EditText) findViewById(R.id.editTextDescription)).setText("");
        ((EditText) findViewById(R.id.linkEditText)).setText("");
        ((RadioGroup) findViewById(R.id.radio_group_status)).clearCheck();
        selectedImageView.setSelected(false);
        selectedTextView.setSelected(false);
    }


    private void handleOptionSelection(ImageView imageView, TextView textView) {
        // Deselect the previously selected ImageView (if any)
        if (selectedImageView != null) {
            selectedImageView.setSelected(false);
            selectedTextView.setSelected(false);
        }

        // Select the new ImageView
        imageView.setSelected(true);
        textView.setSelected(true);

        // Update the references to the selected option
        selectedImageView = imageView;
        selectedTextView = textView;
    }

    private void setupOptionClickListener(ImageView imageView, TextView textView) {
        // Handle clicks on the ImageView
        imageView.setOnClickListener(v -> handleOptionSelection(imageView, textView));

        // Handle clicks on the TextView
        textView.setOnClickListener(v -> handleOptionSelection(imageView, textView));
    }
    private void setupSelectableOptions() {
        // Define the ImageViews and TextViews for the options
        ImageView optionImage1 = findViewById(R.id.roundedRectCollab10);
        TextView optionText1 = findViewById(R.id.EnergyAccess);

        ImageView optionImage2 = findViewById(R.id.roundedRectCollab11);
        TextView optionText2 = findViewById(R.id.RenewableEnergy);

        ImageView optionImage3 = findViewById(R.id.roundedRectCollab12);
        TextView optionText3 = findViewById(R.id.SustainableAndSocial);

        ImageView optionImage4 = findViewById(R.id.roundedRectCollab13);
        TextView optionText4 = findViewById(R.id.CleanEnergyTechnology);

        ImageView optionImage5 = findViewById(R.id.roundedRectCollab14);
        TextView optionText5 = findViewById(R.id.PolicyAndEconomics);

        ImageView optionImage6 = findViewById(R.id.roundedRectCollab15);
        TextView optionText6 = findViewById(R.id.EnergyEfficiency);

        ImageView optionImage7 = findViewById(R.id.roundedRectCollab16);
        TextView optionText7 = findViewById(R.id.Other);

        // Add click listeners for each option
        setupOptionClickListener(optionImage1, optionText1);
        setupOptionClickListener(optionImage2, optionText2);
        setupOptionClickListener(optionImage3, optionText3);
        setupOptionClickListener(optionImage4, optionText4);
        setupOptionClickListener(optionImage5, optionText5);
        setupOptionClickListener(optionImage6, optionText6);
        setupOptionClickListener(optionImage7, optionText7);

    }


    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // This is the ID of the "Collaboration" menu item
    }
}
