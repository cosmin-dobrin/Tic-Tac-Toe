package com.dobrincosminiulian.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

public class LocaleManager {

    private Context context;
    private Activity activity;

    public LocaleManager(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    public void showChangeLanguageDialog() {
        final String[] listItems = {activity.getString(R.string.english),
                activity.getString(R.string.german), activity.getString(R.string.spanish),
                activity.getString(R.string.french), activity.getString(R.string.italian),
                activity.getString(R.string.romanian)};

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle(R.string.choose_language);
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0) {
                    setLocale("en");
                    activity.recreate();
                } else if(i == 1) {
                    setLocale("de");
                    activity.recreate();
                } else if(i == 2) {
                    setLocale("es");
                    activity.recreate();
                } else if(i == 3) {
                    setLocale("fr");
                    activity.recreate();
                } else if(i == 4) {
                    setLocale("it");
                    activity.recreate();
                } else if(i == 5) {
                    setLocale("ro");
                    activity.recreate();
                }

                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
}