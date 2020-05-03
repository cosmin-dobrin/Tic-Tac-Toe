package com.example.tictactoe.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.tictactoe.R;

public class GameActivity extends AppCompatActivity {

    GameEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        engine = new GameEngine(this);
        setIds();
        engine.setListeners();
    }

    public void setIds() {
        Button[][] buttons = new Button[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonId = "button_" + i + j;
                int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = findViewById(resId);
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        TextView textViewPlayer1 = findViewById(R.id.text_view_p1);
        TextView textViewPlayer2 = findViewById(R.id.text_view_p2);
        engine.setButtonsId(buttons, buttonReset);
        engine.setTextViewPlayerId(textViewPlayer1, textViewPlayer2);
    }
}