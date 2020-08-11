package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.example.tictactoe.game.GameEngine;

public class SettingsActivity extends AppCompatActivity {

    private LocaleManager localeManager = new LocaleManager(this, this);
    private GameEngine gameEngine = new GameEngine();
    private View decorView;
    private RadioGroup mRadioGroupStart;
    private RadioGroup mRadioGroupDraw;
    private RadioGroup mRadioGroupSymbol;
    private Switch mSwitchSystemBars;
    private int mHideSystemBars = 0;

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
                if (mHideSystemBars == SettingsUtility.HIDE_SYSTEM_BARS) {
                    if (visibility == 0) {
                        decorView.setSystemUiVisibility(hideSystemBars());
                    }
                }
            }
        });

        mRadioGroupStart = findViewById(R.id.radio_group_who_starts);
        mRadioGroupDraw = findViewById(R.id.radio_group_who_starts_case_draw);
        mSwitchSystemBars = findViewById(R.id.switch_show_system_bars);
        mRadioGroupSymbol = findViewById(R.id.radio_group_symbol_chooser);

        mRadioGroupStart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        mRadioGroupDraw.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        mRadioGroupSymbol.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_symbol_X:
                        gameEngine.setPlayer1Symbol("X");
                        break;
                    case R.id.radio_button_symbol_O:
                        gameEngine.setPlayer1Symbol("O");
                        break;
                }
            }
        });

        mSwitchSystemBars.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mHideSystemBars = SettingsUtility.HIDE_SYSTEM_BARS;
                    decorView.setSystemUiVisibility(hideSystemBars());
                } else {
                    mHideSystemBars = SettingsUtility.SHOW_SYSTEM_BARS;
                    decorView.setSystemUiVisibility(showSystemBars());
                }
            }
        });

        loadSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }

    void saveSettings() {
        SharedPreferences.Editor editor = this.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putInt("start_button_id", mRadioGroupStart.getCheckedRadioButtonId());
        editor.putInt("draw_button_id", mRadioGroupDraw.getCheckedRadioButtonId());
        editor.putInt("symbol_button_id", mRadioGroupSymbol.getCheckedRadioButtonId());

        editor.putInt("who_starts_value", gameEngine.getWhoStarts());
        editor.putInt("who_starts_draw_value", gameEngine.getWhoStartsDraw());
        editor.putString("symbol_value", gameEngine.getPlayer1Symbol());
        editor.putBoolean("switch_state", mSwitchSystemBars.isChecked());
        editor.putInt("hide_system_bars_value", mHideSystemBars);
        editor.apply();
    }

    public void loadSettings() {
        SharedPreferences prefs = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        mRadioGroupStart.check(prefs.getInt("start_button_id", R.id.radio_button_winner));
        mRadioGroupDraw.check(prefs.getInt("draw_button_id", R.id.radio_button_draw_other));
        mRadioGroupSymbol.check(prefs.getInt("symbol_button_id", R.id.radio_button_symbol_X));
        mHideSystemBars = prefs.getInt("hide_system_bars_value", 0);
        mSwitchSystemBars.setChecked(prefs.getBoolean("switch_state", false));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mHideSystemBars == SettingsUtility.HIDE_SYSTEM_BARS) {
            if (hasFocus) {
                decorView.setSystemUiVisibility(hideSystemBars());
            } else {
                if (hasFocus) {
                    decorView.setSystemUiVisibility(showSystemBars());
                }
            }
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

    private int showSystemBars() {
        return View.SYSTEM_UI_FLAG_VISIBLE;
    }
}