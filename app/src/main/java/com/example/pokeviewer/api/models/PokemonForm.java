package com.example.pokeviewer.api.models;

public class PokemonForm {
    public String name;
    public String url;

    @Override
    public String toString() {
        return "PokemonForm{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
