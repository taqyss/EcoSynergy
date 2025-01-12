package com.example.ecosynergy;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecosynergy.models.Module;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContentManagementActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ModuleAdapter adapter;
    private List<Module> moduleList;
    private List<Module> filteredModuleList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_management);
        setupBottomNavigation();

        databaseReference = FirebaseDatabase.getInstance().getReference("modules");

        //Initalize mock data
        moduleList = new ArrayList<>();

        // Initialize buttons
        Button btnEditModule = findViewById(R.id.btn_editmodule);
        Button btnDelete = findViewById(R.id.btn_delete);
        Button btnCreate = findViewById(R.id.btn_create);

        ImageView backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SearchView searchView = findViewById(R.id.search);

        // Initialize the filtered list
        filteredModuleList = new ArrayList<>(moduleList);

        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterModules(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterModules(newText);
                return true;
            }
        });


        // Initialize recycler view
        recyclerView = findViewById(R.id.recycler_view_module);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up adapter
        adapter = new ModuleAdapter(moduleList, new ModuleAdapter.OnModuleClickListener() {
            @Override

            public void onEdit(Module module) {
                showEditModuleDialog(module);
            }

            @Override
            public void onDelete(int position) {
                showDeleteModuleDialog(position);
            }
        });
        recyclerView.setAdapter(adapter);

        // Add this listener for the create button
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateModuleDialog();
            }
        });

        recyclerView.scrollToPosition(0); // Scroll to top after filter

        loadModulesFromDatabase();
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

    private void loadModulesFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                moduleList.clear();
                for (DataSnapshot moduleSnapshot : snapshot.getChildren()) {
                    Module module = moduleSnapshot.getValue(Module.class);
                    moduleList.add(module);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContentManagementActivity.this, "Failed to load modules", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditModuleDialog(Module module) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_module, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        EditText etModuleName = dialogView.findViewById(R.id.etModuleName);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
        SeekBar seekBar = dialogView.findViewById(R.id.seekBar);
        TextView progressText = dialogView.findViewById(R.id.progressText);

        etModuleName.setText(module.getName());
        int currentProgress = module.getProgress();
        progressBar.setProgress(currentProgress);
        seekBar.setProgress(currentProgress);
        progressText.setText("Progress: " + currentProgress + "%");

        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // Update ProgressBar as SeekBar is changed
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar.setProgress(progress);
                progressText.setText("Progress: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(view -> {
            String newModuleName = etModuleName.getText().toString();

            if (!newModuleName.isEmpty()) {

                    int newProgress = seekBar.getProgress();

                    module.setName(newModuleName);
                    module.setProgress(newProgress);

                    // update the module in the database
                    databaseReference.child(module.getName()).setValue(module)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Module Edited: " + newModuleName + " with progress " + newProgress, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Failed to update module", Toast.LENGTH_SHORT).show();
                            }
                        });


                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Module Edited: " + newModuleName + " with progress " + newProgress , Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter a module name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showDeleteModuleDialog(int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_delete_module, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        Button btnConfirmDelete = dialogView.findViewById(R.id.btnConfirmDelete);
        Button btnCancelDelete = dialogView.findViewById(R.id.btnCancelDelete);

        AlertDialog dialog = builder.create();

        btnCancelDelete.setOnClickListener(view -> dialog.dismiss());

        btnConfirmDelete.setOnClickListener(view -> {
            Module moduleToDelete = moduleList.get(position);

            // Remove from Firebase
            String moduleId = moduleToDelete.getName();  // Get the name of the module to delete
            if (moduleId != null) {
                databaseReference.child(moduleId).removeValue()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Remove the module from the local list and notify the adapter
                                moduleList.remove(position);
                                filteredModuleList.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(ContentManagementActivity.this, "Module Deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ContentManagementActivity.this, "Failed to delete module", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(ContentManagementActivity.this, "Module ID is null", Toast.LENGTH_SHORT).show();
            }

            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Module Deleted", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void showCreateModuleDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_create_module, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        EditText etModuleName = dialogView.findViewById(R.id.etModuleName);
        Button btnCreate = dialogView.findViewById(R.id.btnCreate);

        AlertDialog dialog = builder.create();

        etModuleName.setText("");

        btnCreate.setOnClickListener(view -> {
            String moduleName = etModuleName.getText().toString();
            if (!moduleName.isEmpty()) {
                Module newModule = new Module(moduleName, 0);
                moduleList.add(newModule);
                filteredModuleList.add(newModule);

                adapter.notifyDataSetChanged();
                String moduleId = databaseReference.push().getKey();  // Generate a unique key for the new module
                newModule.setName(moduleId);  // Set the generated ID in the module
                databaseReference.child(moduleId).setValue(newModule)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ContentManagementActivity.this, "Module Created: " + moduleName, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ContentManagementActivity.this, "Failed to create module", Toast.LENGTH_SHORT).show();
                            }
                        });



                // Clear the search query to show the full list
                SearchView searchView = findViewById(R.id.search);
                searchView.setQuery("", false);

                Toast.makeText(this, "Module Created: " + moduleName, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter a module name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void filterModules(String query) {
        List<Module> filteredList = new ArrayList<>();

        if (!query.isEmpty()) {
            for (Module module : moduleList) {
                // Check if the module name matches the query (you can improve this condition)
                if (module.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(module);
                }
            }
            }
        else {
                filteredList = new ArrayList<>(moduleList);
            }

            // Notify the adapter about the dataset change
            adapter.updateList(filteredList);
        }
    }
