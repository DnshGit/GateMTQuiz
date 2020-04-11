package com.example.gatemtquiz3;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
/*------------------------------Variables Declaration---------------------------------------------*/
    public static String solutionPdfName;
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    public static final String EXTRA_CATEGORY_ID = "extraCategoryId";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
/*----------------------------------UI Declaration------------------------------------------------*/
    private Spinner spinnerDifficulty;
    private Spinner spinnerCategory;
    private Button btnStartQuiz;
/*---------------------------------AdView Declaration---------------------------------------------*/
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*------------------------------Code for Category Selector----------------------------------------*/
        spinnerCategory = findViewById(R.id.spinner_category);
        loadCategories();
/*--------------------------------Code for Sets Selector------------------------------------------*/
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        loadDifficultyLevels();
/*------------------------------Code for Showing Banner Ad----------------------------------------*/
        mAdView = findViewById(R.id.adViewBanner);
        showBannerAd();
/*----------------------------------Code for Start Quiz-------------------------------------------*/
        btnStartQuiz = findViewById(R.id.btn_start_quiz);
        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                btnStartQuiz.setScaleX((float) 0.9);
                btnStartQuiz.setScaleY((float) 0.9);
                startQuiz();
            }
        });
    }
/*---------------------------------Method for Start Quiz------------------------------------------*/
    private void startQuiz() {
        Categories selectedCategory = (Categories) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        //To Generate saved PDF name
        solutionPdfName = categoryName+" "+difficulty+".pdf";

        Intent intent = new Intent(MainActivity.this,QuizActivity.class);
        //Passing variables to Next Activity
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        startActivity(intent);
        //To finish this Activity after Starting Next Activity
        finish();
    }
/*-------------------------------Method for Category Selector-------------------------------------*/
    private void loadCategories() {
        //Getting Categories from Database
        DatabaseAccess dbHelper = DatabaseAccess.getInstance(this);
        dbHelper.open();
        List<Categories> categories = dbHelper.getAllCategories();
        dbHelper.close();
        //Loading categories into Spinner using Array Adapter(Category class)
        ArrayAdapter<Categories> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }
/*--------------------------Method for Sets(Difficulty) Selector----------------------------------*/
    private void loadDifficultyLevels() {
        //Getting Sets(Difficulty) from Question class
        String[] difficultyLevels = Question.getAllDifficultyLevels();
        //Loading Sets(Difficulty) into Spinner using Array Adapter(String)
        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }
/*-----------------------------Method for Closing App Alert---------------------------------------*/
    public void showAppCloseAlert(Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        //Alert Message
        alertBuilder.setMessage("Do you want to Close this Application ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                        moveTaskToBack(true);
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
/*---------------------------------Method for Showing Banner Ad-----------------------------------*/
    private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }
            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
        mAdView.loadAd(adRequest);
    }
/*------------------------------Method for Back Press Button--------------------------------------*/
    @Override
    public void onBackPressed() {
        showAppCloseAlert(this);
    }
/*-------------------------Methods for Activity Life Cycle Management-----------------------------*/
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
/*--------------------------------------End of Main Activity Code---------------------------------*/
