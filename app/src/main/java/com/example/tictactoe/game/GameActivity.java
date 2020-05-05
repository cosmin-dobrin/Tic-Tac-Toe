package com.example.tictactoe.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tictactoe.R;

public class GameActivity extends AppCompatActivity {

    GameEngine engine = new GameEngine(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setUp();
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
                        engine.gameButtonClicked(v);
                    }
                });
            }
        }

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engine.resetGame();
            }
        });

        engine.setButtonsId(buttons);
        engine.setTextViewPlayerId(textViewPlayer1, textViewPlayer2);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", engine.roundCount);
        outState.putInt("player1Points", engine.player1Points);
        outState.putInt("player2Points", engine.player2Points);
        outState.putBoolean("player1Turn", engine.player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        engine.roundCount = savedInstanceState.getInt("roundCount");
        engine.player1Points = savedInstanceState.getInt("player1Points");
        engine.player2Points = savedInstanceState.getInt("player2Points");
        engine.player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}