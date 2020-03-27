package com.example.cascade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * @author Emilien Caillard
 *
 * This class
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;

    public GridView gameGrid;

    public ImageView viewScore;

    private Button returnButton;
    private Button playAgainButton;

    private TextView currentScore;
    private TextView bestScore;

    public ArrayList<String> images;
    public ArrayList<String> listImages;

    private GridAdapter gridAdapter;

    private String difficulty;

    private int nbCases;
    private int nbColumns;

    private boolean gameOver;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(this);

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(this);

        viewScore = findViewById(R.id.viewScore);
        viewScore.setOnClickListener(this);

        currentScore = findViewById(R.id.currentScore);
        currentScore.setText("Score : 0");
        bestScore = findViewById(R.id.bestScore);

        gameGrid = findViewById(R.id.gridView);

        difficulty = getIntent().getStringExtra("difficulty");
        switch (difficulty) {
            case "beginner":
                nbColumns = 5;
                break;
            case "easy":
                nbColumns = 6;
                break;
            case "medium":
                nbColumns = 7;
                break;
            case "difficult":
                nbColumns = 8;
                break;
            case "impossible":
                nbColumns = 9;
                break;
            default:
                nbColumns = 4;
        }

        gameGrid.setNumColumns(nbColumns);
        nbCases = nbColumns * nbColumns;

        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);

        bestScore.setText(getString(R.string.game_best_score, sharedPref.getInt(difficulty + "_1", 0)));

        context = this;

        gameOver = false;
        score = 0;

        listImages = new ArrayList<>();
        listImages.add("empty");
        listImages.add("blue");
        listImages.add("red");
        listImages.add("purple");
        listImages.add("yellow");

        images = new ArrayList();

        for (int i = 0; i < nbCases; i++) {
            int r = (int) (1 + Math.random() * 4);
            images.add(listImages.get(r));
        }


        gridAdapter = new GridAdapter(this, images);
        gameGrid.setAdapter(gridAdapter);

        gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!gameOver) {

                    if (adjacentCells(position)) {
                        int impactedCells = deleteRec(position);
                        score += calculateScore(impactedCells);
                    } else {
                        score -= 10;
                    }

                    bottomImages();
                    leftAlign();

                    currentScore.setText(getString(R.string.game_current_score, score));

                    if (checkIfGameEnded()) {
                        endGame();
                    }

                    gridAdapter = new GridAdapter(context, images);
                    gameGrid.setAdapter(gridAdapter);
                }
            }
        });
    }

    // Check if one of the cell near the click is the same that the clicked
    public boolean adjacentCells(int position) {
        boolean res = false;
        if ((position % nbColumns) + 1 < nbColumns) {
            if (images.get(position + 1) == images.get(position)) {
                res = true;
            }
        }
        if ((position % nbColumns) - 1 >= 0) {
            if (images.get(position - 1) == images.get(position)) {
                res = true;
            }
        }
        if ((position + nbColumns < nbCases)) {
            if (images.get(position + nbColumns) == images.get(position)) {
                res = true;
            }
        }
        if ((position - nbColumns >= 0)) {
            if (images.get(position - nbColumns) == images.get(position)) {
                res = true;
            }
        }

        return res;
    }

    // Delete all the same cells who touch the cell clicked
    public int deleteRec(int position) {
        String color = images.get(position);

        images.set(position, "empty");
        int score = 1;
        if ((position % nbColumns) + 1 < nbColumns) {
            if (images.get(position + 1) == color) {
                score += deleteRec(position + 1);
            }
        }
        if ((position % nbColumns) - 1 >= 0) {
            if (images.get(position - 1) == color) {
                score += deleteRec(position - 1);
            }
        }
        if ((position + nbColumns < nbCases)) {
            if (images.get(position + nbColumns) == color) {
                score += deleteRec(position + nbColumns);
            }
        }
        if ((position - nbColumns >= 0)) {
            if (images.get(position - nbColumns) == color) {
                score += deleteRec(position - nbColumns);
            }
        }

        return score;
    }

    // Scroll down the pictures
    public void bottomImages() {
        for (int i = 0; i < nbCases - nbColumns; i++) {
            if (images.get(i) != listImages.get(0) && images.get(i + nbColumns) == listImages.get(0)) {
                images.set(i + nbColumns, images.get(i));
                images.set(i, listImages.get(0));
                i = -1;
            }
        }
    }

    // Align all the pictures on the left
    public void leftAlign() {
        for (int i = 1; i < nbColumns; i++) {
            if (images.get(nbCases - (i + 1)) == listImages.get(0) && images.get(nbCases - i) != listImages.get(0)) {
                for (int j = 0; j < nbColumns; j++) {
                    images.set((nbColumns - (i + 1)) + j * nbColumns, images.get((nbColumns - i) + j * nbColumns));
                    images.set((nbColumns - i) + j * nbColumns, listImages.get(0));
                }
                i = 0;
            }
        }
    }

    // Return the score of the current turn
    public int calculateScore(int impactedCells) {
        switch (impactedCells) {
            case 2:
                return 0;
            case 3:
                return 10;
            default:
                return ((impactedCells * 18) + 10);
        }
    }

    // At each turn check if an another turn is possible
    public boolean checkIfGameEnded() {
        boolean end = true;
        for (int i = 0; i < nbCases; i++) {
            if (images.get(i) != listImages.get(0)) {
                if (adjacentCells(i)) {
                    end = false;
                }
            }
        }
        return end;
    }

    // Save the score if it's a good and advertise the player that is ended
    public void endGame() {
        Toast.makeText(context, getString(R.string.game_end_game), Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (score > sharedPref.getInt(difficulty + "_1", 0)) {
            editor.putInt(difficulty+"_5",sharedPref.getInt(difficulty+"_4",0));
            editor.putInt(difficulty+"_4",sharedPref.getInt(difficulty+"_3",0));
            editor.putInt(difficulty+"_3",sharedPref.getInt(difficulty+"_2",0));
            editor.putInt(difficulty+"_2",sharedPref.getInt(difficulty+"_1",0));
            editor.putInt(difficulty+"_1",score);
        } else if (score > sharedPref.getInt(difficulty + "_2", 0)) {
            editor.putInt(difficulty+"_5",sharedPref.getInt(difficulty+"_4",0));
            editor.putInt(difficulty+"_4",sharedPref.getInt(difficulty+"_3",0));
            editor.putInt(difficulty+"_3",sharedPref.getInt(difficulty+"_2",0));
            editor.putInt(difficulty+"_2",score);
        } else if (score > sharedPref.getInt(difficulty + "_3", 0)) {
            editor.putInt(difficulty+"_5",sharedPref.getInt(difficulty+"_4",0));
            editor.putInt(difficulty+"_4",sharedPref.getInt(difficulty+"_3",0));
            editor.putInt(difficulty+"_3",score);
        } else if (score > sharedPref.getInt(difficulty + "_4", 0)) {
            editor.putInt(difficulty+"_5",sharedPref.getInt(difficulty+"_4",0));
            editor.putInt(difficulty+"_4",score);

        } else if (score > sharedPref.getInt(difficulty + "_5", 0)) {
            editor.putInt(difficulty+"_5",score);
        }
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.playAgainButton:
                finish();
                intent = new Intent(this, GameActivity.class);
                intent.putExtra("difficulty", difficulty);
                startActivity(intent);
                break;
            case R.id.returnButton:
                finish();
                break;
            case R.id.viewScore:
                intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("difficulty", difficulty);
                startActivity(intent);
                break;
        }
    }
}
