package com.example.renergify;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private DatabaseReference quizRef;
    private ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();

    private TextView questionText, questionNumberText;
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3, option4;
    private MaterialButton submitButton;
    private CardView questionCard;

    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.questionText);
        questionNumberText = findViewById(R.id.questionNumberText);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        submitButton = findViewById(R.id.submitButton);
        questionCard = findViewById(R.id.questionCard);

        quizRef = FirebaseDatabase.getInstance().getReference("quizQuestions");

        quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizQuestions.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    QuizQuestion question = dataSnapshot.getValue(QuizQuestion.class);
                    quizQuestions.add(question);
                }
                displayQuestion(currentQuestionIndex);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
            }
        });

        submitButton.setOnClickListener(v -> {
            if (!answered) {
                checkAnswer();
            }
        });
    }

    private void displayQuestion(int index) {
        if (index < quizQuestions.size()) {
            QuizQuestion q = quizQuestions.get(index);

            questionNumberText.setText("Question " + (index + 1) + "/" + quizQuestions.size());
            questionText.setText(q.getQuestion());
            option1.setText(q.getOption1());
            option2.setText(q.getOption2());
            option3.setText(q.getOption3());
            option4.setText(q.getOption4());
            optionsGroup.clearCheck();

            // Reset colors
            option1.setBackgroundColor(Color.WHITE);
            option2.setBackgroundColor(Color.WHITE);
            option3.setBackgroundColor(Color.WHITE);
            option4.setBackgroundColor(Color.WHITE);

            answered = false;
        } else {
            showQuizResult();
        }
    }

    private void checkAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
            return;
        }

        answered = true;
        RadioButton selectedRadio = findViewById(selectedId);
        String selectedAnswer = selectedRadio.getText().toString();
        String correctAnswer = quizQuestions.get(currentQuestionIndex).getCorrectAnswer();

        if (selectedAnswer.equals(correctAnswer)) {
            selectedRadio.setBackgroundColor(Color.GREEN);
            score++;
        } else {
            selectedRadio.setBackgroundColor(Color.RED);
            if (option1.getText().toString().equals(correctAnswer)) option1.setBackgroundColor(Color.GREEN);
            if (option2.getText().toString().equals(correctAnswer)) option2.setBackgroundColor(Color.GREEN);
            if (option3.getText().toString().equals(correctAnswer)) option3.setBackgroundColor(Color.GREEN);
            if (option4.getText().toString().equals(correctAnswer)) option4.setBackgroundColor(Color.GREEN);
        }

        new Handler().postDelayed(() -> {
            Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
            questionCard.startAnimation(slideOut);

            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationEnd(Animation animation) {
                    currentQuestionIndex++;
                    displayQuestion(currentQuestionIndex);
                    Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
                    questionCard.startAnimation(slideIn);
                }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });

        }, 1500);
    }

    private void showQuizResult() {
        Toast.makeText(this, "Quiz Completed! Your Score: " + score + "/" + quizQuestions.size(), Toast.LENGTH_LONG).show();
        // You can also navigate to a result screen or restart quiz

        // Delay a bit so user sees the Toast, then finish this activity to return to Knowledge_AreaFragment
        new Handler().postDelayed(() -> {
            finish();  // Ends QuizActivity and returns to previous screen (Knowledge_AreaFragment)
        }, 2000);  // 2 seconds delay (adjust if you want)
    }
}
