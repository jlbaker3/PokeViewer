package com.example.pokeviewer.api.models.sprites;

import com.example.pokeviewer.api.models.PokemonSpriteTypes;
import com.squareup.moshi.Json;

public class PokemonGenerationI {
    @Json(name = "red-blue") public PokemonSpriteTypes red_blue;
    public PokemonSpriteTypes yellow;
}
