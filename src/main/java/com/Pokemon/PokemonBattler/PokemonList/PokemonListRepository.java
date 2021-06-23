package com.Pokemon.PokemonBattler.PokemonList;

import com.Pokemon.PokemonBattler.Pokemon.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonListRepository extends JpaRepository<PokemonList, Long> {
    PokemonList findPokemonByName(String name);
}
