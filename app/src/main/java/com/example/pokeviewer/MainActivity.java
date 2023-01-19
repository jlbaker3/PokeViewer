package com.example.pokeviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokeviewer.api.PokemonAPI;
import com.example.pokeviewer.api.models.list.PokemonListItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    PokemonAPI pokeAPI;
    List<PokemonListItem> listOfPokemon = new ArrayList<>();
    String TAG = "POKEMON";
    EditText etSearch;
    ImageButton btnAbout;
    ImageButton btnSettings;
    RecyclerView recAllPokemon;
    ProgressBar pbLoader;
    ToggleButton tglFavoritesToggle;
    //set the adapter as a global so that it can be updated in the fetch function
    com.example.pokeviewer.PokemonAdapter adapter = new com.example.pokeviewer.PokemonAdapter(listOfPokemon,this);

    Boolean favorites = false;
    Set<String> favoritePokemon;
    int listPosition = 0;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //All the main views
        etSearch = findViewById(R.id.etSearch);
        btnAbout = findViewById(R.id.btnAbout);
        btnSettings = findViewById(R.id.btnSettings);
        recAllPokemon = findViewById(R.id.recAllPokemon);
        pbLoader = findViewById(R.id.pbLoader);
        tglFavoritesToggle = findViewById(R.id.tglFavoritesToggle);

        //Btn about click
        btnAbout.setOnClickListener(view -> {
            //Open the about activity
            Intent intent = new Intent(this, com.example.pokeviewer.AboutActivity.class);
            startActivity(intent);
        });

        //Btn setting click
        btnSettings.setOnClickListener(view -> {
            //Open the settings activity
            Intent intent = new Intent(this, com.example.pokeviewer.SettingsActivity.class);
            startActivity(intent);
        });

        //Apply some changes to the recycle view for a better scroll experience
        recAllPokemon.setHasFixedSize(true);
        recAllPokemon.setItemViewCacheSize(20);
        recAllPokemon.setDrawingCacheEnabled(true);

        //set the adapter for the recycler view
        recAllPokemon.setAdapter(adapter);
        recAllPokemon.setLayoutManager(new LinearLayoutManager(this));

        this.fetchAllPokemon();

        //text changed listener for the search purposes
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        //Toggle the favorites
        tglFavoritesToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //Is it toggled?
            if (isChecked) {
                //Then change the images and check if it can be filtered
                tglFavoritesToggle.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.favorited));
                if (favorites && favoritePokemon.size()>0)
                    filterFavorites();
            }
            else {
                //If its not filtered reset it back to the original list
                tglFavoritesToggle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unfavorited));
                adapter.filteredList((ArrayList<PokemonListItem>) listOfPokemon);
            }
            //Save this too
            savePreferences();
        });



    }

    @Override
    public void onPause(){
        super.onPause();
        //save scroll position
        listPosition = ((LinearLayoutManager) Objects.requireNonNull(recAllPokemon.getLayoutManager())).findFirstCompletelyVisibleItemPosition();
    }

    @Override
    public void onResume(){
        super.onResume();
        //return to saved scroll position
        Objects.requireNonNull(recAllPokemon.getLayoutManager()).scrollToPosition(listPosition);
        adapter.notifyDataSetChanged();
        recAllPokemon.setAdapter(adapter);
        loadPreferences();
        adapter.notifyDataSetChanged();
    }

    private void loadPreferences() {

        //Get the share prefrences
        sharedPreferences = getSharedPreferences("com.example.group3", MODE_PRIVATE);
        //Get the check if favorites is enabled
        favorites = sharedPreferences.getBoolean("favorites", false);
        //Get if the favorites is toggled for list view
        boolean toggledFavorites = sharedPreferences.getBoolean("toggledFavorites", false);
        //Get the list of favorite pokemon
        favoritePokemon = sharedPreferences.getStringSet("favoritePokemon", new HashSet<>());

        //Toggle,filter, and show based on the variables above
        tglFavoritesToggle.setVisibility(favorites ? (View.VISIBLE) : (View.INVISIBLE));
        tglFavoritesToggle.setChecked(toggledFavorites);
        if (toggledFavorites) {
            tglFavoritesToggle.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.favorited));
            if (favorites && favoritePokemon.size()>0)
                filterFavorites();

        } else {
            tglFavoritesToggle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unfavorited));
        }

        //Mention the loadded pokemon to the log
        Log.d(TAG, "loadPreferences: " + favoritePokemon.toString());
        if (!favorites)
            //And update any values for the dapater to be filtered.
            adapter.filteredList((ArrayList<PokemonListItem>) listOfPokemon);

        boolean darkMode = sharedPreferences.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(darkMode ? (AppCompatDelegate.MODE_NIGHT_YES) : (AppCompatDelegate.MODE_NIGHT_NO));
    }

    private void savePreferences() {

        //editor.putBoolean("dontDisplayImages", sharedPreferences.getBoolean("dontDisplayImages", false));
        Log.d(TAG, "savePreferences: SAVING THINGSD HAHAHAHHA");
        sharedPreferences.edit().putBoolean("toggledFavorites", tglFavoritesToggle.isChecked()).apply();
        //editor.putBoolean("darkMode", sharedPreferences.getBoolean("darkMode", false));
    }

    public void filterFavorites(){
        //create new arraylist of pokemonlistitem
        ArrayList<PokemonListItem> filteredList = new ArrayList<>();
        //for each item in the current list
        for (PokemonListItem item : listOfPokemon){
            //for each favorite pokemon
            for (String pokemonName: favoritePokemon) {
                //check if the name matches the current pokemon item name
                if (item.getNameCapital().equals(pokemonName)){
                    //if so add it to the filtered list
                    filteredList.add(item);
                }
            }
        }
        //call the method in the PokemonAdapter class passing in the filtered list
        adapter.filteredList(filteredList);
    }

    //method for filtering the RecyclerView based on the users search
    public void filter(String text){
        //if favorites are enabled and the text is empty, show the favorites
        if (text.equals("") && favorites && favoritePokemon.size()>0) {
            filterFavorites();
        }
        else {
            //create new arraylist of pokemonlistitem
            ArrayList<PokemonListItem> filteredList = new ArrayList<>();

            //for each item in the current list
            for (PokemonListItem item : listOfPokemon){
                //check if the name contains the users text
                if (item.getNameCapital().toLowerCase().contains(text.toLowerCase())){
                    //if so add it to the filtered list
                    filteredList.add(item);
                }
            }
            //call the method in the PokemonAdapter class passing in the filtered list
            adapter.filteredList(filteredList);
        }
    }

    public void fetchAllPokemon() {
        pbLoader.setVisibility(View.VISIBLE);
        //This will fetch all pokemon from the database
        pokeAPI.getInstance(getBaseContext()).fetchAllPokemon(allPokemon -> {
            listOfPokemon.addAll(allPokemon); //This will add it to a List
            //notify the adapter here
            adapter.notifyDataSetChanged();
            pbLoader.setVisibility(View.INVISIBLE);
            Log.d(TAG, "pokemon list size: " + listOfPokemon.size());
            loadPreferences();
        }, error -> {
            Log.d(TAG, error);
        });
    }

    //This function should be in the main pokemon activity.
    public void fetchSinglePokemon(String id) {
        //This will fetch the first pokemon by the id of 1, you can also use names technically.
        pokeAPI.getInstance(getBaseContext()).fetchPokemonByID(id, pokemon -> {
            Log.d(TAG, pokemon.toString()); //Will print all of the data about said pokemon
            //You can look at the Pokemon class for what data it provides. Its a lot.
            //load the data into the objects here, update lists, etc.

            //Example of how to set an image from a 'Pokemon' class
            //Picasso.get().load(PokemonAPI.getPokemonImageURLFromID(pokemon.getID())).into(ivPic);


            //Ahh yes because pokeapi could not make it any more confusing...
            //Pokemon -> Pokemon Species -> Evolution Chain

            //This should only be called in the Evolution activity.
            //Could always store a species ID in a variable.
            fetchSpieces(pokemon.getSpeciesID());
        }, error -> {
            Log.d(TAG, error);
        });
    }
    public void fetchSpieces(String speciesID) {
        pokeAPI.getInstance(getBaseContext()).fetchSpecieFromID(speciesID, specie -> {
            Log.d(TAG, specie.toString());

            //Pass via bundle to the Evolution Activity
            //specie.getEvolutionChainID()\

            //Can we get the list of what eevee evolves to?
            fetchEvolution(specie.getEvolutionChainID());
        },value -> {
            Log.d(TAG, value.toString());
        });
    }

    public void fetchEvolution(String evolutionID) {
        //We can also fetch the evolutions of the pokemon this way.
        //Although this is recursive, so a better way of displaying said evoltions
        //may be needed.
        pokeAPI.getInstance(getBaseContext()).fetchEvolutionChainFromID(evolutionID, value -> {
            Log.d(TAG, value.toString());

            //The siblings method will return a list of PokemonListItem which can be use by an
            //ListView to show the direct siblings of the said parent pokemon (or spices in this case)
            //you can add all of the siblings to a local array then notify the dataset changed
            //eg: arrEvolution.addAll(evolution.siblings())
            //    adpater.notifyDataSetChanged()

            // Log.d(TAG, value.siblings().toString());
        },value -> {
            Log.d(TAG, value.toString());
        });
    }
}