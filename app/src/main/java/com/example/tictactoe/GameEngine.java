package com.example.tictactoe;

import java.util.Random;

public class GameEngine {

    private boolean player1Turn = true;
    private boolean mPlayer1StartsMatch = true;
    private int roundCount;
    private int player1Points = 0;
    private int player2Points = 0;
    private boolean player1Wins = false;
    private boolean player2Wins = false;
    private int whoStarts = 0;
    private int whoStartsDraw = 0;
    private int mSymbolPlayer1 = 0;
    private int mWhoIsPlayer1 = 0;
    private int mDifficultyLevel = 0;
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

    void roundResult(String[][] gameTable) {
        if (checkForWin(gameTable)) {
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

    private boolean checkForWin(String[][] gameTable) {

        for (int i = 0; i < 3; i++) {
            if (gameTable[i][0].equals(gameTable[i][1])
                    && gameTable[i][0].equals(gameTable[i][2])
                    && !gameTable[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (gameTable[0][i].equals(gameTable[1][i])
                    && gameTable[0][i].equals(gameTable[2][i])
                    && !gameTable[0][i].equals("")) {
                return true;
            }
        }

        if (gameTable[0][0].equals(gameTable[1][1])
                && gameTable[0][0].equals(gameTable[2][2])
                && !gameTable[0][0].equals("")) {
            return true;
        }

        if (gameTable[0][2].equals(gameTable[1][1])
                && gameTable[0][2].equals(gameTable[2][0])
                && !gameTable[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void player1Wins() {
        player1Wins = true;
        player2Wins = false;
        player1Points++;
        restartGame();
        completion();
    }

    private void player2Wins() {
        player1Wins = false;
        player2Wins = true;
        player2Points++;
        restartGame();
        completion();
    }

    private void draw() {
        player1Wins = false;
        player2Wins = false;
        restartGame();
        completion();
    }

    private void restartGame() {
        roundCount = 0;
        whoStarts();
    }

    void resetGame() {
        player1Points = 0;
        player2Points = 0;
        roundCount = 0;
        player1Turn = true;
        completion();
    }

    private void whoStarts() {

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
                setPlayer1StartsMatch(!getPlayer1StartsMatch());
                setPlayer1Turn(getPlayer1StartsMatch());
            }

        } else if (getPlayer2Wins()) {
            if (getWhoStarts() == SettingsUtility.WINNER_STARTS) {
                setPlayer1Turn(false);
            } else if (getWhoStarts() == SettingsUtility.DIFFERENT_PLAYER_STARTS) {
                setPlayer1StartsMatch(!getPlayer1StartsMatch());
                setPlayer1Turn(getPlayer1StartsMatch());
            }
        }
    }

    public void check(int row, int column, String[][] gameTable) {
        if (isChecked(row, column, gameTable)) {
            return;
        }

        if (getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
            if (mSymbolPlayer1 == SettingsUtility.X) {
                gameTable[row][column] = "X";
            } else if (mSymbolPlayer1 == SettingsUtility.O) {
                gameTable[row][column] = "O";
            }
        } else if (getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
            if (mSymbolPlayer1 == SettingsUtility.X) {
                gameTable[row][column] = "O";
            } else if (mSymbolPlayer1 == SettingsUtility.O) {
                gameTable[row][column] = "X";
            }
        }
    }

    public boolean isChecked(int row, int column, String[][] gameTable) {
        if (!(gameTable[row][column].equals(""))) {
            return true;
        } else {
            return false;
        }
    }

    private String getSymbolAt(int row, int column, String[][] gameTable) {
        return gameTable[row][column];
    }

    private boolean match(int row1, int column1, int row2, int column2, String[][] gameTable) {
        if (getSymbolAt(row1,column1,gameTable).equals(getSymbolAt(row2,column2,gameTable))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEnemySymbol(int row, int column, String[][] gameTable) {
        if (getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
            if (getSymbolPlayer1() == SettingsUtility.X) {
                return getSymbolAt(row, column, gameTable).equals("O");
            } else if (getSymbolPlayer1() == SettingsUtility.O) {
                return getSymbolAt(row, column, gameTable).equals("X");
            }
        } else if (getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
            if (getSymbolPlayer1() == SettingsUtility.X) {
                return getSymbolAt(row, column, gameTable).equals("X");
            } else if (getSymbolPlayer1() == SettingsUtility.O) {
                return getSymbolAt(row, column, gameTable).equals("O");
            }
        }
        return false;
    }

    public boolean isOwnSymbol(int row, int column, String[][] gameTable) {
        if (getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
            if (getSymbolPlayer1() == SettingsUtility.X) {
                return getSymbolAt(row, column, gameTable).equals("X");
            } else if (getSymbolPlayer1() == SettingsUtility.O) {
                return getSymbolAt(row, column, gameTable).equals("O");
            }
        } else if (getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
            if (getSymbolPlayer1() == SettingsUtility.X) {
                return getSymbolAt(row, column, gameTable).equals("O");
            } else if (getSymbolPlayer1() == SettingsUtility.O) {
                return getSymbolAt(row, column, gameTable).equals("X");
            }
        }
        return false;
    }

    public void checkEdge(String[][] gameTable) {
        if ((!isChecked(0,1, gameTable)) && (!isChecked(2,1, gameTable))) {
            check(0,1, gameTable);
        } else if ((!isChecked(1,0, gameTable)) && (!isChecked(1,2, gameTable))) {
            check(1,0, gameTable);
        } else if ((!isChecked(2,1, gameTable)) && (!isChecked(0,1, gameTable))) {
            check(2,1, gameTable);
        } else if ((!isChecked(1,2, gameTable)) && (!isChecked(1,0, gameTable))) {
            check(1,2, gameTable);
        }
    }

    public void checkCorner(String[][] gameTable) {
        if (!isChecked(0,0, gameTable)) {
            check(0,0, gameTable);
        } else if (!isChecked(0,2, gameTable)) {
            check(0,2, gameTable);
        } else if (!isChecked(2,1, gameTable)) {
            check(2,0, gameTable);
        } else if (!isChecked(1,2, gameTable)) {
            check(2,2, gameTable);
        }
    }

    public boolean findSymbolsOnOppositeEdges(String[][] gameTable) {
        if ((isChecked(0,1, gameTable)) && (isChecked(2,1, gameTable))) {
            return true;
        } else if ((isChecked(1,0, gameTable)) && (isChecked(1,2, gameTable))) {
            return true;
        } else if ((isChecked(2,1, gameTable)) && (isChecked(0,1, gameTable))) {
            return true;
        } else if ((isChecked(1,2, gameTable)) && (isChecked(1,0, gameTable))) {
            return true;
        } else {
            return false;
        }
    }

    public void checkWhatIsLeft(String[][] gameTable) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!isChecked(i, j, gameTable)) {
                    check(i, j, gameTable);
                    return;
                }
            }
        }
    }

    public void checkCornerCloseToEnemyEdge(String[][] gameTable) {

        if (isEnemySymbol(0, 1, gameTable)) {

            if ((isEnemySymbol(2, 0, gameTable)) &&
                    (!isChecked(0, 0, gameTable))) {
                check(0, 0, gameTable);

            } else if ((isEnemySymbol(2, 2, gameTable)) &&
                    (!isChecked(0, 2, gameTable))) {
                check(0, 2, gameTable);
            }


        } else if (isEnemySymbol(1, 0, gameTable)) {

            if ((isEnemySymbol(0, 2, gameTable)) &&
                    (!isChecked(0, 0, gameTable))) {
                check(0, 0, gameTable);

            } else if ((isEnemySymbol(2, 2, gameTable)) &&
                    (!isChecked(2, 2, gameTable))) {
                check(2, 2, gameTable);
            }

        } else if (isEnemySymbol(1, 2, gameTable)) {

            if ((isEnemySymbol(0, 0, gameTable)) &&
                    (!isChecked(0, 2, gameTable))) {
                check(0, 2, gameTable);

            } else if ((isEnemySymbol(2, 0, gameTable)) &&
                    (!isChecked(2, 2, gameTable))) {
                check(2, 2, gameTable);
            }

        } else if (isEnemySymbol(2, 1, gameTable)) {

            if ((isEnemySymbol(0, 0, gameTable)) &&
                    (!isChecked(2, 0, gameTable))) {
                check(2, 0, gameTable);

            } else if ((isEnemySymbol(0, 2, gameTable)) &&
                    (!isChecked(2, 2, gameTable))) {
                check(2, 2, gameTable);
            }

        }
    }

    public void goForWin(String[][] gameTable) {
        boolean stop;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (isOwnSymbol(i, j, gameTable)) {
                    stop = checkVertically(gameTable, i, j);
                    if (stop) return;
                    stop = checkHorizontally(gameTable, i, j);
                    if (stop) return;
                    stop = checkPrincipalDiagonal(gameTable, i, j);
                    if (stop) return;
                    stop = checkSecondaryDiagonal(gameTable, i, j);
                    if (stop) return;
                }
            }
        }
    }

    public boolean canWin(String[][] gameTable) {
        boolean stop;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (isOwnSymbol(i, j, gameTable)) {
                    stop = scanVertically(gameTable, i, j);
                    if (stop) return true;
                    stop = scanHorizontally(gameTable, i, j);
                    if (stop) return true;
                    stop = scanPrincipalDiagonal(gameTable, i, j);
                    if (stop) return true;
                    stop = scanSecondaryDiagonal(gameTable, i, j);
                    if (stop) return true;
                }
            }
        }
        return false;
    }

    public void block(String[][] gameTable) {
        boolean stop;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (isEnemySymbol(i, j, gameTable)) {
                    stop = checkVertically(gameTable, i, j);
                    if (stop) return;
                    stop = checkHorizontally(gameTable, i, j);
                    if (stop) return;
                    stop = checkPrincipalDiagonal(gameTable, i, j);
                    if (stop) return;
                    stop = checkSecondaryDiagonal(gameTable, i, j);
                    if (stop) return;
                }
            }
        }
    }

    public boolean canBlock(String[][] gameTable) {
        boolean stop;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (isEnemySymbol(i, j, gameTable)) {
                    stop = scanVertically(gameTable, i, j);
                    if (stop) return true;
                    stop = scanHorizontally(gameTable, i, j);
                    if (stop) return true;
                    stop = scanPrincipalDiagonal(gameTable, i, j);
                    if (stop) return true;
                    stop = scanSecondaryDiagonal(gameTable, i, j);
                    if (stop) return true;
                }
            }
        }
        return false;
    }

    private boolean checkVertically(String[][] gameTable, int i, int j) {

            if ((i == 0) || (i == 1)) {
                if (match(i, j, i + 1, j, gameTable)) {
                    if ((i == 0) && (!isChecked(i + 2, j, gameTable))) {
                        check(i + 2, j, gameTable);
                        return true;
                    } else if ((i == 1) && (!isChecked(i - 1, j, gameTable))) {
                        check(i - 1, j, gameTable);
                        return true;
                    }
                } else if (i == 0) {
                    if (match(i, j, i + 2, j, gameTable) &&
                            (!isChecked(i + 1, j, gameTable))) {
                        check(i + 1, j, gameTable);
                        return true;
                    }
                }
            }
        return false;
    }

    private boolean checkHorizontally(String[][] gameTable, int i, int j) {

            if ((j == 0) || (j == 1)) {
                if (match(i, j, i, j + 1, gameTable)) {
                    if ((j == 0) && (!isChecked(i, j + 2, gameTable))) {
                        check(i, j + 2, gameTable);
                        return true;
                    } else if ((j == 1) && (!isChecked(i, j - 1, gameTable))) {
                        check(i, j - 1, gameTable);
                        return true;
                    }
                } else if (j == 0) {
                    if (match(i, j, i, j + 2, gameTable) &&
                            (!isChecked(i, j + 1, gameTable))) {

                        check(i, j + 1, gameTable);
                        return true;
                    }
                }
            }
        return false;
    }

    private boolean checkPrincipalDiagonal(String[][] gameTable, int i, int j) {

            if (((i == 0) && (j == 0)) || ((i == 1) && (j == 1))) {
                if (match(i, j, i + 1, j + 1, gameTable)) {
                    if ((i == 0) && (!isChecked(i + 2, j + 2, gameTable))) {
                        check(i + 2, j + 2, gameTable);
                        return true;
                    } else if ((i == 1) && (!isChecked(i - 1, j - 1, gameTable))) {
                        check(i - 1, j - 1, gameTable);
                        return true;
                    }
                } else if (i == 0) {
                    if (match(i, j, i + 2, j + 2, gameTable) &&
                            (!isChecked(i + 1, j + 1, gameTable))) {

                        check(i + 1, j + 1, gameTable);
                        return true;
                    }
                }
            }
        return false;
    }

    private boolean checkSecondaryDiagonal(String[][] gameTable, int i, int j) {

            if (((i == 0) && (j == 2)) || ((i == 1) && (j == 1))) {
                if (match(i, j, i + 1, j - 1, gameTable)) {
                    if ((i == 0) && (!isChecked(i + 2, j - 2, gameTable))) {
                        check(i + 2, j - 2, gameTable);
                        return true;
                    } else if ((i == 1) && (!isChecked(i - 1, j + 1, gameTable))) {
                        check(i - 1, j + 1, gameTable);
                        return true;
                    }
                } else if (i == 0) {
                    if (match(i, j, i + 2, j - 2, gameTable) &&
                            (!isChecked(i + 1, j - 1, gameTable))) {

                        check(i + 1, j - 1, gameTable);
                        return true;
                    }
                }
            }
        return false;
    }

    private boolean scanVertically(String[][] gameTable, int i, int j) {

        if ((i == 0) || (i == 1)) {
            if (match(i, j, i + 1, j, gameTable)) {
                if ((i == 0) && (!isChecked(i + 2, j, gameTable))) {
                    return true;
                } else if ((i == 1) && (!isChecked(i - 1, j, gameTable))) {
                    return true;
                }
            } else if (i == 0) {
                if (match(i, j, i + 2, j, gameTable) &&
                        (!isChecked(i + 1, j, gameTable))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean scanHorizontally(String[][] gameTable, int i, int j) {

        if ((j == 0) || (j == 1)) {
            if (match(i, j, i, j + 1, gameTable)) {
                if ((j == 0) && (!isChecked(i, j + 2, gameTable))) {
                    return true;
                } else if ((j == 1) && (!isChecked(i, j - 1, gameTable))) {
                    return true;
                }
            } else if (j == 0) {
                if (match(i, j, i, j + 2, gameTable) &&
                        (!isChecked(i, j + 1, gameTable))) {

                    return true;
                }
            }
        }
        return false;
    }

    private boolean scanPrincipalDiagonal(String[][] gameTable, int i, int j) {

        if (((i == 0) && (j == 0)) || ((i == 1) && (j == 1))) {
            if (match(i, j, i + 1, j + 1, gameTable)) {
                if ((i == 0) && (!isChecked(i + 2, j + 2, gameTable))) {
                    return true;
                } else if ((i == 1) && (!isChecked(i - 1, j - 1, gameTable))) {
                    return true;
                }
            } else if (i == 0) {
                if (match(i, j, i + 2, j + 2, gameTable) &&
                        (!isChecked(i + 1, j + 1, gameTable))) {

                    return true;
                }
            }
        }
        return false;
    }

    private boolean scanSecondaryDiagonal(String[][] gameTable, int i, int j) {

        if (((i == 0) && (j == 2)) || ((i == 1) && (j == 1))) {
            if (match(i, j, i + 1, j - 1, gameTable)) {
                if ((i == 0) && (!isChecked(i + 2, j - 2, gameTable))) {
                    return true;
                } else if ((i == 1) && (!isChecked(i - 1, j + 1, gameTable))) {
                    return true;
                }
            } else if (i == 0) {
                if (match(i, j, i + 2, j - 2, gameTable) &&
                        (!isChecked(i + 1, j - 1, gameTable))) {

                    return true;
                }
            }
        }
        return false;
    }

    public boolean scanEdgeOppositeToCorner(String[][] gameTable ) {

        if (isEnemySymbol(0, 1, gameTable)) {

            if (isEnemySymbol(2, 0, gameTable)) {
                return true;
            } else if (isEnemySymbol(2, 2, gameTable)) {
                return true;
            }
        } else if (isEnemySymbol(1, 0, gameTable)) {

            if (isEnemySymbol(0, 2, gameTable)) {
                return true;
            } else if (isEnemySymbol(2, 2, gameTable)) {
                return true;
            }
        } else if (isEnemySymbol(1, 2, gameTable)) {

            if (isEnemySymbol(0, 0, gameTable)) {
                return true;
            } else if (isEnemySymbol(2, 0, gameTable)) {
                return true;
            }
        } else if (isEnemySymbol(2, 1, gameTable)) {

            if (isEnemySymbol(0, 0, gameTable)) {
                return true;
            } else if (isEnemySymbol(0, 2, gameTable)) {
                return true;
            }
        }

        return false;
    }

    public boolean scanEnemyCorners(String[][] gameTable) {

        if (isEnemySymbol(0, 0, gameTable))
            return true;

        if (isEnemySymbol(0, 2, gameTable))
            return true;

        if (isEnemySymbol(2, 0, gameTable))
            return true;

        if (isEnemySymbol(2, 2, gameTable))
            return true;

        return false;
    }

    public int randomNumber() {
        Random random = new Random();
        return random.nextInt(3);
    }

    public void randomCheck(String[][] gameTable) {
        int randomRow = randomNumber();
        int randomColumn = randomNumber();

        boolean stop = false;

        while (!stop) {

            if (!isChecked(randomRow, randomColumn, gameTable)) {
                check(randomRow, randomColumn, gameTable);
                stop = true;
            } else {
                randomRow = randomNumber();
                randomColumn = randomNumber();
            }
        }
    }

    private void completion() {
        if (gameCompletionListener != null) {
            gameCompletionListener.onCompletion();
        }
    }

    void updateRoundCount() {
        roundCount++;
    }

    /** Setters/Getters: **/

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

    public int getSymbolPlayer1() {
        return this.mSymbolPlayer1;
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

    public void setSymbolPlayer1(int mSymbolPlayer1) {
        this.mSymbolPlayer1 = mSymbolPlayer1;
    }

    public int getWhoIsPlayer1() {
        return mWhoIsPlayer1;
    }

    public void setWhoIsPlayer1(int mWhoIsPlayer1) {
        this.mWhoIsPlayer1 = mWhoIsPlayer1;
    }

    public int getDifficultyLevel() {
        return mDifficultyLevel;
    }

    public void setDifficultyLevel(int mDifficultyLevel) {
        this.mDifficultyLevel = mDifficultyLevel;
    }

    public boolean getPlayer1StartsMatch() {
        return mPlayer1StartsMatch;
    }

    public void setPlayer1StartsMatch(boolean player1StartsMatch) {
        this.mPlayer1StartsMatch = player1StartsMatch;
    }
}