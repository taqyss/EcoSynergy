package com.example.ecosynergy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends BaseActivity {

    // UI Elements
    private TextView scoreText, timerText, questionText;
    private RadioGroup answerOptions;
    private Button nextButton;

    // Quiz Data
    private int score = 0;
    private int lives = 3;
    private int currentQuestionIndex = 0;
    private List<Questions> questionsList;

    // Timer for the quiz
    private CountDownTimer timer;

    // Firebase reference
    private FirebaseDatabase database;

    private String currentCategory;
    private String currentLevel;
    private FirebaseDataFetcher dataFetcher;
    private DatabaseReference questionsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);  // Make sure this is your XML layout

        setupToolbar(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Recap");
        }
        setupBottomNavigation();

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("CATEGORY_NAME");
        String hierarchy = intent.getStringExtra("HIERARCHY");

        if (categoryName != null && hierarchy != null) {
            currentCategory = categoryName;
            currentLevel = hierarchy;
        }

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        questionsRef = database.getReference("questions");

        // Initialize views
        initializeViews();

        dataFetcher = new FirebaseDataFetcher();

        // Fetch all data modules first
        dataFetcher.fetchDataModules(new FirebaseDataFetcher.FirebaseCallback() {
            @Override
            public void onDataFetched(List<DataModule> dataModules) {

//                // Get data passed via Intent
//                currentCategory = getIntent().getStringExtra("CATEGORY");
//
//                // Pass data to the fragment
//                Bundle arguments = new Bundle();
//                arguments.putString("SUBCATEGORY", currentCategory);

                // Load questions from Firebase
                loadQuestionsFromFirebase("Basic","Solar Energy");

            }

            @Override
            public void onError(String errorMessage) {
                // Handle error here, e.g., log it or show a Toast message
                Log.e("ModulesContentActivity", "Error fetching data: " + errorMessage);
                Toast.makeText(QuizActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }

        });

        // Start the timer
        startTimer();

        // Set up Next Button click listener
        nextButton.setOnClickListener(v -> onNextButtonClick());
    }

    private void initializeViews() {
        scoreText = findViewById(R.id.score_text);
        timerText = findViewById(R.id.timer_text);
        questionText = findViewById(R.id.question_text);
        answerOptions = findViewById(R.id.answer_options);
        nextButton = findViewById(R.id.next_button);
    }

    private void startTimer() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText("Time: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                endQuiz();
            }
        }.start();
    }
    public static void openQuizActivity(Context context, String currentCategory, String currentLevel, DataSnapshot dataSnapshot) {
        Intent quizIntent = new Intent(context, QuizActivity.class);
        quizIntent.putExtra("Category", currentCategory);
        quizIntent.putExtra("Level", currentLevel);
        quizIntent.putExtra("QuestionSetKey", dataSnapshot.getChildren().iterator().next().getKey());
        context.startActivity(quizIntent);
    }

    private void onNextButtonClick() {
        // Check if an answer is selected
        int selectedOptionIndex = answerOptions.indexOfChild(findViewById(answerOptions.getCheckedRadioButtonId()));
        if (selectedOptionIndex == -1) {
            Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
        } else {
            // Check if the answer is correct
            Questions currentQuestion = questionsList.get(currentQuestionIndex);
            if (currentQuestion.isCorrectAnswer(selectedOptionIndex)) {
                score++;
            } else {
                lives--;
                if (lives <= 0) {
                    Toast.makeText(QuizActivity.this, "Game Over!", Toast.LENGTH_SHORT).show();
                    // You can end the game here or navigate to another screen
                }
            }

            // Update score and lives
            updateScoreAndLives();

            // Go to the next question
            currentQuestionIndex++;
            if (currentQuestionIndex < questionsList.size()) {
                displayQuestion();
            } else {
                Toast.makeText(QuizActivity.this, "Quiz Finished!", Toast.LENGTH_SHORT).show();
                // You can finish the activity or navigate to a summary page
            }
        }
    }

    private void updateScoreAndLives() {
        scoreText.setText("Score: " + score);

        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < lives; i++) {
            if (i >= score) {  // When the score is lower than lives, show broken hearts for incorrect answers
                hearts.append("💔");
            } else {
                hearts.append("❤\uFE0F");  // Use a heart symbol for correct answers
            }
        }
        TextView livesTextView = findViewById(R.id.lives_text);
        livesTextView.setText(hearts.toString());
    }

    private void loadQuestionsFromFirebase(String level, String category) {
        questionsList = new ArrayList<>();

        // Adjust the reference to include the specific level and category
        DatabaseReference questionsRef = FirebaseDatabase.getInstance()
                .getReference("dataModules")
                .child(level) // Navigate to the specific level, e.g., "Basic"
                .child(category) // Navigate to the specific category, e.g., "Solar Energy"
                .child("questionSets")
                .child("basicSolar") // Assuming "basicSolar" is the identifier for the question set
                .child("questions");

        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    String questionText = questionSnapshot.child("questionText").getValue(String.class);
                    List<String> options = new ArrayList<>();
                    for (DataSnapshot optionSnapshot : questionSnapshot.child("options").getChildren()) {
                        options.add(optionSnapshot.getValue(String.class));
                    }
                    String correctAnswer = questionSnapshot.child("correctAnswer").getValue(String.class);

                    // Convert correctAnswer to index (optional, if needed by your logic)
                    int correctAnswerIndex = options.indexOf(correctAnswer);

                    // Create a question object and add to the list
                    Questions question = new Questions(questionText, "Select the correct answer", options, correctAnswerIndex);
                    questionsList.add(question);
                }

                // Display the first question if the list is not empty
                if (!questionsList.isEmpty()) {
                    displayQuestion();
                } else {
                    Toast.makeText(QuizActivity.this, "No questions found for this level and category.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                Toast.makeText(QuizActivity.this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void displayQuestion() {
        Questions currentQuestion = questionsList.get(currentQuestionIndex);

        questionText.setText(currentQuestion.getTitle());

        // Clear previous options
        answerOptions.removeAllViews();

        // Add new options to RadioGroup
        for (String option : currentQuestion.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            answerOptions.addView(radioButton);
        }
    }

    private void endQuiz() {
        Toast.makeText(this, "Quiz Finished!", Toast.LENGTH_SHORT).show();
        // Go back to ModulesFragment
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}