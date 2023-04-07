package com.example.eduapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



public class ForgotPasswordFragment extends BaseFragment {
    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
    Button toLogin_btn, toRegister_btn;
    EditText resetPassword_Email_eT;
    TextView resetPassword_tV;
    ImageButton back_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resetPassword_Email_eT = view.findViewById(R.id.resetPassword_Email_eT);
        toLogin_btn = view.findViewById(R.id.toLogin_btn);
        toRegister_btn = view.findViewById(R.id.toRegister_btn);
        resetPassword_tV = view.findViewById(R.id.resetPassword_tV);
        back_btn = view.findViewById(R.id.forgotPass_back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)requireActivity()).onBackPressed();
            }
        });

        toLogin_btn.setOnClickListener(v -> Navigation.findNavController(getView()).navigate(R.id.action_forgotPasswordFragment_to_loginFragment));

        toRegister_btn.setOnClickListener(v -> Navigation.findNavController(getView()).navigate(R.id.action_forgotPasswordFragment_to_registerFragment));

        resetPassword_tV.setOnClickListener(v -> {
            String userEmail = resetPassword_Email_eT.getText().toString();

            if (TextUtils.isEmpty(userEmail)){
                resetPassword_tV.setTextColor(Color.parseColor("#808080"));
                resetPassword_Email_eT.setError("Enter an email");
            }
            else if ( !userEmail.matches(regex)){
                resetPassword_tV.setTextColor(Color.parseColor("#808080"));
                resetPassword_Email_eT.setError("Enter an valid email");
            }
            else{
                if (!TextUtils.isEmpty(userEmail) || userEmail.matches(regex)){
                    resetPassword_tV.setTextColor(Color.parseColor("#154734"));
                }
                firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(),"Check your email account...",Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);

                    }
                    else{
                        String message= task.getException().getMessage();
                        Toast.makeText(getContext(),"Error Occured: "+message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}