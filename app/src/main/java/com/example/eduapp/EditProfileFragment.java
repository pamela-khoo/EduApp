package com.example.eduapp;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class EditProfileFragment extends Fragment {

    EditText name_tv_profile, editProfile_dob;
    TextView gender, editProfile_dob_tv, editProfile_title;
    Button male_btn, female_btn, updateProfile_btn;
    ImageView editProfile_back_btn, editProfileImage;
    String genderString;
    final int PICK_IMAGE = 1;
    Uri imageUri;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://kidoozedatabase-default-rtdb.firebaseio.com/");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    DocumentReference documentReference;
    StorageReference storageReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)requireActivity()).setBottomNavVisibility(true);
        mainViewModel =
                new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


            name_tv_profile = view.findViewById(R.id.name_tv_profile);
            editProfile_dob = view.findViewById(R.id.editProfile_dob);
            gender = view.findViewById(R.id.gender);
            editProfile_dob_tv =view.findViewById(R.id.editProfile_dob_tv);
            editProfile_title =view.findViewById(R.id.editProfile_title);
            male_btn = view.findViewById(R.id.male_btn);
            female_btn = view.findViewById(R.id.female_btn);
            updateProfile_btn = view.findViewById(R.id.updateProfile_btn);
            editProfile_back_btn = view.findViewById(R.id.editProfile_back_btn);
            editProfileImage = view.findViewById(R.id.editProfileImage);

            editProfile_back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().onBackPressed();
                }
            });

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String currentUserId = user.getUid();

            storageReference = FirebaseStorage.getInstance().getReference("Profile images");
            editProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImage();
                }
            });

            male_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeGender(false);
                }
            });

            female_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeGender(true);
                }
            });

            documentReference = db.collection("User").document(currentUserId);
            documentReference.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String nameResult = task.getResult().getString("name");
                                String dob_result = task.getResult().getString("dob");
                                String url = task.getResult().getString("url");
                                String gender = task.getResult().getString("gender");

                                Picasso.get().load(url).into(editProfileImage);
                                name_tv_profile.setText(nameResult);
                                editProfile_dob.setText(dob_result);

                                setGenderBackground(gender);
                            } else {
                                Toast.makeText(getContext(), "No profile exist!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            updateProfile_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageUri == null){

                        updateProfile(null,null );
                    }
                    else {
                        uploadImage();
                    }
                }
            });
        }

        private void changeGender(boolean isGirl) {
            if (!isGirl) {
                male_btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                genderString = male_btn.getText().toString();
                female_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
            } else {
                male_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                genderString = female_btn.getText().toString();
                female_btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
            }
        }

        public void setGenderBackground(String gender) {
            if (gender == null) {
                male_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                female_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
            } else if (gender.equals("Male")) {
                male_btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                female_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
            } else {
                male_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                female_btn.setBackgroundColor(getResources().getColor(R.color.purple_200));
            }
        }

        private String getFileExt(Uri uri) {
            ContentResolver contentResolver = getContext().getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
        }

        public void uploadImage() {

            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(imageUri));
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
//                            Log.d("HTTP", "onSuccess: " + downloadUri);
                            Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                            updateProfile(downloadUri.toString(), reference);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void chooseImage() {
            Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGallery, PICK_IMAGE);

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            try {
                if (requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data.getData() != null) {
                    imageUri = data.getData();
                    Picasso.get().load(imageUri).into(editProfileImage);
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Choose an image to update", Toast.LENGTH_SHORT).show();

            }
        }


        private void updateProfile(String downloadUrl, StorageReference reference) {

            String new_name = name_tv_profile.getText().toString();
            String new_dob = editProfile_dob.getText().toString();

            final DocumentReference doc = db.collection("User").document(currentUserId);

            db.runTransaction(new Transaction.Function<Void>() {
                @Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(doc);
                    if (downloadUrl != null) {
                        transaction.update(doc, "url", downloadUrl);

                    }
                        transaction.update(doc, "name", new_name);
                        transaction.update(doc, "dob", new_dob);
                        transaction.update(doc, "gender", genderString);

                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void avoid) {
                    Toast.makeText(getContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Transaction Failed" + e, Toast.LENGTH_SHORT).show();
                            if (reference != null){
                            reference.delete();}
                        }
                    });

        }

}