package com.dobrincosminiulian.tictactoe;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameStateRepository {

    private GameState gameState;

    public GameStateRepository(GameState gameState) {
        this.gameState = gameState;
    }

    public void save() {

        try {
            FileOutputStream fos = new FileOutputStream("GameState.ser");
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(gameState);
            os.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {

        try {
            FileInputStream fis = new FileInputStream("GameState.ser");
            ObjectInputStream os = new ObjectInputStream(fis);
            gameState = (GameState) os.readObject();
            os.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}