package com.example.eduapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.eduapp.MainActivity;
import com.example.eduapp.MainViewModel;
import com.example.eduapp.R;
import com.example.eduapp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class HomeFragment extends BaseFragment {

    private MainViewModel mainViewModel;
    ImageButton guide_btn;
    Button xp_btn;
    Button streaks_btn;
    TextView welcome_text;
    CardView lesson_btn;
    CardView playground_btn;
    TextView progress_Text;
    ProgressBar progressBar;
    List<String> lessonsCompleted;
    List<Integer> nextLesson;
    TextView buttonLessonText;

    int xp_unicode = 0x1F31F;
    int fire_unicode = 0x1F525;
    String name = "Mike";
    String next_lesson = "Lesson 4: What Comes Next?";
    int num_compLessons = 0;
    int total_lessons = 8;
    int streaks = 0;
    Long scoreResult = Long.valueOf(0);

    List<String> lessonNames = Arrays.asList("Abstraction", "Algorithmic Thinking", "Decomposition", "Pattern Recognition");
    List<String> lessonNumbers = Arrays.asList("Lesson 1", "Lesson 2");

    List<String> lessonTitles = Arrays.asList("What Comes Next?", "Art Fun!", "Sandwich Time", "Burger Time", "What's That on The Tree?", "Pizzalicious!", "Puzzle 1.0", "Puzzle 2.0");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel =
                new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        checkStreak();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        guide_btn = view.findViewById(R.id.guide_btn);
        xp_btn = view.findViewById(R.id.XP_btn);
        streaks_btn = view.findViewById(R.id.streaks_btn);
        welcome_text = view.findViewById(R.id.welcomeText);
        lesson_btn = view.findViewById(R.id.main_lessonButton);
        playground_btn = view.findViewById(R.id.main_playgroundButton);
        progress_Text = view.findViewById(R.id.progress_text);
        progressBar = view.findViewById(R.id.progressBar);
        buttonLessonText = view.findViewById(R.id.main_lessonText);

        setUpHome();

        guide_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_guideFragment);
            }
        });
        playground_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_playground);
            }
        });
        lesson_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), TutorialActivity.class);
                i.putExtra("Course", lessonNames.get(nextLesson.get(0)));
                i.putExtra("Lesson", lessonNumbers.get(nextLesson.get(1)));
                startActivity(i);
            }
        });
        streaks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_streaksFragment);
            }
        });
        progressBar.setMax(total_lessons);

//        progressBar.setProgress(num_compLessons);
//        progress_Text.setText(num_compLessons + " out of " + total_lessons + " lessons completed");
//        xp_btn.setText(score + " " + new String(Character.toChars(xp_unicode)));
//        streaks_btn.setText(streaks + " " + new String(Character.toChars(fire_unicode)));
//        welcome_text.setText("Welcome, " + name + "!");

    }

    public void setUpHome() {
        // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = user.getUid();

        DocumentReference documentReference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        documentReference = firestore.collection("User").document(currentId);

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            String nameResult = task.getResult().getString("name");
                            String streakResult = task.getResult().getString("consecutiveStreakDays");
                            scoreResult = task.getResult().getLong("score");
                            lessonsCompleted = (List<String>) task.getResult().get("lessonsCompleted");

                            name = String.valueOf(nameResult);
                            streaks = Integer.parseInt(streakResult);
                            //score = Integer.parseInt(scoreResult);
                            num_compLessons = lessonsCompleted.size();

                            welcome_text.setText("Welcome, " + name + "!");
                            streaks_btn.setText(streaks + " " + new String(Character.toChars(fire_unicode)));
                            xp_btn.setText(scoreResult + " " + new String(Character.toChars(xp_unicode)));
                            progress_Text.setText(num_compLessons + " out of " + total_lessons + " lessons completed");
                            progressBar.setProgress(num_compLessons);

                            nextLesson = generateRandomLesson(lessonsCompleted, num_compLessons);

                            String lessonTitle = getLessonTitle(lessonNames.get(nextLesson.get(0)) + " " + lessonNumbers.get(nextLesson.get(1)));
                            buttonLessonText.setText(lessonTitle + " >>");

                        }else{
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //check streak should be called upon app loading to check if last day played was >=2 days ago
    public void checkStreak(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference doc = db.collection("User").document(currentUserId);

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Calendar c = Calendar.getInstance();
                        int thisDay = c.get((Calendar.DAY_OF_YEAR));
                        int lastDay;

                        //get lastdayfield from firestore document
                        lastDay = Integer.parseInt(document.getString("lastStreakDay"));
                        int counterOfConsecutiveDays = Integer.parseInt(document.getString("consecutiveStreakDays"));
                        //if last day played was yesterday


                        if (lastDay <= (thisDay -2)) {
                            counterOfConsecutiveDays = 0;
                            writeIntoDatabaseStreakDays(counterOfConsecutiveDays);
                        }
                    } else {
                        Log.d("bobo", "No such document");
                    }
                } else {
                    Log.d("bobo", "get failed with ", task.getException());
                }
            }
        });

    }

    private void writeIntoDatabaseStreakDays(int counterOfConsecutiveDays){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("User").document(currentUserId);


        doc
                .update("consecutiveStreakDays", String.valueOf(counterOfConsecutiveDays))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("dodo", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dodo", "Error updating document", e);
                    }
                });

    }

    public List<Integer> generateRandomLesson(List<String> lessonsCompleted, int numLessonsCompleted) {
        Random rand = new Random();
        Integer title = 0;
        Integer number = 0;
        String lessonTitle = "";

        if (numLessonsCompleted < total_lessons) {
            do {
                title = rand.nextInt(lessonNames.size());
                number = rand.nextInt(lessonNumbers.size());
                lessonTitle = lessonNames.get(title) + " " + lessonNumbers.get(number);
            }
            while (lessonsCompleted.contains(lessonTitle));
        }

        List<Integer> lessonSelection = Arrays.asList(title, number);

        return lessonSelection;
    }

    public String getLessonTitle(String lessonTitle) {
        if (lessonTitle.equals("Abstraction Lesson 1")) {
            return lessonTitles.get(0);
        } else if (lessonTitle.equals("Abstraction Lesson 2")){
            return lessonTitles.get(1);
        } else if (lessonTitle.equals("Algorithmic Thinking Lesson 1")){
            return lessonTitles.get(2);
        } else if (lessonTitle.equals("Algorithmic Thinking Lesson 2")){
            return lessonTitles.get(3);
        } else if (lessonTitle.equals("Decomposition Lesson 1")){
            return lessonTitles.get(4);
        } else if (lessonTitle.equals("Decomposition Lesson 2")){
            return lessonTitles.get(5);
        } else if (lessonTitle.equals("Pattern Recognition Lesson 1")){
            return lessonTitles.get(6);
        } else if (lessonTitle.equals("Pattern Recognition Lesson 2")){
            return lessonTitles.get(7);
        } else {
            return lessonTitles.get(0);
        }
    }
}