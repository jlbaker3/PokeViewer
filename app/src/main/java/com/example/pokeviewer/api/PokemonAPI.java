package com.example.pokeviewer.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pokeviewer.api.models.Pokemon;
import com.example.pokeviewer.api.models.evolution.PokemonEvolution;
import com.example.pokeviewer.api.models.list.PokemonListItem;
import com.example.pokeviewer.api.models.list.PokemonListResults;
import com.example.pokeviewer.api.models.species.PokemonSpecie;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class PokemonAPI {
    private static PokemonAPI pokemonAPI;

    private String API_ALL_POKEMON = "https://pokeapi.co/api/v2/pokemon-species?offset=0&limit=905";

    //The queue to request values for volley
    public RequestQueue queue;

    //Cache for pokemon, to save for rate limited calls.
    private HashMap<String, Pokemon> pokemonCache;

    public static PokemonAPI getInstance() {
        if (pokemonAPI == null) {
            pokemonAPI = new PokemonAPI();
        }
        return pokemonAPI;
    }

    /**
     * getInstance - Provides a singleton instance which context can be provided.
     * Context needs to be provided once to the valid
     * @param context
     * @return
     */
    public static PokemonAPI getInstance(Context context) {
        if (pokemonAPI == null) {
            pokemonAPI = new PokemonAPI();
            if (pokemonAPI.queue == null) pokemonAPI.queue = Volley.newRequestQueue(context);
        }
        return pokemonAPI;
    }

    /**
     * fetchAllPokemon - Fetches the base list of pokemon with their name and URL reference to fetch their information.
     * @param successAction
     * @param errorAction
     */
    public void fetchAllPokemon(PokemonFetchAction<List<PokemonListItem>> successAction, PokemonFetchAction<String> errorAction) {
       if (queue != null) {
           StringRequest stringRequest = new StringRequest(Request.Method.GET, API_ALL_POKEMON,response -> {
               Log.d("POKEMON", response);
               Moshi moshi = new Moshi.Builder().build();
               JsonAdapter<PokemonListResults> jsonAdapter = moshi.adapter(PokemonListResults.class);

               PokemonListResults pokemonListResults = null;
               try {
                   pokemonListResults = jsonAdapter.fromJson(response);
                   Log.d("POKEMON", pokemonListResults.toString());
               } catch (IOException e) {
                   e.printStackTrace();
               }
               successAction.action(pokemonListResults.results);
           }, error -> {
               //Provide the error back to the application
               errorAction.action(error.toString());
           });
           queue.add(stringRequest);
       } else {
           errorAction.action("[PokemonAPI] You forgot to pass the context to the api instance at least once");
       }
    }

    /**
     * fetchPokemonByID
     * @param id The ID of a pokemon
     * @param successAction The success action of the API call
     * @param errorAction The fail action of the API call
     */
    public void fetchPokemonByID(String id, PokemonFetchAction<Pokemon> successAction, PokemonFetchAction<String> errorAction) {
        if (queue != null) {

            //Check if we have any cached pokemon
            if (pokemonCache == null) {
                pokemonCache = new HashMap<>();
            }

            //Implement a basic cache system
            if (pokemonCache.containsKey(id)) {
                //TODO: Check if UI thread runs on this normally
                successAction.action( pokemonCache.get(id));
                return;
            }

            //The url for the main pokemon API
            String url = "https://pokeapi.co/api/v2/pokemon/" + id;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,response -> {

                Log.d("POKEMON", response);

                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<Pokemon> jsonAdapter = moshi.adapter(Pokemon.class);

                Pokemon pokemon = null;

                try {
                    pokemon = jsonAdapter.fromJson(response);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                pokemonCache.put(id, pokemon);
                successAction.action(pokemon);
            }, error -> {
                //Provide the error back to the application
                errorAction.action(error.toString());
            });
            queue.add(stringRequest);
        } else {
            errorAction.action("[PokemonAPI] You forgot to pass the context to the api instance at least once");
        }
    }


    /**
     * fetchSpecieFromID
     * @param specieID The specie ID of a pokemon
     * @param successAction The success action of the API call
     * @param errorAction The fail action of the API call
     */
    public void fetchSpecieFromID(String specieID, PokemonFetchAction<PokemonSpecie> successAction, PokemonFetchAction<String> errorAction) {
        if (queue != null) {
            String url = "https://pokeapi.co/api/v2/pokemon-species/" + specieID;

            //TODO: maybe implement species cache

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,response -> {
                Log.d("POKEMON", response);

                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<PokemonSpecie> jsonAdapter = moshi.adapter(PokemonSpecie.class);

                PokemonSpecie specie = null;

                try {
                    specie = jsonAdapter.fromJson(response);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                successAction.action(specie);
            }, error -> {
                //Provide the error back to the application
                errorAction.action(error.toString());
            });
            queue.add(stringRequest);
        } else {
            errorAction.action("[PokemonAPI] You forgot to pass the context to the api instance at least once");
        }
    }

    /**
     * fetchSpecieFromID
     * @param evolutionID The evolution ID of a pokemon
     * @param successAction The success action of the API call
     * @param errorAction The fail action of the API call
     */
    public void fetchEvolutionChainFromID(String evolutionID, PokemonFetchAction<PokemonEvolution> successAction, PokemonFetchAction<String> errorAction) {
        if (queue != null) {
            String url = "https://pokeapi.co/api/v2/evolution-chain/" + evolutionID;

            //TODO: maybe implement evolution cache

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,response -> {
                Log.d("POKEMON", response);

                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<PokemonEvolution> jsonAdapter = moshi.adapter(PokemonEvolution.class);

                PokemonEvolution pokemon = null;

                try {
                    pokemon = jsonAdapter.fromJson(response);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                successAction.action(pokemon);
            }, error -> {
                //Provide the error back to the application
                errorAction.action(error.toString());
            });
            queue.add(stringRequest);
        } else {
            errorAction.action("[PokemonAPI] You forgot to pass the context to the api instance at least once");
        }
    }

    /**
     * getImageURL - Provides a high resolution URL for a said pokemon id.
     * Note: not all pokemon have just a numeric id, but also a variant of said
     * id so for example 585-summer,585-fall,585-winter,585-spring also exists.
     * @return
     */
    public static String getPokemonImageURLFromID(String id)  {
        //First we need to pad the id with 000 zeroes
        String formmatedId = String.format("%03d", Integer.parseInt(id));
        String imageLoc = "https://raw.githubusercontent.com/HybridShivam/Pokemon/master/assets/images/";
        return imageLoc + formmatedId + ".png";
    }
}
