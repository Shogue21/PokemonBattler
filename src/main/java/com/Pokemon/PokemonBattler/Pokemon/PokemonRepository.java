package com.Pokemon.PokemonBattler.Pokemon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long>{

    Pokemon findPokemonByName(String name);
    List<Pokemon> findPokemonByUsernameAndTeam(String username, String team);
    Pokemon findPokemonByNameAndUsernameAndTeam(String name, String username, String team);
    Pokemon findPokemonByCurrentAndUsernameAndTeam(boolean current, String username, String team);
}
