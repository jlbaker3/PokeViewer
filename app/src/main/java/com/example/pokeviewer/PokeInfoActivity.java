package com.example.pokeviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pokeviewer.api.PokemonAPI;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;

public class PokeInfoActivity extends AppCompatActivity {

    ToggleButton tglFavorite;
    Button btnEvolution;
    ImageView ivDefault;

    TextView tvIndex;
    TextView tvName;
    TextView tvHeight;
    TextView tvWeight;
    TextView tvTypes;
    TextView tvAbilities;

    TextView tvHPAmount;
    TextView tvAttackAmount;
    TextView tvDefenseAmount;
    TextView tvSPAttackAmount;
    TextView tvSPDefenseAmount;
    TextView tvSpeedAmount;

    ProgressBar pbHP;
    ProgressBar pbAttack;
    ProgressBar pbDefense;
    ProgressBar pbSPAttack;
    ProgressBar pbSPDefense;
    ProgressBar pbSpeed;

    Intent intent;
    String TAG = "POKEMON_INFO";
    String speciesID;
    PokemonAPI pokeAPI;

    Boolean showImages = true;
    Boolean favorites = false;
    Set<String> favoritePokemon;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pokeinfo);


        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button


        //get the intent from the main activity and pull the pokemon index
        intent = getIntent();
        String index = intent.getStringExtra("ID");

        loadPreferences();

        //get access to all components of the xml

        btnEvolution = findViewById(R.id.btnEvolution);
        ivDefault = findViewById(R.id.ivDefault);

        tvIndex = findViewById(R.id.tvIndex);
        tvName = findViewById(R.id.tvName);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        tvTypes = findViewById(R.id.tvTypes);
        tvAbilities = findViewById(R.id.tvAbilities);

        tvHPAmount = findViewById(R.id.tvHPAmount);
        tvAttackAmount = findViewById(R.id.tvAttackAmount);
        tvDefenseAmount = findViewById(R.id.tvDefenseAmount);
        tvSPAttackAmount = findViewById(R.id.tvSPAttackAmount);
        tvSPDefenseAmount = findViewById(R.id.tvSPDefenseAmount);
        tvSpeedAmount = findViewById(R.id.tvSpeedAmount);

        pbHP = findViewById(R.id.pbHP);
        pbAttack = findViewById(R.id.pbAttack);
        pbDefense = findViewById(R.id.pbDefense);
        pbSPAttack = findViewById(R.id.pbSPAttack);
        pbSPDefense = findViewById(R.id.pbSPDefense);
        pbSpeed  = findViewById(R.id.pbSpeed);

        tglFavorite = findViewById(R.id.tglFavorite);
        //set the favorites toggle visibility based on the favorites setting
        tglFavorite.setVisibility(favorites ? (View.VISIBLE) : (View.INVISIBLE));


        //calls the method to fetch poke info and populate the activity
        this.fetchSinglePokemon(index);


        //button used to direct the user to the evolutions activity
        //rename the class in the intent if needed
        btnEvolution.setOnClickListener(view -> {
            Intent i = new Intent(this, EvolutionActivity.class);
            i.putExtra("ID", speciesID);
            i.putExtra("NAME", tvName.getText() + "");
            startActivity(i);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //load the don't show images and favorites settings
    private void loadPreferences() {
        sharedPreferences = getSharedPreferences("com.example.group3", MODE_PRIVATE);
        showImages = !sharedPreferences.getBoolean("dontDisplayImages", false);
        favorites = sharedPreferences.getBoolean("favorites", false);
        //string set to hold the names of all favorite pokemon
        favoritePokemon = sharedPreferences.getStringSet("favoritePokemon", new HashSet<>());
    }

    private void savePreferences() {
        Log.d(TAG, "savePreferences: saving the pokemon favorites");
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        sharedPreferences.edit().putStringSet("favoritePokemon", favoritePokemon).apply();
        //editor.apply();
    }

    public void fetchSinglePokemon(String id) {
        //fetch the pokemon with the corresponding id
        pokeAPI.getInstance(getBaseContext()).fetchPokemonByID(id, pokemon -> {
            //set image from a pokemon class using Picasso
            //check if the don't show images setting is selected
            if (showImages)
                Picasso.get().load(PokemonAPI.getPokemonImageURLFromID(pokemon.id)).fit().into(ivDefault);

            //set the favorites toggle checked property and background image based on the favorites setting and if the current pokemon is saved as a favorite
            tglFavorite.setChecked(favoritePokemon.contains(pokemon.getNameCapital()));
            tglFavorite.setBackground(ContextCompat.getDrawable(getApplicationContext(), favoritePokemon.contains(pokemon.getNameCapital()) ? (R.drawable.favorited) : (R.drawable.unfavorited)));
            //changes the toggle background image and adds and removes the current pokemon from the favorites set based on checked property
            tglFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    tglFavorite.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.favorited));
                    favoritePokemon.add(pokemon.getNameCapital());
                    savePreferences();
                }
                else {
                    tglFavorite.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unfavorited));
                    favoritePokemon.remove(pokemon.getNameCapital());
                    savePreferences();
                }
            });

            //set the pokemon id and name
            tvIndex.setText("#"+pokemon.id);
            tvName.setText(pokemon.getNameCapital());

            //Set the height and weight
            tvHeight.setText(pokemon.height/10.0+" M");
            tvWeight.setText(pokemon.weight/10.0+" KG");

            //Split the types string into an array
            String[] typeArr = pokemon.types.toString().split(",");
            //check the array length to see if the pokemon has one or two types
            if(typeArr.length > 3)
                tvTypes.setText(typeArr[1].split("'")[1].toUpperCase() +", "+typeArr[4].split("'")[1].toUpperCase());
            else
                tvTypes.setText(typeArr[1].split("'")[1].toUpperCase());

            //Split the abilities into an array
            String[] abilitiesArr = pokemon.abilities.toString().split(",");
            //check the array length to see how many abilities the pokemon can have
            if(abilitiesArr.length > 8)
                tvAbilities.setText(abilitiesArr[0].split("'")[1].toUpperCase()+", "+
                                    abilitiesArr[4].split("'")[1].toUpperCase()+", "+
                                    abilitiesArr[8].split("'")[1].toUpperCase());
            else if(abilitiesArr.length > 5)
                tvAbilities.setText(abilitiesArr[0].split("'")[1].toUpperCase()+", "+
                                    abilitiesArr[4].split("'")[1].toUpperCase());
            else
                tvAbilities.setText(abilitiesArr[0].split("'")[1].toUpperCase());

            //order of the stats using pokemon.stats.toString()
            //hp, attack, defense, special attack, special defense, speed
            //0    4        8            12               16         20     index of statsArr
            String[] statsArr = pokemon.stats.toString().split(",");

            //set the stat amounts
            tvHPAmount.setText(statsArr[0].split("=")[1]);
            tvAttackAmount.setText(statsArr[4].split("=")[1]);
            tvDefenseAmount.setText(statsArr[8].split("=")[1]);
            tvSPAttackAmount.setText(statsArr[12].split("=")[1]);
            tvSPDefenseAmount.setText(statsArr[16].split("=")[1]);
            tvSpeedAmount.setText(statsArr[20].split("=")[1]);

            //update the progress bars with the correct progress
            pbHP.setProgress(Integer.parseInt(statsArr[0].split("=")[1]));
            pbAttack.setProgress(Integer.parseInt(statsArr[4].split("=")[1]));
            pbDefense.setProgress(Integer.parseInt(statsArr[8].split("=")[1]));
            pbSPAttack.setProgress(Integer.parseInt(statsArr[12].split("=")[1]));
            pbSPDefense.setProgress(Integer.parseInt(statsArr[16].split("=")[1]));
            pbSpeed.setProgress(Integer.parseInt(statsArr[20].split("=")[1]));

            speciesID = pokemon.getSpeciesID();
            //set the text on the evoultion button with the pokemons name
            btnEvolution.setText(pokemon.name+"'s evolution chart");


//            for(int i=0; i<statsArr.length; i++){
//                Log.d(TAG, statsArr[i]);
//            }
            //Log.d(TAG, pokemon.stats.toString());
            //Log.d(TAG, pokemon.types+"");
        }, error -> {
            Log.d(TAG, error);
        });
    }
}