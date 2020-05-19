package com.example.tictactoe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tictactoe.game.GameActivity;

import java.util.Locale;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_menu);

        Button buttonLaunch = findViewById(R.id.button_start);
        Button buttonLanguage = findViewById(R.id.button_language);

        buttonLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGame(v);
            }
        });

        buttonLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });
    }

    private void launchGame(View v) {
        Intent intent = new Intent(this, GameActivity.class);

        startActivity(intent);
    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"English", "German", "Spanish", "French", "Italian", "Romanian"};

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MenuActivity.this);
        mBuilder.setTitle("Choose Language: ");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0) {
                    setLocale("en");
                    recreate();
                } else if(i == 1) {
                    setLocale("de");
                    recreate();
                } else if(i == 2) {
                    setLocale("es");
                    recreate();
                } else if(i == 3) {
                    setLocale("fr");
                    recreate();
                } else if(i == 4) {
                    setLocale("it");
                    recreate();
                } else if(i == 5) {
                    setLocale("ro");
                    recreate();
                }

                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }
}