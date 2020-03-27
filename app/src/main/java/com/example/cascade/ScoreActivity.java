package com.example.cascade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ScoreActivity extends AppCompatActivity implements View.OnClickListener {

    private Button returnButton;
    private Button reinitButton;

    private TextView textView;

    private ListView listView;

    public String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(this);

        reinitButton = findViewById(R.id.reinitButton);
        reinitButton.setOnClickListener(this);

        listView = findViewById(R.id.listView);

        difficulty = getIntent().getStringExtra("difficulty");

        textView = findViewById(R.id.textDisplay);

        switch (difficulty) {
            case "beginner":
                textView.setText(getString(R.string.score_text_display_difficulty, getString(R.string.main_beginner_button)));
                break;
            case "easy":
                textView.setText(getString(R.string.score_text_display_difficulty, getString(R.string.main_easy_button)));
                break;
            case "medium":
                textView.setText(getString(R.string.score_text_display_difficulty, getString(R.string.main_medium_button)));
                break;
            case "difficult":
                textView.setText(getString(R.string.score_text_display_difficulty, getString(R.string.main_difficult_button)));
                break;
            case "impossible":
                textView.setText(getString(R.string.score_text_display_difficulty, getString(R.string.main_impossible_button)));
                break;
        }

        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);

        ArrayList<String> list = new ArrayList<>();

        Map<String, ?> allEntries = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String[] split = entry.getKey().split("_");
            if (split[0].equals(difficulty)) {
                list.add(split[1] + " : " + entry.getValue().toString());
            }
        }

        Collections.sort(list);

        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, list));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.returnButton:
                finish();
                break;
            case R.id.reinitButton:

                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putInt(difficulty+"_5",0);
                editor.putInt(difficulty+"_4",0);
                editor.putInt(difficulty+"_3",0);
                editor.putInt(difficulty+"_2",0);
                editor.putInt(difficulty+"_1",0);

                editor.commit();

                Intent intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("difficulty", difficulty);
                finish();
                startActivity(intent);
        }
    }
}
