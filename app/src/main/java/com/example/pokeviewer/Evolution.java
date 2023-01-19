package com.example.pokeviewer;

import com.example.pokeviewer.api.models.list.PokemonListItem;

public class Evolution {
    PokemonListItem preEvolution;
    // PokemonEvolutionDetails evolutionDetails;
    PokemonListItem postEvolution;

    public Evolution(PokemonListItem preEvolution, PokemonListItem postEvolution) {
        this.preEvolution = preEvolution;
        // this.evolutionDetails = evolutionDetails;
        this.postEvolution = postEvolution;
    }
}
