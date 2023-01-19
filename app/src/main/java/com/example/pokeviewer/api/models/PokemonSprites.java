package com.example.pokeviewer.api.models;

public class PokemonSprites  extends  PokemonSpriteTypes{
    public PokemonSpritesOther other ;
    public PokemonSpritesVersions versions;

    @Override
    public String toString() {
        return "PokemonSprites{" +
                "other=" + other +
                ", versions=" + versions +
                '}';
    }
}
