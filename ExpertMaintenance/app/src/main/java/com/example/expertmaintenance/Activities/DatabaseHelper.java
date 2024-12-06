package com.example.expertmaintenance.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.expertmaintenance.Models.Intervention;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "organilogdb";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_INTERVENTIONS = "interventions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CLIENT_NAME = "client_name";
    private static final String COLUMN_SITE_ADDRESS = "site_address";
    private static final String COLUMN_SITE_CITY = "site_city";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_END_TIME = "end_time";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_VALSYNC = "valsync";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_INTERVENTIONS + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CLIENT_NAME + " TEXT, " +
                COLUMN_SITE_ADDRESS + " TEXT, " +
                COLUMN_SITE_CITY + " TEXT, " +
                COLUMN_START_TIME + " TEXT, " +
                COLUMN_END_TIME + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_VALSYNC + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERVENTIONS);
        onCreate(db);
    }

    public ArrayList<Intervention> getInterventionsByDate(String date) {
        ArrayList<Intervention> interventions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INTERVENTIONS, null, COLUMN_DATE + "=?", new String[]{date}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = getColumnValue(cursor, COLUMN_ID);
                String title = getColumnValue(cursor, COLUMN_TITLE);
                String clientName = getColumnValue(cursor, COLUMN_CLIENT_NAME);
                String siteAddress = getColumnValue(cursor, COLUMN_SITE_ADDRESS);
                String siteCity = getColumnValue(cursor, COLUMN_SITE_CITY);
                String startTime = getColumnValue(cursor, COLUMN_START_TIME);
                String endTime = getColumnValue(cursor, COLUMN_END_TIME);

                if (id != null && title != null && clientName != null) {
                    interventions.add(new Intervention(
                            id, title, clientName, siteAddress, siteCity, startTime, endTime
                    ));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return interventions;
    }

    public boolean shouldUpdateLocalDatabase(String id, int remoteValsync) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INTERVENTIONS, new String[]{COLUMN_VALSYNC}, COLUMN_ID + "=?", new String[]{id}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int valsyncIndex = cursor.getColumnIndex(COLUMN_VALSYNC);
            if (valsyncIndex != -1) {
                int localValsync = cursor.getInt(valsyncIndex);
                cursor.close();
                return remoteValsync > localValsync;
            }
            cursor.close();
        }
        return true; // If no record exists, we should insert it
    }

    public void insertOrUpdateIntervention(Intervention intervention, int valsync, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, intervention.getId());
        values.put(COLUMN_TITLE, intervention.getTitle());
        values.put(COLUMN_CLIENT_NAME, intervention.getClientName());
        values.put(COLUMN_SITE_ADDRESS, intervention.getSiteAddress());
        values.put(COLUMN_SITE_CITY, intervention.getSiteCity());
        values.put(COLUMN_START_TIME, intervention.getStartTime());
        values.put(COLUMN_END_TIME, intervention.getEndTime());
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_VALSYNC, valsync);

        db.insertWithOnConflict(TABLE_INTERVENTIONS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void bulkInsertInterventions(JSONArray interventionsArray, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            for (int i = 0; i < interventionsArray.length(); i++) {
                JSONObject interventionObject = interventionsArray.getJSONObject(i);
                insertOrUpdateIntervention(
                        new Intervention(
                                interventionObject.getString("id"),
                                interventionObject.getString("titre"),
                                interventionObject.getString("client_name"),
                                interventionObject.getString("site_address"),
                                interventionObject.getString("site_city"),
                                interventionObject.getString("heuredebutplan"),
                                interventionObject.getString("heurefinplan")
                        ),
                        interventionObject.getInt("valsync"),
                        date
                );
            }
            db.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private String getColumnValue(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex != -1 ? cursor.getString(columnIndex) : null;
    }
}
