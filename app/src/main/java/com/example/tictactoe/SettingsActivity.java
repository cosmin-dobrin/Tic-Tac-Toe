package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.RadioGroup;

import com.example.tictactoe.game.GameEngine;

public class SettingsActivity extends AppCompatActivity {

    private LocaleManager localeManager = new LocaleManager(this, this);
    private GameEngine gameEngine = new GameEngine();
    private View decorView;
    private RadioGroup radioGroupStart;
    private RadioGroup radioGroupDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localeManager.loadLocale();
        setContentView(R.layout.activity_settings);
        decorView = getWindow().getDecorView();
        setUpSettings();
    }

    private void setUpSettings() {

        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        radioGroupStart = findViewById(R.id.radio_group_who_starts);
        radioGroupDraw = findViewById(R.id.radio_group_who_starts_case_draw);

        radioGroupStart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_winner:
                        gameEngine.setWhoStarts(SettingsUtility.WINNER_STARTS);
                        break;
                    case R.id.radio_button_different:
                        gameEngine.setWhoStarts(SettingsUtility.DIFFERENT_PLAYER_STARTS);
                        break;
                }
            }
        });

        radioGroupDraw.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_draw_other:
                        gameEngine.setWhoStartsDraw(SettingsUtility.DRAW_OTHER_PLAYER_STARTS);
                        break;
                    case R.id.radio_button_draw_same:
                        gameEngine.setWhoStartsDraw(SettingsUtility.DRAW_SAME_PLAYER_STARTS);
                        break;
                }
            }
        });

        loadSettings();

        if (radioGroupStart.getCheckedRadioButtonId() == 0) {
            radioGroupStart.check(R.id.radio_button_winner);
        }
        if (radioGroupDraw.getCheckedRadioButtonId() == 0) {
            radioGroupDraw.check(R.id.radio_button_draw_other);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }

    void saveSettings() {
        SharedPreferences.Editor editor = this.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putInt("start_button_id", radioGroupStart.getCheckedRadioButtonId());
        editor.putInt("draw_button_id", radioGroupDraw.getCheckedRadioButtonId());
        editor.putInt("who_starts_value", gameEngine.getWhoStarts());
        editor.putInt("who_starts_draw_value", gameEngine.getWhoStartsDraw());
        editor.apply();
    }

    public void loadSettings() {
        SharedPreferences prefs = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        radioGroupStart.check(prefs.getInt("start_button_id", 0));
        radioGroupDraw.check(prefs.getInt("draw_button_id", 0));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
}