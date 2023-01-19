package com.example.pokeviewer.api.models;

public class PokemonType {
    public String name;
    public String url;

    @Override
    public String toString() {
        return "PokemonType{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
