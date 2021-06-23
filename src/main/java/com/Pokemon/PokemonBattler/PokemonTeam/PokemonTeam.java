package com.Pokemon.PokemonBattler.PokemonTeam;

import com.Pokemon.PokemonBattler.Pokemon.Pokemon;
import lombok.Data;

@Data public class PokemonTeam {
    public Pokemon pokemon1;
    public Pokemon pokemon2;
    public Pokemon pokemon3;
    public Pokemon pokemon4;
    public Pokemon pokemon5;
    public Pokemon pokemon6;

    public PokemonTeam(Pokemon p1, Pokemon p2, Pokemon p3, Pokemon p4, Pokemon p5, Pokemon p6){}
}
