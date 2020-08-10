package com.example.tictactoe.game;

import com.example.tictactoe.SettingsUtility;

public class GameEngine {

    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points = 0;
    private int player2Points = 0;
    private boolean player1Wins = false;
    private boolean player2Wins = false;
    private int whoStarts = 0;
    private int whoStartsDraw = 0;
    private GameState gameState = new GameState();
    private GameStateRepository gameStateRepository = new GameStateRepository(gameState);
    private GameCompletionListener gameCompletionListener;

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

    void roundResult(String[][] field) {
        if (checkForWin(field)) {
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

    private boolean checkForWin(String[][] field) {

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
        completion();
        restartGame();
    }

    private void player2Wins() {
        player1Wins = false;
        player2Wins = true;
        player2Points++;
        completion();
        restartGame();
    }

    private void draw() {
        player1Wins = false;
        player2Wins = false;
        completion();
        restartGame();
    }

    private void restartGame() {
        roundCount = 0;
        whoStarts();
    }

    void resetGame() {
        player1Points = 0;
        player2Points = 0;
        restartGame();
        completion();
    }

    void whoStarts() {

        if ((!getPlayer1Wins()) && (!getPlayer2Wins())) {

            if (getWhoStartsDraw() == SettingsUtility.DRAW_OTHER_PLAYER_STARTS) {
                setPlayer1Turn((!getPlayer1Turn()));
            } else if (getWhoStartsDraw() == SettingsUtility.DRAW_SAME_PLAYER_STARTS) {
                setPlayer1Turn(getPlayer1Turn());
            }

        } else if (getPlayer1Wins()) {

            if (getWhoStarts() == SettingsUtility.WINNER_STARTS) {
                setPlayer1Turn(true);
            } else if ((getWhoStarts() == SettingsUtility.DIFFERENT_PLAYER_STARTS) ) {
                    setPlayer1Turn(false);
            }

        } else {
            if (getWhoStarts() == SettingsUtility.WINNER_STARTS) {
                setPlayer1Turn(false);
            } else if (getWhoStarts() == SettingsUtility.DIFFERENT_PLAYER_STARTS) {
                if ((getPlayer1Points() == 0) && (getPlayer2Points() == 1)) {
                    setPlayer1Turn(false);
                } else {
                    setPlayer1Turn(true);
                }
            }
        }
    }

    public boolean getPlayer1Wins() {
        return player1Wins;
    }

    public boolean getPlayer2Wins() {
        return player2Wins;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public boolean getPlayer1Turn() {
        return player1Turn;
    }

    public int getPlayer1Points() {
        return player1Points;
    }

    public int getPlayer2Points() {
        return player2Points;
    }

    public int getWhoStarts() {
        return  whoStarts;
    }

    public int getWhoStartsDraw() {
        return whoStartsDraw;
    }

    void updateRoundCount() {
        roundCount++;
    }

    private void completion() {
        if (gameCompletionListener != null) {
            gameCompletionListener.onCompletion();
        }
    }

    public void setCompletionListener(GameCompletionListener gameCompletionListener) {
        this.gameCompletionListener = gameCompletionListener;
    }

    public void setPlayer1Turn(Boolean isPlayer1Turn) {
        player1Turn = isPlayer1Turn;
    }

    public void setWhoStarts(int whoStarts) {
        this.whoStarts = whoStarts;
    }

    public void setWhoStartsDraw(int whoStartsDraw) {
        this.whoStartsDraw = whoStartsDraw;
    }
}