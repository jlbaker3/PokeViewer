package com.example.pokeviewer.api.models.evolution;

public class PokemonEvolution {
    public PokemonEvolutionBabyTrigger baby_trigger_item;
    public PokemonEvolutionChain chain;
    public int id;

    @Override
    public String toString() {
        return "PokemonEvolution{" +
                "baby_trigger_item=" + baby_trigger_item +
                ", chain=" + chain +
                '}';
    }

}
