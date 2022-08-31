package com.sidmobileapps.gatequiz2;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.loopj.android.http.*;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpHeaders;

public class MainActivity extends AppCompatActivity {
/*------------------------------Variables Declaration---------------------------------------------*/
    public static String solutionPdfName;
    public static String sectionName;
    public static String subSectionName;
    //Constant key Values
    public static final String EXTRA_SECTION_NAME = "extraSectionName";
    public static final String EXTRA_SUB_SECTION = "extraSubSection";
    //UI components declaration
    private Spinner spinnerSubsections;
    private Spinner spinnerSections;
    private Button btnStartQuiz;
    private Button btnStartCalc;
    //AdView declaration
    private AdView mAdView;
    // Create a Cloud Storage reference from the app
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    ArrayList imageList = new ArrayList();


    private int REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialising Variables
        spinnerSections = findViewById(R.id.spinner_section);
        spinnerSubsections = findViewById(R.id.spinner_sub_section);
        mAdView = findViewById(R.id.adViewBanner);
        btnStartQuiz = findViewById(R.id.btn_start_quiz);
        btnStartCalc = findViewById(R.id.btn_calc_link);

        appUpdate();

        showBannerAd();

        loadSections();



        AsyncHttpClient client = new AsyncHttpClient();
        //client.addHeader("Content-Type", "multipart/form-data");
        client.get("http://localhost:8181/api/siddipet ec.png", new FileAsyncHttpResponseHandler(/* Context */ this) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                // called when response HTTP status is "200 OK"

                Log.d("http", "success get file"+response.getAbsolutePath());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, File file) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("http", "failure"+file.getName());
            }
        });

        //sectionName=spinnerSections.getSelectedItem().toString();
        spinnerSections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("select subject")) {
                    sectionName = "none";
                }else{
                    sectionName = parent.getItemAtPosition(position).toString();
                    loadSubSections();
                    spinnerSubsections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getItemAtPosition(position).equals("select topic")){
                                subSectionName = "none";
                            }else{
                                subSectionName = parent.getItemAtPosition(position).toString();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            subSectionName = parent.getItemAtPosition(0).toString();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sectionName = parent.getItemAtPosition(0).toString();
            }
        });

        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                btnStartQuiz.setScaleX((float) 0.9);
                btnStartQuiz.setScaleY((float) 0.9);
                if (sectionName == "none" || subSectionName == "none") {
                    Toast.makeText(MainActivity.this, "Subject or Topic not selected", Toast.LENGTH_LONG).show();
                }else {
                    storageRef.child("images").listAll()
                            .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                @Override
                                public void onSuccess(ListResult listResult) {
                                    for (StorageReference item : listResult.getItems()) {
                                        // All the items under listRef.
                                        imageList.add(item);
                                    }
                                    Toast.makeText(MainActivity.this, "imgsz "+imageList.size(), Toast.LENGTH_SHORT).show();
                                    startQuiz(imageList);
                                }
                            });

                }
            }
        });

        btnStartCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WebCalculator.class));
            }
        });
    }
/*---------------------------------Method for Start Quiz------------------------------------------*/
    private void startQuiz(ArrayList imgList) {
        //To Generate saved PDF name
        solutionPdfName = sectionName+" "+subSectionName+".pdf";

        Intent intent = new Intent(MainActivity.this,InterstitialAd2.class);
        intent.putParcelableArrayListExtra("imgList", imgList);
        startActivity(intent);
        //To finish this Activity after Starting Next Activity
        finish();
    }
/*-------------------------------Method for Category Selector-------------------------------------*/
    private void loadSections() {
        //Getting Sections from Database
        DatabaseAccess dbHelper = DatabaseAccess.getInstance(this);
        dbHelper.open();
        List<String> sections = dbHelper.getAllSections();
        dbHelper.close();
        sections.add(0,"select subject");
        //Loading categories into Spinner using Array Adapter(Category class)
        ArrayAdapter<String> adapterSections = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sections);
        adapterSections.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSections.setAdapter(adapterSections);
    }
/*--------------------------Method for Sets(Difficulty) Selector----------------------------------*/
    private void loadSubSections() {
        //Getting SubSections from Question class
        DatabaseAccess dbHelper = DatabaseAccess.getInstance(this);
        dbHelper.open();
        List<String> subSections = dbHelper.getAllSubSections();
        dbHelper.close();
        subSections.add(0, "select topic");
        //Loading Sets(Difficulty) into Spinner using Array Adapter(String)
        ArrayAdapter<String> adapterSubSections = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, subSections);
        adapterSubSections.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubsections.setAdapter(adapterSubSections);
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
        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }
            @Override
            public void onAdClosed() {
                //Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                //Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdLeftApplication() {
                //Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
    }

    private void appUpdate(){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if(result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                    try {
                        appUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE, MainActivity.this,REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
            if (resultCode !=RESULT_OK){
                Toast.makeText(this, "Update failed"+resultCode, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
/*--------------------------------------End of Main Activity Code---------------------------------*/
