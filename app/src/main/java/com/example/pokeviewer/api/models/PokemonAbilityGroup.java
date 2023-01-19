package com.example.pokeviewer.api.models;

public class PokemonAbilityGroup {
    public PokemonAbility ability;
    public boolean is_hidden;
    public int slot;

    @Override
    public String toString() {
        return "PokemonAbilityGroup{" +
                "ability=" + ability +
                ", is_hidden=" + is_hidden +
                ", slot=" + slot +
                '}';
    }
}
