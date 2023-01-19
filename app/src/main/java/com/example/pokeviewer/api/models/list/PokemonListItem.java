package com.example.pokeviewer.api.models.list;

import com.example.pokeviewer.api.PokemonAPI;
import com.example.pokeviewer.api.models.evolution.PokemonEvolutionDetails;

import java.net.URI;
import java.net.URISyntaxException;

public class PokemonListItem {

    public String name;
    public String url;
    public PokemonEvolutionDetails evolutionDetails;


    /**E
     * getNameCapital - Get the name of the pokemon with a capital first letter
     * @return
     */
    public String getNameCapital() {
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }

    /**
     * getID - This gets the id of the pokemon by the id of the pokemon from the URL
     * @return
     */
    public String getID() throws URISyntaxException {
        URI uri = new URI(this.url);
        String[] segments = uri.getPath().split("/");
        String idStr = segments[segments.length-1];
        return idStr;
    }

    public String getImageURL() {
        try {
            return PokemonAPI.getPokemonImageURLFromID(this.getID());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "PokemonListItem{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
