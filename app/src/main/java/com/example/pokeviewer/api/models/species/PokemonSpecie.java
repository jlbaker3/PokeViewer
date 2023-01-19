package com.example.pokeviewer.api.models.species;

import java.net.URI;

public class PokemonSpecie {
    public PokemonSpecieEvolutionChain evolution_chain;

    public String getEvolutionChainID()  {
        URI uri = null;
        try {
            uri = new URI(this.evolution_chain.url);
            String[] segments = uri.getPath().split("/");
            String idStr = segments[segments.length-1];
            return idStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "PokemonSpecie{" +
                "evolutionChain=" + evolution_chain +
                '}';
    }
}
