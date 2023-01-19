package com.example.pokeviewer.api.models;

public class PokemonSpecies {
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
