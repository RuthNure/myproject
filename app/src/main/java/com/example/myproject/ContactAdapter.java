package com.example.myproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {  // ✅ Correct generic typing
    private ArrayList<Contact> contactData;
    private View.OnClickListener mOnItemClickListener; // Click listener

    // Constructor
    public ContactAdapter(ArrayList<Contact> arrayList) {
        contactData = arrayList;
    }

    // Method to set click listener from Activity
    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Log.d("DEBUG", "onBindViewHolder called for position: " + position);
        Contact contact = contactData.get(position);

        // Set contact name
        if (holder.textContactName == null) {
            Log.e("ERROR", "getContactTextView() returned null at position: " + position);
        } else {
            Log.d("DEBUG", "Setting contact name for position: " + position);
            holder.textContactName.setText(contactData.get(position).getContactName());
        }

        // Set phone number
        if (holder.textPhoneNumber == null) {
            Log.e("ERROR", "getTextPhoneView() returned null at position: " + position);
        } else {
            Log.d("DEBUG", "Setting phone number for position: " + position);
            holder.textPhoneNumber.setText(contactData.get(position).getPhoneNumber());
        }

        // ✅ Set click listener here (not in the ViewHolder constructor)
        holder.itemView.setTag(holder);
        holder.itemView.setOnClickListener(mOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        return contactData != null ? contactData.size() : 0;
    }

    // ViewHolder Class
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView textContactName;
        public TextView textPhoneNumber;
        public Button deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textContactName = itemView.findViewById(R.id.textContactName);  // ✅ Ensure correct ID
            textPhoneNumber = itemView.findViewById(R.id.textPhoneNumber);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);

            // Debugging logs
            if (textContactName == null) {
                Log.e("ERROR", "TextView textContactName is NULL in ViewHolder");
            }
            if (textPhoneNumber == null) {
                Log.e("ERROR", "TextView textPhoneNumber is NULL in ViewHolder");
            }
            if (deleteButton == null) {
                Log.e("ERROR", "Button deleteButton is NULL in ViewHolder");
            }
        }
    }
}
