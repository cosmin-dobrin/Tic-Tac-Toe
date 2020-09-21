package com.dobrincosminiulian.tictactoe;

import java.io.Serializable;

public class GameState implements Serializable {

    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;

    public void setPlayer1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public void setPlayer1Points(int player1Points) {
        this.player1Points = player1Points;
    }

    public void setPlayer2Points(int player2Points) {
        this.player2Points = player2Points;
    }

    public boolean getPlayer1Turn() {
        return player1Turn;
    }

    public int getRoundCount()  {
        return roundCount;
    }

    public int getPlayer1Points() {
        return player1Points;
    }

    public int getPlayer2Points()   {
        return player2Points;
    }
}