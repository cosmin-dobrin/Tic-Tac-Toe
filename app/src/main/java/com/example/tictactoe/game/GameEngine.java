package com.example.tictactoe.game;

class GameEngine {

    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points = 0;
    private int player2Points = 0;
    private boolean player1Wins = false;
    private boolean player2Wins = false;
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
        player1Turn = true;
    }

    void resetGame() {
        player1Points = 0;
        player2Points = 0;
        restartGame();
        completion();
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

    int getPlayer1Points() {
        return player1Points;
    }

    int getPlayer2Points() {
        return player2Points;
    }

    void updateRoundCount() {
        roundCount++;
    }

    private void completion() {
        if (gameCompletionListener != null) {
            gameCompletionListener.onCompletion();
        }
    }

    void setCompletionListener(GameCompletionListener gameCompletionListener) {
        this.gameCompletionListener = gameCompletionListener;
    }
}