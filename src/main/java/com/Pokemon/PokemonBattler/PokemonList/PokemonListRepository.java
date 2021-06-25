package com.Pokemon.PokemonBattler.PokemonList;

import com.Pokemon.PokemonBattler.Pokemon.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PokemonListRepository extends JpaRepository<PokemonList, Long> {
    PokemonList findPokemonByName(String name);

    @Override
    ArrayList<PokemonList> findAll();
}
