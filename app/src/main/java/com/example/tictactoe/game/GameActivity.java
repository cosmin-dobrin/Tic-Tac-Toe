package com.example.tictactoe.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
                updatePointsText();
            }
        });
    }

    private void updatePointsText() {
        gameEngine.getTextViewPlayer1().setText(getResources().getString(R.string.player_1_updatePointsText, gameEngine.getPlayer1Points())); // String resources + placeholders
        gameEngine.getTextViewPlayer2().setText(getResources().getString(R.string.player_2_updatePointsText, gameEngine.getPlayer2Points()));
    }

    private void showToast() {
        if ((gameEngine.getPlayer1Wins()) && (gameEngine.getRoundCount() != 0)) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.player_1_wins), Toast.LENGTH_SHORT).show();
        } else if((gameEngine.getPlayer2Wins()) && (gameEngine.getRoundCount() != 0)) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.player_2_wins), Toast.LENGTH_SHORT).show();
        } else if(gameEngine.getRoundCount() != 0) {
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