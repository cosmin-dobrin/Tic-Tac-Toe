package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tictactoe.game.GameActivity;

import java.util.Locale;

public class MenuActivity extends AppCompatActivity {

    public static final String EXTRA_SYMBOL = "com.example.tictactoe.SYMBOL";
    LocaleManager localeManager = new LocaleManager(this, this);
    private View decorView;
    private String symbolPlayer1;

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
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private void setUpMenu() {
        Button buttonLaunch = findViewById(R.id.button_start);
        Button buttonLanguage = findViewById(R.id.button_language);
        Button buttonSettings = findViewById(R.id.button_settings);

        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        buttonLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChooser();
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
        intent.putExtra(EXTRA_SYMBOL, symbolPlayer1);
        if (symbolPlayer1 != null) {
            startActivity(intent);
        }
    }

    private void createChooser() {
        final String[] itemList = {"X" , "O"};
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Choose a symbol for Player 1: ");
        dialogBuilder.setSingleChoiceItems(itemList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    symbolPlayer1 = "X";
                } else if (which == 1) {
                    symbolPlayer1 = "O";
                }
                dialog.dismiss();
                launchGame();
            }
        });

        AlertDialog mDialog = dialogBuilder.create();
        mDialog.show();
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