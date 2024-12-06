package  com.example.expertmaintenance.Activities;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.expertmaintenance.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InterventionDetails extends AppCompatActivity {

    private Button btnDetails, btnFichiers, btnSignature;
    ImageButton retour,supp,syncIcon;
    private View detailsView, fichiersView, signatureView;
    private String apiPrevuUrl, apiEffectueUrl, apiClientUrl, apiPlanificationUrl, apiPrioriteUrl;
    private TextView emptyMessage;
    private LinearLayout photoContainer;
    // TextViews for displaying intervention details
    private TextView interventionNameTextView, companyNameTextView, interventionDateTextView, interventionTimeTextView, statusTextView;
    private Switch switchTerminer;
    private String apiDeleteUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention_details);
        // Initialize your views
        interventionNameTextView = findViewById(R.id.interventionName);
        companyNameTextView = findViewById(R.id.companyName);
        interventionDateTextView = findViewById(R.id.interventionDate);
        interventionTimeTextView = findViewById(R.id.interventionTime);
        statusTextView = findViewById(R.id.statusText);
        switchTerminer = findViewById(R.id.switch1);

        // Retrieve the Intent passed data
        Intent intent = getIntent();

        // Get the intervention ID (important to handle cases where it's missing)
        String id = intent.getStringExtra("interventionID");
        if (id == null || id.isEmpty()) {
            Log.e("InterventionDetails", "ID missing");
            finish(); // Close the activity if ID is missing
            return;
        }

        // Retrieve other data passed through the Intent
        String interventionName = intent.getStringExtra("interventionName");
        String companyName = intent.getStringExtra("companyName");
        String date = intent.getStringExtra("date");
        String startTime = intent.getStringExtra("startTime");
        String endTime = intent.getStringExtra("endTime");
        String status = intent.getStringExtra("status");

        // Set the data to the TextViews
        if (interventionName != null) {
            interventionNameTextView.setText(interventionName);
        } else {
            Toast.makeText(this, "Intervention name not found", Toast.LENGTH_SHORT).show();
        }

        if (companyName != null) {
            companyNameTextView.setText(companyName);
        } else {
            Toast.makeText(this, "Company name not found", Toast.LENGTH_SHORT).show();
        }

        if (date != null) {
            interventionDateTextView.setText(date);
        } else {
            interventionDateTextView.setText("Date not available");
        }

        if (startTime != null && endTime != null) {
            interventionTimeTextView.setText(startTime + " - " + endTime);
        } else {
            interventionTimeTextView.setText("Time not available");
        }

        if (status != null) {
            statusTextView.setText(status);
        } else {
            statusTextView.setText("Status: Unknown");
        }

        // Optionally, handle the state of the Switch based on the intervention status (e.g., "completed" or "not")
        switchTerminer.setChecked("completed".equals(status));  // Example logic

        // Handle any actions based on the Switch state (e.g., mark as completed or not)
        switchTerminer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                statusTextView.setText("Completed");
                statusTextView.setTextColor(getResources().getColor(R.color.green)); // Mark green for completed
            } else {
                statusTextView.setText("Not completed");
                statusTextView.setTextColor(getResources().getColor(R.color.red)); // Mark red for not completed
            }
        });
// Initialize TextViews for displaying the intervention data
        interventionNameTextView = findViewById(R.id.interventionName);
        companyNameTextView = findViewById(R.id.companyName);
        interventionDateTextView = findViewById(R.id.interventionDate);
        interventionTimeTextView = findViewById(R.id.interventionTime);
        statusTextView = findViewById(R.id.statusText);
        switchTerminer = findViewById(R.id.switch1);
        // Initialize Buttons
        btnDetails = findViewById(R.id.btnDetails);
        btnFichiers = findViewById(R.id.btnFichiers);
        btnSignature = findViewById(R.id.btnSignature);
        retour = findViewById(R.id.retour);
        supp = findViewById(R.id.supp);
        syncIcon = findViewById(R.id.syncIcon);

        // Initialize Views
        detailsView = getLayoutInflater().inflate(R.layout.fragment_details, null);
        fichiersView = getLayoutInflater().inflate(R.layout.fragment_fichier, null);
        signatureView = getLayoutInflater().inflate(R.layout.fragment_signature, null);

        // Initialize TextViews for "Planifié" section
        TextView txtDatePlanifie = detailsView.findViewById(R.id.txtDatePlanifie);
        TextView txtHeurePlanifie = detailsView.findViewById(R.id.txtHeurePlanifie);
        TextView txtTempsTravailler = detailsView.findViewById(R.id.txtTempsTravailler);
        TextView txtActionPlanifie = detailsView.findViewById(R.id.txtActionPlanifie);
        TextView txtCommentairePlanifie = detailsView.findViewById(R.id.txtCommentairePlanifie);

        // Initialize TextViews for "Effectué" section
        TextView txtTermineEffectue = detailsView.findViewById(R.id.txtTermineEffectue);
        TextView txtDateEffectue = detailsView.findViewById(R.id.txtDateEffectue);
        TextView txtHeureEffectue = detailsView.findViewById(R.id.txtHeureEffectue);
        TextView txtActionEffectue = detailsView.findViewById(R.id.txtActionEffectue);
        TextView txtCommentaireEffectue = detailsView.findViewById(R.id.txtCommentaireEffectuer);

        // Initialize TextViews for "Client" section
        TextView txtNomComplaitClient = detailsView.findViewById(R.id.txtNomComplaitClient);
        TextView txtTitreClient = detailsView.findViewById(R.id.txtTitreClient);
        TextView txtSocieteClient = detailsView.findViewById(R.id.txtSocieteClient);
        TextView txtEmailClient = detailsView.findViewById(R.id.txtEmailClient);
        TextView txtTelClient = detailsView.findViewById(R.id.txtTelClient);

        // Initialize Buttons for other actions
        Button mapButton = detailsView.findViewById(R.id.mapButton);
        Button historique = detailsView.findViewById(R.id.historyButton);
        Button buttonTakePhoto = fichiersView.findViewById(R.id.button_take_photo);
        Button buttonImportPhoto = fichiersView.findViewById(R.id.button_import_photo);
        photoContainer = fichiersView.findViewById(R.id.photo_container);
        emptyMessage = fichiersView.findViewById(R.id.empty_message);

        // Construct API URLs
        String apiPrevuUrl = "http://172.20.10.5/www/expert_maintenance_api/controllers/getPrevu.php?id=" + id;
        String apiEffectueUrl = "http://172.20.10.5/www/expert_maintenance_api/controllers/getEffectuer.php?id=" + id;
        String apiClientUrl = "http://172.20.10.5/www/expert_maintenance_api/controllers/getClient.php?id=" + id;
        String apiPlanificationUrl = "http://172.20.10.5/www/expert_maintenance_api/controllers/getDatePlanification.php?id=" + id;
        String apiPrioriteUrl = "http://172.20.10.5/www/expert_maintenance_api/controllers/priorite.php?id=" + id;
        String apiDeleteUrl = "http://172.20.10.5/www/expert_maintenance_api/controllers/effacerIntervention.php?id=" + id;

        // Fetch data and populate UI components
        fetchDataFromApi(apiPrevuUrl, txtDatePlanifie, txtHeurePlanifie, txtTempsTravailler, txtActionPlanifie, txtCommentairePlanifie);
        fetchEffectueeData(apiEffectueUrl, txtTermineEffectue, txtDateEffectue, txtHeureEffectue, txtActionEffectue, txtCommentaireEffectue);
        fetchClientData(apiClientUrl, txtNomComplaitClient, txtTitreClient, txtSocieteClient, txtEmailClient, txtTelClient);
        fetchDatePlanification(apiPlanificationUrl, txtDatePlanifie); // For planification
        fetchPrioriteData(apiPrioriteUrl, txtActionEffectue); // Set priority data into relevant TextView

        // Set up default content view
        setContent(detailsView);

        // Button click listeners
        btnDetails.setOnClickListener(v -> setContent(detailsView));
        btnFichiers.setOnClickListener(v -> setContent(fichiersView));
        btnSignature.setOnClickListener(v -> setContent(signatureView));

        mapButton.setOnClickListener(v -> fetchLocationAndOpenMap());

        historique.setOnClickListener(v -> {
            Intent intent3 = new Intent(InterventionDetails.this, HistoriqueActivity.class);
            startActivity(intent3);

        });

        supp.setOnClickListener(v -> deleteIntervention(apiDeleteUrl));

        syncIcon.setOnClickListener(v -> synchronizeData());

        retour.setOnClickListener(v -> {
            Intent intent2 = new Intent(InterventionDetails.this, InterventionActivity.class);
            startActivity(intent2);
            finish();
        });

        buttonTakePhoto.setOnClickListener(v -> takePhoto());
        buttonImportPhoto.setOnClickListener(v -> importPhoto());

        // Check permissions
        checkPermissions();
    }

    private void synchronizeData() {
        String url = "http://172.20.10.5/www/expert_maintenance_api/controllers/sync.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Toast.makeText(this, "Données synchronisées", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(this, "Erreur lors de la synchronisation", Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void deleteIntervention(String url) {
        // Create and show the confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this intervention?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // User confirmed, proceed with deletion
                    RequestQueue requestQueue = Volley.newRequestQueue(this);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            null,
                            response -> {
                                try {
                                    if (response.has("message")) {
                                        String message = response.getString("message");
                                        Toast.makeText(InterventionDetails.this, message, Toast.LENGTH_SHORT).show();

                                        // Redirection vers MainActivity2 après la suppression
                                        Intent intent = new Intent(InterventionDetails.this, InterventionActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    Log.e("InterventionDetails", "Erreur JSON : " + e.getMessage());
                                }
                            },
                            error -> {
                                Log.e("InterventionDetails", "Erreur API : " + error.getMessage());
                                Toast.makeText(InterventionDetails.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                            }
                    );
                    requestQueue.add(jsonObjectRequest);
                })
                .setNegativeButton(android.R.string.no, null) // Do nothing if user cancels
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void fetchDataFromApi(String url, TextView prevuTextView, TextView interventionTimeTextView) {
        // Implémentez la méthode fetchDataFromApi pour récupérer les données
    }
    private void fetchLocationAndOpenMap() {
        String locationApiUrl = "http:// 172.20.10.5/www/expert_maintenance_api/controllers/location.php";

        // Préparer les données pour POST
        JSONObject postData = new JSONObject();
        try {
            postData.put("intervention_id", getIntent().getStringExtra("id")); // Utiliser l'ID passé à l'activité
        } catch (JSONException e) {
            Toast.makeText(this, "Erreur lors de la création des données", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                locationApiUrl,
                postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("longitude") && response.has("latitude")) {
                                double longitude = response.getDouble("longitude");
                                double latitude = response.getDouble("latitude");

                                // Ouvrir Google Maps avec les coordonnées
                                openGoogleMaps(latitude, longitude);

                            } else {
                                Toast.makeText(InterventionDetails.this, "Coordonnées introuvables", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(InterventionDetails.this, "Erreur JSON", Toast.LENGTH_SHORT).show();
                            Log.e("InterventionDetails", "Erreur JSON : " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InterventionDetails.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                        Log.e("InterventionDetails", "Erreur API : " + error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void openGoogleMaps(double latitude, double longitude) {
        String geoUri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(Localisation)";
        Uri uri = Uri.parse(geoUri);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps"); // Assurer l'ouverture dans Google Maps

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps n'est pas installé", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchDataFromApi(String apiPrevuUrl, TextView txtDatePlanifie, TextView txtHeurePlanifie, TextView txtTempsTravailler, TextView txtActionPlanifie, TextView txtCommentairePlanifie) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiPrevuUrl,
                null,
                response -> {
                    try {
                        if (response.has("data")) {
                            JSONObject data = response.getJSONObject("data");
                            txtDatePlanifie.setText(data.optString("dateplanification", "N/A"));
                            txtHeurePlanifie.setText(data.optString("heuredebutplan", "N/A") + " - " + data.optString("heurefinplan", "N/A"));
                            txtTempsTravailler.setText(data.optString("duree", "N/A"));
                            txtActionPlanifie.setText(data.optString("action", "N/A"));
                            txtCommentairePlanifie.setText(data.optString("commentaires", "N/A"));
                        } else {
                            Toast.makeText(this, "Aucune donnée planifiée trouvée", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("InterventionDetails", "Erreur JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("InterventionDetails", "Erreur API: " + error.getMessage())
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void fetchDatePlanification(String apiUrl, TextView interventionDateTextView) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("data")) {
                                JSONObject data = response.getJSONObject("data");
                                String datePlanification = data.getString("dateplanification");

                                interventionDateTextView.setText(datePlanification);
                            } else {
                                Toast.makeText(InterventionDetails.this, "Aucune date de planification trouvée", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("InterventionDetailsActivity", "Erreur JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("InterventionDetailsActivity", "Erreur API: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void fetchEffectueeData(String apiEffectueUrl, TextView txtTermineEffectue, TextView txtDateEffectue, TextView txtHeureEffectue, TextView txtActionEffectue, TextView txtCommentaireEffectue) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiEffectueUrl,
                null,
                response -> {
                    try {
                        if (response.has("data")) {
                            JSONObject data = response.getJSONObject("data");
                            txtTermineEffectue.setText(data.optString("terminee", "N/A"));
                            txtDateEffectue.setText(data.optString("datedebutplan", "N/A") + " - " + data.optString("datefinplan", "N/A"));
                            txtHeureEffectue.setText(data.optString("heuredebutplan", "N/A") + " - " + data.optString("heurefinplan", "N/A"));
                            txtActionEffectue.setText(data.optString("action", "N/A"));
                            txtCommentaireEffectue.setText(data.optString("commentaires", "N/A"));
                        } else {
                            Toast.makeText(this, "Aucune donnée effectuée trouvée", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("InterventionDetails", "Erreur JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("InterventionDetails", "Erreur API: " + error.getMessage())
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void fetchClientData(String apiClientUrl, TextView txtNomComplaitClient, TextView txtTitreClient, TextView txtSocieteClient, TextView txtEmailClient, TextView txtTelClient) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiClientUrl,
                null,
                response -> {
                    try {
                        if (response.has("data")) {
                            JSONObject data = response.getJSONObject("data");
                            txtNomComplaitClient.setText(data.optString("nom", "N/A"));
                            txtTitreClient.setText(data.optString("titre", "N/A"));
                            txtSocieteClient.setText(data.optString("societe", "N/A"));
                            txtEmailClient.setText(data.optString("email", "N/A"));
                            txtTelClient.setText(data.optString("tel", "N/A"));
                        } else {
                            Toast.makeText(this, "Aucune donnée client trouvée", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("InterventionDetails", "Erreur JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("InterventionDetails", "Erreur API: " + error.getMessage())
        );

        requestQueue.add(jsonObjectRequest);
    }

    // Nouvelle méthode pour récupérer les données de priorité
    private void fetchPrioriteData(String apiUrl, TextView statusTextView) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("data")) {
                                JSONObject data = response.getJSONObject("data");
                                String priorite = data.getString("priorite");

                                statusTextView.setText(priorite);
                            } else {
                                Toast.makeText(InterventionDetails.this, "Aucune priorité trouvée", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("InterventionDetailsActivity", "Erreur JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("InterventionDetailsActivity", "Erreur API: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void setContent(View view) {
        FrameLayout contentFrame = findViewById(R.id.contentFrame);
        contentFrame.removeAllViews();
        contentFrame.addView(view);
    }
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            // Vérifier si les permissions nécessaires ont été accordées
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si les permissions pour la caméra et le stockage sont accordées, lancer l'action associée
                // Exemple : Enregistrer la signature
                //saveSignature();
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                // Si les permissions sont refusées, afficher un message d'erreur
                Toast.makeText(this, "Permissions denied. Unable to save signature.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Launcher for taking a photo
    private final ActivityResultLauncher<Intent> takePhotoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                    addPhotoToContainer(photo);
                } else {
                    Toast.makeText(this, "Failed to take photo.", Toast.LENGTH_SHORT).show();
                }
            });

    // Launcher for importing a photo
    private final ActivityResultLauncher<Intent> importPhotoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri photoUri = result.getData().getData();
                    addPhotoToContainer(photoUri);
                } else {
                    Toast.makeText(this, "Failed to import photo.", Toast.LENGTH_SHORT).show();
                }
            });

    // Take a photo using the camera
    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            takePhotoLauncher.launch(takePhotoIntent);
        } else {
            Toast.makeText(this, "Unable to open camera.", Toast.LENGTH_SHORT).show();
        }
    }

    // Import a photo from the gallery
    private void importPhoto() {
        Intent importPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        importPhotoIntent.setType("image/*");
        if (importPhotoIntent.resolveActivity(getPackageManager()) != null) {
            importPhotoLauncher.launch(importPhotoIntent);
        } else {
            Toast.makeText(this, "Unable to open gallery.", Toast.LENGTH_SHORT).show();
        }
    }

    // Add a photo (Bitmap) to the container
    private void addPhotoToContainer(Bitmap photo) {
        if (photo != null) {
            emptyMessage.setVisibility(TextView.GONE);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(photo);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            imageView.setPadding(0, 16, 0, 16);
            photoContainer.addView(imageView);
        }
    }

    // Add a photo (URI) to the container
    private void addPhotoToContainer(Uri photoUri) {
        if (photoUri != null) {
            emptyMessage.setVisibility(TextView.GONE);
            ImageView imageView = new ImageView(this);
            imageView.setImageURI(photoUri);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            imageView.setPadding(0, 16, 0, 16);
            photoContainer.addView(imageView);
        }
    }


}
