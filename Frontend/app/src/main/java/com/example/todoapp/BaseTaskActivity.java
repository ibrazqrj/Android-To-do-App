package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseTaskActivity extends AppCompatActivity {
    public enum FilterMode { REGULAR, BIN }
    protected FilterMode filterMode;
    protected boolean isLoading = false;
    protected static final int pageLimit = 10;
    protected int offset = 0;
    protected String accessToken;
    protected ImageButton logoutButton;
    protected ISingleAccountPublicClientApplication msalClient;
    protected TodoApiService apiService; // Service für die API-Kommunikation
    protected List<TodoItem> todoItems = new ArrayList<>(); // Liste der aktuellen Tasks
    protected RecyclerView taskRecyclerView;
    protected TextView taskCountText;
    protected RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accessToken = getIntent().getStringExtra("ACCESS_TOKEN"); // Access-Token empfangen
        if (accessToken == null) {
            Log.e("TaskBinActivity", "Access Token is null");
            Toast.makeText(this, "Access Token is missing", Toast.LENGTH_SHORT).show();
            return; // Beende die Aktivität oder handle den Fehler
        }

        apiService = ApiClient.getClient(accessToken).create(TodoApiService.class);
    }

    protected void setupUI() {
        taskRecyclerView = findViewById(R.id.task_recycler_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        taskCountText = findViewById(R.id.task_count_text);

        logoutButton = findViewById(R.id.logoutbutton);
        logoutButton.setOnClickListener(v -> logout());

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        //Swipe-To-Refresh Listener
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchTodos();
            swipeRefreshLayout.setRefreshing(false);
        });

        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastVisibleItemPosition() == todoItems.size() - 1) {
                    fetchTodos(); // Lade mehr Tasks, wenn der User nach unten scrollt
                }
            }
        });
    }

    protected void logout() {
        if (msalClient != null) {
            msalClient.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
                @Override
                public void onSignOut() {
                    Toast.makeText(BaseTaskActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

                    // Zurück zur MainActivity navigieren
                    Intent intent = new Intent(BaseTaskActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(@NonNull MsalException exception) {
                    Toast.makeText(BaseTaskActivity.this, "Logout failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MSAL", "Logout Error: " + exception.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "MSAL Client not initialized!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Tasks von der API abrufen und in der RecyclerView anzeigen.
     */
    protected void fetchTodos() {
        if (isLoading || apiService == null) return;
        isLoading = true;

        Log.d("API", "Loading Tasks with Offset: " + offset);

        apiService.getTodos(offset, pageLimit).enqueue(new Callback<List<TodoItem>>() {
            @Override
            public void onResponse(Call<List<TodoItem>> call, Response<List<TodoItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TodoItem> newTodos = response.body();
                    updateTaskCount();
                    categorizeTasks(newTodos);

                    Log.d("API", "Loaded Tasks: " + newTodos.size());

                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<List<TodoItem>> call, Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
                Toast.makeText(BaseTaskActivity.this, "Error while loading Tasks", Toast.LENGTH_SHORT).show();
                isLoading = false;            }
        });
    }

    /**
     * Aufgaben nach Datum in Gruppen sortieren
     */
    public void categorizeTasks(List<TodoItem> tasks) { // Nimmt eine Liste von tasks
        // Map ist ein Interface und mappt bzw. speichert einen Key und eine Value als Paar und LinkedHashMap ist eine sortierte Version von HashMap (Die Elemente sind in der Reihenfolge)
        Map<String, List<TodoItem>> groupedTasks = new LinkedHashMap<>(); // Erstellt eine Map mit Kategorien als Schlüssel(String variablen) und Listen von Aufgaben als Werte

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()); // Datumformat festlegen in dd.MM.yyyy
        Date today = new Date(); // today speichert das heutige Datum

        Calendar calendar = Calendar.getInstance(); //Calendar Objekt erstellen um mit Datumswerte zu arbeiten

        /**
         * String-Variablen für die Arraylisten, welche als Keys in der Map benutzt werden
         */
        String todayKey = "TODAY";
        String yesterdayKey = "YESTERDAY";
        String tomorrowKey = "TOMORROW";
        String previousKey = "PREVIOUS";
        String pendingKey = "PENDING";

        /**
         * Leere listen für jede Kategorie werden in die Map eingefügt.
         */
        groupedTasks.put(previousKey, new ArrayList<>());
        groupedTasks.put(yesterdayKey, new ArrayList<>());
        groupedTasks.put(todayKey, new ArrayList<>());
        groupedTasks.put(tomorrowKey, new ArrayList<>());
        groupedTasks.put(pendingKey, new ArrayList<>());

        for (TodoItem task : tasks) { //foreach für die Sortierung der Aufgaben (TodoItems werden mit der variable task in der Taskliste durchgegangen)
            if(filterMode == FilterMode.REGULAR) {
                if (task.isComplete()) continue;
            }
            else{
                if(!task.isComplete()) continue;
            }

            Date taskDate = task.getTime(); // Nimmt vom erstellten Task das Datum und speichert es in taskDate
            calendar.setTime(today); // Nimmt das heutige Datum
            calendar.add(Calendar.DAY_OF_YEAR, -1); // Berechnet das Heutige Datum mit -1 um die Gruppenfunktion von yesterday zu bestimmen
            Date yesterday = calendar.getTime(); // Der Datumswert wird in yesterday gespeichert

            calendar.setTime(today); // Setzt das Datum wieder auf den heutigen Tag
            calendar.add(Calendar.DAY_OF_YEAR, 1); // Berechnet das heutige Datum mit +1 um die Gruppenfunktion von tomorrow zu bestimmen
            Date tomorrow = calendar.getTime(); // Speichert den berechneten Datumswert in tomorrow

            if (sdf.format(taskDate).equals(sdf.format(yesterday))) { //.before testet ob das Datum vom Task welches durchgegangen wird nach dem übergebenen Parameter yesterday (Datum vom vortag) ist oder nicht (bool)
                groupedTasks.get(yesterdayKey).add(task); // Wenn die Bedingung stimmt, wird der task in die gemappte Arraylist previousKey hinzugefügt
            } else if (taskDate.before(yesterday)) { // Oder wenn das heutige datum im angegebenen Format == das gestrige Datum im angegebenen Format ist
                groupedTasks.get(previousKey).add(task); // Wenn die Bedingung stimmt, wird der task in die gemappte Arraylist yesterdayKey hinzugefügt
            } else if (sdf.format(taskDate).equals(sdf.format(today))) { // Oder wenn das heutige Datum im angegebenen Format == das heutige Datum im angegeben Format ist
                groupedTasks.get(todayKey).add(task); // Wenn die Bedingung stimmt, wird der task in die gemappte Arraylist todayKey hinzugefügt
            } else if (sdf.format(taskDate).equals(sdf.format(tomorrow))) { // Oder wenn, das heutige Datum im angegebenen Format == der kommende Tag im angegebenen Format ist
                groupedTasks.get(tomorrowKey).add(task); // Wenn die Bedingung stimmt, wird der task in die gemappte Arraylist tomorrowKey hinzugefügt
            } else { // Wenn keine Bedingung stimmt
                groupedTasks.get(pendingKey).add(task); // Wird der Task in die gemappte Arraylist pendingKey hinzugefügt
            }
        }
        displayGroupedTasks(groupedTasks); // displayGroupedTasks wird mit dem Parameter groupedTasks (die Map) aufgerufen
    }

    /**
     * Zeigen der gruppierten Aufgaben in der RecyclerView
     */
    public void displayGroupedTasks(Map<String, List<TodoItem>> groupedTasks) {
        List<TodoItem> sortedTasks = new ArrayList<>(); // Neue ArrayList aus TodoItems namens sortedTasks

        for (Map.Entry<String, List<TodoItem>> entry : groupedTasks.entrySet()) { // Iterieren von einem einzelnen Paar (.Entry) in der Map namens entry in groupedTasks mit allen Einträgen (.entrySet)
            if(!entry.getValue().isEmpty()) { // Wenn das einzelne Paar nicht leer ist
                TodoItem header = new TodoItem(entry.getKey(), "", new Date(), false); // Neues TodoItem Objekt wird erstellt mit dem Header zb TODAY
                header.setId(-1); // Header bekommt die ID -1 damit er nicht als tatsächlicher Task aufgenommen wird
                sortedTasks.add(header); // Header wird zu sortedTasks hinzugefügt
                sortedTasks.addAll(entry.getValue()); // Die Anzahl Tasks zb für Today (Sagen wir 3 sind für Today) werden dann unter diesem Header eingesetzt und sind dann richtige Tasks
            }
        }

        todoItems.clear(); // todoItems zuerst clearen, falls noch irgendwas vorhanden ist
        todoItems.addAll(sortedTasks); // in todoItems die sortiertenTasks hinzufügen
        adapter.notifyDataSetChanged(); // recyclerAdapter mitgeben, dass die Daten sich angepasst haben damit die Ansicht aktualisiert wird
    }

    /**
     * Aktualisiere die Anzeige der Anzahl der Tasks im TextView.
     */
    private void updateTaskCount() {
        if (apiService == null) return;

        apiService.getTodos(offset, pageLimit).enqueue(new Callback<List<TodoItem>>() {
            @Override
            public void onResponse(Call<List<TodoItem>> call, Response<List<TodoItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //Holen der Header-Werte vor der Schleife
                    String totalCountHeaderActive = response.headers().get("X-Total-Active-Count");
                    String totalCountHeaderBin = response.headers().get("X-Total-Bin-Count");

                    int activeTasks = 0;
                    int binTasks = 0;

                    //Header-Werte sicher parsen
                    if (totalCountHeaderActive != null) {
                        try {
                            activeTasks = Integer.parseInt(totalCountHeaderActive);
                        } catch (NumberFormatException e) {
                            Log.e("API", "Parsing error for X-Total-Active-Count: " + e.getMessage());
                        }
                    }

                    if (totalCountHeaderBin != null) {
                        try {
                            binTasks = Integer.parseInt(totalCountHeaderBin);
                        } catch (NumberFormatException e) {
                            Log.e("API", "Parsing error for X-Total-Bin-Count: " + e.getMessage());
                        }
                    }

                    //UI aktualisieren je nach FilterMode
                    if (filterMode == FilterMode.REGULAR) {
                        taskCountText.setText("Tasks: " + activeTasks);
                    } else if (filterMode == FilterMode.BIN) {
                        taskCountText.setText("Bin: " + binTasks);
                    }

                    Log.d("API", "Active Tasks: " + activeTasks + " | Bin Tasks: " + binTasks);
                }
            }

            @Override
            public void onFailure(Call<List<TodoItem>> call, Throwable t) {
                taskCountText.setText("Tasks: 0 | Bin: 0");
                isLoading = false;
                Log.e("API", "Error while loading tasks: " + t.getMessage());
            }
        });
    }
}
