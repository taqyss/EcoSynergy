package com.example.ecosynergy;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecosynergy.models.Module;

import java.util.ArrayList;
import java.util.List;

public class ContentManagementActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ModuleAdapter adapter;
    private List<Module> moduleList;
    private List<Module> filteredModuleList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_management);
        setupBottomNavigation();

        //Initalize mock data
        moduleList = new ArrayList<>();
        moduleList.add(new Module("Solar Energy", 100));
        moduleList.add(new Module("Wind Energy", 75));
        moduleList.add(new Module("Hydropower", 50));

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
        }

    private void showEditModuleDialog(Module module) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_module, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        EditText etModuleName = dialogView.findViewById(R.id.etModuleName);
        etModuleName.setText(moduleList.get(0).getName());
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(view -> {
            String newModuleName = etModuleName.getText().toString();
            if (!newModuleName.isEmpty()) {
                module.setName(newModuleName);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Module Edited: " + newModuleName, Toast.LENGTH_SHORT).show();
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
            moduleList.remove(position);
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
                moduleList.add(new Module(moduleName, 0));
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Module Created: " + moduleName, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter a module name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void filterModules(String query) {
        filteredModuleList.clear();

        if (query.isEmpty()) {
            filteredModuleList.addAll(moduleList); //Show all items if query is empty
        } else {
            for (Module module : moduleList) {
                if (module.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredModuleList.add(module);
                }
            }
        }

        // Notify the adapter about the dataset change
    }
}
