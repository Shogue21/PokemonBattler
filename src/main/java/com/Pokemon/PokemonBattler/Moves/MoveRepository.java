package com.Pokemon.PokemonBattler.Moves;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {

    ArrayList<Move> findMovesByPokemonAndUsernameAndTeam(String pokemon, String username, String team);
    ArrayList<Move> findMovesByUsernameAndTeam(String username, String team);
    Move findMoveByNameAndPokemonAndUsernameAndTeam(String name, String pokemon, String username, String team);

}
