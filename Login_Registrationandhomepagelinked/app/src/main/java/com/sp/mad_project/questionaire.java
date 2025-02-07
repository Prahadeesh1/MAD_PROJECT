package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class questionaire extends AppCompatActivity {

    private String[] questions = {
            "You prefer to spend your free time:", "When making decisions, you rely more on:",
            "You are more comfortable with:", "Your workspace is usually:",
            "You find it easier to remember:", "You feel more energized by:",
            "In a group project, you prefer to:", "You enjoy stories that are:",
            "Your friends describe you as:", "You prefer to plan your day:",
            "Do you enjoy social gatherings?", "Are you more detail-oriented or big-picture?",
            "Do you trust your intuition?", "Do you prefer working alone or in a team?",
            "Are you more organized or spontaneous?", "Do you make decisions quickly or carefully?",
            "Do you prefer stability or excitement?", "Are you an early bird or night owl?",
            "Do you follow rules or challenge them?", "Do you enjoy trying new things?"
    };

    private String[][] options = {
            {"With friends", "Alone"}, {"Logic", "Feelings"},
            {"Routine", "Spontaneity"}, {"Tidy", "Messy"},
            {"Facts", "Concepts"}, {"Being social", "Being alone"},
            {"Take charge", "Follow"}, {"Realistic", "Imaginative"},
            {"Practical", "Creative"}, {"Scheduled", "Flexible"},
            {"Yes", "No"}, {"Details", "Big Picture"},
            {"Yes", "No"}, {"Alone", "Team"},
            {"Organized", "Spontaneous"}, {"Quickly", "Carefully"},
            {"Stability", "Excitement"}, {"Early Bird", "Night Owl"},
            {"Follow", "Challenge"}, {"Yes", "No"}
    };


    private int currentQuestion = 0;
    private int score = 0;
    private ProgressBar progressBar;
    private TextView progressText;
    private int totalQuestions = questions.length; // Update this if you change the number of questions

    private TextView[] questionTexts = new TextView[4];
    private RadioGroup[] optionGroups = new RadioGroup[4];
    private RadioButton[][] optionButtons = new RadioButton[4][2];

    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionaire);

        questionTexts[0] = findViewById(R.id.questionText1);
        questionTexts[1] = findViewById(R.id.questionText2);
        questionTexts[2] = findViewById(R.id.questionText3);
        questionTexts[3] = findViewById(R.id.questionText4);

        optionGroups[0] = findViewById(R.id.optionsGroup1);
        optionGroups[1] = findViewById(R.id.optionsGroup2);
        optionGroups[2] = findViewById(R.id.optionsGroup3);
        optionGroups[3] = findViewById(R.id.optionsGroup4);

        optionButtons[0][0] = findViewById(R.id.option1_1);
        optionButtons[0][1] = findViewById(R.id.option1_2);
        optionButtons[1][0] = findViewById(R.id.option2_1);
        optionButtons[1][1] = findViewById(R.id.option2_2);
        optionButtons[2][0] = findViewById(R.id.option3_1);
        optionButtons[2][1] = findViewById(R.id.option3_2);
        optionButtons[3][0] = findViewById(R.id.option4_1);
        optionButtons[3][1] = findViewById(R.id.option4_2);

        nextButton = findViewById(R.id.nextButton);

        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        loadQuestions();

        nextButton.setOnClickListener(v -> {
            for (int i = 0; i < 4; i++) {
                if (optionGroups[i].getCheckedRadioButtonId() != -1) {
                    if (optionButtons[i][0].isChecked()) score++;
                }
            }

            currentQuestion += 4;
            if (currentQuestion < questions.length) {
                loadQuestions();
            } else {
                Intent intent = new Intent(questionaire.this, welcomepage.class);
                intent.putExtra("SCORE", score);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestions() {
        for (int i = 0; i < 4; i++) {
            if (currentQuestion + i < questions.length) {
                questionTexts[i].setText(questions[currentQuestion + i]);
                optionButtons[i][0].setText(options[currentQuestion + i][0]);
                optionButtons[i][1].setText(options[currentQuestion + i][1]);
                optionGroups[i].clearCheck();
            } else {
                questionTexts[i].setVisibility(View.GONE);
                optionGroups[i].setVisibility(View.GONE);
            }
        }

        int progress = (int) (((float) currentQuestion / totalQuestions) * 100);
        progressBar.setProgress(progress);
        progressText.setText(progress + "% Completed");

    }
}