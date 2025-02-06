package com.example.todoapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0; // Gruppentitel
    private static final int VIEW_TYPE_TASK = 1;   // Normale Tasks

    private Context context;
    private List<TodoItem> todoItems;
    private TodoApiService apiService;

    public TaskRecyclerAdapter(Context context, List<TodoItem> todoItems, String accessToken) {
        this.context = context;
        this.todoItems = todoItems;
        this.apiService = ApiClient.getClient(accessToken).create(TodoApiService.class);
    }

    @Override
    public int getItemViewType(int position) {
        // Unterscheidung zwischen Header (ID = -1) und normalem Task
        return todoItems.get(position).getId() == -1 ? VIEW_TYPE_HEADER : VIEW_TYPE_TASK; // Abfrage ob -1 eine Header ID ist oder eine Task ID
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.task_group_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false);
            return new TaskViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TodoItem item = todoItems.get(position);

        if (holder instanceof HeaderViewHolder) { // Wenn der holder eine instanc von HeaderViewHolder ist bzw. ein Key wie Previous
            // Header anzeigen
            ((HeaderViewHolder) holder).headerText.setText(item.getTitle()); // Wird vom Task zb. Previous mit der ID -1 der Titel genommen und als Header gesetzt
        } else if (holder instanceof TaskViewHolder) {
            // Normales Task-Item anzeigen
            TaskViewHolder taskHolder = (TaskViewHolder) holder;

            taskHolder.taskName.setText(item.getTitle());
            taskHolder.taskDescription.setText(item.getDescription());

            SimpleDateFormat displayDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String formattedDate = displayDate.format(item.getTime());

            taskHolder.taskTime.setText(formattedDate);
            taskHolder.checkBox.setChecked(item.isComplete());

            // Beschreibung aufklappbar machen
            taskHolder.taskDescription.setMaxLines(1);
            taskHolder.taskDescription.setEllipsize(TextUtils.TruncateAt.END);
            taskHolder.taskDescription.setOnClickListener(v -> {
                if (taskHolder.taskDescription.getMaxLines() == 1) {
                    taskHolder.taskDescription.setMaxLines(Integer.MAX_VALUE);
                    taskHolder.taskDescription.setEllipsize(null);
                } else {
                    taskHolder.taskDescription.setMaxLines(1);
                    taskHolder.taskDescription.setEllipsize(TextUtils.TruncateAt.END);
                }
            });

            // Checkbox-Änderungen speichern
            taskHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (context instanceof TaskActivity) {
                    ((TaskActivity) context).changeCheckBoxStatus(position, isChecked);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    // ViewHolder für normale Tasks
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, taskDescription, taskTime;
        CheckBox checkBox;
        Button deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_name);
            taskDescription = itemView.findViewById(R.id.task_note);
            taskTime = itemView.findViewById(R.id.task_time);
            checkBox = itemView.findViewById(R.id.task_checkbox);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    // ViewHolder für Gruppen-Header
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerText;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.group_header);
        }
    }
}
