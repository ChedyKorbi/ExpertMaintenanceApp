package com.example.expertmaintenance.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.expertmaintenance.Data.AuthRequest;
import com.example.expertmaintenance.Data.AuthResponse;
import com.example.expertmaintenance.Models.Employe;
import com.example.expertmaintenance.Network.AuthService;
import com.example.expertmaintenance.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText txtLogin, txtPassword;
    private Button btnConnection;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);
        btnConnection = findViewById(R.id.btnConnection);

        // Initialize Retrofit for the AuthService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.5/www/expert_maintenance_api/controllers/") // Replace with your IP
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authService = retrofit.create(AuthService.class);

        // Set up the login button listener
        btnConnection.setOnClickListener(v -> {
            String login = txtLogin.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                authenticateUser(login, password);
            }
        });
    }

    private void authenticateUser(String login, String password) {
        AuthRequest loginRequest = new AuthRequest(login, password);
        Call<AuthResponse> call = authService.login(loginRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse loginResponse = response.body();
                    if (loginResponse.isSuccess()) {
                        Employe employe = loginResponse.getData(); // Correctly accessing getData() on the instance

                        if (employe != null && employe.getName() != null && !employe.getName().isEmpty()) {
                            Log.d("MainActivity", "User ID: " + employe.getId());
                            Log.d("MainActivity", "User Name: " + employe.getName());
                            // Display welcome message with employee's name
                            Toast.makeText(MainActivity.this, "Welcome back, " + employe.getName(), Toast.LENGTH_SHORT).show();
                            showMainFeatures(employe); // Navigate to MainActivity2 with user data
                        } else {
                            Toast.makeText(MainActivity.this, "Utilisateur sans nom", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Set red outline for incorrect login or password
                        txtLogin.setBackgroundColor(Color.RED);
                        txtPassword.setBackgroundColor(Color.RED);
                        Toast.makeText(MainActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Erreur de réponse du serveur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur de connexion : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMainFeatures(Employe employe) {
        // Check if the employe object is not null and if 'id' and 'nom' are valid (non-empty)
        if (employe != null && employe.getId() != 0 && employe.getName() != null && !employe.getName().isEmpty()) {
            // Create an Intent to transition to the next activity
            Intent intent = new Intent(MainActivity.this, InterventionActivity.class);

            // Pass the employee's ID and name to the new activity
            intent.putExtra("employeId", employe.getId());  // Pass the employe's ID as an integer
            intent.putExtra("employeName", employe.getName());  // Pass the employe's name

            // Start the new activity and finish the current one
            startActivity(intent);
            finish();
        } else {
            // If the employee's data is invalid, show an error message
            Toast.makeText(MainActivity.this, "Données employé manquantes ou invalides", Toast.LENGTH_SHORT).show();
        }
    }



}
