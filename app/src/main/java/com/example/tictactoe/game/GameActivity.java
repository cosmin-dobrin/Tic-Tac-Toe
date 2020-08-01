package com.example.tictactoe.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.LocaleManager;
import com.example.tictactoe.MenuActivity;
import com.example.tictactoe.R;
import com.example.tictactoe.SettingsUtility;

public class GameActivity extends AppCompatActivity {

    GameEngine gameEngine = new GameEngine();
    LocaleManager localeManager = new LocaleManager(this, this);
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private Button[][] buttons;
    private View decorView;
    String symbolPlayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localeManager.loadLocale();
        setContentView(R.layout.activity_game);
        decorView = getWindow().getDecorView();
        gameEngine.loadGameState();
        textViewPlayer1 = findViewById(R.id.text_view_p2);
        textViewPlayer2 = findViewById(R.id.text_view_p1);
        buttons = new Button[3][3];
        Intent symbolIntent = getIntent();
        symbolPlayer1 = symbolIntent.getStringExtra(MenuActivity.EXTRA_SYMBOL);
        setUpGame();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    void setUpGame() {
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameEngine.resetGame();
            }
        });

        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonId = "button_" + i + j;
                int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = findViewById(resId);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameButtonClicked(v);
                    }
                });

                int autoSizeMaxTextSize = getResources().getInteger(R.integer.size);

                TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(buttons[i][j],
                        12, autoSizeMaxTextSize,1, 1);
            }
        }

        gameEngine.setCompletionListener(new GameCompletionListener() {
            @Override
            public void onCompletion() {
                updatePointsText();
                showToast();
                cleanBoard();
            }
        });

        firstRound();
        showWhoStarts();
        updatePointsText();
    }

    private void showWhoStarts() {
        if (gameEngine.getPlayer1Turn()) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.player_1_starts), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.player_2_starts), Toast.LENGTH_SHORT).show();
        }
    }

    private void firstRound() {
        if (gameEngine.getRoundCount() == 0) {
            if (symbolPlayer1.equals("X")) {
                gameEngine.setPlayer1Turn(true);
            } else {
                gameEngine.setPlayer1Turn(false);
            }
        }
    }

    private void gameButtonClicked(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (gameEngine.getPlayer1Turn()) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }

        String[][] field = new String[3][3];
        loadButtonsText(field);
        gameEngine.updateRoundCount();
        gameEngine.roundResult(field);
    }

    private void loadButtonsText(String[][] field) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
    }

    private void cleanBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private void updatePointsText() {
        textViewPlayer1.setText(getResources().getString(R.string.player_1_pointsText, gameEngine.getPlayer1Points())); // String resources + placeholders
        textViewPlayer2.setText(getResources().getString(R.string.player_2_pointsText, gameEngine.getPlayer2Points()));
    }

    private void showToast() {
        if ((gameEngine.getPlayer1Wins()) && (gameEngine.getRoundCount() != 0)) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.player_1_wins), Toast.LENGTH_SHORT).show();
        } else if((gameEngine.getPlayer2Wins()) && (gameEngine.getRoundCount() != 0)) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.player_2_wins), Toast.LENGTH_SHORT).show();
        } else if(gameEngine.getRoundCount() != 0) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.draw), Toast.LENGTH_SHORT).show();
        }
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameEngine.saveGameState();
    }
}