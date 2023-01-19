package com.example.pokeviewer.api.models;

public class PokemonTypesGroup {
    public int slot;
    public PokemonType type;

    @Override
    public String toString() {
        return "PokemonTypesGroup{" +
                "slot=" + slot +
                ", type=" + type +
                '}';
    }
}
