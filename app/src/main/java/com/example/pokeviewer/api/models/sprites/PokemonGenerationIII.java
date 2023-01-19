package com.example.pokeviewer.api.models.sprites;

import com.example.pokeviewer.api.models.PokemonSpriteTypes;
import com.squareup.moshi.Json;

public class PokemonGenerationIII {
    public PokemonSpriteTypes emerald;
    @Json(name = "firered-leafgreen")  public PokemonSpriteTypes firered_leafgreen;
    @Json(name = "ruby-sapphire")  public PokemonSpriteTypes ruby_sapphire;
}
