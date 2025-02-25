package com.example.myproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

public class ContactMapActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener gpsListener;

    LocationListener networkListener;
    final int PERMISSION_REQUEST_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.buttonGetLocation), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initGetLocationButton();
        openContacts();
        openSetting();
        openMap();

    }

    private void openContacts() {
        Intent intent = new Intent(ContactMapActivity.this, ContactListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void openSetting() {
        Intent intent = new Intent(ContactMapActivity.this, ContactSettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void openMap() {
        ImageButton ibMap = findViewById(R.id.btnMap);
        ibMap.setEnabled(false);
    }

    private void initGetLocationButton() {
        Button locationButton = findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(v -> {
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(ContactMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ContactMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Snackbar.make(findViewById(R.id.activity_contact_map),
                                            "MyContactList requires this permission to locate your contacts",
                                            Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", view -> ActivityCompat.requestPermissions(ContactMapActivity.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            PERMISSION_REQUEST_LOCATION))
                                    .show();
                        } else {
                            ActivityCompat.requestPermissions(ContactMapActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSION_REQUEST_LOCATION);
                        }
                    } else {
                        startLocationUpdates();
                    }
                } else {
                    startLocationUpdates();
                }
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Error Requesting permission", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            EditText editAddress = findViewById(R.id.editAddress);
            EditText editCity = findViewById(R.id.editCity);
            EditText editState = findViewById(R.id.editState);
            EditText editZipcode = findViewById(R.id.editZipcode);
            String address = editAddress.getText().toString() + "," + editCity.getText().toString() + ","
                    + editState.getText().toString() + "," + editZipcode.getText().toString();
            Geocoder geo = new Geocoder(ContactMapActivity.this);
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    TextView textLatitude = findViewById(R.id.textLatitude);
                    TextView textLongitude = findViewById(R.id.textLongitude);
                    TextView textAccuracy = findViewById(R.id.textAccurcay);
                    textLatitude.setText(String.valueOf(location.getLatitude()));
                    textLongitude.setText(String.valueOf(location.getLongitude()));
                    textAccuracy.setText(String.valueOf(location.getAccuracy()));
                }
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider) {}
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error, Location not available", Toast.LENGTH_LONG).show();
        }

        try {
            if (locationManager != null && gpsListener != null) {
                locationManager.removeUpdates(gpsListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            EditText editAddress = findViewById(R.id.editAddress);
            EditText editCity = findViewById(R.id.editCity);
            EditText editState = findViewById(R.id.editState);
            EditText editZipcode = findViewById(R.id.editZipcode);
            String address = editAddress.getText().toString() + "," + editCity.getText().toString() + ","
                    + editState.getText().toString() + "," + editZipcode.getText().toString();
            Geocoder geo = new Geocoder(ContactMapActivity.this);
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    TextView textLatitude = findViewById(R.id.textLatitude);
                    TextView textLongitude = findViewById(R.id.textLongitude);
                    TextView textAccuracy = findViewById(R.id.textAccurcay);
                    textLatitude.setText(String.valueOf(location.getLatitude()));
                    textLongitude.setText(String.valueOf(location.getLongitude()));
                    textAccuracy.setText(String.valueOf(location.getAccuracy()));
                }
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider) {}
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error, Location not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); //overidden?
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(ContactMapActivity.this,
                            "MyContactList will not locate your contacts.",
                            Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
