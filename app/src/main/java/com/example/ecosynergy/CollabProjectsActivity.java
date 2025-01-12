package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.view.ViewGroup;


public class CollabProjectsActivity extends BaseActivity {

    private RecyclerView projectsRecyclerView;
    private ProjectAdapter projectAdapter;
    private List<Project> projectList;
    private List<Project> filteredList; // For search and filter
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    private EditText searchEditText; // For Find functionality
    private ImageView searchIcon, filterIcon; // For Search and Filter functionality

    private boolean isSearchVisible = false; // To track the visibility of the search bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_project1);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize RecyclerView
        projectsRecyclerView = findViewById(R.id.recyclerViewProjects);
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectList = new ArrayList<>();
        filteredList = new ArrayList<>(); // For filtered results
        projectAdapter = new ProjectAdapter(this, filteredList);
        projectsRecyclerView.setAdapter(projectAdapter);

        // Initialize Find and Filter
        searchEditText = findViewById(R.id.searchEditText);
        searchIcon = findViewById(R.id.collabSearchIcon);
        filterIcon = findViewById(R.id.collabFilterIcon);

        // Set up toolbar and navigation
        setupToolbar(true);
        getSupportActionBar().setTitle("Work on Projects");
        setupBottomNavigation();

        // Add project
        setupNavigationListeners();

        // Load projects from Firebase
        loadAllProjects();

        // Set up Find functionality
        setupFind();

        // Set up Search functionality
        setupSearch();

        // Set up Filter functionality
        setupFilter();
    }

    private void loadAllProjects() {
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projectList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot projectsSnapshot = userSnapshot.child("Projects");
                    for (DataSnapshot projectSnapshot : projectsSnapshot.getChildren()) {
                        Project project = projectSnapshot.getValue(Project.class);
                        if (project != null && project.getProjectTitle() != null) {
                            projectList.add(project);
                        }
                    }
                }
                filteredList.clear();
                filteredList.addAll(projectList); // Initially show all projects
                projectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CollabProjectsActivity.this, "Error loading projects: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupNavigationListeners() {
        ImageView collabAddIcon = findViewById(R.id.collabAddIcon);
        TextView addNewTitle = findViewById(R.id.addNewTitle);
        ImageView roundedRectCollab4 = findViewById(R.id.roundedRectCollab4);

        View.OnClickListener navigateToAddProjects = view -> {
            Intent intent = new Intent(CollabProjectsActivity.this, CollabProjectsAddActivity.class);
            startActivity(intent);
        };

        collabAddIcon.setOnClickListener(navigateToAddProjects);
        addNewTitle.setOnClickListener(navigateToAddProjects);
        roundedRectCollab4.setOnClickListener(navigateToAddProjects);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setupSearch() {
        searchIcon.setOnClickListener(view -> {
            if (isSearchVisible) {
                // Hide search bar
                searchEditText.setVisibility(View.GONE);
                isSearchVisible = false;

                // Clear search field and reset list
                searchEditText.setText("");
                filteredList.clear();
                filteredList.addAll(projectList);
                projectAdapter.notifyDataSetChanged();

                // Hide the keyboard
                hideKeyboard();
            } else {
                // Show search bar
                searchEditText.setVisibility(View.VISIBLE);
                searchEditText.requestFocus();
                isSearchVisible = true;

                // Show the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        // Add a listener for when the EditText loses focus
        searchEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                // Hide the search bar and keyboard when focus is lost
                searchEditText.setVisibility(View.GONE);
                isSearchVisible = false;

                hideKeyboard();
            }
        });

        // Optionally, hide the search bar when the RecyclerView is clicked
        projectsRecyclerView.setOnTouchListener((view, motionEvent) -> {
            if (isSearchVisible) {
                searchEditText.clearFocus(); // Remove focus
            }
            return false; // Allow RecyclerView to handle touch events
        });
    }




    private void setupFind() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterProjectsBySearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void filterProjectsBySearch(String query) {
        filteredList.clear();
        for (Project project : projectList) {
            String projectTitle = project.getProjectTitle();
            if (projectTitle != null && projectTitle.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(project);
            }
        }
        projectAdapter.notifyDataSetChanged();
    }

    private void setupFilter() {
        filterIcon.setOnClickListener(view -> {
            // Open a dialog or activity for selecting filter options
            openFilterDialog();
        });
    }

    private void filterProjectsByCategoryAndStatus(String category, String status) {
        filteredList.clear();
        for (Project project : projectList) {
            boolean matchesCategory = category.equals("All") || project.getCategory().equalsIgnoreCase(category);
            boolean matchesStatus = status.equals("All") || project.getStatus().equalsIgnoreCase(status);

            if (matchesCategory && matchesStatus) {
                filteredList.add(project);
            }
        }
        projectAdapter.notifyDataSetChanged();
    }


    private void openFilterDialog() {
        FilterDialogFragment filterDialog = new FilterDialogFragment();
        filterDialog.setFilterListener((selectedCategory, selectedStatus) -> {
            filterProjectsByCategoryAndStatus(selectedCategory, selectedStatus);
        });
        filterDialog.show(getSupportFragmentManager(), "FilterDialog");
    }

    @Override
    public int getCount() {
        // Return the count of items. For example:
        return filteredList != null ? filteredList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return filteredList != null ? filteredList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position; // You can use the position as the item ID if there is no unique ID.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Return null or implement a custom view if needed
        return null;
    }

}
