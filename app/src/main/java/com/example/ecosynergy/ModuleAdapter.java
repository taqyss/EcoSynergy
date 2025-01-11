package com.example.ecosynergy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecosynergy.models.Module;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {

    private List<Module> moduleList;
    private OnModuleClickListener listener;

    public ModuleAdapter(List<Module> moduleList, OnModuleClickListener listener) {
        this.moduleList = moduleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = moduleList.get(position);
        holder.tvModuleName.setText(module.getName());
        holder.progressBar.setProgress(module.getProgress());

       // Set up edit functionality
        holder.itemView.setOnClickListener(view -> listener.onEdit(module));

        // Set up delete functionality
        holder.btnDelete.setOnLongClickListener(view -> {
            listener.onDelete(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        TextView tvModuleName;
        ProgressBar progressBar;
        Button btnDelete;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModuleName = itemView.findViewById(R.id.tvModuleName);
            progressBar = itemView.findViewById(R.id.progressBar);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
        public interface OnModuleClickListener {
            void onEdit(Module module);
            void onDelete(int position);
        }

        public void updateList(List<Module> updatedList) {
            this.moduleList = updatedList;
            notifyDataSetChanged();
        }

    public List<Module> getModuleList() {
        return moduleList;
    }
}
