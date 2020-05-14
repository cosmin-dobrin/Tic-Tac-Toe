package com.example.tictactoe.game;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameStateRepository {

    private Context fileContext;
    private static final String FILE_NAME = "State.ser";
    private GameState gameState;

    GameStateRepository(GameState gameState) {
        this.gameState = gameState;
    }



    void save() {

        try {
            FileOutputStream fos = fileContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(gameState);
            os.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {

        try {
            FileInputStream fis = fileContext.openFileInput(FILE_NAME);
            ObjectInputStream os = new ObjectInputStream(fis);
            gameState = (GameState) os.readObject();
            os.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}