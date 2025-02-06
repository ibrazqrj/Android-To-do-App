package com.example.todoapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends BaseTaskActivity {
    private static final int REQUEST_CODE_BIN = 1;
    private Button addButton; // Button zum Hinzufügen eines neuen Tasks
    private ImageButton binButton;
    private TaskRecyclerAdapter recyclerAdapter; // RecyclerAdapter zum Anzeigen der Tasks in der RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        setupUI();

        filterMode = FilterMode.REGULAR;

        binButton = findViewById(R.id.recyclebin);
        binButton.setOnClickListener(v -> {
            Intent switchActivityIntent = new Intent(TaskActivity.this, TaskBinActivity.class);
            switchActivityIntent.putExtra("ACCESS_TOKEN", accessToken); // Access-Token übergeben
            startActivityForResult(switchActivityIntent, REQUEST_CODE_BIN);
        });

        // RecyclerView für die Anzeige der Tasks initialisieren
        recyclerAdapter = new TaskRecyclerAdapter(this, todoItems, accessToken); // Adapter mit der Task-Liste verbinden

        // RecyclerView LayoutManager setzen (LinearLayout für vertikale Liste)
        taskRecyclerView.setAdapter(recyclerAdapter); // Adapter der RecyclerView zuweisen

        adapter = recyclerAdapter;

        // TextView zur Anzeige der Anzahl der Tasks verbinden
        taskCountText = findViewById(R.id.task_count_text);

        // Button zum Hinzufügen eines neuen Tasks
        addButton = findViewById(R.id.add_task_button);
        addButton.setOnClickListener(v -> showAddTaskDialog()); // Zeige Dialog zum Hinzufügen eines neuen Tasks

        fetchTodos(); // Tasks von der API abrufen
    }

    /**
     * Zeigt einen Dialog an, um einen neuen Task hinzuzufügen.
     */
    private void showAddTaskDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog, null); // Dialoglayout laden

        EditText taskInput = dialogView.findViewById(R.id.nameEdit); // Eingabefeld für den Task-Titel
        EditText descriptionInput = dialogView.findViewById(R.id.descriptionEdit); // Eingabefeld für die Beschreibung
        DatePicker datePicker = dialogView.findViewById(R.id.datePicker); // Eingabefeld für die Zeit

        // Dialog zur Eingabe eines neuen Tasks anzeigen
        new AlertDialog.Builder(this)
                .setTitle("Enter your new to-do data:")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String taskTitle = taskInput.getText().toString();
                    String descriptionText = descriptionInput.getText().toString();

                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int day = datePicker.getDayOfMonth();
                    //String dateFormat = String.format("%04d-%02d-%02d", year, month, day);

                    if (!taskTitle.isEmpty() && !descriptionText.isEmpty()) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, day);
                        saveTask(taskTitle, descriptionText, cal.getTime()); // Task speichern
                    } else {
                        Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    /**
     * Speichere einen neuen Task über die API und aktualisiere die RecyclerView.
     *
     * @param title       Titel des neuen Tasks
     * @param description Beschreibung des neuen Tasks
     * @param time        Zeit des neuen Tasks
     */
    private void saveTask(String title, String description, Date time) {
        TodoItem newTask = new TodoItem(title, description, time, false);

        // Hinzufügen eines neuen Tasks über die API
        apiService.addTodo(newTask).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchTodos(); // Liste der Tasks aktua lisieren
                    //Log.d("JSON Payload", gson.toJson(newTask));
                } else {
                    Log.e("CreateTodo", "Failed :" + response.message());
                    Toast.makeText(TaskActivity.this, "Failed to create task. Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d("CreateTodo", "Task Data: " + newTask.toString());
                    //Log.d("JSON Payload", gson.toJson(newTask));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CreateTodo", "Error: " + t.getMessage());
                Toast.makeText(TaskActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Speichere den Status der Checkbox und aktualisiere die Daten via API
     * @param position      Taskposition (id)
     * @param isChecked     Taskstatus
     */
    public void changeCheckBoxStatus(int position, boolean isChecked) {
        if (position >= 0 && position < todoItems.size()) {
            TodoItem todoItem = todoItems.get(position);
            todoItem.setComplete(isChecked);

            apiService.updateTodo(todoItem.getId(), todoItem).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        if (isChecked) {
                            todoItems.remove(position);
                            recyclerAdapter.notifyItemRemoved(position);
                        }

                        Toast.makeText(TaskActivity.this, "Task status updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TaskActivity.this, "Task update didn't work...", Toast.LENGTH_SHORT).show();
                        Log.e("API", "Something went wrong: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(TaskActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API", "Fehler: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Invalid task position", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BIN && resultCode == RESULT_OK) {
            fetchTodos(); // Aktualisiert die Todos, wenn von TaskBinActivity zurückgekehrt wird
        }
    }
}
