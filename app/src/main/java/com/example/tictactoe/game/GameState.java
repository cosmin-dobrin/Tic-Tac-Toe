package com.example.tictactoe.game;

import java.io.Serializable;

class GameState implements Serializable {

    private transient GameEngine engine = new GameEngine();

    private boolean player1Turn = engine.getPlayer1Turn();
    private int roundCount = engine.getRoundCount();
    private int player1Points = engine.getPlayer1Points();
    private int player2Points = engine.getPlayer2Points();

    void setPlayer1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
    }

    void setRoundCount(int roundCount) {
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