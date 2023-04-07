package com.example.eduapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import pl.droidsonroids.gif.GifImageView;

public class TutorialActivity extends AppCompatActivity {


    Intent intent;
    String course;
    String lesson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        intent = getIntent();
        course = intent.getStringExtra("Course");
        lesson = intent.getStringExtra("Lesson");

        String source = String.format(course + " " + lesson);
        source = source.replaceAll("\\s+","_");
        source = source.toLowerCase();
        int resID = getResources().getIdentifier(source, "drawable", this.getPackageName());
        ((GifImageView) findViewById(R.id.gifImageView3)).setImageResource(resID);

    }

    public void onStartClick(View v){
        Intent i = new Intent(this, QuestionActivity.class);

        i.putExtra("Course", course);
        i.putExtra("Lesson", lesson);
        startActivity(i);
        finish();
    }

    public void goToHome(View v){
        onBackPressed();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}