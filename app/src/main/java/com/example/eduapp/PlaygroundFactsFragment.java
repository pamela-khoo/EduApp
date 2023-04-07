package com.example.eduapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class PlaygroundFactsFragment extends Fragment{
    Button wow;
    TextView facts;
    ImageView picture;
    Random rand = new Random();
    MediaPlayer audio;

    private SharedPreferences kidoozePrefs;
    private String kidoozePrefFile = "com.example.android.kidoozePrefs";

    private boolean soundFX = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kidoozePrefs = getContext().getSharedPreferences(kidoozePrefFile, Context.MODE_PRIVATE);
        soundFX = kidoozePrefs.getBoolean(getString(R.string.soundFX), true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.playground_facts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wow = view.findViewById(R.id.wowBtn);
        facts = view.findViewById(R.id.facts);
        picture = view.findViewById(R.id.factsPic);
        int randNum = rand.nextInt(5);
        String[] factsList = {
                "The first Olympics started in Greece in 1896.",
                "A giraffe's tongue is actually blue in color.",
                "The tallest mountain in the world is Mount Everest.",
                "Camels are also known as the 'Ship of the Desert'.",
                "Mercury is the closest planet to the Sun."
        };
        int[] pics = {
                R.drawable.olympics,
                R.drawable.giraffe,
                R.drawable.everest,
                R.drawable.camel,
                R.drawable.mercury
        };

        facts.setText(factsList[randNum]);
        picture.setImageResource(pics[randNum]);
        audio = MediaPlayer.create(getActivity(), R.raw.wow);
        wow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (soundFX) {
                    audio.start();
                }
                Navigation.findNavController(requireView()).navigate(R.id.action_factsFragment_to_playground);
            }
        });
    }

}