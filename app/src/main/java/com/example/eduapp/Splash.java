package com.example.eduapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class Splash extends BaseFragment {
    private static int SPLASH_TIME_OUT =3000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.splash_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(user!=null) {

            Navigation.findNavController(view).navigate(R.id.action_splash_to_navigation_home);
            Toast.makeText(getContext(), user.getUid(), Toast.LENGTH_SHORT).show();

        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        Navigation.findNavController(view).navigate(R.id.action_splash_to_fragment_introduce);
                }
            }, SPLASH_TIME_OUT);
        }
    }


}
