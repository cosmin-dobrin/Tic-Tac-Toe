package com.example.tictactoe.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class GameStateRepository {

    private GameState gameState;

    GameStateRepository(GameState gameState) {
        this.gameState = gameState;
    }

    void save() {

        try {
            FileOutputStream fos = new FileOutputStream("State.ser");
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(gameState);
            os.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void load() {

        try {
            FileInputStream fis = new FileInputStream("State.ser");
            ObjectInputStream os = new ObjectInputStream(fis);
            gameState = (GameState) os.readObject();
            os.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}