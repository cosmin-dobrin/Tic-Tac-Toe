package com.example.tictactoe;

import com.example.tictactoe.game.GameEngine;

public class BotEngine {

    GameEngine gameEngine;

    public BotEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    /** Game mode: HARD -- For Bot being Player 1: **/

    void botP1HardFirstMove(String[][] buttons) {
        if (gameEngine.getRoundCount() == 0)
            gameEngine.check(0, 0, buttons);
    }

    void botP1HardSecondMove(String[][] buttons) {
        if (gameEngine.getRoundCount() == 2) {
            if (!(gameEngine.isChecked(2, 2, buttons))) {
                gameEngine.check(2, 2, buttons);
            } else {
                gameEngine.check(2, 0, buttons);
            }
        }
    }

    void botP1HardThirdMove(String[][] buttons) {
        if (gameEngine.getRoundCount() == 4) {
            if (gameEngine.isEnemySymbol(1, 1, buttons)) {
                if (gameEngine.canWin(buttons)) {
                    gameEngine.goForWin(buttons);
                } else if (gameEngine.canBlock(buttons)) {
                    gameEngine.block(buttons);
                }
            } else if (gameEngine.isOwnSymbol(0, 0, buttons) &&
                    gameEngine.isOwnSymbol(2, 2, buttons)) {

                if (gameEngine.canWin(buttons)) {
                    gameEngine.goForWin(buttons);
                } else if (gameEngine.canBlock(buttons)) {
                    gameEngine.block(buttons);
                } else {
                    gameEngine.checkCorner(buttons);
                }
            }
        }
    }

    void botP1HardFourthMove(String[][] buttons) {
        if (gameEngine.getRoundCount() == 6) {
            if (gameEngine.canWin(buttons)) {
                gameEngine.goForWin(buttons);
            } else if (gameEngine.canBlock(buttons)) {
                gameEngine.block(buttons);
            } else {
                gameEngine.checkWhatIsLeft(buttons);
            }
        }
    }

    void botP1HardFifthMove(String[][] buttons) {
        if (gameEngine.getRoundCount() == 8)
            gameEngine.checkWhatIsLeft(buttons);
    }

    public String[][] botP1HardMoves(String[][] buttons) {
        botP1HardFirstMove(buttons);
        botP1HardSecondMove(buttons);
        botP1HardThirdMove(buttons);
        botP1HardFourthMove(buttons);
        botP1HardFifthMove(buttons);

        return buttons;
    }

    /** Game mode: HARD -- For Bot being Player 2: **/

    void botP2HardFirstMove(String[][] buttons) {
        if (gameEngine.getRoundCount() == 1) {
            if (!gameEngine.isEnemySymbol(1, 1, buttons)) {
                gameEngine.check(1, 1, buttons);
            } else {
                gameEngine.checkCorner(buttons);
            }
        }
    }

    void botP2HardSecondMove(String[][] buttons) {
        if (gameEngine.getRoundCount() == 3) {
            if (gameEngine.isOwnSymbol(1,1, buttons)) {
                if (gameEngine.scanEnemyCorners(buttons)) {
                    if (gameEngine.canBlock(buttons)) {
                        gameEngine.block(buttons);
                    } else if (gameEngine.scanEdgeOppositeToCorner(buttons)) {
                        gameEngine.checkCornerCloseToEnemyEdge(buttons);
                    } else {
                        gameEngine.checkEdge(buttons);
                    }
                } else if (gameEngine.findSymbolsOnOppositeEdges(buttons)) {
                    gameEngine.check(0,0, buttons);
                } else {
                    gameEngine.checkWhatIsLeft(buttons);
                }
            } else if (gameEngine.isEnemySymbol(1,1, buttons)) {
                if (gameEngine.canBlock(buttons)) {
                    gameEngine.block(buttons);
                } else {
                    gameEngine.checkCorner(buttons);
                }
            }
        }
    }

    void botP2HardThirdMove(String[][] buttons) {
        if (gameEngine.getRoundCount() == 5) {
            if (gameEngine.canWin(buttons)) {
                gameEngine.goForWin(buttons);
            } else if (gameEngine.canBlock(buttons)) {
                gameEngine.block(buttons);
            } else {
                gameEngine.checkWhatIsLeft(buttons);
            }
        }
    }

    void botP2HardFourthMove(String[][] buttons) {
        if (gameEngine.getRoundCount() == 7) {
            if (gameEngine.canWin(buttons)) {
                gameEngine.goForWin(buttons);
            } else if (gameEngine.canBlock(buttons)) {
                gameEngine.block(buttons);
            } else {
                gameEngine.checkWhatIsLeft(buttons);
            }
        }
    }

    public String[][] botP2HardMoves(String[][] buttons) {
        botP2HardFirstMove(buttons);
        botP2HardSecondMove(buttons);
        botP2HardThirdMove(buttons);
        botP2HardFourthMove(buttons);

        return buttons;
    }

    /** Game mode: EASY -- For Bot being Player 1: **/

    void botP1EasyFirstMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 0) {
            gameEngine.randomCheck(gameTable);
        }
    }

    void botP1EasySecondMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 2) {
            gameEngine.randomCheck(gameTable);
        }
    }

    void botP1EasyThirdMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 4) {
            gameEngine.randomCheck(gameTable);
        }
    }

    void botP1EasyFourthMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 6) {
            gameEngine.randomCheck(gameTable);
        }
    }

    void botP1EasyFifthMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 8) {
            gameEngine.randomCheck(gameTable);
        }
    }

    public String[][] botP1EasyMoves(String[][] gameTable) {
        botP1EasyFirstMove(gameTable);
        botP1EasySecondMove(gameTable);
        botP1EasyThirdMove(gameTable);
        botP1EasyFourthMove(gameTable);
        botP1EasyFifthMove(gameTable);

        return gameTable;
    }

    /** Game mode: EASY -- For Bot being Player 2: **/

    void botP2EasyFirstMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 1) {
            gameEngine.randomCheck(gameTable);
        }
    }

    void botP2EasySecondMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 3) {
            gameEngine.randomCheck(gameTable);
        }
    }

    void botP2EasyThirdMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 5) {
            gameEngine.randomCheck(gameTable);
        }
    }

    void botP2EasyFourthMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 7) {
            gameEngine.randomCheck(gameTable);
        }
    }

    public String[][] botP2EasyMoves(String[][] gameTable) {
        botP2EasyFirstMove(gameTable);
        botP2EasySecondMove(gameTable);
        botP2EasyThirdMove(gameTable);
        botP2EasyFourthMove(gameTable);

        return gameTable;
    }
}
