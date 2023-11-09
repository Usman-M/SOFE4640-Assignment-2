package com.example.geocoder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class EditActivity extends AppCompatActivity {
    Integer id = -1;
    EditText etAddress;
    EditText etLatitude;
    EditText etLongitude;
    Button btnFind;
    Button btnSave;
    Button btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        // Get references to each EditText view
        etAddress = findViewById(R.id.etAddress);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);

        // Check if id was passed with the intent
        this.id = getIntent().getIntExtra("ID", -1);
        // if an id was passed, lookup the associated location from the database
        if (this.id != -1) {
            TextView tvEditTitle = findViewById(R.id.tvEditTitle);
            tvEditTitle.setText("Update Location");
            DBHelper dbHelper = new DBHelper(this);
            Location location = dbHelper.getLocation(this.id);
            etAddress.setText(location.getAddress());
            etLatitude.setText(location.getLatitude());
            etLongitude.setText(location.getLongitude());
        }
        // Implement search functionality - if a location is not returned, then it was not
        // found in the database for the query provided. A Toast will be made to indicate as such.
        // if an address is found, all three EditViews will be populated.
        btnFind = findViewById(R.id.btnFind);
        btnFind.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(this);
            String address = etAddress.getText().toString();
            Location location = dbHelper.findAddress(address);
            if (location == null) {
                Toast toast = Toast.makeText(this, "Address not found!", Toast.LENGTH_LONG);
                toast.show();
            } else {
                etAddress.setText(location.getAddress());
                etLatitude.setText(location.getLatitude());
                etLongitude.setText(location.getLongitude());
            }
        });

        // Save changes made to each EditText view and return to the main activity
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(this);
            String latitude = etLatitude.getText().toString();
            String longitude = etLongitude.getText().toString();
            String address = etAddress.getText().toString();
            dbHelper.saveLocation(new Location(this.id, latitude, longitude, address));
            finish();
        });

        // Cancel changes and return to the main activity
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
            finish();
        });
    }
}