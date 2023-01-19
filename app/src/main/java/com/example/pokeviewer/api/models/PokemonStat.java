package com.example.pokeviewer.api.models;

public class PokemonStat {
    public String name;
    public String url;

    @Override
    public String toString() {
        return "PokemonStat{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
