package com.example.renergify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PaymentFragment extends Fragment {

    private TextView tvCompanyName, tvLocation, tvProduction, tvPrice, tvTax, tvTotalPrice, tvUserName, tvUserEmail;
    private Button confirmButton;

    private double priceValue = 0.0;
    private final double TAX_RATE = 0.06;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        tvCompanyName = view.findViewById(R.id.tvCompanyName);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvProduction = view.findViewById(R.id.tvProduction);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvTax = view.findViewById(R.id.tvTax);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        confirmButton = view.findViewById(R.id.confirmPurchaseButton);

        // Get passed arguments
        Bundle args = getArguments();
        if (args != null) {
            String companyName = args.getString("assetName", "N/A");
            String location = args.getString("location", "N/A");
            String production = args.getString("production", "N/A");
            String priceStr = args.getString("price", "0");

            // Assuming price is RM per kW, convert to double
            try {
                priceValue = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                priceValue = 0;
            }

            // Simulate user info - you can replace with actual user data from FirebaseAuth if available
            String userName = "John Doe";
            String userEmail = "johndoe@example.com";

            tvCompanyName.setText(companyName);
            tvLocation.setText(location);
            tvProduction.setText(production);
            tvPrice.setText(String.format("RM %.2f", priceValue));
            tvUserName.setText(userName);
            tvUserEmail.setText(userEmail);

            double taxAmount = priceValue * TAX_RATE;
            double totalPrice = priceValue + taxAmount;

            tvTax.setText(String.format("RM %.2f (6%% tax)", taxAmount));
            tvTotalPrice.setText(String.format("RM %.2f", totalPrice));
        }

        confirmButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Purchase successful!", Toast.LENGTH_LONG).show();

            // Go back to ContributionFragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
