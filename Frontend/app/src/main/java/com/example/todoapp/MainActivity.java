package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

public class MainActivity extends AppCompatActivity { // Definiert die Hauptaktivität der App, die von AppCompatActivity erbt.
    private Button loginButton;
    private ISingleAccountPublicClientApplication msalClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Methode, die beim Start der Aktivität aufgerufen wird.
        super.onCreate(savedInstanceState); // Ruft die Methode der Elternklasse auf.
        setContentView(R.layout.activity_main); // Verbindet die Aktivität mit dem Layout "activity_main.xml".

        loginButton = findViewById(R.id.loginButton);

        PublicClientApplication.createSingleAccountPublicClientApplication(
                this,
                R.raw.auth_config,
                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        msalClient = application;

                        forceLogout();

                        setupLoginButton();
                    }

                    @Override
                    public void onError(MsalException exception) {
                        String errorMessage = "Login failed: " + exception.getMessage();
                        Toast.makeText(MainActivity.this, "MSAL initialization failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupLoginButton() {
        if (msalClient == null) {
            Toast.makeText(this, "MSAL-Client is not initialized!", Toast.LENGTH_LONG).show();
            return;
        }

        loginButton.setOnClickListener(v -> {
            msalClient.signIn(this, null, new String[]{"api://6f1ef8a4-18ee-4983-9d4e-b44abe56f234/api_access"}, getAuthCallback());
            // msalClient.signIn(this, null, new String[]{"User.Read"}, getAuthCallback());
        });
    }

    private AuthenticationCallback getAuthCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                String accessToken = authenticationResult.getAccessToken();
                String username = authenticationResult.getAccount().getUsername();

                Toast.makeText(MainActivity.this, "Welcome " + username, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, AuthenticatedActivity.class);
                intent.putExtra("ACCESS_TOKEN", accessToken);
                startActivity(intent);
            }

            @Override
            public void onError(MsalException exception) {
                Toast.makeText(MainActivity.this, "Login failed.", Toast.LENGTH_LONG).show();
            }
        };
    }

    public void forceLogout(){
        if(msalClient == null) {
            Toast.makeText(this, "MSAL-Client is not initialized!", Toast.LENGTH_LONG).show();
            return;
        }

        msalClient.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
            @Override
            public void onSignOut() {
                Toast.makeText(MainActivity.this, "User logged out. Please login again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Toast.makeText(MainActivity.this, "Logout failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
