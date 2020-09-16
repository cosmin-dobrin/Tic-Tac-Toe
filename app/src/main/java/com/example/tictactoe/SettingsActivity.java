package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;


public class SettingsActivity extends AppCompatActivity {

    private LocaleManager localeManager = new LocaleManager(this, this);
    private GameEngine gameEngine = new GameEngine();
    private View decorView;
    private RadioGroup mRadioGroupStart;
    private RadioGroup mRadioGroupDraw;
    private RadioGroup mRadioGroupSymbol;
    private RadioGroup mRadioGroupPlayer1;
    private RadioGroup mRadioGroupDifficulty;
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
        mRadioGroupSymbol = findViewById(R.id.radio_group_symbol_chooser);
        mRadioGroupPlayer1 = findViewById(R.id.radio_group_player1_chooser);
        mRadioGroupDifficulty = findViewById(R.id.radio_group_difficulty_chooser);
        mSwitchSystemBars = findViewById(R.id.switch_show_system_bars);

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
                        gameEngine.setSymbolPlayer1(SettingsUtility.X);
                        break;
                    case R.id.radio_button_symbol_O:
                        gameEngine.setSymbolPlayer1(SettingsUtility.O);
                        break;
                }
            }
        });

        mRadioGroupPlayer1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_you:
                        gameEngine.setWhoIsPlayer1(SettingsUtility.YOU_ARE_PLAYER_1);
                        break;
                    case R.id.radio_button_robot:
                        gameEngine.setWhoIsPlayer1(SettingsUtility.BOT_IS_PLAYER_1);
                        break;
                }
            }
        });

        mRadioGroupDifficulty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_easy:
                        gameEngine.setDifficultyLevel(SettingsUtility.DIFFICULTY_LEVEL_EASY);
                        break;
                    case R.id.radio_button_medium:
                        gameEngine.setDifficultyLevel(SettingsUtility.DIFFICULTY_LEVEL_MEDIUM);
                        break;
                    case R.id.radio_button_hard:
                        gameEngine.setDifficultyLevel(SettingsUtility.DIFFICULTY_LEVEL_HARD);
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

    private void saveSettings() {
        SharedPreferences.Editor editor = this.getSharedPreferences(SettingsUtility.PREFS_FILE_SETTINGS, Context.MODE_PRIVATE).edit();
        editor.putInt(SettingsUtility.PREFS_START_BUTTON_ID, mRadioGroupStart.getCheckedRadioButtonId());
        editor.putInt(SettingsUtility.PREFS_DRAW_BUTTON_ID, mRadioGroupDraw.getCheckedRadioButtonId());
        editor.putInt(SettingsUtility.PREFS_SYMBOL_BUTTON_ID, mRadioGroupSymbol.getCheckedRadioButtonId());
        editor.putInt(SettingsUtility.PREFS_PLAYER1_BUTTON_ID, mRadioGroupPlayer1.getCheckedRadioButtonId());
        editor.putInt(SettingsUtility.PREFS_DIFFICULTY_BUTTON_ID, mRadioGroupDifficulty.getCheckedRadioButtonId());

        editor.putInt(SettingsUtility.PREFS_WHO_STARTS_VALUE, gameEngine.getWhoStarts());
        editor.putInt(SettingsUtility.PREFS_WHO_STARTS_DRAW_VALUE, gameEngine.getWhoStartsDraw());
        editor.putInt(SettingsUtility.PREFS_SYMBOL_VALUE, gameEngine.getSymbolPlayer1());
        editor.putInt(SettingsUtility.PREFS_PLAYER1_VALUE, gameEngine.getWhoIsPlayer1());
        editor.putInt(SettingsUtility.PREFS_DIFFICULTY_VALUE, gameEngine.getDifficultyLevel());
        editor.putBoolean(SettingsUtility.PREFS_SWITCH_VALUE, mSwitchSystemBars.isChecked());
        editor.putInt(SettingsUtility.PREFS_HIDE_SYSTEM_BARS_VALUE, mHideSystemBars);
        editor.apply();
    }

    private void loadSettings() {
        SharedPreferences preferences = this.getSharedPreferences(SettingsUtility.PREFS_FILE_SETTINGS, Context.MODE_PRIVATE);
        mRadioGroupStart.check(preferences.getInt(SettingsUtility.PREFS_START_BUTTON_ID, R.id.radio_button_winner));
        mRadioGroupDraw.check(preferences.getInt(SettingsUtility.PREFS_DRAW_BUTTON_ID, R.id.radio_button_draw_other));
        mRadioGroupSymbol.check(preferences.getInt(SettingsUtility.PREFS_SYMBOL_BUTTON_ID, R.id.radio_button_symbol_X));
        mRadioGroupPlayer1.check(preferences.getInt(SettingsUtility.PREFS_PLAYER1_BUTTON_ID, R.id.radio_button_you));
        mRadioGroupDifficulty.check(preferences.getInt(SettingsUtility.PREFS_DIFFICULTY_BUTTON_ID, R.id.radio_button_easy));

        mHideSystemBars = preferences.getInt(SettingsUtility.PREFS_HIDE_SYSTEM_BARS_VALUE, 0);
        mSwitchSystemBars.setChecked(preferences.getBoolean(SettingsUtility.PREFS_SWITCH_VALUE, false));
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