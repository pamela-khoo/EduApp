package com.example.eduapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


public class PopcatFragment extends BaseFragment {
    private MainViewModel mainViewModel;
    private int scoreCount;
    private ImageButton mBackButton;
    private TextView mScoreView;

    private SharedPreferences kidoozePrefs;
    private String kidoozePrefFile = "com.example.android.kidoozePrefs";

    private boolean soundFX = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        kidoozePrefs = getContext().getSharedPreferences(kidoozePrefFile, Context.MODE_PRIVATE);
        soundFX = kidoozePrefs.getBoolean(getString(R.string.soundFX), true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popcat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scoreCount = 0;

        mScoreView = view.findViewById(R.id.score);
        mScoreView.setText(String.valueOf(scoreCount));

        final MediaPlayer audio = MediaPlayer.create(getActivity(), R.raw.pop);
        final ImageButton mPopcatImage = (ImageButton) view.findViewById(R.id.popcat);
        mPopcatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (soundFX) {
                    audio.start();
                }
                showScore();
            }
        });

        mBackButton = view.findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_popcatFragment_to_playground);
            }
        });
    }

    public void showScore(){
        scoreCount++;
        mScoreView.setText(String.valueOf(scoreCount));
    }
}