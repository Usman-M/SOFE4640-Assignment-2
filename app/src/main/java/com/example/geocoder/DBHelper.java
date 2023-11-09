package com.example.geocoder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context)  {
        super(context, "GeocoderDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create locations table
        db.execSQL("CREATE TABLE Locations(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "latitude TEXT," +
                "longitude TEXT," +
                "address TEXT" +
                ");");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // When the database is to be upgraded drop the existing table and start fresh
        db.execSQL("DROP TABLE IF EXISTS Locations;");
        onCreate(db);
    }
    public void persistLocations(ArrayList<Location> locations) {

        // When bulk loading address data, drop existing data, recreate the table
        // and begin bulk loading each location
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Locations;");
        onCreate(db);
        try {
            // start transaction to bulk load addresses
            db.beginTransaction();

            // for every location provided, persist the latitude, longitude and address
            for (Location location : locations) {
                ContentValues cv = new ContentValues();
                cv.put("latitude", location.getLatitude());
                cv.put("longitude", location.getLongitude());
                cv.put("address", location.getAddress());
                db.insert("Locations", null, cv);
            }

            // commit transaction
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    public Location getLocation(Integer id) {
        // Find a specific row in the database using the primary key "id"
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Locations WHERE id = ?", new String[] { String.valueOf(id) });
        Location location = null;
        if (cursor.moveToFirst()) {
            location = new Location(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3));
        }
        cursor.close();
        db.close();
        return location;
    }
    public ArrayList<Location> getAllLocations() {
        // To get all locations persisted in the database, create an empty ArrayList of Locations
        // and for each returned row create a new Location object
        ArrayList<Location> locations = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Locations", null);
        while (cursor.moveToNext()) {
            locations.add(new Location(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3))
            );
        }
        cursor.close();
        db.close();
        return locations;
    }
    public Location findAddress(String searchAddress) {
        // The findAddress method takes advantage of the SQL syntax of LIKE with '%' to indicate
        // that variations are accepted at the end of the string but not at the beginning.
        // This way a partial query can help return a complete address.
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Locations WHERE address LIKE ? LIMIT 1", new String[] { searchAddress + "%"});
        Location location = null;
        if (cursor.moveToFirst()) {
            location = new Location(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
        }
        cursor.close();
        db.close();
        return location;
    }

    public long saveLocation(Location location) {
        // To save a location get the individual components from the location object
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("latitude", location.getLatitude());
        cv.put("longitude", location.getLongitude());
        cv.put("address", location.getAddress());

        // if a valid id is provided, an update operation will be made in the database
        long id = -1;
        if (location.getId() != null && location.getId() != -1) {
            db.update("Locations", cv, "id = ?", new String[] { String.valueOf(location.getId()) });
            id = location.getId();
        } else {
            // when a valid id was not provided an insert is assumed and the id is collected from the insert operation
            id = db.insert("Locations", null, cv);
        }
        db.close();
        return id;
    }
    public boolean deleteLocation(Integer id) {
        // A delete operation is carried out using the primary key "id" to identify which row needs to be deleted
        SQLiteDatabase db = getWritableDatabase();
        int deleted = db.delete("Locations", "id = ?", new String[] { String.valueOf(id) });
        db.close();
        return deleted > 0;
    }

}
