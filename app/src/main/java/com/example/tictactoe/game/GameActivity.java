package com.example.tictactoe.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tictactoe.R;

public class GameActivity extends AppCompatActivity {

    GameEngine gameEngine = new GameEngine(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameEngine.loadGameState();
        setContentView(R.layout.activity_game);

        setUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        gameEngine.saveGameState();
    }

    public void setUp() {
        Button[][] buttons = new Button[3][3];
        Button buttonReset = findViewById(R.id.button_reset);
        TextView textViewPlayer1 = findViewById(R.id.text_view_p1);
        TextView textViewPlayer2 = findViewById(R.id.text_view_p2);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonId = "button_" + i + j;
                int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = findViewById(resId);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameEngine.gameButtonClicked(v);
                    }
                });
            }
        }

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameEngine.resetGame();
            }
        });

        gameEngine.setButtonsId(buttons);
        gameEngine.setTextViewPlayerId(textViewPlayer1, textViewPlayer2);
    }
}