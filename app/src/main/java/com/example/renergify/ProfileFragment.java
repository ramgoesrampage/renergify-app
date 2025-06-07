package com.example.renergify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView nameText, ageText, contactText, emailText, addressText;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private Button editProfileButton, deleteProfileButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return view;
        }

        String uid = currentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        nameText = view.findViewById(R.id.nameText);
        ageText = view.findViewById(R.id.ageText);
        contactText = view.findViewById(R.id.contactText);
        emailText = view.findViewById(R.id.emailText);
        addressText = view.findViewById(R.id.addressText);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        deleteProfileButton = view.findViewById(R.id.deleteProfileButton);

        emailText.setText(currentUser.getEmail());

        fetchUserData();

        editProfileButton.setOnClickListener(v -> showEditDialog());

        deleteProfileButton.setOnClickListener(v -> confirmDeleteProfile());

        return view;
    }

    private void fetchUserData() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nameText.setText(snapshot.child("name").getValue(String.class));
                    ageText.setText(snapshot.child("age").getValue(String.class));
                    contactText.setText(snapshot.child("contact").getValue(String.class));
                    addressText.setText(snapshot.child("address").getValue(String.class));
                } else {
                    Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_profile, null);

        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editAge = dialogView.findViewById(R.id.editAge);
        EditText editContact = dialogView.findViewById(R.id.editContact);
        EditText editAddress = dialogView.findViewById(R.id.editAddress);

        editName.setText(nameText.getText().toString());
        editAge.setText(ageText.getText().toString());
        editContact.setText(contactText.getText().toString());
        editAddress.setText(addressText.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Profile");
        builder.setView(dialogView);
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button saveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            saveBtn.setOnClickListener(view -> {
                String newName = editName.getText().toString().trim();
                String newAge = editAge.getText().toString().trim();
                String newContact = editContact.getText().toString().trim();
                String newAddress = editAddress.getText().toString().trim();

                if (newName.isEmpty() || newAge.isEmpty() || newContact.isEmpty() || newAddress.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateUserData(newName, newAge, newContact, newAddress, dialog);
            });
        });

        dialog.show();
    }

    private void updateUserData(String name, String age, String contact, String address, AlertDialog dialog) {
        userRef.child("name").setValue(name);
        userRef.child("age").setValue(age);
        userRef.child("contact").setValue(contact);
        userRef.child("address").setValue(address)
                .addOnSuccessListener(aVoid -> {
                    nameText.setText(name);
                    ageText.setText(age);
                    contactText.setText(contact);
                    addressText.setText(address);
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void confirmDeleteProfile() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Profile")
                .setMessage("Are you sure you want to delete your profile? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteProfile())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    currentUser.delete().addOnCompleteListener(authTask -> {
                        if (authTask.isSuccessful()) {
                            Toast.makeText(getContext(), "Profile deleted successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            requireActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Failed to delete user: " + authTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Failed to delete data: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
