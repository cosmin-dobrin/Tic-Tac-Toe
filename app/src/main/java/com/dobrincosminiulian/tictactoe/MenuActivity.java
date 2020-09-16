package com.dobrincosminiulian.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    public static final String EXTRA_SINGLE_PLAYER_MODE = "com.dobrincosminiulian.tictactoe.extra.SINGLE_PLAYER_MODE";
    private LocaleManager localeManager = new LocaleManager(this, this);
    private View decorView;
    private int mHideSystemBars = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localeManager.loadLocale();
        setContentView(R.layout.activity_menu);
        decorView = getWindow().getDecorView();
        setUpMenu();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mHideSystemBars == SettingsUtility.HIDE_SYSTEM_BARS) {
            if (hasFocus) {
                decorView.setSystemUiVisibility(hideSystemBars());
            }
        } else {
            if (hasFocus) {
                decorView.setSystemUiVisibility(showSystemBars());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
    }

    private void setUpMenu() {
        Button buttonLaunchTwoPlayers = findViewById(R.id.button_start);
        Button buttonLanguage = findViewById(R.id.button_language);
        Button buttonSettings = findViewById(R.id.button_settings);
        Button buttonLaunchSinglePlayer = findViewById(R.id.button_start_robot);

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

        loadSettings();

        buttonLaunchTwoPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchTwoPlayers();
            }
        });

        buttonLaunchSinglePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSinglePlayer();
            }
        });

        buttonLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localeManager.showChangeLanguageDialog();
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSettingsActivity();
            }
        });
    }

    private void launchSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void launchTwoPlayers() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_SINGLE_PLAYER_MODE, SettingsUtility.SINGLE_PLAYER_MODE_OFF);
        startActivity(intent);
    }

    private void launchSinglePlayer() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_SINGLE_PLAYER_MODE , SettingsUtility.SINGLE_PLAYER_MODE_ON);
        startActivity(intent);
    }

    private void loadSettings() {
        SharedPreferences preferences = getSharedPreferences(SettingsUtility.PREFS_FILE_SETTINGS, Context.MODE_PRIVATE);
        mHideSystemBars = preferences.getInt(SettingsUtility.PREFS_HIDE_SYSTEM_BARS_VALUE, 0);
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