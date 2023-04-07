package com.example.eduapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String question;
    private String questionImage;
    private List<String> answers = new ArrayList<>();
    private String rightAnswer;
    private int mAnswered;

    public Question(String question, String questionImage, String rightAnswer, ArrayList<String> answers ) {
        this.question = question;
        this.questionImage = questionImage;
        this.rightAnswer = rightAnswer;
        this.answers.add(answers.get(0));
        this.answers.add(answers.get(1));
        this.answers.add(answers.get(2));
        this.answers.add(answers.get(3));
        mAnswered = 0;
    }


    public String getQuestion() {
        return question;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public int getAnswered() {
        return mAnswered;
    }

}
