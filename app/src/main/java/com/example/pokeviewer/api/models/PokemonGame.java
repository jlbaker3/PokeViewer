package com.example.pokeviewer.api.models;

public class PokemonGame {
    public int game_index;
    public PokemonGameVersion version;

    @Override
    public String toString() {
        return "PokemonGame{" +
                "game_index=" + game_index +
                ", version=" + version +
                '}';
    }
}
