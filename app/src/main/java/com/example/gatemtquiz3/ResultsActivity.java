package com.example.gatemtquiz3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private TextView textViewTotalScore;
    private TextView textViewAccuracy;
    private Button btnShowSolutions;
    private LinearLayout gifLayout;

    private ListView resultsListView;
    ArrayList<ResultListItem> resultList;
    private ArrayList<SolutionListItem> solutionsList;

    private RewardedVideoAd mRewardedVideoAd;

    private double score=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

        gifLayout = findViewById(R.id.gif_layout);
        textViewTotalScore = findViewById(R.id.textview_score);
        textViewAccuracy = findViewById(R.id.textview_accuracy);
        resultsListView = findViewById(R.id.list_results);
        btnShowSolutions = findViewById(R.id.btn_show_solutions);

        gifLayout.setVisibility(View.VISIBLE);
        btnShowSolutions.setEnabled(false);

        resultList = this.getIntent().getExtras().getParcelableArrayList(QuizActivity.EXTRA_RESULT_LIST);
        solutionsList = this.getIntent().getExtras().getParcelableArrayList(QuizActivity.EXTRA_SOLUTION_LIST);

        showVideoAd();
    }

    private void showResults() {
        gifLayout.setVisibility(View.GONE);
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

        btnShowSolutions.setEnabled(true);
        btnShowSolutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent solutionsIntent = new Intent(ResultsActivity.this, SolutionsActivity.class);
                Bundle solutionBundle = new Bundle();
                solutionBundle.putParcelableArrayList(QuizActivity.EXTRA_SOLUTION_LIST, solutionsList);
                solutionsIntent.putExtras(solutionBundle);
                //        startActivity(solutionsIntent);

                Intent solutionsPdfIntent = new Intent(ResultsActivity.this, SolutionsActivity2.class);
                startActivity(solutionsPdfIntent);
            }
        });
    }

    private void showVideoAd() {
        // Real AdUnitId mRewardedVideoAd.loadAd(getString(R.string.rewarded_video)
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder()
                            .build());
        // make sure the ad is loaded completely before showing it
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
        // setting listener on Ad
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(ResultsActivity.this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " +
                        rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(ResultsActivity.this, "onRewardedVideoAdLeftApplication",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRewardedVideoAdClosed() {
            }
            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                showResults();
            }
            @Override
            public void onRewardedVideoAdLoaded() {
                showResults();
                Toast.makeText(ResultsActivity.this, "Please Watch Video To Continue", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRewardedVideoAdOpened() {

            }
            @Override
            public void onRewardedVideoStarted() {
            }
        });
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        MainActivity mainActivity = new MainActivity();
        mainActivity.showAppCloseAlert(ResultsActivity.this);
    }
}
