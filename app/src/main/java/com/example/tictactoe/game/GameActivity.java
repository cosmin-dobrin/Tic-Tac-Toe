package com.example.tictactoe.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tictactoe.R;

public class GameActivity extends AppCompatActivity {

    GameEngine gameEngine = new GameEngine(this, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameEngine.loadGameState();
        setContentView(R.layout.activity_game);
        gameEngine.setUpGame();

        gameEngine.setCompletionListener(new GameCompletionListener() {
            @Override
            public void onCompletion() {
                showToast();
            }
        });
    }

    private void showToast() {
        if (gameEngine.getPlayer1Wins()) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.player_1_wins), Toast.LENGTH_SHORT).show();
        } else if(gameEngine.getPlayer2Wins()) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.player_2_wins), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.draw), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameEngine.saveGameState();
    }
}