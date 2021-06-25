package com.Pokemon.PokemonBattler.Moves;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {

    ArrayList<Move> findMovesByPokemonAndTeam(String pokemon, String team);
}
