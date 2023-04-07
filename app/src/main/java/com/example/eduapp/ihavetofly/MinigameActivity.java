package com.example.eduapp.ihavetofly;
import com.example.eduapp.LessonFragment;
import com.example.eduapp.MainActivity;
import com.example.eduapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MinigameActivity extends AppCompatActivity {

    private boolean isMute;
    private boolean bgm;

    MediaPlayer mediaPlayer;

    // Shared preferences file for settings to play bgm, sound fx, dark mode
    private SharedPreferences kidoozePrefs;
    private String kidoozePrefFile = "com.example.android.kidoozePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_minigame);

        findViewById(R.id.minigame_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });

        findViewById(R.id.howtoplay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HELLO","nla");
                Intent inten = new Intent(MinigameActivity.this, popUpGuideActivity.class);
                startActivity(inten);

            }
        });

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MinigameActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView highScoreTxt = findViewById(R.id.highScoreTxt);

        // Load and read shared preference file
        kidoozePrefs = getSharedPreferences(kidoozePrefFile, MODE_PRIVATE);

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highScoreTxt.setText("HighScore: " + prefs.getInt("highscore", 0));

        bgm = kidoozePrefs.getBoolean(getString(R.string.bgm), true);
        isMute = !bgm;

        // Set the settings
        playMusic(!isMute);

        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);

        if (isMute)
            volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
        else
            volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);

        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isMute = !isMute;
                playMusic(!isMute);
                if (isMute){
                    volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
                }
                else {
                    volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);
                }
                SharedPreferences.Editor editor = kidoozePrefs.edit();
                editor.putBoolean(getString(R.string.bgm), !isMute);
                editor.apply();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        playMusic(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playMusic(!isMute);
    }

    // Plays bgm if bgm settings is true, overrode from OnSettingsChangedListener interface
    public void playMusic(boolean bgm) {
        if (bgm) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.bgm_game);
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        }
    }

}
