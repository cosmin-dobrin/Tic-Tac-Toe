package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tictactoe.game.GameActivity;

import java.util.Locale;

public class MenuActivity extends AppCompatActivity {

    LocaleManager localeManager = new LocaleManager(this, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localeManager.loadLocale();
        setContentView(R.layout.activity_menu);
        setUpMenu();
    }

    private void setUpMenu() {
        Button buttonLaunch = findViewById(R.id.button_start);
        Button buttonLanguage = findViewById(R.id.button_language);

        buttonLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGame();
            }
        });

        buttonLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localeManager.showChangeLanguageDialog();
            }
        });
    }

    private void launchGame() {
        Intent intent = new Intent(this, GameActivity.class);

        startActivity(intent);
    }
}