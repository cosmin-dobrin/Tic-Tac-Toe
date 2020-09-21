package com.dobrincosminiulian.tictactoe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dobrincosminiulian.tictactoe.engines.BotEngine;
import com.dobrincosminiulian.tictactoe.GameCompletionListener;
import com.dobrincosminiulian.tictactoe.engines.GameEngine;
import com.dobrincosminiulian.tictactoe.LocaleManager;
import com.dobrincosminiulian.tictactoe.R;
import com.dobrincosminiulian.tictactoe.SettingsUtility;

public class GameActivity extends AppCompatActivity {

    private GameEngine gameEngine = new GameEngine();
    private LocaleManager localeManager = new LocaleManager(this, this);
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private TextView textViewSymbolPlayer1;
    private TextView textViewSymbolPlayer2;
    private Button[][] buttons;
    private View decorView;
    private int mHideSystemBars = 0;
    private boolean mSinglePlayerMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localeManager.loadLocale();
        setContentView(R.layout.activity_game);
        decorView = getWindow().getDecorView();
        loadSettings();
        gameEngine.loadGameState();
        Intent intent = getIntent();
        mSinglePlayerMode = intent.getBooleanExtra(MenuActivity.EXTRA_SINGLE_PLAYER_MODE, false);
        setUpGame();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mHideSystemBars == SettingsUtility.HIDE_SYSTEM_BARS) {
            if (hasFocus) {
                decorView.setSystemUiVisibility(hideSystemBars());
            } else {
                if (hasFocus) {
                    decorView.setSystemUiVisibility(showSystemBars());
                }
            }
        }
    }

    private void setUpGame() {
        textViewPlayer1 = findViewById(R.id.text_view_p2);
        textViewPlayer2 = findViewById(R.id.text_view_p1);
        textViewSymbolPlayer1 = findViewById(R.id.text_view_symbol_1);
        textViewSymbolPlayer2 = findViewById(R.id.text_view_symbol_2);
        buttons = new Button[3][3];
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameEngine.resetGame();
                highlightWhoStarts();
            }
        });

        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (mHideSystemBars == SettingsUtility.HIDE_SYSTEM_BARS) {
                    if (visibility == 0) {
                        decorView.setSystemUiVisibility(hideSystemBars());
                    }
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
                botMove();
            }
        });

        showPlayerSymbol();
        updatePointsText();
        highlightWhoStarts();
        botMove();
    }

    private void botMove() {
        if (mSinglePlayerMode) {
            BotEngine botEngine = new BotEngine(gameEngine);
            if (gameEngine.getDifficultyLevel() == SettingsUtility.DIFFICULTY_LEVEL_HARD) {
                if (gameEngine.getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
                        if (gameEngine.getPlayer1Turn()) {

                            updateButtonsText(botEngine.botP1HardMoves(loadButtonsText()));
                            updateButtonsText(botEngine.botP2HardMoves(loadButtonsText()));

                            gameEngine.updateRoundCount();
                            gameEngine.roundResult(loadButtonsText());
                            highlightWhoStarts();
                        }
                    } else if (gameEngine.getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
                        if (!gameEngine.getPlayer1Turn()) {

                            updateButtonsText(botEngine.botP2HardMoves(loadButtonsText()));
                            updateButtonsText(botEngine.botP1HardMoves(loadButtonsText()));

                            gameEngine.updateRoundCount();
                            gameEngine.roundResult(loadButtonsText());
                            highlightWhoStarts();
                        }
                    }
                } else if (gameEngine.getDifficultyLevel() == SettingsUtility.DIFFICULTY_LEVEL_MEDIUM) {
                    if (gameEngine.getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
                        if (gameEngine.getPlayer1Turn()) {

                            updateButtonsText(botEngine.botP1MediumMoves(loadButtonsText()));
                            updateButtonsText(botEngine.botP2MediumMoves(loadButtonsText()));

                            gameEngine.updateRoundCount();
                            gameEngine.roundResult(loadButtonsText());
                            highlightWhoStarts();
                        }
                    } else if (gameEngine.getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
                        if (!gameEngine.getPlayer1Turn()) {

                            updateButtonsText(botEngine.botP2MediumMoves(loadButtonsText()));
                            updateButtonsText(botEngine.botP1MediumMoves(loadButtonsText()));

                            gameEngine.updateRoundCount();
                            gameEngine.roundResult(loadButtonsText());
                            highlightWhoStarts();
                        }
                    }
                } else if (gameEngine.getDifficultyLevel() == SettingsUtility.DIFFICULTY_LEVEL_EASY) {
                    if (gameEngine.getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
                        if (gameEngine.getPlayer1Turn()) {

                            updateButtonsText(botEngine.botP1EasyMoves(loadButtonsText()));
                            updateButtonsText(botEngine.botP2EasyMoves(loadButtonsText()));

                            gameEngine.updateRoundCount();
                            gameEngine.roundResult(loadButtonsText());
                            highlightWhoStarts();
                        }
                    } else if (gameEngine.getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
                        if (!gameEngine.getPlayer1Turn()) {

                            updateButtonsText(botEngine.botP2EasyMoves(loadButtonsText()));
                            updateButtonsText(botEngine.botP1EasyMoves(loadButtonsText()));

                            gameEngine.updateRoundCount();
                            gameEngine.roundResult(loadButtonsText());
                            highlightWhoStarts();
                        }
                    }
                }
            }
        }

    private void gameButtonClicked(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (gameEngine.getSymbolPlayer1() == SettingsUtility.X) {
            if (gameEngine.getPlayer1Turn()) {
                ((Button) v).setText("X");
            } else {
                ((Button) v).setText("O");
            }
        } else if (gameEngine.getSymbolPlayer1() == SettingsUtility.O) {
            if (gameEngine.getPlayer1Turn()) {
                ((Button) v).setText("O");
            } else {
                ((Button) v).setText("X");
            }
        }

        gameEngine.updateRoundCount();
        gameEngine.roundResult(loadButtonsText());
        highlightWhoStarts();
        
        botMove();
    }

    private void updateButtonsText(String[][] gameTable) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                buttons[i][j].setText(gameTable[i][j]);
            }
        }
    }

    private String[][] loadButtonsText() {

        String[][] field = new String[3][3];

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        return field;
    }

    private void cleanBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private void updatePointsText() {
        textViewPlayer1.setText(getResources().getString(R.string.player_1_pointsText,
                gameEngine.getPlayer1Points())); // String resources + placeholders
        textViewPlayer2.setText(getResources().getString(R.string.player_2_pointsText,
                gameEngine.getPlayer2Points()));
    }

    private void showToast() {
        if (gameEngine.getPlayer1Wins()) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.player_1_wins), Toast.LENGTH_SHORT).show();
        } else if (gameEngine.getPlayer2Wins()) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.player_2_wins), Toast.LENGTH_SHORT).show();
        } else if((!gameEngine.getPlayer1Wins()) && (!gameEngine.getPlayer1Wins())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.draw), Toast.LENGTH_SHORT).show();
        }
    }

    private void highlightWhoStarts() {
        if (gameEngine.getPlayer1Turn()) {
            textViewSymbolPlayer1.setTextColor(Color.BLACK);
            textViewSymbolPlayer2.setTextColor(Color.GRAY);
        } else {
            textViewSymbolPlayer1.setTextColor(Color.GRAY);
            textViewSymbolPlayer2.setTextColor(Color.BLACK);
        }
    }

    private void showPlayerSymbol() {
            if (gameEngine.getSymbolPlayer1() == SettingsUtility.X) {
                textViewSymbolPlayer1.setText("X");
                textViewSymbolPlayer2.setText("O");
            } else if (gameEngine.getSymbolPlayer1() == SettingsUtility.O) {
                textViewSymbolPlayer1.setText("O");
                textViewSymbolPlayer2.setText("X");
            }
            textViewSymbolPlayer1.setTextColor(Color.BLACK);
            textViewSymbolPlayer2.setTextColor(Color.GRAY);
    }

    private void loadSettings() {
        SharedPreferences preferences = this.getSharedPreferences(SettingsUtility.PREFS_FILE_SETTINGS, Context.MODE_PRIVATE);
        gameEngine.setWhoStarts(preferences.getInt(SettingsUtility.PREFS_WHO_STARTS_VALUE, SettingsUtility.WINNER_STARTS));
        gameEngine.setWhoStartsDraw(preferences.getInt(SettingsUtility.PREFS_WHO_STARTS_DRAW_VALUE, SettingsUtility.DRAW_OTHER_PLAYER_STARTS));
        gameEngine.setWhoIsPlayer1(preferences.getInt(SettingsUtility.PREFS_PLAYER1_VALUE, SettingsUtility.BOT_IS_PLAYER_1));
        gameEngine.setDifficultyLevel(preferences.getInt(SettingsUtility.PREFS_DIFFICULTY_VALUE, SettingsUtility.DIFFICULTY_LEVEL_HARD));
        gameEngine.setSymbolPlayer1(preferences.getInt(SettingsUtility.PREFS_SYMBOL_VALUE, SettingsUtility.X ));
        mHideSystemBars = preferences.getInt(SettingsUtility.PREFS_HIDE_SYSTEM_BARS_VALUE, 0);
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    private int showSystemBars() {
        return View.SYSTEM_UI_FLAG_VISIBLE;
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameEngine.saveGameState();
    }
}