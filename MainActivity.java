package com.example.gpsmaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.location.Location;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private TextView latText, lonText;
    private Button getLocationBtn, navigateBtn;
    private double latitude, longitude;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference("locations");

        // Initialize FusedLocationProviderClient for location data
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // UI elements
        latText = findViewById(R.id.latText);
        lonText = findViewById(R.id.lonText);
        getLocationBtn = findViewById(R.id.getLocationBtn);
        navigateBtn = findViewById(R.id.navigateBtn);

        // Button to get current location
        getLocationBtn.setOnClickListener(v -> getLocation());

        // Button to navigate to Google Maps using current coordinates
        navigateBtn.setOnClickListener(v -> navigateToMaps());
    }

    // Fetches current location and updates Firebase and UI
    private void getLocation() {
        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Get last known location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Fetch latitude and longitude
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            // Display coordinates in TextViews
                            latText.setText("Latitude: " + latitude);
                            lonText.setText("Longitude: " + longitude);

                            // Store the coordinates in Firebase
                            storeLocationInFirebase(latitude, longitude);

                            Toast.makeText(MainActivity.this, "Location stored in Firebase!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Unable to get location!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Navigate to Google Maps using the latitude and longitude
    private void navigateToMaps() {
        if (latitude != 0.0 && longitude != 0.0) {
            String uri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

            // Specify the Google Maps package
            intent.setPackage("com.google.android.apps.maps");

            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Google Maps is not installed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No location to navigate to.", Toast.LENGTH_SHORT).show();
        }
    }


    // Store the fetched coordinates in Firebase Realtime Database
    private void storeLocationInFirebase(double lat, double lon) {
        LocationData locationData = new LocationData(lat, lon);
        mDatabase.push().setValue(locationData);
    }

    // Request permission result callback
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    // Class to represent location data
    public static class LocationData {
        public double latitude;
        public double longitude;

        public LocationData() {
            // Default constructor required for Firebase
        }

        public LocationData(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
