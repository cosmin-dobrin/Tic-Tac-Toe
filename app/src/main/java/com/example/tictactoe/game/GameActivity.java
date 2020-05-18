package com.example.tictactoe.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.R;

public class GameActivity extends AppCompatActivity {

    GameEngine gameEngine = new GameEngine();

    TextView textViewPlayer1;
    TextView textViewPlayer2;
    private Button[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameEngine.loadGameState();
        textViewPlayer1 = findViewById(R.id.text_view_p2);
        textViewPlayer2 = findViewById(R.id.text_view_p1);
        buttons = new Button[3][3];
        setUpGame();



        gameEngine.setCompletionListener(new GameCompletionListener() {
            @Override
            public void onCompletion() {
                updatePointsText();
                showToast();
                cleanBoard();
            }
        });
    }

    void setUpGame() {
        Button buttonReset = findViewById(R.id.button_reset);

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
            }
        }

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameEngine.resetGame();
            }
        });
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

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        gameEngine.updateRoundCount();
        gameEngine.roundResult(field);
    }

    private void cleanBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private void updatePointsText() {
        textViewPlayer1.setText(getResources().getString(R.string.player_1_updatePointsText, gameEngine.getPlayer1Points())); // String resources + placeholders
        textViewPlayer2.setText(getResources().getString(R.string.player_2_updatePointsText, gameEngine.getPlayer2Points()));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameEngine.saveGameState();
    }
}