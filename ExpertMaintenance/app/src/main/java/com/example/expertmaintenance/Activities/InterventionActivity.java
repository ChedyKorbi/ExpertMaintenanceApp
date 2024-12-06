package com.example.expertmaintenance.Activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.expertmaintenance.Models.Intervention;
import com.example.expertmaintenance.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class InterventionActivity extends AppCompatActivity {

    // Database constants
    private static final String DATABASE_NAME = "organilog";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_INTERVENTIONS = "interventions";

    // Table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CLIENT_NAME = "client_name";
    private static final String COLUMN_SITE_ADDRESS = "site_address";
    private static final String COLUMN_SITE_CITY = "site_city";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_END_TIME = "end_time";
    private static final String COLUMN_DATE = "date";

    private SQLiteDatabase db;

    private DrawerLayout drawerLayout;
    private TextView dateTextView;
    private Calendar calendar;
    private RecyclerView recyclerView;
    private InterventionAdapter adapter;
    private ArrayList<Intervention> interventions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention);

        // Initialize the database
        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createTableIfNotExists();

        // Initialize views
        ImageButton menuIcon = findViewById(R.id.button2);
        ImageButton syncIcon = findViewById(R.id.synch);
        dateTextView = findViewById(R.id.tvDate);
        recyclerView = findViewById(R.id.recyclerViewInterventions);
        ImageView prevDate = findViewById(R.id.prevDate);
        ImageView nextDate = findViewById(R.id.nextDate);

        // Calendar and adapter setup
        calendar = Calendar.getInstance();
        interventions = new ArrayList<>();
        adapter = new InterventionAdapter(InterventionActivity.this, interventions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Button listeners
        menuIcon.setOnClickListener(v -> toggleDrawer());
        syncIcon.setOnClickListener(v -> synchronizeData());

        int employeId = getIntent().getIntExtra("employeId", -1);
        String userName = getIntent().getStringExtra("employeName");

        // User data check
        if (employeId == -1 || userName == null) {
            Toast.makeText(this, "Erreur : données utilisateur manquantes", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        updateDateDisplay();
        fetchInterventions(employeId, dateTextView.getText().toString());

        // Set listeners for date navigation
        prevDate.setOnClickListener(v -> moveToPreviousDay(employeId));
        nextDate.setOnClickListener(v -> moveToNextDay(employeId));

        // Date picker on date text click
        dateTextView.setOnClickListener(v -> showDatePickerDialog(employeId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    private void createTableIfNotExists() {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_INTERVENTIONS + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CLIENT_NAME + " TEXT, " +
                COLUMN_SITE_ADDRESS + " TEXT, " +
                COLUMN_SITE_CITY + " TEXT, " +
                COLUMN_START_TIME + " TEXT, " +
                COLUMN_END_TIME + " TEXT, " +
                COLUMN_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateTextView.setText(sdf.format(calendar.getTime()));
    }

    private void fetchInterventions(int employeId, String date) {
        interventions.clear(); // Clear current interventions before updating the list

        // Check if interventions exist locally for the selected date
        ArrayList<Intervention> localInterventions = getInterventionsByDate(date);
        if (!localInterventions.isEmpty()) {
            interventions.addAll(localInterventions); // Add filtered interventions to the list
            Log.d("InterventionActivity", "Loaded " + localInterventions.size() + " interventions from local DB.");
            adapter.notifyDataSetChanged(); // Notify adapter to update RecyclerView
            return; // Exit method since local data exists
        }

        // Fetch from the server if no local data
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://172.20.10.5/www/expert_maintenance_api/controllers/getInterventions.php?employee_id=" + employeId + "&date=" + date;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    bulkInsertInterventions(response, date);
                    interventions.clear(); // Clear existing data before adding new ones
                    interventions.addAll(getInterventionsByDate(date)); // Add new interventions from DB
                    adapter.notifyDataSetChanged(); // Notify adapter to update RecyclerView
                },
                error -> {
                    Log.e("Volley Error", error.toString());
                    Toast.makeText(this, "Pas d'interventions pour cette date", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonArrayRequest);
    }


    private ArrayList<Intervention> getInterventionsByDate(String date) {
        ArrayList<Intervention> interventions = new ArrayList<>();
        Cursor cursor = db.query(TABLE_INTERVENTIONS, null, COLUMN_DATE + "=?", new String[]{date}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = safeGetString(cursor, COLUMN_ID);
                String title = safeGetString(cursor, COLUMN_TITLE);
                String clientName = safeGetString(cursor, COLUMN_CLIENT_NAME);
                String siteAddress = safeGetString(cursor, COLUMN_SITE_ADDRESS);
                String siteCity = safeGetString(cursor, COLUMN_SITE_CITY);
                String startTime = safeGetString(cursor, COLUMN_START_TIME);
                String endTime = safeGetString(cursor, COLUMN_END_TIME);

                if (id != null && title != null && clientName != null) {
                    interventions.add(new Intervention(id, title, clientName, siteAddress, siteCity, startTime, endTime));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return interventions;
    }


    private String safeGetString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex != -1 ? cursor.getString(columnIndex) : null;
    }

    private void bulkInsertInterventions(JSONArray interventionsArray, String date) {
        try {
            db.beginTransaction();
            for (int i = 0; i < interventionsArray.length(); i++) {
                JSONObject interventionObject = interventionsArray.getJSONObject(i);

                String id = interventionObject.optString("id", null);
                String title = interventionObject.optString("titre", null);
                String clientName = interventionObject.optString("client_name", null);
                String siteAddress = interventionObject.optString("site_address", null);
                String siteCity = interventionObject.optString("site_city", null);
                String startTime = interventionObject.optString("heuredebutplan", null);
                String endTime = interventionObject.optString("heurefinplan", null);

                if (id != null && title != null && clientName != null) {
                    db.execSQL("INSERT OR REPLACE INTO " + TABLE_INTERVENTIONS + " (" +
                                    COLUMN_ID + ", " +
                                    COLUMN_TITLE + ", " +
                                    COLUMN_CLIENT_NAME + ", " +
                                    COLUMN_SITE_ADDRESS + ", " +
                                    COLUMN_SITE_CITY + ", " +
                                    COLUMN_START_TIME + ", " +
                                    COLUMN_END_TIME + ", " +
                                    COLUMN_DATE + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                            new Object[]{id, title, clientName, siteAddress, siteCity, startTime, endTime, date});
                }
            }
            db.setTransactionSuccessful();
        } catch (JSONException e) {
            Log.e("BulkInsert", "Error inserting interventions", e);
        } finally {
            db.endTransaction();
        }
    }

    private void synchronizeData() {
        String date = dateTextView.getText().toString();
        fetchInterventions(-1, date);
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void moveToPreviousDay(int employeId) {
        calendar.add(Calendar.DAY_OF_MONTH, -1);  // Move to the previous day
        updateDateDisplay();  // Update the date display in the UI
        fetchInterventions(employeId, dateTextView.getText().toString());  // Fetch interventions for the new date
    }

    private void moveToNextDay(int employeId) {
        calendar.add(Calendar.DAY_OF_MONTH, 1);  // Move to the next day
        updateDateDisplay();  // Update the date display in the UI
        fetchInterventions(employeId, dateTextView.getText().toString());  // Fetch interventions for the new date
    }


    private void showDatePickerDialog(int employeId) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateDisplay();  // Update the displayed date
            fetchInterventions(employeId, dateTextView.getText().toString());  // Fetch interventions for the new date
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

}
