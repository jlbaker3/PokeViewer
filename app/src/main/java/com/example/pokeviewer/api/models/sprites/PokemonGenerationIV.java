package com.example.pokeviewer.api.models.sprites;

import com.example.pokeviewer.api.models.PokemonSpriteTypes;
import com.squareup.moshi.Json;

public class PokemonGenerationIV {
    @Json(name = "diamond-pearl")  public PokemonSpriteTypes diamond_pearl;
    @Json(name = "heartgold-soulsilver")  public PokemonSpriteTypes heartgold_soulsilver;
    public PokemonSpriteTypes platinum;
}
