package com.example.tictactoe.game;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.R;

class GameEngine {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private boolean player1Wins = false;
    private boolean player2Wins = false;
    private Context context;
    private Activity activity;
    private GameState gameState = new GameState();
    private GameStateRepository gameStateRepository = new GameStateRepository(gameState);
    private GameCompletionListener gameCompletionListener;

    GameEngine(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    void saveGameState() {

        gameState.setPlayer1Turn(player1Turn);
        gameState.setRoundCount(roundCount);
        gameState.setPlayer1Points(player1Points);
        gameState.setPlayer2Points(player2Points);

        gameStateRepository.save();
    }

    void loadGameState() {

        gameStateRepository.load();

        player1Turn = gameState.getPlayer1Turn();
        roundCount = gameState.getRoundCount();
        player1Points = gameState.getPlayer1Points();
        player2Points = gameState.getPlayer2Points();
    }

    private void gameButtonClicked(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }
        updateRoundCount();
        roundResult();
    }

    private void roundResult() {
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
                completion();
            } else {
                player2Wins();
                completion();
            }
        } else if (roundCount == 9) {
            draw();
            completion();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void player1Wins() {
        player1Wins = true;
        player2Wins = false;
        player1Points++;
        updatePointsText();
        resetBoard();
    }

    private void player2Wins() {
        player1Wins = false;
        player2Wins = true;
        player2Points++;
        updatePointsText();
        resetBoard();
    }

    private void draw() {
        player1Wins = false;
        player2Wins = false;
        resetBoard();
    }

    private void updatePointsText() {
        textViewPlayer1.setText(context.getResources().getString(R.string.player_1_updatePointsText, player1Points)); // String resources + placeholders
        textViewPlayer2.setText(context.getResources().getString(R.string.player_2_updatePointsText, player2Points));
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    boolean getPlayer1Wins() {
        return player1Wins;
    }

    boolean getPlayer2Wins() {
        return player2Wins;
    }

    int getRoundCount() {
        return roundCount;
    }

    boolean getPlayer1Turn() {
        return player1Turn;
    }

    void setUpGame() {
        Button buttonReset = activity.findViewById(R.id.button_reset);
        this.textViewPlayer1 = activity.findViewById(R.id.text_view_p2);
        this.textViewPlayer2 = activity.findViewById(R.id.text_view_p1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonId = "button_" + i + j;
                int resId = context.getResources().getIdentifier(buttonId, "id", context.getPackageName());
                buttons[i][j] = activity.findViewById(resId);
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
                resetGame();
            }
        });
    }

    private void completion() {
        if (gameCompletionListener != null) {
            gameCompletionListener.onCompletion();
        }
    }

    void setCompletionListener(GameCompletionListener gameCompletionListener) {
        this.gameCompletionListener = gameCompletionListener;
    }

    private void updateRoundCount() {
        roundCount++;
    }
}