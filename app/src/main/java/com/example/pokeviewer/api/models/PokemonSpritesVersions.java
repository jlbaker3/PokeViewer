package com.example.pokeviewer.api.models;

import com.example.pokeviewer.api.models.sprites.*;
import com.squareup.moshi.Json;

public class PokemonSpritesVersions {
    @Json(name = "generation-i") public PokemonGenerationI generation_i;
    @Json(name = "generation-ii") public PokemonGenerationII generation_ii;
    @Json(name = "generation-iii") public PokemonGenerationIII generation_iii;
    @Json(name = "generation-iv") public PokemonGenerationIV generation_iv;
    @Json(name = "generation-v") public PokemonGenerationV generation_v;
    @Json(name = "generation-vi") public PokemonGenerationVI generation_vi;
    @Json(name = "generation-vii") public PokemonGenerationVII generation_vii;
    @Json(name = "generation-viii") public PokemonGenerationVII generation_viii;

    @Override
    public String toString() {
        return "PokemonSpritesVersions{" +
                "generation_i=" + generation_i +
                ", generation_ii=" + generation_ii +
                ", generation_iii=" + generation_iii +
                ", generation_iv=" + generation_iv +
                ", generation_v=" + generation_v +
                ", generation_vi=" + generation_vi +
                ", generation_vii=" + generation_vii +
                ", generation_viii=" + generation_viii +
                '}';
    }
}
