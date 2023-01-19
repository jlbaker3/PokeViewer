package com.example.pokeviewer.api.models;

public class PokemonStatGroup {
    public int base_stat;
    public int effort;
    public PokemonStat stat;

    @Override
    public String toString() {
        return "PokemonStatGroup{" +
                "base_stat=" + base_stat +
                ", effort=" + effort +
                ", stat=" + stat +
                '}';
    }
}
