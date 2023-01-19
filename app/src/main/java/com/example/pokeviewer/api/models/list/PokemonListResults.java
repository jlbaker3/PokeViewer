package com.example.pokeviewer.api.models.list;

import java.util.List;

public class PokemonListResults {
    public int count;
    public List<PokemonListItem> results;

    @Override
    public String toString() {
        return "PokemonListResults{" +
                "count=" + count +
                ", results=" + results +
                '}';
    }
}
