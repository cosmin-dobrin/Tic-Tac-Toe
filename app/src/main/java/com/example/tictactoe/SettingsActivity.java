package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.tictactoe.game.GameEngine;

public class SettingsActivity extends AppCompatActivity {

    RadioGroup radioGroupDraw;
    RadioGroup radioGroupStart;
    private LocaleManager localeManager = new LocaleManager(this, this);
    private GameEngine gameEngine = new GameEngine();
    private View decorView;

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
        loadSettings();
        if ((radioGroupStart.getCheckedRadioButtonId() == 0) | (radioGroupDraw.getCheckedRadioButtonId() == 0)) {
            radioGroupStart.check(R.id.radio_button_winner);
            radioGroupDraw.check(R.id.radio_button_draw_other);
        }

        radioGroupStart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case 0:
                        gameEngine.setWhoStarts(SettingsUtility.WINNER_STARTS);
                        break;
                    case 1:
                        gameEngine.setWhoStarts(SettingsUtility.DIFFERENT_PLAYER_STARTS);
                        break;
                }
                saveSettings();
            }
        });

        radioGroupDraw.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case 0:
                        gameEngine.setWhoStarts(SettingsUtility.DRAW_OTHER_PLAYER_STARTS);
                        break;
                    case 1:
                        gameEngine.setWhoStarts(SettingsUtility.DRAW_SAME_PLAYER_STARTS);
                        break;
                }
                saveSettings();
            }
        });

    }

    void saveSettings() {
        int radioButtonStartId = radioGroupStart.getCheckedRadioButtonId();
        int radioButtonDrawId = radioGroupDraw.getCheckedRadioButtonId();
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putInt("who_starts_setting", radioButtonStartId);
        editor.putInt("who_starts_draw_setting", radioButtonDrawId);
        editor.apply();
    }

    void loadSettings() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int radioButtonStartId = prefs.getInt("who_starts_setting", 0);
        int radioButtonDrawId = prefs.getInt("who_starts_draw_setting", 0);
        radioGroupStart.check(radioButtonStartId);
        radioGroupDraw.check(radioButtonDrawId);
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