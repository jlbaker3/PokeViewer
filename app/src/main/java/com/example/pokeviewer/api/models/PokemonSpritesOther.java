package com.example.pokeviewer.api.models;

import com.squareup.moshi.Json;

public class PokemonSpritesOther {
    public PokemonSpriteTypes dream_world;
    public PokemonSpriteTypes home;
    @Json(name = "official-artwork") public PokemonSpriteTypes official_artwork;

    @Override
    public String toString() {
        return "PokemonSpritesOther{" +
                "dream_world=" + dream_world +
                ", home=" + home +
                ", official_artwork=" + official_artwork +
                '}';
    }
}
