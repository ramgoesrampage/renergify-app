package com.example.renergify;

public class QuizQuestion {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String correctAnswer;

    public QuizQuestion() {
        // Needed for Firebase
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
