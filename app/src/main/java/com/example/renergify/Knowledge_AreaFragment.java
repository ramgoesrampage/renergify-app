package com.example.renergify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Knowledge_AreaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledge__area, container, false);

        View item1 = view.findViewById(R.id.item1);
        View item2 = view.findViewById(R.id.item2);
        View item3 = view.findViewById(R.id.item3);
        View item4 = view.findViewById(R.id.item4);
        View item5 = view.findViewById(R.id.item5);

        setTitleSubtitle(item1, "Solar Power", "Harness the energy from the sun");
        setTitleSubtitle(item2, "Wind Energy", "Clean electricity from wind turbines");
        setTitleSubtitle(item3, "Hydropower", "Energy from flowing water");
        setTitleSubtitle(item4, "What is Renewable Energy?", "The general definition");
        setTitleSubtitle(item5, "World Enviromental Day", "Know about the environment day");

        item1.setOnClickListener(v -> openArticleFragment(
                "Solar Power",
                "Solar energy can be captured “actively” or “passively.”\n" +
                        "\n" +
                        "Active solar energy uses special technology to capture the sun’s rays. The two main types of equipment are photovoltaic cells (also called PV cells or solar cells) and mirrors that focus sunlight in a specific spot. These active solar technologies use sunlight to generate electricity, which we use to power lights, heating systems, computers, and televisions.\n" +
                        "\n" +
                        "Passive solar energy does not use any equipment. Instead, it gets energy from the way sunlight naturally changes throughout the day. For example, people can build houses so their windows face the path of the sun. This means the house will get more heat from the sun. It will take less energy from other sources to heat the house.\n" +
                        "\n" +
                        "Other examples of passive solar technology are green roofs, cool roofs, and radiant barriers. Green roofs are completely covered with plants. Plants can get rid of pollutants in rainwater and air. They help make the local environment cleaner.\n" +
                        "\n" +
                        "Cool roofs are painted white to better reflect sunlight. Radiant barriers are made of a reflective covering, such as aluminum. They both reflect the sun’s heat instead of absorbing it. All these types of roofs help lower the amount of energy needed to cool the building.",
                R.drawable.solar_energy)); // Replace with actual drawable

        item2.setOnClickListener(v -> openArticleFragment(
                "Wind Energy",
                "People have been harnessing the wind’s energy for a long, long time. Five-thousand years ago, ancient Egyptians made boats powered by the wind. In 200 B.C.E., people used windmills to grind grain in the Middle East and pump water in China.\n" +
                        "\n" +
                        "Today, we capture the wind’s energy with wind turbines. A turbine is similar to a windmill; it has a very tall tower with two or three propeller-like blades at the top. These blades are turned by the wind. The blades turn a generator (located inside the tower), which creates electricity.\n" +
                        "\n" +
                        "Groups of wind turbines are known as wind farms. Wind farms can be found near farmland, in narrow mountain passes, and even in the ocean, where there are steadier and stronger winds. Wind turbines anchored in the ocean are called “offshore wind farms.”\n" +
                        "\n" +
                        "Wind farms create electricity for nearby homes, schools, and other buildings.",
                R.drawable.wind_energy)); // Replace with actual drawable

        item3.setOnClickListener(v -> openArticleFragment(
                "Hydropower",
                "Hydroelectric energy is made by flowing water. Most hydroelectric power plants are located on large dams, which control the flow of a river.\n" +
                        "\n" +
                        "Dams block the river and create an artificial lake, or reservoir. A controlled amount of water is forced through tunnels in the dam. As water flows through the tunnels, it turns huge turbines and generates electricity.\n" +
                        "\n" +
                        "Advantages and Disadvantages\n" +
                        "Hydroelectric energy is fairly inexpensive to harness. Dams do not need to be complex, and the resources to build them are not difficult to obtain. Rivers flow all over the world, so the energy source is available to millions of people.\n" +
                        "\n" +
                        "Hydroelectric energy is also fairly reliable. Engineers control the flow of water through the dam, so the flow does not depend on the weather (the way solar and wind energies do).\n",
                R.drawable.hydro_energy)); // Replace with actual drawable

        item4.setOnClickListener(v -> openArticleFragment(
                "What is Renewable Energy?",
                "Renewable energy is energy derived from natural sources that are replenished at a higher rate than they are consumed. Sunlight and wind, for example, are such sources that are constantly being replenished. Renewable energy sources are plentiful and all around us.\n" +
                        "\n" +
                        "Fossil fuels - coal, oil and gas - on the other hand, are non-renewable resources that take hundreds of millions of years to form. Fossil fuels, when burned to produce energy, cause harmful greenhouse gas emissions, such as carbon dioxide.\n" +
                        "\n" +
                        "Generating renewable energy creates far lower emissions than burning fossil fuels. Transitioning from fossil fuels, which currently account for the lion’s share of emissions, to renewable energy is key to addressing the climate crisis.\n" +
                        "\n" +
                        "Renewables are now cheaper in most countries, and generate three times more jobs than fossil fuels.",
                R.drawable.renewableenergy)); // Replace with actual drawable

        item5.setOnClickListener(v -> openArticleFragment(
                "World Enviromental Day",
                ".Led by the United Nations Environment Programme (UNEP) and held annually on 5 June since 1973, World Environment Day is the largest global platform for environmental public outreach and is celebrated by millions of people across the world. In 2025, it is hosted by the Republic of Korea.\n" +
                        "\n" +
                        "Why take part?\n" +
                        "Time is running out, and nature is in emergency mode. To keep global warming below 1.5°C this century, we must halve annual greenhouse gas emissions by 2030. Without action, exposure to air pollution beyond safe guidelines will increase by 50 per cent within the decade and plastic waste flowing into aquatic ecosystems will nearly triple by 2040.\n" +
                        "\n" +
                        "We need urgent action to address these pressing   issues.",
                R.drawable.environment_day)); // Replace with actual drawable

        View buttonTakeQuiz = view.findViewById(R.id.take_quiz_button);
        buttonTakeQuiz.setOnClickListener(v -> {
            // Open QuizActivity
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            startActivity(intent);
        });


        return view;
    }

    private void setTitleSubtitle(View item, String title, String subtitle) {
        TextView titleText = item.findViewById(R.id.titleText);
        TextView subtitleText = item.findViewById(R.id.subtitleText);
        if (titleText != null) titleText.setText(title);
        if (subtitleText != null) subtitleText.setText(subtitle);
    }

    private void openArticleFragment(String title, String description, int imageResId) {
        KnowledgeFragment fragment = new KnowledgeFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putInt("imageResId", imageResId);
        fragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
