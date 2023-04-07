package com.example.eduapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements OnSettingsChangedListener {

    BottomNavigationView navView;
    MainViewModel mainViewModel;
    NavController navController;
    MediaPlayer mediaPlayer;

    // Shared preferences file for settings to play bgm, sound fx, dark mode
    private SharedPreferences kidoozePrefs;
    private String kidoozePrefFile = "com.example.android.kidoozePrefs";

    // Preferences
    private boolean bgm = true;
    private boolean soundFX = true;
    private boolean darkMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navView = (BottomNavigationView) findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_playground, R.id.navigation_profile)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        Log.d("LAUNCH", "yoyo");
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.mobile_navigation, true)
                        .build();

                navController.navigate(item.getItemId(), null, navOptions);
                return true;
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Log.d("onDestinationChanged", destination.getId() + " home frag: " + R.id.navigation_home);
                switch (destination.getId()) {
                    case R.id.navigation_home:
                    case R.id.navigation_dashboard:
                    case R.id.navigation_profile:
                    case R.id.navigation_playground:
                        navView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        navView.setVisibility(View.GONE);
                }
            }
        });

        // Load and read shared preference file
        kidoozePrefs = getSharedPreferences(kidoozePrefFile, MODE_PRIVATE);

        bgm = kidoozePrefs.getBoolean(getString(R.string.bgm), true);
        soundFX = kidoozePrefs.getBoolean(getString(R.string.soundFX), true);
        darkMode = kidoozePrefs.getBoolean(getString(R.string.darkMode), false);

        // Set the settings
        playMusic(bgm);
        setDarkMode(darkMode);
    }


    @Override
    protected void onPause() {
        super.onPause();
        playMusic(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bgm = kidoozePrefs.getBoolean(getString(R.string.bgm), true);
        playMusic(bgm);
    }

    @Override
    // Plays bgm if bgm settings is true, overrode from OnSettingsChangedListener interface
    public void playMusic(boolean bgm) {
        if (bgm) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kidoozebgm);
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        }
    }

    @Override
    public void setDarkMode(boolean darkMode) {
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void setSoundFX(boolean soundFX) {
        this.soundFX = soundFX;
    }

    @Override
    public void onBackPressed() {
        //top level destinations
        Log.d("HELLO", navController+"");
        switch (navController.getCurrentDestination().getId()) {
            case R.id.navigation_home:
            case R.id.navigation_dashboard:
            case R.id.navigation_profile:
            case R.id.navigation_playground:
                super.onBackPressed();
                break;
            default:
                super.onBackPressed();
        }
    }
    public void selectLesson(View v) {
        Intent i = new Intent(this, TutorialActivity.class);

        switch (v.getId()) {
            case R.id.AbsL1_btn:
                i.putExtra("Course", "Abstraction");
                i.putExtra("Lesson", "Lesson 1");
                Log.d("lololol", "ran yea");
                break;
            case R.id.AbsL2_btn:
                i.putExtra("Course", "Abstraction");
                i.putExtra("Lesson", "Lesson 2");
                break;
            case R.id.AlgL1_btn:
                i.putExtra("Course", "Algorithmic Thinking");
                i.putExtra("Lesson", "Lesson 1");
                break;
            case R.id.AlgL2_btn:
                i.putExtra("Course", "Algorithmic Thinking");
                i.putExtra("Lesson", "Lesson 2");
                break;
            case R.id.DecL1_btn:
                i.putExtra("Course", "Decomposition");
                i.putExtra("Lesson", "Lesson 1");
                break;
            case R.id.DecL2_btn:
                i.putExtra("Course", "Decomposition");
                i.putExtra("Lesson", "Lesson 2");
                break;
            case R.id.PatL1_btn:
                i.putExtra("Course", "Pattern Recognition");
                i.putExtra("Lesson", "Lesson 1");
                break;
            case R.id.PatL2_btn:
                i.putExtra("Course", "Pattern Recognition");
                i.putExtra("Lesson", "Lesson 2");
                break;
        }

        startActivity(i);
    }
}