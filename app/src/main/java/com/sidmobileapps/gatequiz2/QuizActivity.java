package com.sidmobileapps.gatequiz2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
/*------------------------------Variables Declaration---------------------------------------------*/
    // Test time Constant in MilliSeconds
    private static final long COUNTDOWN_IN_MILLIS = (45*60*1000);
    // Constant KeyValues
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";
    public static final String KEY_IMAGE_NAME = "keyImageName";
    public static final String KEY_RESULT_LIST = "keyResultList";
    public static final String KEY_ANSWER_COLOR_LIST = "keyAnswerColorList";
    public static final String KEY_ANSWERED_LIST_ARRAY = "keyAnsweredListArray";
    public static final String KEY_ANSWERED_ARRAY = "keyAnsweredArray";
    public static final String EXTRA_RESULT_LIST = "extraResultList";
    public static final String NOT_ANSWERED = "Not Attempted";
    public static final String CORRECT_ANSWER = "Correct";
    public static final String WRONG_ANSWER = "Incorrect";
    // UI Components declaration
    private TextView textViewQuestionNum;
    private TextView textViewSection;
    private TextView textViewClock;
    private ImageView imageView;
    private String questionImage;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button btnReview;
    private Button btnEndQuiz;
    private Button btnNext;
    private Button btnQuestionsGrid;
    private Button btnCalc;
    // Questions grid UI components
    private Button btnCloseGrid;
    private GridView questionsGridView;
    private LinearLayout questionsGridLayout;
    private RelativeLayout instructionsLayout;
    private Button btnCloseInstructions;
    // Timer declaration
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    // Other Variables
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private String answer;
    private RadioButton rbSelected;
    // Arrays and Lists declaration
    private int[] answeredList;
    private boolean[] answered;
    private ArrayList<Question> questionList;
    private ArrayList<ResultListItem> resultList;
    private ArrayList<QuestionGridItem> answerStatusColorList;

    private WebView webview2;
    private Button btnBackCalc;
    private RelativeLayout calcLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        calcLayout = findViewById(R.id.calc_layout);
        btnBackCalc = findViewById(R.id.btn_back_calc);
        webview2 = findViewById(R.id.webview2);
        webview2.setWebViewClient(new WebViewClient());
        webview2.getSettings().setJavaScriptEnabled(true);
        webview2.getSettings().setDomStorageEnabled(true);
        webview2.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview2.loadUrl("https://www.tcsion.com/OnlineAssessment/ScientificCalculator/Calculator.html#nogo");
        //Initialisation
        textViewSection = findViewById(R.id.textview_section);
        textViewClock = findViewById(R.id.textview_clock);
        textViewQuestionNum = findViewById(R.id.textview_question_num);
        imageView = findViewById(R.id.image_view);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_btn1);
        rb2 = findViewById(R.id.radio_btn2);
        rb3 = findViewById(R.id.radio_btn3);
        rb4 = findViewById(R.id.radio_btn4);
        btnQuestionsGrid = findViewById(R.id.button_questions_grid);
        btnReview = findViewById(R.id.btn_review);
        btnEndQuiz = findViewById(R.id.btn_end_quiz);
        btnNext = findViewById(R.id.btn_next);
        btnCalc = findViewById(R.id.btn_calc);
        btnCloseGrid = findViewById(R.id.button_close_grid);
        questionsGridView = findViewById(R.id.questions_gridview);
        questionsGridLayout = findViewById(R.id.questions_grid_layout);
        instructionsLayout = findViewById(R.id.instructions_layout);
        btnCloseInstructions = findViewById(R.id.close_instructions_button);
        //Getting Values from MainActivity
        String sectionName = MainActivity.sectionName;
        String subSection = MainActivity.subSectionName;
        //Setting Category and Set(Difficulty)
        textViewSection.setText(sectionName + " ( " + subSection + " )");
        //Setting Radio Button text
        rb1.setText("A");
        rb2.setText("B");
        rb3.setText("C");
        rb4.setText("D");
/*------------------------------------Activity Starts First time----------------------------------*/
        if(savedInstanceState == null) {
            // Getting questions from Database
            DatabaseAccess dbHelper = DatabaseAccess.getInstance(this);
            dbHelper.open();
            questionList = dbHelper.getQuestions(sectionName, subSection);
            dbHelper.close();
            ArrayList imageList = new ArrayList();
            imageList = this.getIntent().getParcelableArrayListExtra("imgList");
            //Toast.makeText(this, "img sz "+imageList.size(), Toast.LENGTH_SHORT).show();
            //Initialising Arrays and Lists
            questionCountTotal = questionList.size();
            resultList = new ArrayList<ResultListItem>(questionCountTotal+1);
            answerStatusColorList = new ArrayList<QuestionGridItem>(questionCountTotal+1);
            answeredList = new int[questionCountTotal+1];
            answered = new boolean[questionCountTotal+1];
        //    Collections.shuffle(questionList);
            // Loading First question
            questionCounter=0;
            showQuestion();
            // Saving Defaults States
            for (int i=questionCounter; i<questionCountTotal; i++) {
                QuestionGridItem answerStatus = new QuestionGridItem(R.drawable.rbgroup_border);
                answerStatusColorList.add(i,answerStatus);
                ResultListItem result = new ResultListItem(i, NOT_ANSWERED, 0.0);
                resultList.add(i, result);
            }
            questionsGridLayout.setVisibility(View.GONE);
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            btnCloseInstructions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    instructionsLayout.setVisibility(View.GONE);
                    startCountDown();
                }
            });
        }else {
/*---------------------------------Activity Restarts From Saved state-----------------------------*/
            instructionsLayout.setVisibility(View.GONE);
            questionsGridLayout.setVisibility(View.GONE);
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT) +1;
            currentQuestion = questionList.get(questionCounter);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            questionImage = savedInstanceState.getString(KEY_IMAGE_NAME);
            imageView.setImageResource(getResources().getIdentifier(questionImage,"drawable", getPackageName()));
            resultList = savedInstanceState.getParcelableArrayList(KEY_RESULT_LIST);
            answerStatusColorList = savedInstanceState.getParcelableArrayList(KEY_ANSWER_COLOR_LIST);
            answeredList = savedInstanceState.getIntArray(KEY_ANSWERED_LIST_ARRAY);
            answered = savedInstanceState.getBooleanArray(KEY_ANSWERED_ARRAY);
            updateCountDownText();
            startCountDown();
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                if (questionCounter<questionCountTotal){
                    questionCounter++;
                    showQuestion();
                }
            }
        });

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcLayout.setVisibility(View.VISIBLE);
            }
        });

        btnBackCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcLayout.setVisibility(View.GONE);
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
/*-------------------------------Method to View Questions grid------------------------------------*/
    private void viewQuestionsGrid() {
        // Loading grid using Custom Array Adapter
        QuestionsGridAdapter adapter = new QuestionsGridAdapter(this, R.layout.questions_grid_item, answerStatusColorList);
        questionsGridView.setAdapter(adapter);
        // Setting listener for grid items
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

        questionsGridLayout.setVisibility(View.VISIBLE);
    }
/*-----------------------------------Method to Show Question--------------------------------------*/
    private void showQuestion() {
        // Setting radio button from saved state
        if(answered[questionCounter]) {
            rbGroup.check(answeredList[questionCounter]);
        }else {
            rbGroup.clearCheck();
        }
        // Loading question
        if(questionCounter<questionCountTotal) {
            btnReview.setEnabled(true);
            btnNext.setEnabled(true);
            textViewQuestionNum.setText("Question : " + (questionCounter+1) + "/" + questionCountTotal);

            currentQuestion = questionList.get(questionCounter);
            questionImage = currentQuestion.getQuestionImage();
            imageView.setImageResource(getResources().getIdentifier(questionImage,"drawable", getPackageName()));
        }else {
            toastCenter("This is Last Question. Click End Quiz to get Result");
            // Disabling review button after last question
            btnReview.setEnabled(false);
            btnNext.setEnabled(false);
        }
        // Disabling Next button for last question
        if (questionCounter==questionCountTotal-1){
            btnNext.setText("SAVE");
        }else {
            btnNext.setText("SAVE & NEXT");
        }
    }
/*---------------------------------Method for Checking and Saving Answer--------------------------*/
    private void checkAnswer() {
        if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
            answeredList[questionCounter] = rbGroup.getCheckedRadioButtonId();
            rbSelected = findViewById(answeredList[questionCounter]);
            answer = rbSelected.getText().toString();
            updateQuestionGrid(R.drawable.bg_green);
            Double correctMarks = Double.valueOf(currentQuestion.getMarks());
            Double negativeMarks = -(correctMarks/3);
            if(answer.equals(currentQuestion.getAnswer())) {
                saveResultAs(CORRECT_ANSWER, correctMarks);
            }else {
                saveResultAs(WRONG_ANSWER, negativeMarks);
            }
        }else {
            saveResultAs(NOT_ANSWERED, 0.0);
            updateQuestionGrid(R.drawable.bg_red);
        }
        answered[questionCounter] = true;
    }
/*-----------------------------------------Other sub methods======================================*/
    private void saveResultAs(String answeredAs, Double currentScore) {
        ResultListItem result = new ResultListItem(questionCounter, answeredAs, currentScore);
        resultList.set(questionCounter, result);
    }

    private void updateQuestionGrid(int bgColor) {
        QuestionGridItem answerStatus = new QuestionGridItem(bgColor);
        answerStatusColorList.set(questionCounter,answerStatus);
    }

    private void toastCenter(String sample){
        Toast toast = Toast.makeText(QuizActivity.this, sample, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
/*----------------------------------Methods for managing Timer------------------------------------*/
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
/*---------------------------------Method to Show End Quiz Alert----------------------------------*/
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
/*------------------------------------Method to Finish Quiz Activity------------------------------*/
    private void finishQuiz() {
        countDownTimer.cancel();
        // Passing Array Lists to Results Activity
        Intent resultIntent = new Intent(QuizActivity.this, ResultsActivity.class);
        Bundle resultBundle = new Bundle();
        resultBundle.putParcelableArrayList(EXTRA_RESULT_LIST, resultList);
        resultIntent.putExtras(resultBundle);
        setResult(RESULT_OK, resultIntent);

        startActivity(resultIntent);
        finish();
    }
/*------------------------------Method for Back Press Button--------------------------------------*/
    @Override
    public void onBackPressed() {
        toastCenter("Back Button is Disabled");
    }
/*-------------------------Methods for Activity Life Cycle Management-----------------------------*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer!= null) {
            countDownTimer.cancel();
        }
    }
/*--------------------------------Method for Saving Activity State--------------------------------*/
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
        outState.putString(KEY_IMAGE_NAME, questionImage);
        outState.putParcelableArrayList(KEY_RESULT_LIST, resultList);
        outState.putParcelableArrayList(KEY_ANSWER_COLOR_LIST, answerStatusColorList);
        outState.putIntArray(KEY_ANSWERED_LIST_ARRAY, answeredList);
        outState.putBooleanArray(KEY_ANSWERED_ARRAY, answered);
    }
}
/*--------------------------------------End of Quiz Activity Code---------------------------------*/