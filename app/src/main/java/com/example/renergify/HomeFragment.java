package com.example.renergify;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

public class HomeFragment extends Fragment {

    private DatabaseReference databaseReference;
    private TextView hydroText, windText, solarText, biomassText;
    private BarChart barChart;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("energyData");

        // Connect TextViews
        hydroText = view.findViewById(R.id.hydroText);
        windText = view.findViewById(R.id.windText);
        solarText = view.findViewById(R.id.solarText);
        biomassText = view.findViewById(R.id.biomassText);

        barChart = view.findViewById(R.id.barChart);

        // Push random values and display them
        pushRandomValuesToFirebase();

        return view;
    }

    private void pushRandomValuesToFirebase() {
        Random random = new Random();
        int hydro = random.nextInt(900) + 100;
        int wind = random.nextInt(500) + 50;
        int solar = random.nextInt(200) + 50;
        int biomass = random.nextInt(100) + 10;

        Map<String, Object> energyData = new HashMap<>();
        energyData.put("hydropower", hydro);
        energyData.put("windEnergy", wind);
        energyData.put("solarEnergy", solar);
        energyData.put("biomassEnergy", biomass);

        databaseReference.setValue(energyData).addOnSuccessListener(unused -> fetchAndDisplayEnergyData());
    }

    private void fetchAndDisplayEnergyData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int hydro = snapshot.child("hydropower").getValue(Integer.class);
                    int wind = snapshot.child("windEnergy").getValue(Integer.class);
                    int solar = snapshot.child("solarEnergy").getValue(Integer.class);
                    int biomass = snapshot.child("biomassEnergy").getValue(Integer.class);

                    hydroText.setText(hydro + " kWh");
                    windText.setText(wind + " kWh");
                    solarText.setText(solar + " kWh");
                    biomassText.setText(biomass + " kWh");

                    showBarChart(hydro, wind, solar, biomass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void showBarChart(int hydro, int wind, int solar, int biomass) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, hydro));
        entries.add(new BarEntry(1, wind));
        entries.add(new BarEntry(2, solar));
        entries.add(new BarEntry(3, biomass));

        BarDataSet dataSet = new BarDataSet(entries, "Energy Sources");
        dataSet.setColors(Color.YELLOW, Color.BLUE, Color.GREEN, Color.MAGENTA);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(16f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        String[] labels = new String[]{"Hydro", "Wind", "Solar", "Biomass"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12f);

        barChart.getAxisLeft().setTextColor(Color.BLACK);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
