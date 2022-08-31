package com.sidmobileapps.gatequiz2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private TextView textViewTotalScore;
    private TextView textViewAccuracy;
    private Button btnShowSolutions;
    private LinearLayout gifLayout;

    private ListView resultsListView;
    ArrayList<ResultListItem> resultList;

    private double score=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        gifLayout = findViewById(R.id.gif_layout);
        textViewTotalScore = findViewById(R.id.textview_score);
        textViewAccuracy = findViewById(R.id.textview_accuracy);
        resultsListView = findViewById(R.id.list_results);
        btnShowSolutions = findViewById(R.id.btn_show_solutions);

        resultList = this.getIntent().getExtras().getParcelableArrayList(QuizActivity.EXTRA_RESULT_LIST);

        prepResults();
        showResults();
    }

    private void prepResults() {
        int i, totalAnswered=0, totalCorrect=0;
        double accuracy=0.0;
        for (i=0;i<resultList.size();i++) {
            double currScore = resultList.get(i).getCurrentScore();
            score+= currScore;

            if (currScore!= 0.0) {
                totalAnswered++;
            }
            if (currScore == 1.0) {
                totalCorrect++;
            }
        }
        if (totalAnswered!=0) {
            accuracy = ((double) totalCorrect/(double) totalAnswered)*100;
        }

        textViewTotalScore.setText(String.format("%.2f",score));
        textViewAccuracy.setText(String.format("%.1f",accuracy) + "%");

        ResultListAdapter adapter = new ResultListAdapter(this, R.layout.list_item, resultList);
        resultsListView.setAdapter(adapter);

        btnShowSolutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ResultsActivity.this, InterstitialAdActivity.class));
            }
        });
    }

    private void showResults() {
        gifLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        MainActivity mainActivity = new MainActivity();
        mainActivity.showAppCloseAlert(ResultsActivity.this);
    }
}
