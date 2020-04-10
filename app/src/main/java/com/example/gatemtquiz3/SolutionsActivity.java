package com.example.gatemtquiz3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SolutionsActivity extends AppCompatActivity {
    private ListView solutionsListView;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solutions);

        solutionsListView = findViewById(R.id.list_solutions);
        ArrayList<SolutionListItem> solutionsList;

        solutionsList = this.getIntent().getExtras().getParcelableArrayList(QuizActivity.EXTRA_SOLUTION_LIST);

        SolutionsListAdapter adapter = new SolutionsListAdapter(this, R.layout.solution_list_item, solutionsList);
        solutionsListView.setAdapter(adapter);

        Button solutions2 = findViewById(R.id.button_solutions2);
        solutions2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SolutionsActivity.this, SolutionsActivity2.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent backIntent = new Intent(SolutionsActivity.this, MainActivity.class);
            startActivity(backIntent);
            finish();
        }else {
            Toast.makeText(this, "Press back again", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}
