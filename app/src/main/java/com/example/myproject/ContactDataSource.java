package com.example.myproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ContactDataSource {

    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;

    public ContactDataSource (Context context) {
        dbHelper = new ContactDBHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public  boolean insertContact (Contact contact) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("contactname",contact.getContactName());
            initialValues.put("streetaddress",contact.getStreetAddress());
            initialValues.put("city",contact.getCity());
            initialValues.put("state",contact.getState());
            initialValues.put("zipcode",contact.getZipCode());
            initialValues.put("phonenumber",contact.getPhoneNumber());
            initialValues.put("cellnumber",contact.getCellNumber());
            initialValues.put("email",contact.geteMail());
            initialValues.put("birthday",String.valueOf(contact.getBirthday().getTimeInMillis()));

            didSucceed = database.insert("contact", null, initialValues) >0;
        } catch (Exception e) {

        }
        return didSucceed;
    }
    public  boolean updateContact (Contact contact) {
        boolean didSucceed = false;
        try {
            Long rowID = (long) contact.getContactID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("contactname",contact.getContactName());
            updateValues.put("streetaddress",contact.getStreetAddress());
            updateValues.put("city",contact.getCity());
            updateValues.put("state",contact.getState());
            updateValues.put("zipcode",contact.getZipCode());
            updateValues.put("phonenumber",contact.getPhoneNumber());
            updateValues.put("cellnumber",contact.getCellNumber());
            updateValues.put("email",contact.geteMail());
            updateValues.put("birthday",String.valueOf(contact.getBirthday().getTimeInMillis()));

            didSucceed = database.update("contact", updateValues, "_id = "+ rowID, null)>0;
        } catch (Exception e) {

        }
        return didSucceed;
    }

    public int getLastContactID(){
        int lastId;
        try {
            String query = "Select MAX(_id) from contact";
            Cursor cursor = database.rawQuery(query,null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e){
            lastId = -1;
        }
        return lastId;
    }


}
