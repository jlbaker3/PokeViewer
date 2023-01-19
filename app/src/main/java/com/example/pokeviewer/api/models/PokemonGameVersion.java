package com.example.pokeviewer.api.models;

public class PokemonGameVersion {
    public String name;
    public String url;

    @Override
    public String toString() {
        return "PokemonGameVersion{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
