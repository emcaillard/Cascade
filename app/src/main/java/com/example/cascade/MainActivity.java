package com.example.cascade;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button beginnerButton;
    private Button easyButton;
    private Button mediumButton;
    private Button difficultButton;
    private Button impossibleButton;

    private ImageView score_beginner;
    private ImageView score_easy;
    private ImageView score_medium;
    private ImageView score_difficult;
    private ImageView score_impossible;

    private Spinner languageSpinner;

    private Context context;

    private boolean initialisation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // memorize context to OnItemSelected who change "this"
        context = this;

        // to avoid first launch of OnItemSelected during onCreate function
        initialisation = true;

        // buttons for each difficulty
        beginnerButton = findViewById(R.id.beginnerButton);
        beginnerButton.setOnClickListener(this);

        easyButton = findViewById(R.id.easyButton);
        easyButton.setOnClickListener(this);

        mediumButton = findViewById(R.id.mediumButton);
        mediumButton.setOnClickListener(this);

        difficultButton = findViewById(R.id.difficultButton);
        difficultButton.setOnClickListener(this);

        impossibleButton = findViewById(R.id.impossibleButton);
        impossibleButton.setOnClickListener(this);

        // imageViews to access scores
        score_beginner = findViewById(R.id.score_beginner);
        score_beginner.setOnClickListener(this);

        score_easy = findViewById(R.id.score_easy);
        score_easy.setOnClickListener(this);

        score_medium = findViewById(R.id.score_medium);
        score_medium.setOnClickListener(this);

        score_difficult = findViewById(R.id.score_difficult);
        score_difficult.setOnClickListener(this);

        score_impossible = findViewById(R.id.score_impossible);
        score_impossible.setOnClickListener(this);

        // when item selected in language spinner, refresh activity with new config
        languageSpinner = findViewById(R.id.languageSpinner);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!initialisation) {
                    String selectedItem = parent.getItemAtPosition(position).toString();

                    String lang = null;

                    switch (selectedItem) {
                        case "Français":
                            lang = "fr";
                            break;
                        case "English":
                            lang = "en";
                            break;
                    }

                    if (!lang.equals(null) && !Locale.getDefault().getLanguage().equals(lang)) {
                        Configuration config = new Configuration(context.getResources().getConfiguration());

                        switch (lang) {
                            case "fr":
                                config.setLocale(Locale.FRENCH);
                                break;
                            case "en":
                                config.setLocale(Locale.ENGLISH);
                                break;
                        }

                        context.getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                        Intent refresh = new Intent(context, MainActivity.class);
                        finish();
                        startActivity(refresh);
                    }
                } else {
                    initialisation = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> languages = new ArrayList();
        switch (Locale.getDefault().getLanguage()) {
            case "fr":
                languages.add("Français");
                languages.add("English");
                break;
            case "en":
                languages.add("English");
                languages.add("Français");
                break;
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languages);
        languageSpinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, getString(R.string.main_loading), Toast.LENGTH_SHORT).show();

        Intent intent;
        switch (v.getId()) {
            case R.id.beginnerButton:
                intent = new Intent(this, GameActivity.class);
                intent.putExtra("difficulty", "beginner");
                break;
            case R.id.easyButton:
                intent = new Intent(this, GameActivity.class);
                intent.putExtra("difficulty", "easy");
                break;
            case R.id.mediumButton:
                intent = new Intent(this, GameActivity.class);
                intent.putExtra("difficulty", "medium");
                break;
            case R.id.difficultButton:
                intent = new Intent(this, GameActivity.class);
                intent.putExtra("difficulty", "difficult");
                break;
            case R.id.impossibleButton:
                intent = new Intent(this, GameActivity.class);
                intent.putExtra("difficulty", "impossible");
                break;
            case R.id.score_beginner:
                intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("difficulty", "beginner");
                break;
            case R.id.score_easy:
                intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("difficulty", "easy");
                break;
            case R.id.score_medium:
                intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("difficulty", "medium");
                break;
            case R.id.score_difficult:
                intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("difficulty", "difficult");
                break;
            case R.id.score_impossible:
                intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("difficulty", "impossible");
                break;
            default:
                intent = new Intent(this, GameActivity.class);
                intent.putExtra("difficulty", "beginner");
        }
        startActivity(intent);
    }
}
