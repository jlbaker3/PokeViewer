package com.example.pokeviewer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.HashSet;

public class SettingsActivity extends AppCompatActivity {

    Button btnSave;
    Button btnCancel;
    Button btnClearFavorites;
    CheckBox chkDontDisplayImages;
    CheckBox chkFavorites;
    Switch swMode;
    Boolean dontDisplayImages = false;
    Boolean favorites = false;
    Boolean darkMode = false;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnClearFavorites = findViewById(R.id.btnClearFavorites);
        chkDontDisplayImages = findViewById(R.id.chkDontDisplayImages);
        chkFavorites = findViewById(R.id.chkFavorites);
        swMode = findViewById(R.id.swMode);

        //load the preferences when the settings activity is created
        loadPreferences();

        //save preferences and finish settings to go back to the home screen with the saved settings
        btnSave.setOnClickListener(view -> {
            savePreferences();
            Toast.makeText(this, "Saved Settings", Toast.LENGTH_SHORT).show();
            finish();
        });

        //when cancel is clicked reset the night mode if it was changed and just call finish not savePreferences
        btnCancel.setOnClickListener(view -> {
            AppCompatDelegate.setDefaultNightMode(darkMode ? (AppCompatDelegate.MODE_NIGHT_YES) : (AppCompatDelegate.MODE_NIGHT_NO));
            finish();
        });

        //clear the favorites list by putting a new empty set in the preferences for "favoritePokemon"
        btnClearFavorites.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.group3", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("favoritePokemon", new HashSet<>());
            editor.putBoolean("toggledFavorites", false);
            editor.apply();
            Toast.makeText(this, "Clear favorites", Toast.LENGTH_SHORT).show();
        });

        //activate and deactivate night mode based on the checked value of the slider
        swMode.setOnClickListener(view -> {
            AppCompatDelegate.setDefaultNightMode(swMode.isChecked() ? (AppCompatDelegate.MODE_NIGHT_YES) : (AppCompatDelegate.MODE_NIGHT_NO));
            sharedPreferences.edit().putBoolean("darkMode", swMode.isChecked()).apply();
        });
    }

    //loads shared preferences
    private void loadPreferences() {
        sharedPreferences = getSharedPreferences("com.example.group3", MODE_PRIVATE);
        //get the saved settings (default false if no preferences yet)
        dontDisplayImages = sharedPreferences.getBoolean("dontDisplayImages", false);
        favorites = sharedPreferences.getBoolean("favorites", false);
        darkMode = sharedPreferences.getBoolean("darkMode", false);
        //set the views checked property based on the saved preference values
        swMode.setChecked(darkMode);
        chkDontDisplayImages.setChecked(dontDisplayImages);
        chkFavorites.setChecked(favorites);
    }

    //this method saves preferences which is essentially saving the settings the user has selected
    private void savePreferences() {
        //use the view checked properties at the time this method is called to get the settings values
        dontDisplayImages = chkDontDisplayImages.isChecked();
        favorites = chkFavorites.isChecked();
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.group3", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dontDisplayImages", dontDisplayImages);
        editor.putBoolean("favorites", favorites);
        editor.apply();
    }

}