package com.example.tictactoe.game;

import java.io.Serializable;

class GameState implements Serializable {

    private boolean player1Turn;
    private int roundCount;
    private int player1Points;
    private int player2Points;

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

    int getRoundCount()  {
        return roundCount;
    }

    int getPlayer1Points() {
        return player1Points;
    }

    int getPlayer2Points()   {
        return player2Points;
    }
}