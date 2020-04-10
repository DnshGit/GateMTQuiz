package com.example.gatemtquiz3;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 2700000;
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
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
    private Button btnReview;
    private Button btnEndQuiz;
    private Button btnNext;
    private Button btnQuestionsGrid;

    private Button btnCloseGrid;
    private GridView questionsGridView;
    private LinearLayout questionsGridLayout;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> questionList;
    private ArrayList<ResultListItem> resultList;
    private ArrayList<SolutionListItem> solutionsList;
    private ArrayList<QuestionGridItem> answerStatusColorList;

    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private double score;
    private String answer;
    private RadioButton rbSelected;
    private int[] answeredList;
    private boolean[] answered;
    private long backPressedTime;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        rb1.setText("A");
        rb2.setText("B");
        rb3.setText("C");
        rb4.setText("D");

        btnQuestionsGrid = findViewById(R.id.button_questions_grid);
        btnReview = findViewById(R.id.btn_review);
        btnEndQuiz = findViewById(R.id.btn_end_quiz);
        btnNext = findViewById(R.id.btn_next);

        btnCloseGrid = findViewById(R.id.button_close_grid);
        questionsGridView = findViewById(R.id.questions_gridview);
        questionsGridLayout = findViewById(R.id.questions_grid_layout);

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
            answerStatusColorList = new ArrayList<QuestionGridItem>(questionCountTotal+1);
            answeredList = new int[questionCountTotal+1];
            answered = new boolean[questionCountTotal+1];

        //    Collections.shuffle(questionList);

            questionCounter=0;
            showQuestion();

            for (int i=questionCounter; i<questionCountTotal; i++) {
                QuestionGridItem answerStatus = new QuestionGridItem(R.drawable.rbgroup_border);
                answerStatusColorList.add(i,answerStatus);
                ResultListItem result = new ResultListItem(i, NOT_ANSWERED, 0.0);
                resultList.add(i, result);
                SolutionListItem solution = new SolutionListItem(i, questionList.get(i).getSolutionImage());
                solutionsList.add(i, solution);
            }
            questionsGridLayout.setVisibility(View.GONE);

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        }else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT) +1;
            currentQuestion = questionList.get(questionCounter);
            score = savedInstanceState.getDouble(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            questionImage = savedInstanceState.getString(KEY_IMAGE_NAME);
            imageView.setImageResource(getResources().getIdentifier(questionImage,"drawable", getPackageName()));
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

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                updateQuestionGrid(R.drawable.bg_blue);
                questionCounter++;
                showQuestion();
            }
        });

        btnQuestionsGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewQuestionsGrid();
            }
        });

        btnEndQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndQuizAlert();
            }
        });

    }

    private void viewQuestionsGrid() {
        QuestionsGridAdapter adapter = new QuestionsGridAdapter(this, R.layout.questions_grid_item, answerStatusColorList);
        questionsGridView.setAdapter(adapter);
        questionsGridLayout.setVisibility(View.VISIBLE);

        questionsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                questionCounter = position;
                showQuestion();
                questionsGridLayout.setVisibility(View.GONE);
            }
        });

        btnCloseGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionsGridLayout.setVisibility(View.GONE);
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

        if(questionCounter == questionCountTotal-1) {
            btnNext.setEnabled(false);
        }
    }

    private void checkAnswer() {

        if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
            answeredList[questionCounter] = rbGroup.getCheckedRadioButtonId();
            rbSelected = findViewById(answeredList[questionCounter]);
            answer = rbSelected.getText().toString();
            updateQuestionGrid(R.drawable.bg_green);
            if(answer.equals(currentQuestion.getAnswer())) {
                saveResultAs(CORRECT_ANSWER, 1.0);
            }else {
                saveResultAs(WRONG_ANSWER, -0.333);
            }
        }else {
            saveResultAs(NOT_ANSWERED, 0.0);
            updateQuestionGrid(R.drawable.bg_red);
        }
        answered[questionCounter] = true;
    }

    private void saveResultAs(String answeredAs, Double currentScore) {
        ResultListItem result = new ResultListItem(questionCounter, answeredAs, currentScore);
        resultList.set(questionCounter, result);
        solutionImage = currentQuestion.getSolutionImage();
        SolutionListItem solution = new SolutionListItem(questionCounter, solutionImage);
        solutionsList.set(questionCounter, solution);
    }

    private void updateQuestionGrid(int bgColor) {
        QuestionGridItem answerStatus = new QuestionGridItem(bgColor);
        answerStatusColorList.set(questionCounter,answerStatus);
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
                finishQuiz();

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
        Toast toast = Toast.makeText(QuizActivity.this, sample, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void showEndQuizAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Do you want to End Quiz?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishQuiz();
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

    private void finishQuiz() {
        checkAnswer();
        countDownTimer.cancel();
        Intent resultIntent = new Intent(QuizActivity.this, ResultsActivity.class);
        resultIntent.putExtra(EXTRA_SCORE,score);
        Bundle resultBundle = new Bundle();
        resultBundle.putParcelableArrayList(EXTRA_RESULT_LIST, resultList);
        resultBundle.putParcelableArrayList(EXTRA_SOLUTION_LIST, solutionsList);
        resultIntent.putExtras(resultBundle);
        setResult(RESULT_OK, resultIntent);
        startActivity(resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        showEndQuizAlert();
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
