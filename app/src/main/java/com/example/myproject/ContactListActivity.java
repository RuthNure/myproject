package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {
    private ImageButton btnMap, btnContacts, btnSettings;
    private ContactDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.buttonSave), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnMap = findViewById(R.id.btnMap);
        btnContacts = findViewById(R.id.btnContacts);
        btnSettings = findViewById(R.id.btnSettings);

        btnMap.setOnClickListener(v -> openMap());
        btnContacts.setOnClickListener(v -> openContacts());
        btnSettings.setOnClickListener(v -> openSettings());

        dataSource = new ContactDataSource(this);

        ArrayList<String> names;
        try {
            Log.d("DEBUG", "Attempting to open database...");
            dataSource.open();

            Log.d("DEBUG", "Database opened successfully, retrieving contact names...");
            names = dataSource.getContactName(); // Get contact names

            dataSource.close();
            Log.d("DEBUG", "Database closed successfully.");

            if (names == null) {
                Log.w("WARNING", "getContactName() returned null. Initializing empty list.");
                names = new ArrayList<>(); // Prevent null crash
            }

            RecyclerView contactList = findViewById(R.id.rvContacts);
            if (contactList == null) {
                Log.e("ERROR", "RecyclerView rvContacts not found in layout");
                Toast.makeText(this, "RecyclerView not found", Toast.LENGTH_LONG).show();
                return;
            }

            Log.d("DEBUG", "Setting up RecyclerView...");
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            contactList.setLayoutManager(layoutManager);

            ContactAdapter contactAdapter = new ContactAdapter(names);
            contactList.setAdapter(contactAdapter);

            Log.d("DEBUG", "Contacts loaded successfully");
        } catch (Exception e) {
            Log.e("ERROR", "Exception retrieving contacts", e);
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
    }
    private void openSettings() {
        Intent intent = new Intent(ContactListActivity.this, ContactSettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void openMap() {
        Intent intent = new Intent(ContactListActivity.this, ContactMapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void openContacts() {
        ImageButton ibContacts = findViewById(R.id.btnContacts);
        ibContacts.setEnabled(false);

    }
}