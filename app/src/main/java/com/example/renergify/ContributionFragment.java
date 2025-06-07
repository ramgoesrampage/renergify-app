package com.example.renergify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContributionFragment extends Fragment {

    private TextView assetTitle, assetLocation, assetProduction, assetSaved, assetDate;
    private Button buyButton;

    private DatabaseReference databaseRef;

    private String price = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contribution, container, false);

        // Link views to XML
        assetTitle = view.findViewById(R.id.assetTitle);
        assetLocation = view.findViewById(R.id.assetLocation);
        assetProduction = view.findViewById(R.id.assetProduction);
        assetSaved = view.findViewById(R.id.assetSaved);
        assetDate = view.findViewById(R.id.assetDate);
        buyButton = view.findViewById(R.id.buyButton1);

        databaseRef = FirebaseDatabase.getInstance().getReference();

        // Fetch data from "contributions/renergify" node
        databaseRef.child("contributions").child("renergify").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String title = snapshot.child("title").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);
                    String production = snapshot.child("production").getValue(String.class);
                    Integer priceInt = snapshot.child("price").getValue(Integer.class);
                    String date = snapshot.child("date").getValue(String.class);

                    price = (priceInt != null) ? String.valueOf(priceInt) : "";

                    assetTitle.setText(title != null ? title : "N/A");
                    assetLocation.setText(location != null ? location : "N/A");
                    assetProduction.setText(production != null ? production : "N/A");
                    assetSaved.setText(!price.isEmpty() ? "RM " + price + " /kW" : "Price not available");
                    assetDate.setText(date != null ? date : "N/A");
                } else {
                    Toast.makeText(getContext(), "No data found for renergify", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Buy button click - manual fragment transaction (no nav_graph)
        buyButton.setOnClickListener(v -> {
            if (!price.isEmpty()) {
                PaymentFragment paymentFragment = new PaymentFragment();

                // Pass data with bundle
                Bundle bundle = new Bundle();
                bundle.putString("assetName", assetTitle.getText().toString());
                bundle.putString("location", assetLocation.getText().toString());
                bundle.putString("production", assetProduction.getText().toString());
                bundle.putString("price", price);
                paymentFragment.setArguments(bundle);

                // Replace fragment manually
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, paymentFragment) // replace with your container id
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "Price not loaded yet", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
