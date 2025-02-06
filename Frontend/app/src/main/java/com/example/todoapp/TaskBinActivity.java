package com.example.todoapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskBinActivity extends BaseTaskActivity {
    private ImageButton returnButton;
    private TaskBinRecyclerAdapter binRecyclerAdapter; // RecyclerAdapter zum Anzeigen der Tasks in der RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycling_bin);
        setupUI();

        filterMode = FilterMode.BIN;

        // RecyclerView für die Anzeige der Tasks initialisieren
        binRecyclerAdapter = new TaskBinRecyclerAdapter(this, todoItems, accessToken); // Adapter mit der Task-Liste verbinden

        // RecyclerView LayoutManager setzen (LinearLayout für vertikale Liste)
        taskRecyclerView.setAdapter(binRecyclerAdapter); // Adapter der RecyclerView zuweisen

        adapter = binRecyclerAdapter;

        returnButton = findViewById(R.id.returnbutton);
        returnButton.setOnClickListener(v -> {
            setResult(TaskBinActivity.RESULT_OK);
            finish();
        });

        fetchTodos(); // Tasks von der API abrufen

    }

    /**
     * Lösche einen Task basierend auf seiner Position in der Liste.
     *
     * @param position Die Position des Tasks, der gelöscht werden soll.
     */
    public void deleteTask(int position) {
        if (position >= 0 && position < todoItems.size()) { // Überprüfen, ob die Position gültig ist
            TodoItem taskToDelete = todoItems.get(position); // Task abrufen
            int taskId = taskToDelete.getId(); // ID des Tasks abrufen

            // Löschanfrage an die API senden
            apiService.deleteTodo(taskId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        todoItems.remove(position);
                        binRecyclerAdapter.notifyItemRemoved(position);
                        Toast.makeText(TaskBinActivity.this, "Task deleted successfully!", Toast.LENGTH_SHORT).show();
                        fetchTodos();
                    } else {
                        Toast.makeText(TaskBinActivity.this, "Failed to delete task", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(TaskBinActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Invalid task position", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeCheckBoxStatus(int position) {
        if (position >= 0 && position < todoItems.size()) {
            TodoItem todoItem = todoItems.get(position);
            todoItem.setComplete(false); // Task wiederherstellen (isComplete = false)

            apiService.updateTodo(todoItem.getId(), todoItem).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        todoItems.remove(position);
                        binRecyclerAdapter.notifyItemRemoved(position);
                        Toast.makeText(TaskBinActivity.this, "Task restored!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TaskBinActivity.this, "Task restore failed...", Toast.LENGTH_SHORT).show();
                        Log.e("API", "Restore failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(TaskBinActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API", "Fehler: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Invalid task position", Toast.LENGTH_SHORT).show();
        }
    }
}
