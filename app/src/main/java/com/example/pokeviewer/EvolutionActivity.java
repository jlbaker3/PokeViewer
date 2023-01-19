package com.example.pokeviewer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokeviewer.api.PokemonAPI;
import com.example.pokeviewer.api.models.evolution.PokemonEvolutionChain;
import com.example.pokeviewer.api.models.list.PokemonListItem;

import java.util.ArrayList;
import java.util.List;

public class EvolutionActivity extends AppCompatActivity {

    PokemonAPI pokeAPI;
    String TAG = "EVOLUTION";

    Intent intent;
    String speciesID, speciesName;

    List<com.example.pokeviewer.Evolution> evolutionList = new ArrayList<>();
    com.example.pokeviewer.EvolutionAdapter adapter;

    TextView tvwEvolutionaryLine;
    RecyclerView recEvolutionChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evolution);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button


        tvwEvolutionaryLine = findViewById(R.id.tvwEvolutionaryLine);
        recEvolutionChart = findViewById(R.id.recEvolutionChart);

        // Grab extras passed in from PokeInfoActivity
        intent = getIntent();
        speciesID = intent.getStringExtra("ID");
        speciesName = intent.getStringExtra("NAME");

        tvwEvolutionaryLine.setText(speciesName + "'s Evolutionary Line");

        // Bind the recycler view to the adapter
        adapter = new com.example.pokeviewer.EvolutionAdapter(evolutionList, this, speciesName);
        recEvolutionChart.setAdapter(adapter);
        recEvolutionChart.setLayoutManager(new LinearLayoutManager(this));

        // Call a method to fetch the pokemon species
        fetchSpecies(speciesID);


    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Method fetches species info based on species id passed in
    public void fetchSpecies(String speciesID) {
        pokeAPI.getInstance(getBaseContext()).fetchSpecieFromID(speciesID, species -> {
            Log.d(TAG, species.toString());

            // Can we get the list of what Eevee evolves to? Yes, yes we can
            // Call a method to fetch the evolution chain of the given pokemon species
            fetchEvolutionChain(species.getEvolutionChainID());
        },value -> {
            Log.d(TAG, value.toString());
        });
    }

    // Method fetches the full evolution chain for a specific pokemon
    public void fetchEvolutionChain(String evolutionID) {
        pokeAPI.getInstance(getBaseContext()).fetchEvolutionChainFromID(evolutionID, value -> {
            Log.d(TAG, value.toString());
            Log.d(TAG, siblings(value.chain).toString());

            // If the first evolution chain link's evolveTo array is empty (meaning the pokemon has no evolutions),
            if (value.chain.evolves_to.isEmpty()) {
                // Create two PokemonListItems, the first of which hold the pokemon, the second of
                // which will note that no evolutions exist
                PokemonListItem parent = new PokemonListItem();
                PokemonListItem noEvo = new PokemonListItem();

                parent.name = value.chain.species.name;
                parent.url = value.chain.species.url;
                noEvo.name = "noEvo";

                // Add a mock evolution to the evolutions list. The noEvo item will tell the
                // EvolutionAdapter that there are no evolutions
                evolutionList.add(new com.example.pokeviewer.Evolution(parent, noEvo));
            }
            // Otherwise,
            else {
                // Call a method to fetch the next link of the evolution chain
                fetchEvolution(value.chain);
            }

            adapter.notifyDataSetChanged();
        },value -> {
            Log.d(TAG, value.toString());
        });
    }

    // Recursive Method
    // Method will add each evolution in a single link of the chain to the evolutionList, then call
    // itself to do the same with the next link in the chain. The method will continue to recursively
    // call itself this way until the end of the evolution chain
    public void fetchEvolution(PokemonEvolutionChain chain) {
        // Create a PokemonListItem to store the "parent" pokemon (aka the pokemon that's being evolved
        // from, or pre-evolution) and a list of PokemonListItems to store the "sibling" pokemon
        // (aka the one or more pokemon that are being evolves to, or post-evolutions)
        PokemonListItem parent = new PokemonListItem();
        List<PokemonListItem> siblings = siblings(chain);

        parent.name = chain.species.name;
        parent.url = chain.species.url;

        // For each sibling pokemon,
        for (PokemonListItem sibling : siblings) {
            // Add an evolution to the list, from the parent to the single sibling
            evolutionList.add(new com.example.pokeviewer.Evolution(parent, sibling));
        }

        // If the next evolution chain link's evolveTo array is not empty (meaning the pokemon has further evolutions),
        if (!chain.evolves_to.isEmpty()) {
            // Note: Method is called recursively in a forEach loop in the rare event that one link
            //       in the chain connects forward to multiple other links (meaning one pokemon can
            //       evolve into multiple different pokemon). For pokemon that have split evolution
            //       trees, the tree are very simple, so the method recursion should not become too
            //       complex.
            // For each sibling chain link,
            for (PokemonEvolutionChain c : chain.evolves_to) {
                // Recursively call the method to fetch the next link in the chain
                fetchEvolution(c);
            }
        }
    }

    // Method returns a list of every "sibling" pokemon in the given chain link
    public List<PokemonListItem> siblings(PokemonEvolutionChain chain) {
        List<PokemonListItem> siblings = new ArrayList<>();

        // For each sibling chain link,
        for (PokemonEvolutionChain c: chain.evolves_to) {
            // Add a PokemonListItem to the list of siblings
            PokemonListItem item = new PokemonListItem();
            item.name =  c.species.name;
            item.url =  c.species.url;
            if (!c.evolution_details.isEmpty()) {
                item.evolutionDetails = c.evolution_details.get(0);
            }
            siblings.add(item);
        }
        return siblings;
    }
}