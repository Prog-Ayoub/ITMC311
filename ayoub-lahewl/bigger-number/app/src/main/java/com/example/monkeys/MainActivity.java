package com.example.monkeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button buttonLeft, buttonRight;
    TextView scoreText, highScoreText;
    int score = 0;
    int highScore = 0;
    int currentStreak = 0;
    Random random;

    public static final String PREFS_NAME = "BigNumberGamePrefs";
    public static final String HIGH_SCORE_KEY = "highScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);
        scoreText = findViewById(R.id.scoreText);
        highScoreText = findViewById(R.id.highScoreText);
        random = new Random();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        highScore = prefs.getInt(HIGH_SCORE_KEY, 0);
        updateHighScoreText();

        generateNewNumbers();

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
    }

    void generateNewNumbers() {
        int num1 = random.nextInt(100);
        int num2 = random.nextInt(100);

        while (num1 == num2) {
            num2 = random.nextInt(100);
        }

        buttonLeft.setText(String.valueOf(num1));
        buttonRight.setText(String.valueOf(num2));
    }

    void checkAnswer(boolean isLeftSelected) {
        int left = Integer.parseInt(buttonLeft.getText().toString());
        int right = Integer.parseInt(buttonRight.getText().toString());

        boolean isCorrect = (isLeftSelected && left > right) || (!isLeftSelected && right > left);
        String toastMessage;

        if (isCorrect) {
            score++;
            currentStreak++;
            toastMessage = "Correct! ";

            if (currentStreak >= 2) {
                int bonusPoints = currentStreak - 1;
                score += bonusPoints;
                toastMessage += "Streak x" + currentStreak + "! (+" + bonusPoints + " bonus points)";
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        } else {
            score--;
            currentStreak = 0;
            Toast.makeText(this, "Incorrect! Streak lost.", Toast.LENGTH_SHORT).show();
        }

        scoreText.setText("Points: " + score);

        if (score > highScore) {
            highScore = score;
            updateHighScoreText();
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putInt(HIGH_SCORE_KEY, highScore);
            editor.apply();
        }

        generateNewNumbers();
    }

    void updateHighScoreText() {
        if (highScoreText != null) {
            highScoreText.setText("High Score: " + highScore);
        }
    }
}
