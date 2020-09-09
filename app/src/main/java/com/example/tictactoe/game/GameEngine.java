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
                    setPlayer1Turn(false);
            }

        } else if (getPlayer2Wins()) {
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

    //Single player mode specific methods:

    private void check(int row, int column, String[][] buttons) {
        if (isChecked(row, column, buttons)) {
            return;
        }

        if (getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
            if (mSymbolPlayer1 == SettingsUtility.X) {
                buttons[row][column] = "X";
            } else if (mSymbolPlayer1 == SettingsUtility.O) {
                buttons[row][column] = "O";
            }
        } else if (getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
            if (mSymbolPlayer1 == SettingsUtility.X) {
                buttons[row][column] = "O";
            } else if (mSymbolPlayer1 == SettingsUtility.O) {
                buttons[row][column] = "X";
            }
        }
    }

    private boolean isChecked(int row, int column, String[][] buttons) {
        if (!(buttons[row][column].equals(""))) {
            return true;
        } else {
            return false;
        }
    }

    private String getSymbolAt(int row, int column, String[][] buttons) {
        return buttons[row][column];
    }

    private boolean match(int row1, int column1, int row2, int column2, String[][] buttons) {
        if (getSymbolAt(row1,column1,buttons).equals(getSymbolAt(row2,column2,buttons))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isEnemySymbol(int row, int column, String[][] buttons) {
        if (getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
            if (getSymbolPlayer1() == SettingsUtility.X) {
                return getSymbolAt(row, column, buttons).equals("O");
            } else if (getSymbolPlayer1() == SettingsUtility.O) {
                return getSymbolAt(row, column, buttons).equals("X");
            }
        } else if (getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
            if (getSymbolPlayer1() == SettingsUtility.X) {
                return getSymbolAt(row, column, buttons).equals("X");
            } else if (getSymbolPlayer1() == SettingsUtility.O) {
                return getSymbolAt(row, column, buttons).equals("O");
            }
        }
        return false;
    }

    private boolean isOwnSymbol(int row, int column, String[][] buttons) {
        if (getWhoIsPlayer1() == SettingsUtility.BOT_IS_PLAYER_1) {
            if (getSymbolPlayer1() == SettingsUtility.X) {
                return getSymbolAt(row, column, buttons).equals("X");
            } else if (getSymbolPlayer1() == SettingsUtility.O) {
                return getSymbolAt(row, column, buttons).equals("O");
            }
        } else if (getWhoIsPlayer1() == SettingsUtility.YOU_ARE_PLAYER_1) {
            if (getSymbolPlayer1() == SettingsUtility.X) {
                return getSymbolAt(row, column, buttons).equals("O");
            } else if (getSymbolPlayer1() == SettingsUtility.O) {
                return getSymbolAt(row, column, buttons).equals("X");
            }
        }
        return false;
    }

    private void checkEdge(String[][] buttons) {
        if ((!isChecked(0,1, buttons)) && (!isChecked(2,1, buttons))) {
            check(0,1, buttons);
        } else if ((!isChecked(1,0, buttons)) && (!isChecked(1,2, buttons))) {
            check(1,0, buttons);
        } else if ((!isChecked(2,1, buttons)) && (!isChecked(0,1, buttons))) {
            check(2,1, buttons);
        } else if ((!isChecked(1,2, buttons)) && (!isChecked(1,0, buttons))) {
            check(1,2, buttons);
        }
    }

    private boolean findSymbolsOnOppositeEdges(String[][] buttons) {
        if ((isChecked(0,1, buttons)) && (isChecked(2,1, buttons))) {
            return true;
        } else if ((isChecked(1,0, buttons)) && (isChecked(1,2, buttons))) {
            return true;
        } else if ((isChecked(2,1, buttons)) && (isChecked(0,1, buttons))) {
            return true;
        } else if ((isChecked(1,2, buttons)) && (isChecked(1,0, buttons))) {
            return true;
        } else {
            return false;
        }
    }

    private void checkWhatIsLeft(String[][] buttons) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!isChecked(i, j, buttons)) {
                    check(i, j, buttons);
                    return;
                }
            }
        }
    }

    //private void checkCornerCloseToEnemy()

    private void goForWin(String[][] gameTable) {
        boolean stop;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (isOwnSymbol(i, j, gameTable)) {
                    stop = checkVerticallyForWin(gameTable, i, j);
                    if (stop) break;
                    stop = checkHorizontallyForWin(gameTable, i, j);
                    if (stop) break;
                    stop = checkPrincipalDiagonalForWin(gameTable, i, j);
                    if (stop) break;
                    stop = checkSecondaryDiagonalForWin(gameTable, i, j);
                    if (stop) break;
                }
            }
        }
    }

    private boolean canWin(String[][] buttons) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (isOwnSymbol(i, j, buttons)) {
                    //Check vertically
                    if ((i == 0) || (i == 1)) {
                        if (match(i, j, i + 1, j, buttons)) {
                            if (i == 0) {
                                if (!isChecked(2, j, buttons))
                                    return true;
                            } else {
                                if (!isChecked(0, j, buttons))
                                    return true;
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j, buttons)) {
                                if (!isChecked(1, j, buttons))
                                    return true;
                            }
                        }
                    }
                    //Check horizontally
                    if ((j == 0) || (j == 1)) {
                        if (match(i, j, i, j + 1, buttons)) {
                            if (j == 0) {
                                if (!isChecked(i, 2, buttons))
                                    return true;
                            } else {
                                if (!isChecked(i, 0, buttons))
                                    return true;
                            }
                        } else if (j == 0) {
                            if (match(i, j, i, j + 2, buttons)) {
                                if (!isChecked(i, 1, buttons))
                                    return true;
                            }
                        }
                    }
                    //Check the principal diagonal
                    if (((i == 0) && (j == 0)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j + 1, buttons)) {
                            if (i == 0) {
                                if (!isChecked(2, 2, buttons))
                                    return true;
                            } else {
                                if (!isChecked(0, 0, buttons))
                                    return true;
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j + 2, buttons)) {
                                if (!isChecked(1, 1, buttons))
                                    return true;
                            }
                        }
                    }
                    //Check the secondary diagonal
                    if (((i == 0) && (j == 2)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j - 1, buttons)) {
                            if (i == 0) {
                                if (!isChecked(2, 0, buttons))
                                    return true;
                            } else {
                                if (!isChecked(0, 2, buttons))
                                    return true;
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j - 2, buttons)) {
                                if (!isChecked(1, 1, buttons))
                                    return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void block(String[][] buttons) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (isEnemySymbol(i, j, buttons)) {
                    //Check vertically
                    if ((i == 0) || (i == 1)) {
                        if (match(i, j, i + 1, j, buttons)) {
                            if ((i == 0) && (!isChecked(i + 2, j, buttons))) {
                                check(i + 2, j, buttons);
                                return;
                            } else if ((i == 1) && (!isChecked(i - 1, j, buttons))) {
                                check(i - 1, j, buttons);
                                return;
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j, buttons) &&
                                    (!isChecked(i + 1, j, buttons))) {

                                check(i + 1, j, buttons);
                                return;
                            }
                        }
                    }
                    //Check horizontally
                    if ((j == 0) || (j == 1)) {
                        if (match(i, j, i, j + 1, buttons)) {
                            if ((j == 0) && (!isChecked(i, j + 2, buttons))) {
                                check(i, j + 2, buttons);
                                return;
                            } else if ((j == 1) && (!isChecked(i, j - 1, buttons))) {
                                check(i, j - 1, buttons);
                                return;
                            }
                        } else if (j == 0) {
                            if (match(i, j, i, j + 2, buttons) &&
                                    (!isChecked(i, j + 1, buttons))) {

                                check(i, j + 1, buttons);
                                return;
                            }
                        }
                    }
                    //Check the principal diagonal
                    if (((i == 0) && (j == 0)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j + 1, buttons)) {
                            if ((i == 0) && (!isChecked(i + 2, j + 2, buttons))) {
                                check(i + 2, j + 2, buttons);
                                return;
                            } else if ((i == 1) && (!isChecked(i - 1, j - 1, buttons))) {
                                check(i - 1, j - 1, buttons);
                                return;
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j + 2, buttons) &&
                                    (!isChecked(i + 1, j + 1, buttons))) {

                                check(i + 1, j + 1, buttons);
                                return;
                            }
                        }
                    }
                    //Check the secondary diagonal
                    if (((i == 0) && (j == 2)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j - 1, buttons)) {
                            if ((i == 0) && (!isChecked(i + 2, j - 2, buttons))) {
                                check(i + 2, j - 2, buttons);
                                return;
                            } else if ((i == 1) && (!isChecked( i - 1, j + 1, buttons))) {
                                check(i - 1, j + 1, buttons);
                                return;
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j - 2, buttons) &&
                                    (!isChecked(i + 1, j - 1, buttons))) {

                                check(i + 1, j - 1, buttons);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean canBlock(String[][] buttons) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (isEnemySymbol(i, j, buttons)) {
                    //Check vertically
                    if ((i == 0) || (i == 1)) {
                        if (match(i, j, i + 1, j, buttons)) {
                            if (i == 0) {
                                if (!isChecked(2, j, buttons))
                                    return true;
                            } else {
                                if (!isChecked(0, j, buttons))
                                    return true;
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j, buttons)) {
                                if (!isChecked(1, j, buttons))
                                    return true;
                            }
                        }
                    }
                    //Check horizontally
                    if ((j == 0) || (j == 1)) {
                        if (match(i, j, i, j + 1, buttons)) {
                            if (j == 0) {
                                if (!isChecked(i, 2, buttons))
                                    return true;
                            } else {
                                if (!isChecked(i, 0, buttons))
                                    return true;
                            }
                        } else if (j == 0) {
                            if (match(i, j, i, j + 2, buttons)) {
                                if (!isChecked(i, 1, buttons))
                                    return true;
                            }
                        }
                    }
                    //Check the principal diagonal
                    if (((i == 0) && (j == 0)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j + 1, buttons)) {
                            if (i == 0) {
                                if (!isChecked(2, 2, buttons))
                                    return true;
                            } else {
                                if (!isChecked(0, 0, buttons))
                                    return true;
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j + 2, buttons)) {
                                if (!isChecked(1, 1, buttons))
                                    return true;
                            }
                        }
                    }
                    //Check the secondary diagonal
                    if (((i == 0) && (j == 2)) || ((i == 1) && (j == 1))) {
                        if (match(i, j, i + 1, j - 1, buttons)) {
                            if (i == 0) {
                                if (!isChecked(2, 0, buttons))
                                    return true;
                            } else {
                                if (!isChecked(0, 2, buttons))
                                    return true;
                            }
                        } else if (i == 0) {
                            if (match(i, j, i + 2, j - 2, buttons)) {
                                if (!isChecked(1, 1, buttons))
                                    return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkVerticallyForWin(String[][] gameTable, int i, int j) {

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

    private boolean checkHorizontallyForWin(String[][] gameTable, int i, int j) {

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

    private boolean checkPrincipalDiagonalForWin(String[][] gameTable, int i, int j) {

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

    private boolean checkSecondaryDiagonalForWin(String[][] gameTable, int i, int j) {

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

    //Bot Moves:

    //For Bot being Player 1:

    void botP1HardFirstMove(String[][] buttons) {
        if (getRoundCount() == 0)
            check(0, 0, buttons);
    }

    void botP1HardSecondMove(String[][] buttons) {
        if (getRoundCount() == 2) {
            if (!(isChecked(2, 2, buttons))) {
                check(2, 2, buttons);
            } else {
                check(2, 0, buttons);
            }
        }
    }

    void botP1HardThirdMove(String[][] buttons) {
        if (getRoundCount() == 4) {
            if (isChecked(1, 1, buttons)) {
                if (isChecked(0, 1, buttons)) {
                    check(2, 1, buttons);
                } else if (isChecked(2, 1, buttons)) {
                    check(0, 1, buttons);
                } else if (isChecked(1, 0, buttons)) {
                    check(1, 2, buttons);
                } else if (isChecked(1, 2, buttons)) {
                    check(1, 0, buttons);
                } else if (isChecked(0, 2, buttons)) {
                    check(2, 0, buttons);
                } else {
                    check(0, 2, buttons);
                }
            } else if (match(2, 2, 0, 0, buttons)) {
                if (isChecked(2, 0, buttons) && isChecked(0, 2, buttons)) {
                    check(1, 1, buttons);
                } else if (!(isChecked(2, 0, buttons))) {
                    check(2, 0, buttons);
                } else {
                    check(0, 2, buttons);
                }
            } else {
                if (isChecked(0, 2, buttons)) {
                    check(1, 0, buttons);
                } else {
                    check(0, 2, buttons);
                }
            }
        }
    }

    void botP1HardFourthMove(String[][] buttons) {
        if (getRoundCount() == 6) {
            if (canWin(buttons)) {
                goForWin(buttons);
            } else if (canBlock(buttons)) {
                block(buttons);
            } else {
                checkWhatIsLeft(buttons);
            }
        }
    }

    void botP1HardFifthMove(String[][] buttons) {
        if (getRoundCount() == 8)
            checkWhatIsLeft(buttons);
    }

    String[][] botP1HardMoves(String[][] buttons) {
        botP1HardFirstMove(buttons);
        botP1HardSecondMove(buttons);
        botP1HardThirdMove(buttons);
        botP1HardFourthMove(buttons);
        botP1HardFifthMove(buttons);

        return buttons;
    }

    //For Bot being Player 2:

    void botP2HardFirstMove(String[][] buttons) {
        if (getRoundCount() == 1) {
            if (isEnemySymbol(1,1, buttons)) {
                check(0,0, buttons);
            } else {
                check(1,1, buttons);
            }
        }
    }

    void botP2HardSecondMove(String[][] buttons) {
        if (getRoundCount() == 3) {
            if (match(1, 1, 2, 2, buttons) && isChecked(0,0, buttons)) {
                check(2,0, buttons);
            } else if (isOwnSymbol(1,1, buttons)) {
                if (isEnemySymbol(0,0, buttons) || isEnemySymbol(0,2, buttons) ||
                        isEnemySymbol(2,0, buttons) || isEnemySymbol(2,2, buttons)) {
                    if (canBlock(buttons)) {
                        block(buttons);
                    } else {
                        checkEdge(buttons);
                    }
                } else if (findSymbolsOnOppositeEdges(buttons)) {
                    check(0,0, buttons);
                } else {
                    checkWhatIsLeft(buttons);
                }
            } else if (isEnemySymbol(1,1, buttons)) {
                if (canBlock(buttons))
                    block(buttons);
            }
        }
    }

    void botP2HardThirdMove(String[][] buttons) {
        if (getRoundCount() == 5) {
            if (canWin(buttons)) {
                goForWin(buttons);
            } else if (canBlock(buttons)) {
                block(buttons);
            } else {
                checkWhatIsLeft(buttons);
            }
        }
    }

    void botP2HardFourthMove(String[][] buttons) {
        if (getRoundCount() == 7) {
            if (canWin(buttons)) {
                goForWin(buttons);
            } else if (canBlock(buttons)) {
                block(buttons);
            } else {
                checkWhatIsLeft(buttons);
            }
        }
    }

    String[][] botP2HardMoves(String[][] buttons) {
        botP2HardFirstMove(buttons);
        botP2HardSecondMove(buttons);
        botP2HardThirdMove(buttons);
        botP2HardFourthMove(buttons);

        return buttons;
    }

    // Setters/Getters:

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
}