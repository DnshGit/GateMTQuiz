package com.example.gatemtquiz3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 3600000;
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";
    public static final String KEY_IMAGE_NAME = "keyImageName";
    public static final String EXTRA_RESULT_LIST = "extraResultList";
    public static final String EXTRA_SOLUTION_LIST = "extraSolutionList";
    public static final String NOT_ANSWERED = "Not Attempted";
    public static final String CORRECT_ANSWER = "Correct";
    public static final String WRONG_ANSWER = "Incorrect";

    private TextView textViewQuestion;
    private TextView textViewQuestionNum;
    private TextView textViewDifficulty;
    private TextView textViewCategory;
    private TextView textViewClock;
    private ImageView imageView;
    private String questionImage;
    private String solutionImage;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button btnPrev;
    private Button btnSubmit;
    private Button btnNext;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> questionList;
    private ArrayList<ResultListItem> resultList;
    private ArrayList<SolutionListItem> solutionsList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private double score;
    private String answer;
    private RadioButton rbSelected;
    private int[] answeredList;
    private boolean[] answered;
    private long backPressedTime;

    private AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.textview_question);
        textViewQuestionNum = findViewById(R.id.textview_question_num);
        textViewDifficulty = findViewById(R.id.textview_difficulty);
        textViewCategory = findViewById(R.id.textview_category);
        textViewClock = findViewById(R.id.textview_clock);
        imageView = findViewById(R.id.image_view);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_btn1);
        rb2 = findViewById(R.id.radio_btn2);
        rb3 = findViewById(R.id.radio_btn3);
        rb4 = findViewById(R.id.radio_btn4);
        btnPrev = findViewById(R.id.btn_prev);
        btnSubmit = findViewById(R.id.btn_submit);
        btnNext = findViewById(R.id.btn_next);

        rb1.setText("A");
        rb2.setText("B");
        rb3.setText("C");
        rb4.setText("D");

        alertBuilder = new AlertDialog.Builder(this);

        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);
        String difficulty = intent.getStringExtra(MainActivity.EXTRA_DIFFICULTY);

        textViewCategory.setText(categoryName);
        textViewDifficulty.setText(difficulty);

        if(savedInstanceState == null) {
            DatabaseAccess dbHelper = DatabaseAccess.getInstance(this);
            dbHelper.open();
            questionList = dbHelper.getQuestions(categoryID, difficulty);
            dbHelper.close();
            questionCountTotal = questionList.size();
            resultList = new ArrayList<ResultListItem>(questionCountTotal+1);
            solutionsList = new ArrayList<SolutionListItem>(questionCountTotal+1);
            answeredList = new int[questionCountTotal+1];
            answered = new boolean[questionCountTotal+1];
        //    Collections.shuffle(questionList);

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
            showQuestion();
        }else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT) +1;
            currentQuestion = questionList.get(questionCounter);
            score = savedInstanceState.getDouble(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            questionImage = savedInstanceState.getString(KEY_IMAGE_NAME);
            imageView.setImageResource(getResources().getIdentifier("drawable/question_images" + questionImage,
                    null, getPackageName()));

            updateCountDownText();
            startCountDown();
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                questionCounter++;
                showQuestion();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertBuilder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                checkAnswer();
                                submitQuiz();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = alertBuilder.create();
                //Setting the title manually
                alert.setTitle("Alert!");
                alert.show();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionCounter--;
                showQuestion();
            }
        });

    }

    private void showQuestion() {
        if(answered[questionCounter]) {
            rbGroup.check(answeredList[questionCounter]);
        }else {
            rbGroup.clearCheck();
        }
        textViewQuestionNum.setText("Question : " + (questionCounter+1) + "/" + questionCountTotal);

        currentQuestion = questionList.get(questionCounter);
        textViewQuestion.setText(currentQuestion.getQuestion());
        questionImage = currentQuestion.getQuestionImage();
        imageView.setImageResource(getResources().getIdentifier(questionImage,"drawable", getPackageName()));

        if(questionCounter == 0) {
            btnPrev.setEnabled(false);
        }else {
            btnPrev.setEnabled(true);
        }
        if(questionCounter == questionCountTotal-1) {
            btnNext.setEnabled(false);
        }
    }

    private void checkAnswer() {
        if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
            answeredList[questionCounter] = rbGroup.getCheckedRadioButtonId();
            rbSelected = findViewById(answeredList[questionCounter]);
            answer = rbSelected.getText().toString();
            if(answer.equals(currentQuestion.getAnswer())) {
                saveResultAs(CORRECT_ANSWER, 1.0);
            }else {
                saveResultAs(WRONG_ANSWER, -0.33);
            }
        }else {
            saveResultAs(NOT_ANSWERED, 0.0);
        }
        answered[questionCounter] = true;
    }

    private void saveResultAs(String answeredAs, Double currentScore) {
        if (answered[questionCounter]) {
            resultList.remove(questionCounter);
            solutionsList.remove(questionCounter);
        }
        ResultListItem result = new ResultListItem(currentQuestion.getId(), answeredAs, currentScore);
        resultList.add(questionCounter, result);
        solutionImage = currentQuestion.getSolutionImage();
        SolutionListItem solution = new SolutionListItem(currentQuestion.getId(), solutionImage);
        solutionsList.add(questionCounter, solution);
    }

    private void submitQuiz() {
        while (questionCounter+1 <= questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);
            saveResultAs(NOT_ANSWERED, 0.0);
            questionCounter++;
        }
        finishQuiz();
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
                submitQuiz();

            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes  = (int) (timeLeftInMillis/1000) /60;
        int seconds = (int) (timeLeftInMillis/1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        textViewClock.setText(timeFormatted);

        if(timeLeftInMillis < 300000) {
            textViewClock.setTextColor(Color.RED);
        }else {
            textViewClock.setTextColor(Color.BLACK);
        }
    }

    private void toastCenter(String sample){
        Toast toast = Toast.makeText(QuizActivity.this, sample, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void finishQuiz() {
        countDownTimer.cancel();
        Intent resultIntent = new Intent(QuizActivity.this, ResultsActivity.class);
        resultIntent.putExtra(EXTRA_SCORE,score);
        Bundle resultBundle = new Bundle();
        resultBundle.putParcelableArrayList(EXTRA_RESULT_LIST, resultList);
        resultBundle.putParcelableArrayList(EXTRA_SOLUTION_LIST, solutionsList);
        resultIntent.putExtras(resultBundle);
        setResult(RESULT_OK, resultIntent);
        startActivity(resultIntent);
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent backIntent = new Intent(QuizActivity.this, MainActivity.class);
            startActivity(backIntent);
        }else {
            toastCenter("Press back again");
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer!= null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
        outState.putString(KEY_IMAGE_NAME,questionImage);
    }
}
