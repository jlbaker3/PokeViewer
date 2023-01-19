package com.example.pokeviewer.api.models.sprites;

import com.example.pokeviewer.api.models.PokemonSpriteTypes;
import com.squareup.moshi.Json;

public class PokemonGenerationVII {
    public PokemonSpriteTypes icons;
    @Json(name = "ultra-sun-ultra-moon")  public PokemonSpriteTypes ultra_sun_ultra_moon;
}
