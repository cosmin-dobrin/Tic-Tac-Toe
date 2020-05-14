package com.example.tictactoe.game;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.R;

import java.io.FileOutputStream;
import java.io.Serializable;

class GameEngine {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private Context context;

    GameEngine(){}

    GameEngine(Context context) {
        this.context = context;
    }

    void gameButtonClicked(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }
        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
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
        player1Points++;
        Toast.makeText(context, context.getResources().getString(R.string.player_1_wins), Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void player2Wins() {
        player2Points++;
        Toast.makeText(context, context.getResources().getString(R.string.player_2_wins), Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void draw() {
        Toast.makeText(context, context.getResources().getString(R.string.draw), Toast.LENGTH_SHORT).show();
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

    void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    void setTextViewPlayerId(TextView textViewPlayer1, TextView textViewPlayer2) {
        this.textViewPlayer1 = textViewPlayer1;
        this.textViewPlayer2 = textViewPlayer2;
    }

    void setButtonsId(Button[][] buttons) {
        this.buttons = buttons;
    }

    void setPlayer1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
    }

    void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    void setPlayer1Points(int player1Points) {
        this.player1Points = player1Points;
    }

    void setPlayer2Points(int player2Points) {
        this.player2Points = player2Points;
    }

    boolean getPlayer1Turn() {
        return player1Turn;
    }

    int getRoundCount() {
        return roundCount;
    }

    int getPlayer1Points() {
        return player1Points;
    }

    int getPlayer2Points() {
        return player2Points;
    }
}