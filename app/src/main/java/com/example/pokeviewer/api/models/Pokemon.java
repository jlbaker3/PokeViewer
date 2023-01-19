package com.example.pokeviewer.api.models;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

//Its a Pokemon!
//gotta catch 'em all
public class Pokemon  {
    public List<PokemonAbilityGroup> abilities;
    public Integer base_experience;
    public List<PokemonForm> forms;
    public List<PokemonGame> game_indices;
    public int height;
    //public List<> held_items;
    public String id;
    public boolean is_default;
    public String location_area_encounters;
    public List<PokemonMoveGroup> moves;
    public String name;
    public int order;
    //public List<> past_types;
    public PokemonSpecies species;
    public PokemonSprites sprites;
    public List<PokemonStatGroup> stats;
    public List<PokemonTypesGroup> types;
    public int weight;

    @Override
    public String toString() {
        return "Pokemon{" +
                "abilities=" + abilities +
                ", base_experience=" + base_experience +
                ", forms=" + forms +
                ", game_indices=" + game_indices +
                ", height=" + height +
                ", id=" + id +
                ", is_default=" + is_default +
                ", location_area_encounters='" + location_area_encounters + '\'' +
                ", moves=" + moves +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", species=" + species +
                ", sprites=" + sprites +
                ", stats=" + stats +
                ", types=" + types +
                ", weight=" + weight +
                '}';
    }

    public String getNameCapital() {
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }

    public String getSpeciesID()  {
        URI uri = null;
        try {
            uri = new URI(this.species.url);
            String[] segments = uri.getPath().split("/");
            String idStr = segments[segments.length-1];
            return idStr;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
