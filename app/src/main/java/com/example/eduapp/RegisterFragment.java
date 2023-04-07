package com.example.eduapp;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
    TextView haveAccount;
    ImageView profileImage_register;
    EditText editText_name_register, editText_dob_register, editText_email_register, editText_password_register;
    ImageView register_back_btn;
    Button register_btn;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    ProgressDialog progressDialog;
    User user;
    DatePickerDialog picker;
    private static final int PICK_IMAGE = 1;
   String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = new User();
        haveAccount = view.findViewById(R.id.haveAccount_text);
        editText_name_register = view.findViewById(R.id.editText_name_register);
        editText_dob_register = view.findViewById(R.id.editText_dob_register);
        editText_email_register = view.findViewById(R.id.editText_email_register);
        editText_password_register = view.findViewById(R.id.editText_password_register);
        register_back_btn = view.findViewById(R.id.register_back_btn);
        register_btn = view.findViewById(R.id.register_btn);
        profileImage_register = view.findViewById(R.id.profileImage_register);
        progressDialog = new ProgressDialog(getContext());

        mAuth = FirebaseAuth.getInstance();


        register_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_loginFragment);
                // startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        profileImage_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,PICK_IMAGE);
            }
        });


        register_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AuthenticateUser();
            }
        });
        editText_dob_register.setInputType(InputType.TYPE_NULL);
        editText_dob_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editText_dob_register.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                picker.show();

            }

        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (requestCode == PICK_IMAGE || resultCode == RESULT_OK || data!=null || data.getData() != null){
                imageUri = data.getData();

                Picasso.get().load(imageUri).into(profileImage_register);
            }
        }
        catch(Exception e){
            Toast.makeText(getContext(), "Choose an image!" , Toast.LENGTH_SHORT).show();

        }

    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }


    private void AuthenticateUser() {


        String email = editText_email_register.getText().toString();
        String name = editText_name_register.getText().toString();
        String dob = editText_dob_register.getText().toString();
        String password = editText_password_register.getText().toString();
        Calendar c = Calendar.getInstance();
        String lastDay = String.valueOf(c.get((Calendar.DAY_OF_YEAR))-1); //set last day played to the day before



        if (!email.matches(regex)) {
            editText_email_register.setError("Enter valid email");
        } else if (password.isEmpty() || password.length() < 6) {
            editText_password_register.setError("Enter proper password");
        }
       else if(dob.isEmpty()){
            editText_dob_register.setError("Enter your Date of Birth");
        }
        else if(name.isEmpty()){
            editText_name_register.setError("Enter your name");
        }
        else if (imageUri == null){
            Toast.makeText(getContext(), "Choose an image!" , Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("Please wait for registration");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);

            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        mUser = mAuth.getCurrentUser();
                        currentUserId = mUser.getUid();

                        documentReference = db.collection("User").document(currentUserId);
                        storageReference = FirebaseStorage.getInstance().getReference("Profile images");
                        databaseReference = database.getReference("All Users");

                        final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(imageUri));
                        uploadTask = reference.putFile(imageUri);


                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return reference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();

                                    Map<String, Object> profile = new HashMap<>();
                                    profile.put("name", name);
                                    profile.put("email", email);
                                    profile.put("dob", dob);
                                    profile.put("uid",currentUserId);
                                    profile.put("url", downloadUri.toString());
                                    profile.put("score", 0);
                                    profile.put("lastStreakDay",lastDay);
                                    profile.put("consecutiveStreakDays","0");
                                    profile.put("lessonsCompleted", new ArrayList<String>());


                                    //profile.put("privacy", "Public");

                                    user.setDob(dob);
                                    user.setEmail(email);
                                    user.setUsername(name);
                                    user.setUid(currentUserId);
                                    user.setUrl(downloadUri.toString());
                                    user.setScore(0);
                                    user.setlastStreakDay(lastDay);
                                    user.setconsecutiveStreakDays("0");
                                    user.setLessonsCompleted(new ArrayList<String>());


                                    databaseReference.child(currentUserId).setValue(user);

                                    documentReference.set(profile)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();


                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //FirebaseFirestore.getInstance().collection("User").document(currentUserId).update("lessonsCompleted", FieldValue.arrayUnion("Dummy"));
                                                            Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_navigation_home);
                                                        }
                                                    },2000);
                                                }
                                            });

                                }
                            }
                        });



                }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_navigation_home);
    }
}
