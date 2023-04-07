package com.example.eduapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.savvi.rangedatepicker.CalendarPickerView;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class StreaksFragment extends BaseFragment {

    CalendarPickerView calendar;
    ImageButton back_btn;
    TextView streaks_label;

    int fire_unicode = 0x1F525;
    int streaks = 0;
    int lastStreakDay = 0;

    Calendar c = Calendar.getInstance();
    LocalDate today = LocalDate.now();
    LocalDate lastStreakDayLD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_streaks, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

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
                            String streakResult = task.getResult().getString("consecutiveStreakDays");
                            String lastStreakDayResult = task.getResult().getString("lastStreakDay");

                            streaks = Integer.parseInt(streakResult);
                            lastStreakDay = Integer.parseInt(lastStreakDayResult);

                            lastStreakDayLD = Year.of(today.getYear()).atDay(lastStreakDay);

                            ArrayList<Date> dates = new ArrayList<Date>();

                            if (streaks > 0) {
                                for (int i = 0; i < streaks; i++) {
                                    int streakDay = lastStreakDay - i;
                                    LocalDate streakDayLD = Year.of(today.getYear()).atDay(streakDay);
                                    Date d = new Date(streakDayLD.getYear()-1, streakDayLD.getMonthValue()-1, streakDayLD.getDayOfMonth());
                                    dates.add(d);
                                }
                            }

                            calendar.init(new Date(today.getYear()-1, 0, 1), new Date(2022, 11, 1))
                                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                                    .withSelectedDate(new Date(today.getYear()-1, today.getMonthValue()-1, today.getDayOfMonth()))
                                    .withHighlightedDates(dates);

                            streaks_label.setText(streaks + " " + new String(Character.toChars(fire_unicode)));

                        }else{
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar = view.findViewById(R.id.calendar_view);
        back_btn = view.findViewById(R.id.streaksbck_btn);
        streaks_label = view.findViewById(R.id.streaks_label);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)requireActivity()).onBackPressed();
            }
        });
        calendar.init(new Date(today.getYear()-1, 0, 1), new Date(2022, 3, 1))
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(new Date(today.getYear()-1, today.getMonthValue()-1, today.getDayOfMonth()));
    }

}
