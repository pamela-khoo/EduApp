package com.example.eduapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class SettingsFragment extends Fragment {
    private MainViewModel mainViewModel;

    private SharedPreferences kidoozePrefs;
    private String kidoozePrefFile = "com.example.android.kidoozePrefs";

    private boolean bgm = true;
    private boolean soundFX = true;
    private boolean darkMode = false;

    private OnSettingsChangedListener settingsChangedListener;

    ImageButton back_btn;
    Button credits_btn;
    Button rateus_btn;
    SwitchCompat bgm_switch;
    SwitchCompat soundFX_switch;
    SwitchCompat darkMode_switch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel =
                new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Sets Activity as the listener for changing bgm
        settingsChangedListener = (OnSettingsChangedListener) getActivity();

        kidoozePrefs = getContext().getSharedPreferences(kidoozePrefFile, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back_btn = view.findViewById(R.id.guideback_btn);
        credits_btn = view.findViewById(R.id.credits_btn);
        rateus_btn = view.findViewById(R.id.rateus_btn);
        bgm_switch = view.findViewById(R.id.bgm_switch);
        soundFX_switch = view.findViewById(R.id.soundfx_switch);
        darkMode_switch = view.findViewById(R.id.darkmode_switch);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)requireActivity()).onBackPressed();
            }
        });
        credits_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_creditsFragment);
            }
        });
        rateus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(
                        "https://play.google.com/store/"));
                intent.setPackage("com.android.vending");
                startActivity(intent);

//                // If App is listed on play store
//                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                }
            }
        });

        bgm_switch.setChecked(kidoozePrefs.getBoolean(getString(R.string.bgm), true));
        soundFX_switch.setChecked(kidoozePrefs.getBoolean(getString(R.string.soundFX), true));
        darkMode_switch.setChecked(kidoozePrefs.getBoolean(getString(R.string.darkMode), false));

        SharedPreferences.Editor preferencesEditor = kidoozePrefs.edit();

        bgm_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferencesEditor.putBoolean(getString(R.string.bgm), isChecked);
                preferencesEditor.apply();
                if (settingsChangedListener != null) {
                    settingsChangedListener.playMusic(isChecked);
                }
            }
        });
        darkMode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferencesEditor.putBoolean(getString(R.string.darkMode), b);
                preferencesEditor.apply();
                if (settingsChangedListener != null) {
                    settingsChangedListener.setDarkMode(b);
                    //reloadFragment();
                    requireActivity().recreate();
                }
            }
        });
        soundFX_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferencesEditor.putBoolean(getString(R.string.soundFX), b);
                preferencesEditor.apply();
                if (settingsChangedListener != null) {
                    settingsChangedListener.setSoundFX(b);
                }
            }
        });
    }

    public void reloadFragment() {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.settingsFragment);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
    }

}