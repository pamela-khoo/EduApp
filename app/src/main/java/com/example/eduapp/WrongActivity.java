package com.example.eduapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

public class WrongActivity extends AppCompatActivity {
    // Shared preferences file for settings to play bgm, sound fx, dark mode
    private SharedPreferences kidoozePrefs;
    private String kidoozePrefFile = "com.example.android.kidoozePrefs";

    // Preferences
    private boolean soundFX = true;
    private boolean darkMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong);

        // Load and read shared preference file
        kidoozePrefs = getSharedPreferences(kidoozePrefFile, MODE_PRIVATE);

        soundFX = kidoozePrefs.getBoolean(getString(R.string.soundFX), true);
        darkMode = kidoozePrefs.getBoolean(getString(R.string.darkMode), false);

        // Set the settings
        setDarkMode(darkMode);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        MediaPlayer audio = MediaPlayer.create(this, R.raw.wrong);
        if (soundFX) {
            audio.start();
        }
        thread.start();
    }

    public void setDarkMode(boolean darkMode) {
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
