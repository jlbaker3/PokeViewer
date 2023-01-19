package com.example.pokeviewer.api.models;

public class PokemonSpriteTypes {
    public String back_default;
    public String back_female;
    public String back_shiny;
    public String back_shiny_female;
    public String front_default;
    public String front_female;
    public String front_shiny;
    public String front_shiny_female;

    @Override
    public String toString() {
        return "PokemonSpriteTypes{" +
                "back_default='" + back_default + '\'' +
                ", back_female='" + back_female + '\'' +
                ", back_shiny='" + back_shiny + '\'' +
                ", back_shiny_female='" + back_shiny_female + '\'' +
                ", front_default='" + front_default + '\'' +
                ", front_female='" + front_female + '\'' +
                ", front_shiny='" + front_shiny + '\'' +
                ", front_shiny_female='" + front_shiny_female + '\'' +
                '}';
    }
}
