package com.dobrincosminiulian.tictactoe.engines;

public class BotEngine {

    GameEngine gameEngine;

    public BotEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    /** Game mode: HARD -- For Bot being Player 1: **/

    void botP1HardFirstMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 0)
            gameEngine.check(0, 0, gameTable);
    }

    void botP1HardSecondMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 2) {
            if (!(gameEngine.isChecked(2, 2, gameTable))) {
                gameEngine.check(2, 2, gameTable);
            } else {
                gameEngine.check(2, 0, gameTable);
            }
        }
    }

    void botP1HardThirdMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 4) {

            if (gameEngine.canWin(gameTable)) {
                gameEngine.goForWin(gameTable);
            } else if (gameEngine.canBlock(gameTable)) {
                gameEngine.block(gameTable);
            } else {
                gameEngine.checkCorner(gameTable);
            }
        }
    }

    void botP1HardFourthMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 6) {
            if (gameEngine.canWin(gameTable)) {
                gameEngine.goForWin(gameTable);
            } else if (gameEngine.canBlock(gameTable)) {
                gameEngine.block(gameTable);
            } else {
                gameEngine.checkWhatIsLeft(gameTable);
            }
        }
    }

    void botP1HardFifthMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 8)
            gameEngine.checkWhatIsLeft(gameTable);
    }

    public String[][] botP1HardMoves(String[][] gameTable) {
        botP1HardFirstMove(gameTable);
        botP1HardSecondMove(gameTable);
        botP1HardThirdMove(gameTable);
        botP1HardFourthMove(gameTable);
        botP1HardFifthMove(gameTable);

        return gameTable;
    }

    /** Game mode: HARD -- For Bot being Player 2: **/

    void botP2HardFirstMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 1) {
            if (!gameEngine.isEnemySymbol(1, 1, gameTable)) {
                gameEngine.check(1, 1, gameTable);
            } else {
                gameEngine.checkCorner(gameTable);
            }
        }
    }

    void botP2HardSecondMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 3) {
            if (gameEngine.isOwnSymbol(1,1, gameTable)) {
                if (gameEngine.scanEnemyCorners(gameTable)) {
                    if (gameEngine.canBlock(gameTable)) {
                        gameEngine.block(gameTable);
                    } else if (gameEngine.scanEdgeOppositeToCorner(gameTable)) {
                        gameEngine.checkCornerCloseToEnemyEdge(gameTable);
                    } else {
                        gameEngine.checkEdge(gameTable);
                    }
                } else if (gameEngine.findSymbolsOnOppositeEdges(gameTable)) {
                    gameEngine.check(0,0, gameTable);
                } else {
                    gameEngine.checkWhatIsLeft(gameTable);
                }
            } else if (gameEngine.isEnemySymbol(1,1, gameTable)) {
                if (gameEngine.canBlock(gameTable)) {
                    gameEngine.block(gameTable);
                } else {
                    gameEngine.checkCorner(gameTable);
                }
            }
        }
    }

    void botP2HardThirdMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 5) {
            if (gameEngine.canWin(gameTable)) {
                gameEngine.goForWin(gameTable);
            } else if (gameEngine.canBlock(gameTable)) {
                gameEngine.block(gameTable);
            } else {
                gameEngine.checkWhatIsLeft(gameTable);
            }
        }
    }

    void botP2HardFourthMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 7) {
            if (gameEngine.canWin(gameTable)) {
                gameEngine.goForWin(gameTable);
            } else if (gameEngine.canBlock(gameTable)) {
                gameEngine.block(gameTable);
            } else {
                gameEngine.checkWhatIsLeft(gameTable);
            }
        }
    }

    public String[][] botP2HardMoves(String[][] gameTable) {
        botP2HardFirstMove(gameTable);
        botP2HardSecondMove(gameTable);
        botP2HardThirdMove(gameTable);
        botP2HardFourthMove(gameTable);

        return gameTable;
    }

    /** Game mode: MEDIUM -- For Bot being Player 1: **/

    void botP1MediumFirstMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 0) {
            gameEngine.randomCheck(gameTable);
        }
    }

    void botP1MediumSecondMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 2) {
            gameEngine.randomCheck(gameTable);
        }
    }

    void botP1MediumThirdMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 4) {
            if (gameEngine.canWin(gameTable)) {
                gameEngine.goForWin(gameTable);
            } else if (gameEngine.canBlock(gameTable)) {
                gameEngine.block(gameTable);
            } else {
                gameEngine.randomCheck(gameTable);
            }
        }
    }

    void botP1MediumFourthMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 6) {
            if (gameEngine.canWin(gameTable)) {
                gameEngine.goForWin(gameTable);
            } else if (gameEngine.canBlock(gameTable)) {
                gameEngine.block(gameTable);
            } else {
                gameEngine.checkWhatIsLeft(gameTable);
            }
        }
    }

    void botP1MediumFifthMove(String[][] gameTable)  {
        if (gameEngine.getRoundCount() == 8) {
            gameEngine.checkWhatIsLeft(gameTable);
        }
    }

    public String[][] botP1MediumMoves(String[][] gameTable) {
        botP1MediumFirstMove(gameTable);
        botP1MediumSecondMove(gameTable);
        botP1MediumThirdMove(gameTable);
        botP1MediumFourthMove(gameTable);
        botP1MediumFifthMove(gameTable);

        return gameTable;
    }

    /** Game mode: MEDIUM -- For Bot being Player 2: **/

    void botP2MediumFirstMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 1) {
            gameEngine.randomCheck(gameTable);
        }
    }

    void botP2MediumSecondMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 3) {
            if (gameEngine.canBlock(gameTable)) {
                gameEngine.block(gameTable);
            } else {
                gameEngine.randomCheck(gameTable);
            }
        }
    }

    void botP2MediumThirdMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 5) {
            if (gameEngine.canWin(gameTable)) {
                gameEngine.goForWin(gameTable);
            } else if (gameEngine.canBlock(gameTable)) {
                gameEngine.block(gameTable);
            } else {
                gameEngine.checkWhatIsLeft(gameTable);
            }
        }
    }

    void botP2MediumFourthMove(String[][] gameTable) {
        if (gameEngine.getRoundCount() == 7) {
            if (gameEngine.canBlock(gameTable)) {
                gameEngine.block(gameTable);
            } else if (gameEngine.canWin(gameTable)) {
                gameEngine.goForWin(gameTable);
            } else {
                gameEngine.checkWhatIsLeft(gameTable);
            }
        }
    }

    public String[][] botP2MediumMoves(String[][] gameTable) {
        botP2MediumFirstMove(gameTable);
        botP2MediumSecondMove(gameTable);
        botP2MediumThirdMove(gameTable);
        botP2MediumFourthMove(gameTable);

        return gameTable;
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
