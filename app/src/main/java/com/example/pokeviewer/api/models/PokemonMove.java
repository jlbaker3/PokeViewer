package com.example.pokeviewer.api.models;

public class PokemonMove {
    public String name;
    public String url;

    @Override
    public String toString() {
        return "PokemonMove{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
