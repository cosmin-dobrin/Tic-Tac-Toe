package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tictactoe.game.GameActivity;

public class MenuActivity extends AppCompatActivity {

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
                launchGame();
            }
        });

        buttonLaunchSinglePlayer.setOnClickListener(new View.OnClickListener() {
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

    private void launchGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private void loadSettings() {
        SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        mHideSystemBars = preferences.getInt("hide_system_bars_value", 0);
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