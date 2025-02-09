package com.example.myproject;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.text.format.DateFormat;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    private ToggleButton toggleEdit;
    private Button btnSave, buttonBirthday;
    private TextView textContact, textAddress, textBDay, textBirthday;
    private EditText editContact, editAddress, editCity, editState, editZipcode, editHome, editCell,editEmail;
    private ImageButton btnContacts, btnMap, btnSettings;
    private LinearLayout toolbar, bottomNavigationBar;
    private Contact currentContact;
    private ContactDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        try {
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnSave), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
        dataSource = new ContactDataSource(this);


        // Initialize Views
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationBar = findViewById(R.id.bottomNavigationBar);

        toggleEdit = findViewById(R.id.toggleEdit);
        btnSave = findViewById(R.id.btnSave);
        buttonBirthday = findViewById(R.id.buttonBirthday);

        textContact = findViewById(R.id.textContact);
        textAddress = findViewById(R.id.textAddress);
        textBDay = findViewById(R.id.textBDay);
        textBirthday = findViewById(R.id.textBirthday);

        editContact = findViewById(R.id.editContact);
        editAddress = findViewById(R.id.editAddress);
        editCity = findViewById(R.id.editCity);
        editState = findViewById(R.id.editState);
        editZipcode = findViewById(R.id.editZipcode);
        editHome = findViewById(R.id.editHome);
        editCell = findViewById(R.id.editCell);
        editEmail = findViewById(R.id.editEmail);

        btnContacts = findViewById(R.id.btnContacts);
        btnMap = findViewById(R.id.btnMap);
        btnSettings = findViewById(R.id.btnSettings);

        if (btnSave == null) {
            throw new NullPointerException("btnSave not found in activity_main.xml. Check the ID.");
        }
        // Set click listeners for buttons
        btnSave.setOnClickListener(v -> initSaveButton());
        buttonBirthday.setOnClickListener(v -> initChangeDateButton());
        btnContacts.setOnClickListener(v -> openContacts());
        btnMap.setOnClickListener(v -> openMap());
        btnSettings.setOnClickListener(v -> openSettings());

        // Toggle Edit Mode
        //toggleEdit.setOnCheckedChangeListener((buttonView, isChecked) -> enableEditing(isChecked));
        currentContact = new Contact();

        initTextChangedEvents();


    }

    private void openContacts() {
        Intent intent = new Intent(MainActivity.this, ContactListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void openMap() {
        Intent intent = new Intent(MainActivity.this, ContactMapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void openSettings() {
        Intent intent = new Intent(MainActivity.this, ContactSettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    /*
    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textBirthday.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
        currentContact.setBirthday(selectedTime);

    }*/

    @Override
    public void didFinishDatePickerDialog(java.util.Calendar selectedTime) {
        TextView birthDay = findViewById(R.id.textBDay);
        birthDay.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
        currentContact.setBirthday(selectedTime);
    }

    private void initChangeDateButton(){
        Button changeDate = findViewById(R.id.buttonBirthday);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }

    private void initTextChangedEvents(){
        final EditText editContact = findViewById(R.id.editContact);
        editContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setContactName(editContact.getText().toString());

            }
        });
        final EditText editAddress = findViewById(R.id.editAddress);
        editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setStreetAddress(editAddress.getText().toString());

            }
        });
        final EditText editCity = findViewById(R.id.editCity);
        editCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setCity(editCity.getText().toString());

            }
        });
        final EditText editState = findViewById(R.id.editState);
        editState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setState(editState.getText().toString());

            }
        });
        final EditText editZipcode = findViewById(R.id.editZipcode);
        editZipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setZipCode(editZipcode.getText().toString());

            }
        });
        final EditText editHome = findViewById(R.id.editHome);
        editHome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setPhoneNumber(editHome.getText().toString());

            }
        });
        final EditText editCell = findViewById(R.id.editCell);
        editCell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setCellNumber(editCell.getText().toString());

            }
        });
        final EditText editEmail = findViewById(R.id.editEmail);
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.seteMail(editEmail.getText().toString());

            }
        });
    }
    private void setForEditing(boolean enabled) {
        findViewById(R.id.editContact).setEnabled(enabled);
        findViewById(R.id.editAddress).setEnabled(enabled);
        findViewById(R.id.editCity).setEnabled(enabled);
        findViewById(R.id.editState).setEnabled(enabled);
        findViewById(R.id.editZipcode).setEnabled(enabled);
        findViewById(R.id.editHome).setEnabled(enabled);
        findViewById(R.id.editCell).setEnabled(enabled);
        findViewById(R.id.editEmail).setEnabled(enabled);
        findViewById(R.id.buttonBirthday).setEnabled(enabled);
    }
    private void initSaveButton() {
        btnSave.setOnClickListener(v -> {
            boolean wasSuccessful;
            try {
                dataSource.open();  // Use the existing 'dataSource' object
                if (currentContact.getContactID() == -1) {
                    wasSuccessful = dataSource.insertContact(currentContact); // Use 'dataSource'
                } else {
                    wasSuccessful = dataSource.updateContact(currentContact); // Use 'dataSource'
                }
                dataSource.close();  // Close after operations
            } catch (Exception e) {
                wasSuccessful = false;
                e.printStackTrace();
            }

            if (wasSuccessful) {
                ToggleButton editToggle = findViewById(R.id.toggleEdit);
                if (editToggle != null) {
                    editToggle.toggle();
                }
                setForEditing(false);
            }
        });
    }
}
