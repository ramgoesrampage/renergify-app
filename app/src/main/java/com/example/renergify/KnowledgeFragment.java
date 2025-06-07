package com.example.renergify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class KnowledgeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledge, container, false);

        // Get UI elements
        Button backButton = view.findViewById(R.id.back_button);
        TextView titleTextView = view.findViewById(R.id.knowledge_center_title);
        TextView descriptionTextView = view.findViewById(R.id.description);
        ImageView imageView = view.findViewById(R.id.knowledge_image);

        // Set back button behavior
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Get arguments passed from Knowledge_AreaFragment
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            String description = args.getString("description");
            int imageResId = args.getInt("imageResId", R.drawable.environment_day); // fallback image

            if (title != null) titleTextView.setText(title);
            if (description != null) descriptionTextView.setText(description);
            imageView.setImageResource(imageResId);
        }

        return view;
    }
}
