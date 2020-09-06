package com.example.tictactoe.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.TextViewCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.LocaleManager;
import com.example.tictactoe.MenuActivity;
import com.example.tictactoe.R;
import com.example.tictactoe.SettingsUtility;

public class GameActivity extends AppCompatActivity {

    private GameEngine gameEngine = new GameEngine();
    private LocaleManager localeManager = new LocaleManager(this, this);
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private TextView textViewSymbolPlayer1;
    private TextView textViewSymbolPlayer2;
    private Button[][] buttons;
    private View decorView;
    private int mSymbolPlayer1;
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
            if (gameEngine.getDifficultyLevel() == SettingsUtility.DIFFICULTY_LEVEL_HARD) {
                if (gameEngine.getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
                        if (gameEngine.getPlayer1Turn()) {
                            botP1HardFirstMove();
                            botP1HardSecondMove();
                            botP1HardThirdMove();
                            botP1HardFourthMove();
                            botP1HardFifthMove();

                            String[][] field = new String[3][3];
                            loadButtonsText(field);
                            gameEngine.updateRoundCount();
                            gameEngine.roundResult(field);
                            highlightWhoStarts();
                        }
                    } else if (gameEngine.getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
                        if (!gameEngine.getPlayer1Turn()) {
                            botP2HardFirstMove();
                            botP2HardSecondMove();
                            botP2HardThirdMove();
                            botP2HardFourthMove();

                            String[][] field = new String[3][3];
                            loadButtonsText(field);
                            gameEngine.updateRoundCount();
                            gameEngine.roundResult(field);  //Only at this point player1Turn becomes false
                            highlightWhoStarts();
                        }
                    }
                }
            }
        }

    private void botP2HardFirstMove() {
        if (gameEngine.getRoundCount() == 1) {
            if (isEnemySymbol(1,1)) {
                check(0,0);
            } else {
                check(1,1);
            }
        }
    }

    private void botP2HardSecondMove() {
        if (gameEngine.getRoundCount() == 3) {
            if (match(1, 1, 2, 2) && isChecked(0,0)) {
                check(2,0);
            } else if (!isEnemySymbol(1,1)) {
                if (isEnemySymbol(0,0) || isEnemySymbol(0,2) ||
                        isEnemySymbol(2,0) || isEnemySymbol(2,2)) {
                    if (canBlock()) {
                        block();
                    } else {
                        checkEdge();
                    }
                }
            } else if (isEnemySymbol(1,1)) {
                block();
            } else if (findSymbolsOnOppositeEdges()) {
                check(0,0);
            }
        }
    }

    private void botP2HardThirdMove() {
        if (gameEngine.getRoundCount() == 5) {
            if (canWin()) {
                goForWin();
            } else if (canBlock()) {
                block();
            }
        }
    }

    private void botP2HardFourthMove() {
        if (gameEngine.getRoundCount() == 7) {
            if (canWin()) {
                goForWin();
            } else if (canBlock()) {
                block();
            } else {
                checkWhatIsLeft();
            }
        }
    }

    private void botP1HardFirstMove() {
        check(0, 0);
    }

    private void botP1HardSecondMove() {
        if (gameEngine.getRoundCount() == 2) {
            if (!(isChecked(2, 2))) {
                check(2, 2);
            } else {
                check(2, 0);
            }
        }
    }

    private void botP1HardThirdMove() {
        if (gameEngine.getRoundCount() == 4) {
            if (isChecked(1, 1)) {
                if (isChecked(0, 1)) {
                    check(2, 1);
                } else if (isChecked(2, 1)) {
                    check(0, 1);
                } else if (isChecked(1, 0)) {
                    check(1, 2);
                } else if (isChecked(1, 2)) {
                    check(1, 0);
                } else if (isChecked(0, 2)) {
                    check(2, 0);
                } else {
                    check(0, 2);
                }
            } else if (match(2, 2, 0, 0)) {
                if (isChecked(2, 0) && isChecked(0, 2)) {
                    check(1, 1);
                } else if (!(isChecked(2, 0))) {
                    check(2, 0);
                } else {
                    check(0, 2);
                }
            } else {
                if (isChecked(0, 2)) {
                    check(1, 0);
                } else {
                    check(0, 2);
                }
            }
        }
    }

    private void botP1HardFourthMove() {
        if (gameEngine.getRoundCount() == 6) {
            goForWin();
            if (gameEngine.getRoundCount() == 6) {
                block();
            }
        }
    }

    private void botP1HardFifthMove() {
        if (gameEngine.getRoundCount() == 8) {
            checkWhatIsLeft();
        }
    }

    private void checkWhatIsLeft() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!isChecked(i, j)) {
                    check(i, j);
                    return;
                }
            }
        }
    }

    private boolean findSymbolsOnOppositeEdges() {
        if ((isChecked(0,1)) && (isChecked(2,1))) {
            return true;
        } else if ((isChecked(1,0)) && (isChecked(1,2))) {
            return true;
        } else if ((isChecked(2,1)) && (isChecked(0,1))) {
            return true;
        } else if ((isChecked(1,2)) && (isChecked(1,0))) {
            return true;
        } else {
            return false;
        }
    }

    private void checkEdge() {
        if ((!isChecked(0,1)) && (!isChecked(2,1))) {
            check(0,1);
        } else if ((!isChecked(1,0)) && (!isChecked(1,2))) {
            check(1,0);
        } else if ((!isChecked(2,1)) && (!isChecked(0,1))) {
            check(2,1);
        } else if ((!isChecked(1,2)) && (!isChecked(1,0))) {
            check(1,2);
        }
    }

    private boolean isEnemySymbol(int row, int column) {
        if (gameEngine.getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
            if (gameEngine.getPlayer1Symbol() == SettingsUtility.X) {
                if (getSymbolAt(row, column).equals("O")) {
                    return true;
                }
            } else if (gameEngine.getPlayer1Symbol() == SettingsUtility.O) {
                if (getSymbolAt(row, column).equals("X")) {
                    return true;
                }
            }
        } else if (gameEngine.getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
            if (gameEngine.getPlayer1Symbol() == SettingsUtility.X) {
                if (getSymbolAt(row, column).equals("X")) {
                    return true;
                }
            } else if (gameEngine.getPlayer1Symbol() == SettingsUtility.O) {
                if (getSymbolAt(row, column).equals("O")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void goForWin() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!isEnemySymbol(i, j)) {
                    //Check vertically
                    if ((i == 0) || (i == 1)) {
                        if (match(i, j, i + 1, j)) {
                            if (i == 0) {
                                check(i + 2, j);
                            } else {
                                check(i - 1, j);
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j)) {
                                check(i + 1, j);
                            }
                        }
                    }
                    //Check horizontally
                    if ((j == 0) || (j == 1)) {
                        if (match(i, j, i, j + 1)) {
                            if (j == 0) {
                                check(i, j + 2);
                            } else {
                                check(i, j - 1);
                            }
                        } else if (j == 0) {
                            if (match(i, j, i, j + 2)) {
                                check(i, j + 1);
                            }
                        }
                    }
                    //Check the principal diagonal
                    if (((i == 0) && (j == 0)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j + 1)) {
                            if (i == 0) {
                                check(i + 1, j + 1);
                            } else {
                                check(i - 1, j - 1);
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j + 2)) {
                                check(i + 1, j + 1);
                            }
                        }
                    }
                    //Check the secondary diagonal
                    if (((i == 0) && (j == 2)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j - 1)) {
                            if (i == 0) {
                                check(i + 2, j - 2);
                            } else {
                                check(i - 1, j + 1);
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j - 2)) {
                                check(i + 1, j - 1);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean canWin() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (!isEnemySymbol(i, j)) {
                    //Check vertically
                    if ((i == 0) || (i == 1)) {
                        if (match(i, j, i + 1, j)) {
                            if (i == 0) {
                                return (!isChecked(2, j));
                            } else {
                                return (!isChecked(0, j));
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j)) {
                                return (!isChecked(1, j));
                            }
                        }
                    }
                    //Check horizontally
                    if ((j == 0) || (j == 1)) {
                        if (match(i, j, i, j + 1)) {
                            if (j == 0) {
                                return (!isChecked(i, 2));
                            } else {
                                return (!isChecked(i, 0));
                            }
                        } else if (j == 0) {
                            if (match(i, j, i, j + 2)) {
                                return (!isChecked(i, 1));
                            }
                        }
                    }
                    //Check the principal diagonal
                    if (((i == 0) && (j == 0)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j + 1)) {
                            if (i == 0) {
                                return (!isChecked(2, 2));
                            } else {
                                return (!isChecked(0, 0));
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j + 2)) {
                                return (!isChecked(1, 1));
                            }
                        }
                    }
                    //Check the secondary diagonal
                    if (((i == 0) && (j == 2)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j - 1)) {
                            if (i == 0) {
                                return (!isChecked(2, 0));
                            } else {
                                return (!isChecked(0, 2));
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j - 2)) {
                                return (!isChecked(1, 1));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void block() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isEnemySymbol(i, j)) {
                    //Check vertically
                    if ((i == 0) || (i == 1)) {
                        if (match(i, j, i + 1, j)) {
                            if (i == 0) {
                                check(i + 2, j);
                            } else {
                                check(i - 1, j);
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j)) {
                                check(i + 1, j);
                            }
                        }
                    }
                    //Check horizontally
                    if ((j == 0) || (j == 1)) {
                        if (match(i, j, i, j + 1)) {
                            if (j == 0) {
                                check(i, j + 2);
                            } else {
                                check(i, j - 1);
                            }
                        } else if (j == 0) {
                            if (match(i, j, i, j + 2)) {
                                check(i, j + 1);
                            }
                        }
                    }
                    //Check the principal diagonal
                    if (((i == 0) && (j == 0)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j + 1)) {
                            if (i == 0) {
                                check(i + 1, j + 1);
                            } else {
                                check(i - 1, j - 1);
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j + 2)) {
                                check(i + 1, j + 1);
                            }
                        }
                    }
                    //Check the secondary diagonal
                    if (((i == 0) && (j == 2)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j - 1)) {
                            if (i == 0) {
                                check(i + 2, j - 2);
                            } else {
                                check(i - 1, j + 1);
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j - 2)) {
                                check(i + 1, j - 1);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean canBlock() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (isEnemySymbol(i, j)) {
                    //Check vertically
                    if ((i == 0) || (i == 1)) {
                        if (match(i, j, i + 1, j)) {
                            if (i == 0) {
                                return (!isChecked(2, j));
                            } else {
                                return (!isChecked(0, j));
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j)) {
                                return (!isChecked(1, j));
                            }
                        }
                    }
                    //Check horizontally
                    if ((j == 0) || (j == 1)) {
                        if (match(i, j, i, j + 1)) {
                            if (j == 0) {
                                return (!isChecked(i, 2));
                            } else {
                                return (!isChecked(i, 0));
                            }
                        } else if (j == 0) {
                            if (match(i, j, i, j + 2)) {
                                return (!isChecked(i, 1));
                            }
                        }
                    }
                    //Check the principal diagonal
                    if (((i == 0) && (j == 0)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j + 1)) {
                            if (i == 0) {
                                return (!isChecked(2, 2));
                            } else {
                                return (!isChecked(0, 0));
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j + 2)) {
                                return (!isChecked(1, 1));
                            }
                        }
                    }
                    //Check the secondary diagonal
                    if (((i == 0) && (j == 2)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j - 1)) {
                            if (i == 0) {
                                return (!isChecked(2, 0));
                            } else {
                                return (!isChecked(0, 2));
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j - 2)) {
                                return (!isChecked(1, 1));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean match(int row1, int column1, int row2, int column2) {
        if (getSymbolAt(row1,column1).equals(getSymbolAt(row2,column2))) {
            return true;
        } else {
            return false;
        }
    }

    private String getSymbolAt(int row, int column) {
        return buttons[row][column].getText().toString();
    }

    private boolean isChecked(int row, int column) {
        if (!(buttons[row][column].getText().toString().equals(""))) {
            return true;
        } else {
            return false;
        }
    }

    private void check(int row, int column) {
        if (!(buttons[row][column].getText().toString().equals(""))) {
            return;
        }

        if (gameEngine.getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
            if (mSymbolPlayer1 == SettingsUtility.X) {
                buttons[row][column].setText("X");
            } else if (mSymbolPlayer1 == SettingsUtility.O) {
                buttons[row][column].setText("O");
            }
        } else if (gameEngine.getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
            if (mSymbolPlayer1 == SettingsUtility.X) {
                buttons[row][column].setText("O");
            } else if (mSymbolPlayer1 == SettingsUtility.O) {
                buttons[row][column].setText("X");
            }
        }
    }

    private void gameButtonClicked(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (mSymbolPlayer1 == SettingsUtility.X) {
            if (gameEngine.getPlayer1Turn()) {
                ((Button) v).setText("X");
            } else {
                ((Button) v).setText("O");
            }
        } else if (mSymbolPlayer1 == SettingsUtility.O) {
            if (gameEngine.getPlayer1Turn()) {
                ((Button) v).setText("O");
            } else {
                ((Button) v).setText("X");
            }
        }

        String[][] field = new String[3][3];
        loadButtonsText(field);
        gameEngine.updateRoundCount();
        gameEngine.roundResult(field);
        highlightWhoStarts();

        botMove();
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
        textViewPlayer1.setText(getResources().getString(R.string.player_1_pointsText,
                gameEngine.getPlayer1Points())); // String resources + placeholders
        textViewPlayer2.setText(getResources().getString(R.string.player_2_pointsText,
                gameEngine.getPlayer2Points()));
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
            if (mSymbolPlayer1 == SettingsUtility.X) {
                textViewSymbolPlayer1.setText("X");
                textViewSymbolPlayer2.setText("O");
            } else if (mSymbolPlayer1 == SettingsUtility.O) {
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
        mHideSystemBars = preferences.getInt(SettingsUtility.PREFS_HIDE_SYSTEM_BARS_VALUE, 0);
        mSymbolPlayer1 = preferences.getInt(SettingsUtility.PREFS_SYMBOL_VALUE, SettingsUtility.X );
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