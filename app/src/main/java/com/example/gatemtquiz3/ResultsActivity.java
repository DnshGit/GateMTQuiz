package com.example.gatemtquiz3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private TextView textViewTotalScore;
    private double score=0.0;
    private long backPressedTime;
    private Button btnShowSolutions;

    private ListView resultsListView;
    private ArrayList<SolutionListItem> solutionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        textViewTotalScore = findViewById(R.id.textview_totalscore);
        resultsListView = findViewById(R.id.list_results);
        btnShowSolutions = findViewById(R.id.btn_show_solutions);

        ArrayList<ResultListItem> resultList;
        resultList = this.getIntent().getExtras().getParcelableArrayList(QuizActivity.EXTRA_RESULT_LIST);
        solutionsList = this.getIntent().getExtras().getParcelableArrayList(QuizActivity.EXTRA_SOLUTION_LIST);

        int i, totalAnswered=0, totalCorrect=0;
        double accuracy;
        for (i=0;i<resultList.size();i++) {
            double currScore = resultList.get(i).getCurrentScore();
            score+= currScore;

            if (currScore!= 0.0) {
                totalAnswered++;
            }
            if (currScore > 0.0) {
                totalCorrect++;
            }
        }
        if (totalAnswered==0) {
            accuracy = 0;
        }else {
            accuracy = (totalCorrect/totalAnswered)*100;
        }

        textViewTotalScore.setText("Score : " + score + "/" + accuracy + "%" + totalCorrect + totalAnswered);

        ResultListAdapter adapter = new ResultListAdapter(this, R.layout.list_item, resultList);
        resultsListView.setAdapter(adapter);

        btnShowSolutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent solutionsIntent = new Intent(ResultsActivity.this, SolutionsActivity.class);
                Bundle solutionBundle = new Bundle();
                solutionBundle.putParcelableArrayList(QuizActivity.EXTRA_SOLUTION_LIST, solutionsList);
                solutionsIntent.putExtras(solutionBundle);
                startActivity(solutionsIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent backIntent = new Intent(ResultsActivity.this, MainActivity.class);
            startActivity(backIntent);
            finish();
        }else {
            Toast.makeText(this, "Press back again", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}
