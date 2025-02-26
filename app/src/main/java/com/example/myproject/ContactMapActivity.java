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
    Location currentBestLocation;
    final int PERMISSION_REQUEST_LOCATION = 101;
    private boolean locationUpdatesRequested = false;

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
        // Optionally, call openContacts(), openSetting(), or openMap() from button clicks instead of here.
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
            getCoordinatesFromAddress();
            requestLocationUpdates();
        });
    }

    private void getCoordinatesFromAddress() {
        try {
            EditText editAddress = findViewById(R.id.editAddress);
            EditText editCity = findViewById(R.id.editCity);
            EditText editState = findViewById(R.id.editState);
            EditText editZipcode = findViewById(R.id.editZipcode);
            String addressString = editAddress.getText().toString() + ","
                    + editCity.getText().toString() + ","
                    + editState.getText().toString() + ","
                    + editZipcode.getText().toString();
            Geocoder geo = new Geocoder(ContactMapActivity.this);
            List<Address> results = geo.getFromLocationName(addressString, 1);
            TextView textLatitude = findViewById(R.id.textLatitude);
            TextView textLongitude = findViewById(R.id.textLongitude);
            TextView textAccuracy = findViewById(R.id.textAccurcay);
            if (results != null && !results.isEmpty()) {
                Address bestGuess = results.get(0);
                textLatitude.setText(String.valueOf(bestGuess.getLatitude()));
                textLongitude.setText(String.valueOf(bestGuess.getLongitude()));
                textAccuracy.setText("N/A");
            } else {
                Toast.makeText(ContactMapActivity.this, "No geocoder results for that address", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ContactMapActivity.this, "Geocoder error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!locationUpdatesRequested) {
            requestLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(ContactMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
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
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    if (isBetterLocation(location)) {
                        currentBestLocation = location;
                    }
                    TextView textLatitude = findViewById(R.id.textLatitude);
                    TextView textLongitude = findViewById(R.id.textLongitude);
                    TextView textAccuracy = findViewById(R.id.textAccurcay);
                    if (currentBestLocation != null) {
                        textLatitude.setText(String.valueOf(currentBestLocation.getLatitude()));
                        textLongitude.setText(String.valueOf(currentBestLocation.getLongitude()));
                        textAccuracy.setText(String.valueOf(currentBestLocation.getAccuracy()));
                    }
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }
                @Override
                public void onProviderEnabled(String provider) { }
                @Override
                public void onProviderDisabled(String provider) { }
            };

            networkListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    if (isBetterLocation(location)) {
                        currentBestLocation = location;
                    }
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }
                @Override
                public void onProviderEnabled(String provider) { }
                @Override
                public void onProviderDisabled(String provider) { }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
            locationUpdatesRequested = true;
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error, Location not available", Toast.LENGTH_LONG).show();
        }
    }
    private void stopLocationUpdates() {
        if (locationManager != null) {
            if (gpsListener != null) {
                locationManager.removeUpdates(gpsListener);
                gpsListener = null;
            }
            if (networkListener != null) {
                locationManager.removeUpdates(networkListener);
                networkListener = null;
            }
            locationManager = null;
        }
        locationUpdatesRequested = false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(ContactMapActivity.this, "MyContactList will not locate your contacts.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean isBetterLocation(Location location) {
        boolean isBetter = false;
        if (currentBestLocation == null) {
            isBetter = true;
        } else if (location.getAccuracy() <= currentBestLocation.getAccuracy()) {
            isBetter = true;
        } else if (location.getTime() - currentBestLocation.getTime() > 5 * 60 * 1000) {
            isBetter = true;
        }
        return isBetter;
    }
}
