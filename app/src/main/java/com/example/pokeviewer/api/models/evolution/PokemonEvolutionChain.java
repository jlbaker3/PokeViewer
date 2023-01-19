package com.example.pokeviewer.api.models.evolution;

import com.example.pokeviewer.api.models.PokemonSpecies;

import java.util.List;

public class PokemonEvolutionChain {
    public boolean is_baby;
    public PokemonSpecies species;
    public List<PokemonEvolutionDetails> evolution_details;
    public List<PokemonEvolutionChain> evolves_to;

    @Override
    public String toString() {
        return "PokemonEvolutionChain{" +
                "is_baby=" + is_baby +
                ", species=" + species +
                ", evolution_details=" + evolution_details +
                ", evolves_to=" + evolves_to +
                '}';
    }
}
