package com.example.pokeviewer.api.models.evolution;

import com.example.pokeviewer.api.models.PokemonItem;
import com.example.pokeviewer.api.models.PokemonLocation;
import com.example.pokeviewer.api.models.PokemonMove;
import com.example.pokeviewer.api.models.PokemonSpecies;
import com.example.pokeviewer.api.models.PokemonType;

public class PokemonEvolutionDetails {
    public Integer gender;
    public PokemonItem held_item;
    public PokemonItem item;
    public PokemonMove known_move;
    public PokemonType known_move_type;
    public PokemonLocation location;
    public Integer min_affection;
    public Integer min_beauty;
    public Integer min_happiness;
    public Integer min_level;
    public boolean needs_overworld_rain;
    public PokemonSpecies party_species;
    public PokemonType party_type;
    public Integer relative_physical_stats;
    public String time_of_day;
    public PokemonSpecies trade_species;
    public PokemonEvolutionTrigger trigger;
    public boolean turn_upside_down;

    @Override
    public String toString() {
        return "PokemonEvolutionDetails{" +
                "gender=" + gender +
                ", held_item=" + held_item +
                ", item=" + item +
                ", known_move=" + known_move +
                ", known_move_type=" + known_move_type +
                ", location=" + location +
                ", min_affection=" + min_affection +
                ", min_beauty=" + min_beauty +
                ", min_happiness=" + min_happiness +
                ", min_level=" + min_level +
                ", needs_overworld_rain=" + needs_overworld_rain +
                ", party_species=" + party_species +
                ", party_type=" + party_type +
                ", relative_physical_stats=" + relative_physical_stats +
                ", time_of_day='" + time_of_day + '\'' +
                ", trade_species=" + trade_species +
                ", trigger=" + trigger +
                ", turn_upside_down=" + turn_upside_down +
                '}';
    }
}
