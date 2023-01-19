package com.example.pokeviewer.api.models;

public class PokemonItem {
    public String name;
    public String url;

    @Override
    public String toString() {
        return "PokemonSpecies{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
