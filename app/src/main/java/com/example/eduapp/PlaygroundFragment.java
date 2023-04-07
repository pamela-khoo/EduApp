package com.example.eduapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.eduapp.ihavetofly.MinigameActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;


public class PlaygroundFragment extends BaseFragment {

    MaterialCardView card1, card2, card3;
    ArrayList<Card> cardlist = new ArrayList<>();

    private class Card {

        MaterialCardView card;
        boolean unlock;
        int required_points;
        String title;
        ImageView lock;
        int linking_page;

        public Card(MaterialCardView card, int required_points, String title, ImageView lock, int linking_page) {
            this.card = card;
            this.required_points = required_points;
            unlock = false;
            this.title = title;
            this.lock = lock;
            this.linking_page = linking_page;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playground, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        card1 = view.findViewById(R.id.card);
        card2 = view.findViewById(R.id.card2);
        card3 = view.findViewById(R.id.card3);


        Card c1 = new Card(
                card1,
                2,
                getResources().getString(R.string.playground_card1),
                view.findViewById(R.id.lock_card1),
                R.id.action_playground_to_factsFragment
                );
        Card c2 = new Card(
                card2,
                3,
                getResources().getString(R.string.playground_card2),
                view.findViewById(R.id.lock_card2),
                R.id.action_playground_to_popCatFragment
        );
        Card c3 = new Card(
                card3,
                8,
                getResources().getString(R.string.playground_card3),
                view.findViewById(R.id.lock_card3),
                R.id.action_playground_to_factsFragment // not link to this
        );

        cardlist.add(c1);
        cardlist.add(c2);
        cardlist.add(c3);

        for(Card c: cardlist){
            c.card.setStrokeWidth(0);
            c.card.setClickable(true);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        String currentId = user.getUid();
        DocumentReference documentReference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        documentReference = firestore.collection("User").document(currentId);

        documentReference.get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()){
                        if (null != task.getResult().getLong("score")) {
                            int score = task.getResult().getLong("score").intValue();

                            // Check the status, and set the display of play and unlock buttons
                            for(Card c: cardlist){
                                if (score >= c.required_points){
                                    c.lock.setVisibility(View.INVISIBLE);
                                    c.unlock = true;
                                }
                            }


                            // Add different onclick output
                            for(Card c:cardlist){
                                if (c.lock.getVisibility() == View.VISIBLE){
                                    String message = "You need to reach " + c.required_points + " score to unlock " + c.title;
                                    c.card.setOnClickListener(v -> Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show());
                                }
                                else if(c.required_points == 8){
                                    c.card.setOnClickListener(v -> {
                                        Intent i = new Intent(getActivity().getApplicationContext(), MinigameActivity.class);
                                        startActivity(i);
                                    });
                                }
                                else{
                                    c.card.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(c.linking_page));
                                }

                            }
                        }
                    }else{
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }




}