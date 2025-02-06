package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AuthenticatedActivity extends AppCompatActivity { // Definiert die Hauptaktivität der App, die von AppCompatActivity erbt.

    private TextView animatedLoadingText; // TextView für die Anzeige eines animierten "Loading"-Textes.
    private Handler handler = new Handler(); // Handler zum Planen von verzögerten Aufgaben.
    private int dotCount = 0; // Variable, die die Anzahl der Punkte für die Animation speichert.
    private Button loginButton;
    private boolean loginSuccessfull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Methode, die beim Start der Aktivität aufgerufen wird.
        super.onCreate(savedInstanceState); // Ruft die Methode der Elternklasse auf.
        setContentView(R.layout.activity_authenticated); // Verbindet die Aktivität mit dem Layout "activity_main.xml".

        String accessToken = getIntent().getStringExtra("ACCESS_TOKEN");

        animatedLoadingText = findViewById(R.id.loadingText); // Verbindet die TextView "loadingText" aus dem Layout mit der Variable.
            startLoadingAnimation(); // Startet die Methode zur Animation des "Loading"-Textes.
            new Handler().postDelayed(new Runnable() { // Erstellt einen neuen Handler, der nach einer Verzögerung ausgeführt wird.
                @Override
                public void run() { // Methode, die nach der Verzögerung ausgeführt wird.
                    Intent intent = new Intent(AuthenticatedActivity.this, TaskActivity.class);
                    intent.putExtra("ACCESS_TOKEN", accessToken); // Pass the accessToken
                    startActivity(intent); // Start TaskActivity
                }
            }, 3000); // Verzögerung von 3000 Millisekunden (3 Sekunden).
        }

    private void startLoadingAnimation() { // Methode zum Animieren des "Loading"-Textes.
        handler.postDelayed(new Runnable() { // Plant wiederholte Aufgaben mit einem Handler.
            @Override
            public void run() { // Führt die Animation aus.
                dotCount = (dotCount + 1) % 4; // Erhöht die Anzahl der Punkte und setzt sie nach 3 zurück (0, 1, 2, 3).
                String text = "Loading"; // Basistext.
                for (int i = 0; i < dotCount; i++) { // Fügt Punkte entsprechend der Anzahl von dotCount hinzu.
                    text += "."; // Fügt einen Punkt hinzu.
                }
                animatedLoadingText.setText(text); // Setzt den aktualisierten Text in die TextView.

                handler.postDelayed(this, 500); // Wiederholt die Animation alle 500 Millisekunden.
            }
        }, 500); // Startet die erste Animation nach 500 Millisekunden.
    }
}
